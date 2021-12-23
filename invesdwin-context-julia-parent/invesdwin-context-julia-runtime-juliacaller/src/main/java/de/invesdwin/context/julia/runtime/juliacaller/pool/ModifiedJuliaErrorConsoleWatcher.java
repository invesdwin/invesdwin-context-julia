package de.invesdwin.context.julia.runtime.juliacaller.pool;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.log.error.Err;

@ThreadSafe
public class ModifiedJuliaErrorConsoleWatcher implements Closeable {

    private final BufferedReader errorReader;
    private final BufferedReader infoReader;

    private Thread errorThread;
    private Thread infoThread;

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
                while (true) {
                    try {
                        final String s = errorReader.readLine();
                        synchronized (errorMessage) {
                            errorMessage.append(s);
                        }
                        IScriptTaskRunnerJulia.LOG.warn(s);
                    } catch (final IOException e) {
                        throw Err.process(e);
                    }
                }
            }
        });
        errorThread.start();
        infoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final String s = infoReader.readLine();
                        IScriptTaskRunnerJulia.LOG.info(s);
                    } catch (final IOException e) {
                        throw Err.process(e);
                    }
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