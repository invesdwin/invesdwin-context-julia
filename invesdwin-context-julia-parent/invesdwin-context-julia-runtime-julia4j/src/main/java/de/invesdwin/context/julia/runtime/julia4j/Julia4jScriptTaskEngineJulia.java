package de.invesdwin.context.julia.runtime.julia4j;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.julia4j.internal.JuliaEngineWrapper;
import de.invesdwin.util.concurrent.lock.ILock;

@NotThreadSafe
public class Julia4jScriptTaskEngineJulia implements IScriptTaskEngine {

    private JuliaEngineWrapper juliaEngine;
    private final Julia4jScriptTaskInputsJulia inputs;
    private final Julia4jScriptTaskResultsJulia results;

    public Julia4jScriptTaskEngineJulia(final JuliaEngineWrapper juliaEngine) {
        this.juliaEngine = juliaEngine;
        this.inputs = new Julia4jScriptTaskInputsJulia(this);
        this.results = new Julia4jScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        juliaEngine.eval(expression);
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
        if (juliaEngine != null) {
            juliaEngine.reset();
            juliaEngine = null;
        }
    }

    @Override
    public JuliaEngineWrapper unwrap() {
        return juliaEngine;
    }

    /**
     * Each instance has its own engine, so no shared locking required.
     */
    @Override
    public ILock getSharedLock() {
        return juliaEngine.getLock();
    }

    public static Julia4jScriptTaskEngineJulia newInstance() {
        return new Julia4jScriptTaskEngineJulia(JuliaEngineWrapper.INSTANCE);
    }

}
