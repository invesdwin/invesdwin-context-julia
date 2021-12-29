package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang3.mutable.MutableInt;
import org.arl.jajub.ByteArray;
import org.arl.jajub.DoubleArray;
import org.arl.jajub.FloatArray;
import org.arl.jajub.IntegerArray;
import org.arl.jajub.JuliaExpr;
import org.arl.jajub.LongArray;
import org.arl.jajub.ShortArray;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.util.concurrent.loop.ASpinWait;
import de.invesdwin.util.time.date.FTimeUnit;
import de.invesdwin.util.time.duration.Duration;

/**
 * Fork of: https://github.com/org-arl/jajub/issues/2
 */
@NotThreadSafe
public class ModifiedJuliaBridge {

    private static final int CR = 10;
    private static final Duration TIMEOUT = new Duration(10000, FTimeUnit.MILLISECONDS);
    private static final String TERMINATOR_RAW = "__##@@##__";
    private static final String TERMINATOR = "\"" + TERMINATOR_RAW + "\"";

    private static final String[] JULIA_EXEC = { "bin/julia", "bin/julia.exe" };

    private static final String[] JULIA_ARGS = { "-iq", "--startup-file=no", "-e", "using InteractiveUtils;" //
            + "__type__(::AbstractArray{T,N}) where T where N = Array{T,N};" //
            + "__type__(a) = typeof(a);" //
            + "using Pkg; isinstalled(pkg::String) = any(x -> x.name == pkg && x.is_direct_dep, values(Pkg.dependencies())); if !isinstalled(\"JSON\"); Pkg.add(\"JSON\"); end; using JSON;" //
            + "println(" + TERMINATOR + ");" };
    private static final String TERMINATOR_SUFFIX = ";\n" + TERMINATOR;
    private static final byte[] TERMINATOR_SUFFIX_BYTES = TERMINATOR_SUFFIX.getBytes();

    private final ProcessBuilder jbuilder;
    private Process julia = null;
    private InputStream inp = null;
    private OutputStream out = null;
    private String ver = null;

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

    /**
     * Creates a Java-Julia bridge with custom Julia arguments.
     *
     * @param jargs
     *            custom Julia command-line arguments.
     */
    public ModifiedJuliaBridge(final String... jargs) {
        final List<String> j = new ArrayList<String>();
        j.add(getJuliaExec());
        j.addAll(Arrays.asList(jargs));
        j.addAll(Arrays.asList(JULIA_ARGS));
        jbuilder = new ProcessBuilder(j);
    }

    /**
     * Creates a Java-Julia bridge with custom Julia command and arguments.
     *
     * @param jcmd
     *            custom Julia executable/command.
     * @param jargs
     *            custom Julia command-line arguments.
     */
    public ModifiedJuliaBridge(final String jcmd, final String... jargs) {
        final List<String> j = new ArrayList<String>();
        j.add(jcmd);
        j.addAll(Arrays.asList(jargs));
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
        jbuilder.redirectErrorStream(true);
        julia = jbuilder.start();
        inp = julia.getInputStream();
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
        inp = null;
        out = null;
        ver = null;
    }

    /**
     * Gets Julia version.
     */
    public String getJuliaVersion() {
        openIfNecessary();
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
    public List<String> exec(final String jcode, final Duration timeout) {
        openIfNecessary();
        final List<String> rsp = new ArrayList<String>();
        try {
            flush();
            IScriptTaskRunnerJulia.LOG.debug("> " + jcode);
            out.write(jcode.getBytes());
            out.write(TERMINATOR_SUFFIX_BYTES);
            out.write(CR);
            out.flush();
            while (true) {
                final String s = readline(timeout);
                if (s == null) {
                    //retry, we were a bit too fast as it seems
                    continue;
                }
                if (s.equals(TERMINATOR)) {
                    return rsp;
                }
                rsp.add(s);
            }
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (final IOException ex) {
            throw new RuntimeException("JuliaBridge connection broken", ex);
        }
        return rsp;
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
    public List<String> exec(final JuliaExpr jcode, final Duration timeout) {
        return exec(jcode.toString(), timeout);
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param istream
     *            input stream to read Julia code from.
     * @param timeout
     *            timeout in milliseconds.
     * @return output (stdout + stderr).
     */
    public List<String> exec(final InputStream istream, final Duration timeout) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        while (true) {
            final String s = reader.readLine();
            if (s == null) {
                break;
            }
            sb.append(s);
            sb.append('\n');
        }
        return exec(sb.toString(), timeout);
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param jcode
     *            Julia code to run
     * @return output (stdout + stderr).
     */
    public List<String> exec(final String jcode) {
        return exec(jcode, TIMEOUT);
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param jcode
     *            Julia code to run
     * @return output (stdout + stderr).
     */
    public List<String> exec(final JuliaExpr jcode) {
        return exec(jcode.toString(), TIMEOUT);
    }

    /**
     * Executes Julia code and returns the output.
     *
     * @param istream
     *            input stream to read Julia code from.
     * @return output (stdout + stderr).
     */
    public List<String> exec(final InputStream istream) throws IOException {
        return exec(istream, TIMEOUT);
    }

    /**
     * Sets a variable in the Julia environment.
     *
     * @param varname
     *            name of the variable.
     * @param value
     *            value to bind to the variable.
     */
    public void set(final String varname, final Object value) {
        try {
            final String s = jexpr(value);
            if (s != null) {
                exec(varname + " = " + s);
            } else if (value instanceof LongArray) {
                writeNumeric(varname, ((LongArray) value).data, ((LongArray) value).dims,
                        ((LongArray) value).isComplex);
            } else if (value instanceof IntegerArray) {
                writeNumeric(varname, ((IntegerArray) value).data, ((IntegerArray) value).dims,
                        ((IntegerArray) value).isComplex);
            } else if (value instanceof ShortArray) {
                writeNumeric(varname, ((ShortArray) value).data, ((ShortArray) value).dims,
                        ((ShortArray) value).isComplex);
            } else if (value instanceof ByteArray) {
                writeNumeric(varname, ((ByteArray) value).data, ((ByteArray) value).dims,
                        ((ByteArray) value).isComplex);
            } else if (value instanceof DoubleArray) {
                writeNumeric(varname, ((DoubleArray) value).data, ((DoubleArray) value).dims,
                        ((DoubleArray) value).isComplex);
            } else if (value instanceof FloatArray) {
                writeNumeric(varname, ((FloatArray) value).data, ((FloatArray) value).dims,
                        ((FloatArray) value).isComplex);
            } else if (value instanceof long[]) {
                writeNumeric(varname, (long[]) value, new int[] { ((long[]) value).length }, false);
            } else if (value instanceof int[]) {
                writeNumeric(varname, (int[]) value, new int[] { ((int[]) value).length }, false);
            } else if (value instanceof short[]) {
                writeNumeric(varname, (short[]) value, new int[] { ((short[]) value).length }, false);
            } else if (value instanceof byte[]) {
                writeNumeric(varname, (byte[]) value, new int[] { ((byte[]) value).length }, false);
            } else if (value instanceof double[]) {
                writeNumeric(varname, (double[]) value, new int[] { ((double[]) value).length }, false);
            } else if (value instanceof float[]) {
                writeNumeric(varname, (float[]) value, new int[] { ((float[]) value).length }, false);
            } else {
                throw new RuntimeException("Unsupported type: " + value.getClass().getName());
            }
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (final IOException ex) {
            throw new RuntimeException("JuliaBridge connection broken", ex);
        }
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
        List<String> rsp = exec("__ans__ = " + varname + "; println(__type__(__ans__))");
        if (rsp.size() < 1) {
            throw new RuntimeException("Invalid response from Julia REPL: " + rsp);
        }
        //WORKAROUND: always extract the last output as the type because the executed code might have printed another line
        String type = rsp.get(rsp.size() - 1);
        if (type.contains("ERROR: UndefVarError")) {
            return null;
        }
        if ("Nothing".equals(type)) {
            return null;
        }
        if ("Missing".equals(type)) {
            return null;
        }
        try {
            if ("String".equals(type) || "Char".equals(type)) {
                rsp = exec("println(sizeof(__ans__))");
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
                rsp = exec("println(size(" + varname + "))");
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
            if ("Int64".equals(type)) {
                return readNumeric(8, false, dims, false);
            }
            if ("Int32".equals(type)) {
                return readNumeric(4, false, dims, false);
            }
            if ("Int16".equals(type)) {
                return readNumeric(2, false, dims, false);
            }
            if ("Int8".equals(type)) {
                return readNumeric(1, false, dims, false);
            }
            if ("Float64".equals(type)) {
                return readNumeric(8, true, dims, false);
            }
            if ("Float32".equals(type)) {
                return readNumeric(4, true, dims, false);
            }
            if ("Complex{Int64}".equals(type)) {
                return readNumeric(8, false, dims, true);
            }
            if ("Complex{Int32}".equals(type)) {
                return readNumeric(4, false, dims, true);
            }
            if ("Complex{Int16}".equals(type)) {
                return readNumeric(2, false, dims, true);
            }
            if ("Complex{Int8}".equals(type)) {
                return readNumeric(1, false, dims, true);
            }
            if ("Complex{Float64}".equals(type)) {
                return readNumeric(8, true, dims, true);
            }
            if ("Complex{Float32}".equals(type)) {
                return readNumeric(4, true, dims, true);
            }
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

    private void openIfNecessary() {
        if (isOpen()) {
            return;
        }
        try {
            open();
        } catch (final IOException ex) {
            throw new RuntimeException("Unable to open JuliaBridge", ex);
        }
    }

    //CHECKSTYLE:OFF
    private String jexpr(final Object value) {
        //CHECKSTYLE:ON
        if (value == null) {
            return "nothing";
        }
        if (value instanceof JuliaExpr) {
            return value.toString();
        }
        if (value instanceof String) {
            return "raw\"" + ((String) value).replace("\"", "\\\"") + "\"";
        }
        if (value instanceof Long) {
            return "Int64(" + value + ")";
        }
        if (value instanceof Integer) {
            return "Int32(" + value + ")";
        }
        if (value instanceof Short) {
            return "Int16(" + value + ")";
        }
        if (value instanceof Byte) {
            return "Int8(" + value + ")";
        }
        if (value instanceof Double) {
            return "Float64(" + value + ")";
        }
        if (value instanceof Float) {
            return "Float32(" + value + ")";
        }
        return null;
    }

    private Object readNumeric(final int nbytes, final boolean fp) throws IOException, InterruptedException {
        final byte[] buf = new byte[nbytes];
        read(buf, TIMEOUT);
        if (nbytes == 1) {
            return buf[0];
        }
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buf).order(ByteOrder.nativeOrder());
        if (fp) {
            switch (nbytes) {
            case 4:
                return bb.asFloatBuffer().get();
            case 8:
                return bb.asDoubleBuffer().get();
            default:
                //noop
            }
        } else {
            switch (nbytes) {
            case 2:
                return bb.asShortBuffer().get();
            case 4:
                return bb.asIntBuffer().get();
            case 8:
                return bb.asLongBuffer().get();
            default:
                //noop
            }
        }
        return null;
    }

    //CHECKSTYLE:OFF
    private Object readNumeric(final int nbytes, final boolean fp, int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        //CHECKSTYLE:ON
        if (dims == null && !cplx) {
            return readNumeric(nbytes, fp);
        }
        int nelem = 1;
        if (dims == null) {
            //CHECKSTYLE:OFF
            dims = new int[0];
            //CHECKSTYLE:ON
        }
        for (int i = 0; i < dims.length; i++) {
            nelem *= dims[i];
        }
        if (cplx) {
            nelem *= 2;
        }
        final byte[] buf = new byte[nbytes * nelem];
        read(buf, TIMEOUT);
        if (nbytes == 1) {
            final ByteArray a = new ByteArray();
            a.data = buf;
            a.dims = dims;
            a.isComplex = cplx;
            return a;
        }
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(buf).order(ByteOrder.nativeOrder());
        if (fp) {
            switch (nbytes) {
            case 4: {
                final float[] data = new float[nelem];
                bb.asFloatBuffer().get(data);
                final FloatArray a = new FloatArray();
                a.data = data;
                a.dims = dims;
                a.isComplex = cplx;
                return a;
            }
            case 8: {
                final double[] data = new double[nelem];
                bb.asDoubleBuffer().get(data);
                final DoubleArray a = new DoubleArray();
                a.data = data;
                a.dims = dims;
                a.isComplex = cplx;
                return a;
            }
            default:
                //noop
            }
        } else {
            switch (nbytes) {
            case 2: {
                final short[] data = new short[nelem];
                bb.asShortBuffer().get(data);
                final ShortArray a = new ShortArray();
                a.data = data;
                a.dims = dims;
                a.isComplex = cplx;
                return a;
            }
            case 4: {
                final int[] data = new int[nelem];
                bb.asIntBuffer().get(data);
                final IntegerArray a = new IntegerArray();
                a.data = data;
                a.dims = dims;
                a.isComplex = cplx;
                return a;
            }
            case 8: {
                final long[] data = new long[nelem];
                bb.asLongBuffer().get(data);
                final LongArray a = new LongArray();
                a.data = data;
                a.dims = dims;
                a.isComplex = cplx;
                return a;
            }
            default:
                //noop
            }
        }
        return null;
    }

    private void checkArrayDims(final int len, final int[] dims, final boolean cplx) {
        int nelem = 1;
        for (int i = 0; i < dims.length; i++) {
            nelem *= dims[i];
        }
        if (cplx) {
            nelem *= 2;
        }
        if (nelem != len) {
            throw new RuntimeException("Bad array dimensions");
        }
    }

    private String buildArrayWriter(final String varname, final String type, final int[] dims) {
        final StringBuilder sb = new StringBuilder();
        sb.append(varname);
        sb.append(" = Array{");
        sb.append(type);
        sb.append("}(undef");
        for (int i = 0; i < dims.length; i++) {
            sb.append(", ");
            sb.append(dims[i]);
        }
        sb.append("); read!(stdin, ");
        sb.append(varname);
        sb.append("); ");
        sb.append(TERMINATOR);
        return sb.toString();
    }

    private void waitUntilTerminator() throws IOException, InterruptedException {
        while (true) {
            final String s = readline(TIMEOUT);
            if (s == null || s.equals(TERMINATOR)) {
                return;
            }
        }
    }

    private void writeNumeric(final String varname, final long[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Int64}" : "Int64", dims));
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(data.length * 8);
        bb.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            bb.putLong(data[i]);
        }
        write(bb.array());
        waitUntilTerminator();
    }

    private void writeNumeric(final String varname, final int[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Int32}" : "Int32", dims));
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            bb.putInt(data[i]);
        }
        write(bb.array());
        waitUntilTerminator();
    }

    private void writeNumeric(final String varname, final short[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Int16}" : "Int16", dims));
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(data.length * 2);
        bb.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            bb.putShort(data[i]);
        }
        write(bb.array());
        waitUntilTerminator();
    }

    private void writeNumeric(final String varname, final byte[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Int8}" : "Int8", dims));
        write(data);
        waitUntilTerminator();
    }

    private void writeNumeric(final String varname, final double[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Float64}" : "Float64", dims));
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(data.length * 8);
        bb.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            bb.putDouble(data[i]);
        }
        write(bb.array());
        waitUntilTerminator();
    }

    private void writeNumeric(final String varname, final float[] data, final int[] dims, final boolean cplx)
            throws IOException, InterruptedException {
        checkArrayDims(data.length, dims, cplx);
        write(buildArrayWriter(varname, cplx ? "Complex{Float32}" : "Float32", dims));
        final java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        for (int i = 0; i < data.length; i++) {
            bb.putFloat(data[i]);
        }
        write(bb.array());
        waitUntilTerminator();
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
        out.write(CR);
        out.flush();
    }

    private void write(final byte[] b) throws IOException {
        //        IScriptTaskRunnerJulia.LOG.debug("> (" + (b.length) + " bytes)");
        out.write(b);
        out.write(CR);
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
                    if (b == CR) {
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
