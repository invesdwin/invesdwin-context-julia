package de.invesdwin.context.julia.runtime.juliacaller.pool;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.util.concurrent.Threads;
import de.invesdwin.util.lang.Strings;
import de.invesdwin.util.time.date.FTimeUnit;

@ThreadSafe
public class ModifiedJuliaErrorConsoleWatcher implements Closeable {

    private final BufferedReader errorReader;
    private final BufferedReader infoReader;

    private volatile Thread errorThread;
    private volatile Thread infoThread;

    @GuardedBy("self")
    private final StringBuilder errorMessage = new StringBuilder();

    public ModifiedJuliaErrorConsoleWatcher(final Process process) {
        this.errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        this.infoReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public void startWatching() {
        errorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Threads.isInterrupted() && errorThread != null) {
                        final String s = errorReader.readLine();
                        if (errorThread == null) {
                            return;
                        }
                        if (Strings.isNotBlank(s) && !s.contains("Info: Precompiling ")) {
                            synchronized (errorMessage) {
                                if (errorMessage.length() > 0) {
                                    errorMessage.append("\n");
                                }
                                errorMessage.append(s);
                            }
                            IScriptTaskRunnerJulia.LOG.warn(s);
                        } else {
                            FTimeUnit.MILLISECONDS.sleep(1);
                        }
                    }
                } catch (final Throwable e) {
                    //ignore, process must have been closed
                }
            }
        });
        errorThread.start();
        infoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Threads.isInterrupted() && infoThread != null) {
                        final String s = infoReader.readLine();
                        if (infoThread == null) {
                            return;
                        }
                        if (Strings.isNotBlank(s)) {
                            IScriptTaskRunnerJulia.LOG.info(s);
                        } else {
                            FTimeUnit.MILLISECONDS.sleep(1);
                        }
                    }
                } catch (final Throwable e) {
                    //ignore, process must have been closed
                }
            }
        });
        infoThread.start();
    }

    @Override
    public void close() {
        if (errorThread != null) {
            errorThread.interrupt();
            errorThread = null;
        }
        if (infoThread != null) {
            infoThread.interrupt();
            infoThread = null;
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