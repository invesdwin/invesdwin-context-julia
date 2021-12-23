package de.invesdwin.context.julia.runtime.juliacaller.pool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

import javax.annotation.concurrent.NotThreadSafe;

import org.expr.juliacaller.MaximumTriesForConnectionException;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;

@NotThreadSafe
public class ModifiedJuliaCaller {

    private final String pathToJulia;
    private Socket socket;
    private BufferedWriter bufferedWriterForJuliaConsole, bufferedWriterForSocket;
    private BufferedReader bufferedReaderForSocket;
    private final int port;
    private int maximumTriesToConnect = 60;
    private ModifiedJuliaErrorConsoleWatcher watcher;
    private Process process;

    public ModifiedJuliaCaller(final String pathToJulia, final int port) {
        this.pathToJulia = pathToJulia;
        this.port = port;
    }

    public void setMaximumTriesToConnect(final int tries) {
        this.maximumTriesToConnect = tries;
    }

    public int getMaximumTriesToConnect() {
        return this.maximumTriesToConnect;
    }

    public void startServer() throws IOException {
        process = Runtime.getRuntime().exec(pathToJulia);
        final InputStream is = this.getClass().getResourceAsStream("juliacaller.jl");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder sb = new StringBuilder();
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
            sb.append("\r\n");
        }
        reader.close();
        is.close();
        bufferedWriterForJuliaConsole = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        watcher = new ModifiedJuliaErrorConsoleWatcher(process);
        watcher.startWatching();

        bufferedWriterForJuliaConsole.write(sb.toString());
        bufferedWriterForJuliaConsole.newLine();
        IScriptTaskRunnerJulia.LOG.debug("startServer: Sending serve(%s) request.", this.port);
        bufferedWriterForJuliaConsole.write("serve(" + this.port + ")");
        bufferedWriterForJuliaConsole.newLine();
        bufferedWriterForJuliaConsole.flush();
    }

    public ModifiedJuliaErrorConsoleWatcher getWatcher() {
        return watcher;
    }

    public void connect() throws IOException {
        int numtries = 1;
        boolean connected = false;
        while (numtries <= this.maximumTriesToConnect) {
            try {
                socket = new Socket("localhost", this.port);
                connected = true;
                IScriptTaskRunnerJulia.LOG.trace("Connect: connected!");
                break;
            } catch (final ConnectException ce) {
                numtries++;
                try {
                    IScriptTaskRunnerJulia.LOG.warn("Connect: retrying to connect: %s / %s", numtries,
                            maximumTriesToConnect);
                    Thread.sleep(1000);
                } catch (final InterruptedException ie) {

                }
            }
        }
        if (connected) {
            bufferedWriterForSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReaderForSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } else {
            throw new MaximumTriesForConnectionException(
                    "Socket cannot connect in maximum number of iterations defined as " + maximumTriesToConnect);
        }
    }

    public synchronized void installPackage(final String pkg) throws IOException {
        IScriptTaskRunnerJulia.LOG.trace("Installing package %s", pkg);
        execute("install " + pkg);
    }

    public synchronized void execute(final String command) throws IOException {
        IScriptTaskRunnerJulia.LOG.trace("Execute: Sending '%s'", command);
        bufferedWriterForSocket.write("execute " + command);
        bufferedWriterForSocket.newLine();
        checkError();
    }

    public void checkError() {
        final String error = getWatcher().getErrorMessage();
        if (error != null) {
            throw new IllegalStateException(error);
        }
    }

    public synchronized void executeDefineFunction(final String command) throws IOException {
        IScriptTaskRunnerJulia.LOG.trace("Execute: Sending function definition");
        final File tempfile = File.createTempFile("juliacaller-function-definition", "jl");
        final BufferedWriter writer = new BufferedWriter(new FileWriter(tempfile));
        writer.write(command);
        writer.flush();
        writer.close();
        execute("include(\"" + tempfile.getAbsolutePath() + "\")");
    }

    public void exitSession() throws IOException {
        bufferedWriterForSocket.write("exit");
        bufferedWriterForSocket.newLine();
    }

    public void shutdownServer() throws IOException {
        watcher.close();
        watcher = null;
        bufferedWriterForSocket.write("shutdown");
        bufferedWriterForSocket.newLine();
    }

    public String getAsJSONString(final String varname) throws IOException {
        IScriptTaskRunnerJulia.LOG.debug("GetAsJSONString: Requesting variable %s", varname);
        bufferedWriterForSocket.write("get " + varname);
        bufferedWriterForSocket.newLine();
        bufferedWriterForSocket.flush();
        final String result = bufferedReaderForSocket.readLine();
        checkError();
        return result;
    }

}
