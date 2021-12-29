package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.log.error.Err;
import de.invesdwin.util.concurrent.Threads;
import de.invesdwin.util.lang.Strings;
import de.invesdwin.util.time.date.FTimeUnit;

@ThreadSafe
public class ModifiedJuliaErrorConsoleWatcher implements Closeable {

    private final BufferedReader errorReader;

    private Thread errorThread;

    @GuardedBy("self")
    private final StringBuilder errorMessage = new StringBuilder();

    public ModifiedJuliaErrorConsoleWatcher(final Process process) {
        this.errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    }

    public void startWatching() {
        errorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Threads.isInterrupted()) {
                    try {
                        final String s = errorReader.readLine();
                        if (Strings.isNotBlank(s)) {
                            synchronized (errorMessage) {
                                if (errorMessage.length() > 0) {
                                    errorMessage.append("\n");
                                }
                                errorMessage.append(s);
                            }
                            IScriptTaskRunnerJulia.LOG.warn("< %s", s);
                        } else {
                            FTimeUnit.MILLISECONDS.sleep(1);
                        }
                    } catch (final Exception e) {
                        throw Err.process(e);
                    }
                }
            }
        });
        errorThread.start();
    }

    @Override
    public void close() {
        if (errorThread != null) {
            errorThread.interrupt();
            errorThread = null;
        }
        clearLog();
    }

    public void clearLog() {
        synchronized (errorMessage) {
            errorMessage.setLength(0);
        }
    }

    public String getErrorMessage() {
        synchronized (errorMessage) {
            if (errorMessage.length() == 0) {
                return null;
            }
            final String str = String.valueOf(errorMessage).trim();
            errorMessage.setLength(0);
            return str;
        }
    }
}