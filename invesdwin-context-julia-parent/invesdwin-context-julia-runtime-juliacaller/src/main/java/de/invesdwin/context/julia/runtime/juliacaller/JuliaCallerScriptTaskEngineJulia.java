package de.invesdwin.context.julia.runtime.juliacaller;

import javax.annotation.concurrent.NotThreadSafe;

import org.rosuda.REngine.REXP;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.juliacaller.pool.ExtendedRserveSession;
import de.invesdwin.context.julia.runtime.juliacaller.pool.RsessionObjectPool;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.concurrent.lock.disabled.DisabledLock;

@NotThreadSafe
public class JuliaCallerScriptTaskEngineJulia implements IScriptTaskEngine {

    private ExtendedRserveSession rsession;
    private final JuliaCallerScriptTaskInputsJulia inputs;
    private final JuliaCallerScriptTaskResultsJulia results;

    public JuliaCallerScriptTaskEngineJulia(final ExtendedRserveSession rsession) {
        this.rsession = rsession;
        this.inputs = new JuliaCallerScriptTaskInputsJulia(this);
        this.results = new JuliaCallerScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        final REXP eval = rsession.rawEval(expression);
        if (eval == null) {
            throw new IllegalStateException(
                    String.valueOf(de.invesdwin.context.julia.runtime.juliacaller.pool.internal.RsessionLogger.get(rsession)
                            .getErrorMessage()));
        }
    }

    @Override
    public JuliaCallerScriptTaskInputsJulia getInputs() {
        return inputs;
    }

    @Override
    public JuliaCallerScriptTaskResultsJulia getResults() {
        return results;
    }

    @Override
    public void close() {
        rsession = null;
    }

    @Override
    public ExtendedRserveSession unwrap() {
        return rsession;
    }

    /**
     * Each instance has its own engine, so no shared locking required.
     */
    @Override
    public ILock getSharedLock() {
        return DisabledLock.INSTANCE;
    }

    public static JuliaCallerScriptTaskEngineJulia newInstance() {
        return new JuliaCallerScriptTaskEngineJulia(RsessionObjectPool.INSTANCE.borrowObject()) {
            @Override
            public void close() {
                final ExtendedRserveSession unwrap = unwrap();
                if (unwrap != null) {
                    RsessionObjectPool.INSTANCE.returnObject(unwrap);
                }
                super.close();
            }
        };
    }

}
