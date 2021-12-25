package de.invesdwin.context.julia.runtime.julia4j.internal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.concurrent.NotThreadSafe;

import org.julia.jni.NativeUtils;
import org.julia.jni.swig.Julia4J;
import org.julia.jni.swig.SWIGTYPE_p_jl_value_t;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import de.invesdwin.context.ContextProperties;
import de.invesdwin.context.integration.marshaller.MarshallerJsonJackson;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.julia4j.Julia4jProperties;
import de.invesdwin.context.julia.runtime.julia4j.Julia4jScriptTaskEngineJulia;
import de.invesdwin.util.concurrent.lock.IReentrantLock;
import de.invesdwin.util.concurrent.lock.Locks;
import de.invesdwin.util.lang.Files;

/**
 * Always acquire the lock first before accessing the rengine instance.
 */
@NotThreadSafe
public final class JuliaEngineWrapper {

    public static final JuliaEngineWrapper INSTANCE = new JuliaEngineWrapper();

    private final IReentrantLock lock;
    private final JuliaResetContext resetContext;
    private final ObjectMapper mapper;
    private final File outputFile = new File(ContextProperties.TEMP_DIRECTORY,
            JuliaEngineWrapper.class.getSimpleName() + ".out");
    private final String outputFilePath = outputFile.getAbsolutePath();

    private JuliaEngineWrapper() {
        final String path = new File(Julia4jProperties.JULIA_LIBRARY_PATH, "libjulia.so").getAbsolutePath();
        System.load(path);
        try {
            ModifiedNativeUtils.loadLibraryFromJar(NativeUtils.libnameToPlatform("libjulia4j"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (Julia4J.jl_is_initialized() == 0) {
            Julia4J.jl_init();
            eval("using InteractiveUtils; using Pkg; isinstalled(pkg::String) = any(x -> x.name == pkg && x.is_direct_dep, values(Pkg.dependencies())); if !isinstalled(\"JSON\"); Pkg.add(\"JSON\"); end; using JSON;");
        }
        this.mapper = MarshallerJsonJackson.getInstance().getJsonMapper(false);
        this.lock = Locks.newReentrantLock(JuliaEngineWrapper.class.getSimpleName() + "_lock");
        this.resetContext = new JuliaResetContext(new Julia4jScriptTaskEngineJulia(this));
        this.resetContext.init();
    }

    public void eval(final String command) {
        final String adjCommand = command + "; true";
        IScriptTaskRunnerJulia.LOG.debug("> %s", adjCommand);
        final SWIGTYPE_p_jl_value_t value = Julia4J.jl_eval_string(adjCommand);
        IScriptTaskRunnerJulia.LOG.debug("< %s", Julia4J.jl_unbox_bool(value));
    }

    public JsonNode getAsJsonNode(final String variable) {
        final String command = "try; open(\"" + outputFilePath + "\", \"w\") do io; write(io, JSON.json(" + variable
                + ")) end; catch err; write(io, sprint(showerror, err, catch_backtrace())) end; true";
        IScriptTaskRunnerJulia.LOG.debug("> %s", command);
        final SWIGTYPE_p_jl_value_t value = Julia4J.jl_eval_string(command);
        IScriptTaskRunnerJulia.LOG.debug("< %s", value);
        try {
            final String result = Files.readFileToString(outputFile, Charset.defaultCharset());
            final JsonNode node = mapper.readTree(result);
            if (node instanceof NullNode) {
                return null;
            } else {
                return node;
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        resetContext.reset();
    }

    public IReentrantLock getLock() {
        return lock;
    }

}
