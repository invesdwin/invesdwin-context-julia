package de.invesdwin.context.julia.runtime.juliacaller.pool;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.log.error.Err;

@ThreadSafe
public class ModifiedJuliaErrorConsoleWatcher implements Closeable {

    private final StringBuilder errorMessage = new StringBuilder();
    private final BufferedReader errorReader;
    private final BufferedReader infoReader;

    private Thread errorThread;
    private Thread infoThread;

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
                        errorMessage.append(s);
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
    public synchronized void close() {
        errorThread.interrupt();
        infoThread.interrupt();
        clearLog();
    }

    public synchronized void clearLog() {
        errorMessage.setLength(0);
    }

    public synchronized String getErrorMessage() {
        if (errorMessage.length() == 0) {
            return null;
        }
        return String.valueOf(errorMessage).trim();
    }
}