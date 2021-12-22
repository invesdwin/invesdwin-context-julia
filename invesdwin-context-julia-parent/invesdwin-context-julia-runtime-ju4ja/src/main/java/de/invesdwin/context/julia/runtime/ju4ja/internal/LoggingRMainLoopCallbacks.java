package de.invesdwin.context.julia.runtime.ju4ja.internal;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.util.lang.Strings;

@ThreadSafe
public final class LoggingRMainLoopCallbacks implements RMainLoopCallbacks {

    public static final LoggingRMainLoopCallbacks INSTANCE = new LoggingRMainLoopCallbacks();

    @GuardedBy("this")
    private final StringBuilder message = new StringBuilder();
    @GuardedBy("this")
    private boolean error = false;
    @GuardedBy("this")
    private final StringBuilder errorMessage = new StringBuilder();

    private LoggingRMainLoopCallbacks() {}

    @Override
    public synchronized void rWriteConsole(final Rengine rengine, final String text, final int oType) {
        if (oType != 0) {
            error = true;
            errorMessage.append(text);
        } else {
            //might have been a simple warning message like loading package or so, since a normal message followed it
            errorMessage.setLength(0);
        }
        if (text.endsWith("\n")) {
            message.append(text);
            if (Strings.isNotBlank(message)) {
                if (error) {
                    IScriptTaskRunnerJulia.LOG.warn(String.valueOf(message).trim());
                } else {
                    IScriptTaskRunnerJulia.LOG.debug(String.valueOf(message).trim());
                }
            }
            message.setLength(0);
            error = false;
        } else {
            message.append(text);
        }
    }

    public synchronized String getErrorMessage() {
        return String.valueOf(errorMessage).trim();
    }

    public synchronized void reset() {
        rWriteConsole(null, "\n", 0); //flush existing output
        message.setLength(0);
        error = false;
        errorMessage.setLength(0);
    }

    @Override
    public void rShowMessage(final Rengine rengine, final String message) {
        IScriptTaskRunnerJulia.LOG.warn("rShowMessage: %s", message);
    }

    @Override
    public void rSaveHistory(final Rengine rengine, final String filename) {
        IScriptTaskRunnerJulia.LOG.warn("rSaveHistory: %s", filename);
    }

    @Override
    public String rReadConsole(final Rengine rengine, final String prompt, final int addToHistory) {
        IScriptTaskRunnerJulia.LOG.warn("rReadConsole: %s [%s]", prompt, addToHistory);
        return null;
    }

    @Override
    public void rLoadHistory(final Rengine rengine, final String filename) {
        IScriptTaskRunnerJulia.LOG.warn("rLoadHistory: %s", filename);
    }

    @Override
    public void rFlushConsole(final Rengine rengine) {
        //        IScriptTaskRunner.LOG.warn("rFlushConsole");
    }

    @Override
    public String rChooseFile(final Rengine rengine, final int newFile) {
        IScriptTaskRunnerJulia.LOG.warn("rChooseFile: %s", newFile);
        return null;
    }

    @Override
    public void rBusy(final Rengine rengine, final int which) {
        IScriptTaskRunnerJulia.LOG.warn("rBusy: %s", which);
    }
}