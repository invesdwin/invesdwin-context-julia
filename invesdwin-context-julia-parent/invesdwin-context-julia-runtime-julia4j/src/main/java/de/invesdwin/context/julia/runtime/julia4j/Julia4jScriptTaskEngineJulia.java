package de.invesdwin.context.julia.runtime.julia4j;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.julia4j.pool.ExtendedJuliaCaller;
import de.invesdwin.context.julia.runtime.julia4j.pool.Julia4jObjectPool;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.concurrent.lock.disabled.DisabledLock;

@NotThreadSafe
public class Julia4jScriptTaskEngineJulia implements IScriptTaskEngine {

    private ExtendedJuliaCaller juliaCaller;
    private final Julia4jScriptTaskInputsJulia inputs;
    private final Julia4jScriptTaskResultsJulia results;

    public Julia4jScriptTaskEngineJulia(final ExtendedJuliaCaller juliaCaller) {
        this.juliaCaller = juliaCaller;
        this.inputs = new Julia4jScriptTaskInputsJulia(this);
        this.results = new Julia4jScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        try {
            juliaCaller.execute(expression);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Julia4jScriptTaskInputsJulia getInputs() {
        return inputs;
    }

    @Override
    public Julia4jScriptTaskResultsJulia getResults() {
        return results;
    }

    @Override
    public void close() {
        juliaCaller = null;
    }

    @Override
    public ExtendedJuliaCaller unwrap() {
        return juliaCaller;
    }

    /**
     * Each instance has its own engine, so no shared locking required.
     */
    @Override
    public ILock getSharedLock() {
        return DisabledLock.INSTANCE;
    }

    public static Julia4jScriptTaskEngineJulia newInstance() {
        return new Julia4jScriptTaskEngineJulia(Julia4jObjectPool.INSTANCE.borrowObject()) {
            @Override
            public void close() {
                final ExtendedJuliaCaller unwrap = unwrap();
                if (unwrap != null) {
                    Julia4jObjectPool.INSTANCE.returnObject(unwrap);
                }
                super.close();
            }
        };
    }

}
