package de.invesdwin.context.julia.runtime.jajub;

import javax.annotation.concurrent.NotThreadSafe;

import com.github.rcaller.rstuff.RCaller;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.jajub.pool.JajubObjectPool;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.concurrent.lock.disabled.DisabledLock;

@NotThreadSafe
public class JajubScriptTaskEngineJulia implements IScriptTaskEngine {

    private RCaller rcaller;
    private final JajubScriptTaskInputsJulia inputs;
    private final JajubScriptTaskResultsJulia results;

    public JajubScriptTaskEngineJulia(final RCaller rcaller) {
        this.rcaller = rcaller;
        this.inputs = new JajubScriptTaskInputsJulia(this);
        this.results = new JajubScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        rcaller.getRCode().addRCode(expression);
        rcaller.getRCode().addRCode(JajubScriptTaskRunnerJulia.INTERNAL_RESULT_VARIABLE + " <- c()");
        rcaller.runAndReturnResultOnline(JajubScriptTaskRunnerJulia.INTERNAL_RESULT_VARIABLE);
    }

    @Override
    public JajubScriptTaskInputsJulia getInputs() {
        return inputs;
    }

    @Override
    public JajubScriptTaskResultsJulia getResults() {
        return results;
    }

    @Override
    public void close() {
        rcaller = null;
    }

    @Override
    public RCaller unwrap() {
        return rcaller;
    }

    /**
     * Each instance has its own engine, so no shared locking required.
     */
    @Override
    public ILock getSharedLock() {
        return DisabledLock.INSTANCE;
    }

    public static JajubScriptTaskEngineJulia newInstance() {
        return new JajubScriptTaskEngineJulia(JajubObjectPool.INSTANCE.borrowObject()) {
            @Override
            public void close() {
                final RCaller unwrap = unwrap();
                if (unwrap != null) {
                    JajubObjectPool.INSTANCE.returnObject(unwrap);
                }
                super.close();
            }
        };
    }

}
