package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang3.mutable.MutableInt;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.util.concurrent.Executors;
import de.invesdwin.util.concurrent.loop.ASpinWait;
import de.invesdwin.util.lang.Closeables;
import de.invesdwin.util.time.date.FTimeUnit;
import de.invesdwin.util.time.duration.Duration;

/**
 * Fork of: https://github.com/org-arl/jajub/issues/2
 */
@NotThreadSafe
public class ModifiedJuliaBridge {

    private static final char NEW_LINE = '\n';
    private static final Duration TIMEOUT = new Duration(10000, FTimeUnit.MILLISECONDS);
    private static final String TERMINATOR_RAW = "__##@@##__";
    private static final String TERMINATOR = "\"" + TERMINATOR_RAW + "\"";

    private static final String[] JULIA_EXEC = { "bin/julia", "bin/julia.exe" };

    private static final String[] JULIA_ARGS = { "-iq", "--depwarn=no", "--startup-file=no",
            "--threads=" + Executors.getCpuThreadPoolCount(), "-e", "using InteractiveUtils;" //
                    + "__type__(::AbstractArray{T,N}) where T where N = Array{T,N};" //
                    + "__type__(a) = typeof(a);" //
                    + "using Pkg; isinstalled(pkg::String) = any(x -> x.name == pkg && x.is_direct_dep, values(Pkg.dependencies())); if !isinstalled(\"JSON\"); Pkg.add(\"JSON\"); end; using JSON;" //
                    + "println(" + TERMINATOR + ");" };
    private static final String TERMINATOR_SUFFIX = ";\n" + TERMINATOR;
    private static final byte[] TERMINATOR_SUFFIX_BYTES = TERMINATOR_SUFFIX.getBytes();

    private final ProcessBuilder jbuilder;
    private Process julia = null;
    private InputStream inp = null;
    private ModifiedJuliaErrorConsoleWatcher errWatcher = null;
    private OutputStream out = null;
    private String ver = null;

    private final List<String> rsp = new ArrayList<>();

    ////// public API

    /**
     * Creates a Java-Julia bridge with default settings.
     */
    public ModifiedJuliaBridge() {
        final List<String> j = new ArrayList<String>();
        j.add(getJuliaExec());
        j.addAll(Arrays.asList(JULIA_ARGS));
        jbuilder = new ProcessBuilder(j);
    }

    //CHECKSTYLE:OFF
    @Override
    public void finalize() {
        //CHECKSTYLE:ON
        close();
    }

    /**
     * Checks if Julia process is already running.
     */
    public boolean isOpen() {
        return julia != null;
    }

    public ModifiedJuliaErrorConsoleWatcher getErrWatcher() {
        return errWatcher;
    }

    /**
     * Starts the Julia process.
     *
     * @param timeout
     *            timeout in milliseconds for process to start.
     */
    public void open(final Duration timeout) throws IOException, InterruptedException {
        if (isOpen()) {
            return;
        }
        julia = jbuilder.start();
        inp = julia.getInputStream();
        errWatcher = new ModifiedJuliaErrorConsoleWatcher(julia);
        errWatcher.startWatching();
        out = julia.getOutputStream();
        while (true) {
            final String s = readline(timeout);
            if (s == null) {
                close();
                throw new IOException("Bad Julia process");
            }
            if (s.startsWith("Julia Version ")) {
                ver = s;
            } else if (TERMINATOR.contains(s)) {
                break;
            }
        }
    }

    /**
     * Starts the Julia process.
     */
    public void open() throws IOException {
        try {
            open(TIMEOUT);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stops a running Julia process.
     */
    public void close() {
        if (!isOpen()) {
            return;
        }
        julia.destroy();
        julia = null;
        Closeables.closeQuietly(inp);
        inp = null;
        Closeables.closeQuietly(errWatcher);
        errWatcher = null;
        Closeables.closeQuietly(out);
        out = null;
        ver = null;
    }

    /**
     * Gets Julia version.
     */
    public String getJuliaVersion() {
        return ver;
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param jcode
     *            Julia code to run.
     * @param timeout
     *            timeout in milliseconds.
     * @return output (stdout + stderr).
     */
    public void exec(final String jcode, final Duration timeout) {
        rsp.clear();
        try {
            flush();
            IScriptTaskRunnerJulia.LOG.debug("> " + jcode);
            out.write(jcode.getBytes());
            out.write(TERMINATOR_SUFFIX_BYTES);
            out.write(NEW_LINE);
            out.flush();
            while (true) {
                final String s = readline(timeout);
                if (s == null) {
                    //retry, we were a bit too fast as it seems
                    continue;
                }
                if (s.equals(TERMINATOR)) {
                    return;
                }
                rsp.add(s);
            }
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (final IOException ex) {
            throw new RuntimeException("JuliaBridge connection broken", ex);
        }
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param jcode
     *            Julia code to run
     * @return output (stdout + stderr).
     */
    public void exec(final String jcode) {
        exec(jcode, TIMEOUT);
    }

    /**
     * Sets a variable in the Julia environment.
     *
     * @param varname
     *            name of the variable.
     * @param value
     *            value to bind to the variable.
     */
    public void set(final String varname, final String value) {
        final String s = jexpr(value);
        exec(varname + " = " + s);
    }

    /**
     * Gets a variable from the Julia environment.
     *
     * @param varname
     *            name of the variable.
     * @return value bound to the variable, null if unavailable.
     */
    //CHECKSTYLE:OFF
    public Object get(final String varname) {
        //CHECKSTYLE:ON
        exec("__ans__ = " + varname + "; println(__type__(__ans__))");
        if (rsp.size() < 1) {
            throw new RuntimeException("Invalid response from Julia REPL: " + rsp);
        }
        //WORKAROUND: always extract the last output as the type because the executed code might have printed another line
        String type = rsp.get(rsp.size() - 1);
        if ("Nothing".equals(type)) {
            return null;
        }
        if ("Missing".equals(type)) {
            return null;
        }
        try {
            if ("String".equals(type) || "Char".equals(type)) {
                exec("println(sizeof(__ans__))");
                if (rsp.size() < 1) {
                    throw new RuntimeException("Invalid response from Julia REPL");
                }
                final int n = Integer.parseInt(rsp.get(0));
                write("write(stdout, __ans__)");
                final byte[] buf = new byte[n];
                read(buf, TIMEOUT);
                return new String(buf);
            }
            int[] dims = null;
            if (type.startsWith("Array{") && type.endsWith("}")) {
                int p = type.indexOf(',');
                if (p < 0) {
                    throw new RuntimeException("Invalid response from Julia REPL");
                }
                final int d = Integer.parseInt(type.substring(p + 1, type.length() - 1));
                dims = new int[d];
                type = type.substring(6, p);
                exec("println(size(" + varname + "))");
                if (rsp.size() < 1) {
                    throw new RuntimeException("Invalid response from Julia REPL");
                }
                final String s = rsp.get(0);
                int ofs = 1;
                for (int i = 0; i < d; i++) {
                    p = s.indexOf(',', ofs);
                    if (p < 0) {
                        p = s.indexOf(')', ofs);
                    }
                    if (p < 0) {
                        throw new RuntimeException("Invalid response from Julia REPL");
                    }
                    dims[i] = Integer.parseInt(s.substring(ofs, p).trim());
                    ofs = p + 1;
                }
            }
            write("write(stdout, " + varname + ")");
            throw new RuntimeException("Unsupported type " + type);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (final IOException ex) {
            throw new RuntimeException("JuliaBridge connection broken", ex);
        }
        return null;
    }

    /**
     * Evaluates an expression in Julia.
     *
     * @param jcode
     *            expression to evaluate.
     * @return value of the expression.
     */
    public void eval(final String jcode) {
        exec(jcode);
    }

    ////// private stuff

    //CHECKSTYLE:OFF
    private String jexpr(final String value) {
        //CHECKSTYLE:ON
        if (value == null) {
            return "nothing";
        } else {
            return "raw\"" + value.replace("\"", "\\\"") + "\"";
        }
    }

    private String getJuliaExec() {
        final String jhome = System.getenv("JULIA_HOME");
        if (jhome != null) {
            try {
                for (final String name : JULIA_EXEC) {
                    final File f = new File(jhome, name);
                    if (f.canExecute()) {
                        return f.getCanonicalPath();
                    }
                }
            } catch (final IOException ex) {
                // do nothing
            }
        }
        return "julia";
    }

    private void write(final String s) throws IOException {
        IScriptTaskRunnerJulia.LOG.debug("> " + s);
        out.write(s.getBytes());
        out.write(NEW_LINE);
        out.flush();
    }

    private void flush() throws IOException {
        while (inp.available() > 0) {
            inp.read();
        }
    }

    private int read(final byte[] buf, final Duration timeout) throws IOException, InterruptedException {
        final MutableInt ofs = new MutableInt(0);
        //WORKAROUND: sleeping 10 ms between messages is way too slow
        final ASpinWait spinWait = new ASpinWait() {
            @Override
            public boolean isConditionFulfilled() throws Exception {
                int n = inp.available();
                while (n > 0 && !Thread.interrupted()) {
                    final int m = buf.length - ofs.intValue();
                    ofs.add(inp.read(buf, ofs.intValue(), n > m ? m : n));
                    if (ofs.intValue() == buf.length) {
                        return true;
                    }
                    n = inp.available();
                }
                return false;
            }

            @Override
            protected boolean isSpinAllowed(final long waitingSinceNanos) {
                //spinning not needed, wastes cpu cycles that julia could use
                return false;
            }
        };
        try {
            spinWait.awaitFulfill(System.nanoTime(), timeout);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        //        IScriptTaskRunnerJulia.LOG.debug("< (" + ofs + " bytes)");
        return ofs.intValue();
    }

    private String readline(final Duration timeout) throws IOException, InterruptedException {
        final StringBuilder sb = new StringBuilder();
        //WORKAROUND: sleeping 10 ms between messages is way too slow
        final ASpinWait spinWait = new ASpinWait() {
            @Override
            public boolean isConditionFulfilled() throws Exception {
                while (inp.available() > 0 && !Thread.interrupted()) {
                    final int b = inp.read();
                    if (b == NEW_LINE) {
                        return true;
                    }
                    sb.append((char) b);
                }
                return false;
            }

            @Override
            protected boolean isSpinAllowed(final long waitingSinceNanos) {
                //spinning not needed, wastes cpu cycles that julia could use
                return false;
            }
        };
        try {
            spinWait.awaitFulfill(System.nanoTime(), timeout);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        if (sb.length() == 0) {
            return null;
        }
        final String s = sb.toString();
        if (!TERMINATOR_RAW.equals(s) && !TERMINATOR.equals(s)) {
            IScriptTaskRunnerJulia.LOG.debug("< " + s);
        }
        return s;
    }

}
