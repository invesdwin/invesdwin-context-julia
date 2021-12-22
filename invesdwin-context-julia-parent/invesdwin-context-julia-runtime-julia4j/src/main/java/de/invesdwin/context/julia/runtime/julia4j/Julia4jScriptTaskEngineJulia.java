package de.invesdwin.context.julia.runtime.julia4j;

import javax.annotation.concurrent.NotThreadSafe;

import org.rosuda.JRI.REXP;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.julia4j.internal.LoggingRMainLoopCallbacks;
import de.invesdwin.context.julia.runtime.julia4j.internal.RengineWrapper;
import de.invesdwin.util.concurrent.lock.ILock;

@NotThreadSafe
public class Julia4jScriptTaskEngineJulia implements IScriptTaskEngine {

    private RengineWrapper rengine;
    private final Julia4jScriptTaskInputsJulia inputs;
    private final Julia4jScriptTaskResultsJulia results;

    public Julia4jScriptTaskEngineJulia(final RengineWrapper rengine) {
        this.rengine = rengine;
        this.inputs = new Julia4jScriptTaskInputsJulia(this);
        this.results = new Julia4jScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        final REXP eval = rengine.getRengine().eval("eval(parse(text=\"" + expression.replace("\"", "\\\"") + "\"))");
        if (eval == null) {
            throw new IllegalStateException(String.valueOf(LoggingRMainLoopCallbacks.INSTANCE.getErrorMessage()));
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
        eval(IScriptTaskRunnerJulia.CLEANUP_SCRIPT);
        rengine = null;
    }

    @Override
    public RengineWrapper unwrap() {
        return rengine;
    }

    @Override
    public ILock getSharedLock() {
        return unwrap().getLock();
    }

    public static Julia4jScriptTaskEngineJulia newInstance() {
        return new Julia4jScriptTaskEngineJulia(RengineWrapper.INSTANCE);
    }

}
