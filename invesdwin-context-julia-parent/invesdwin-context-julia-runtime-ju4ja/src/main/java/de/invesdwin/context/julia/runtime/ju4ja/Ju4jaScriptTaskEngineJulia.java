package de.invesdwin.context.julia.runtime.ju4ja;

import javax.annotation.concurrent.NotThreadSafe;

import org.rosuda.JRI.REXP;

import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.ju4ja.internal.LoggingRMainLoopCallbacks;
import de.invesdwin.context.julia.runtime.ju4ja.internal.RengineWrapper;
import de.invesdwin.util.concurrent.lock.ILock;

@NotThreadSafe
public class Ju4jaScriptTaskEngineJulia implements IScriptTaskEngine {

    private RengineWrapper rengine;
    private final Ju4jaScriptTaskInputsJulia inputs;
    private final Ju4jaScriptTaskResultsJulia results;

    public Ju4jaScriptTaskEngineJulia(final RengineWrapper rengine) {
        this.rengine = rengine;
        this.inputs = new Ju4jaScriptTaskInputsJulia(this);
        this.results = new Ju4jaScriptTaskResultsJulia(this);
    }

    @Override
    public void eval(final String expression) {
        final REXP eval = rengine.getRengine().eval("eval(parse(text=\"" + expression.replace("\"", "\\\"") + "\"))");
        if (eval == null) {
            throw new IllegalStateException(String.valueOf(LoggingRMainLoopCallbacks.INSTANCE.getErrorMessage()));
        }
    }

    @Override
    public Ju4jaScriptTaskInputsJulia getInputs() {
        return inputs;
    }

    @Override
    public Ju4jaScriptTaskResultsJulia getResults() {
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

    public static Ju4jaScriptTaskEngineJulia newInstance() {
        return new Ju4jaScriptTaskEngineJulia(RengineWrapper.INSTANCE);
    }

}
