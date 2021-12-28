package de.invesdwin.context.julia.runtime.libjuliaclj.internal;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import de.invesdwin.context.integration.marshaller.MarshallerJsonJackson;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.libjuliaclj.LibjuliacljProperties;
import de.invesdwin.context.julia.runtime.libjuliaclj.LibjuliacljScriptTaskEngineJulia;
import de.invesdwin.util.concurrent.Executors;
import de.invesdwin.util.concurrent.WrappedExecutorService;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.IReentrantLock;
import de.invesdwin.util.concurrent.lock.Locks;
import de.invesdwin.util.math.Booleans;

/**
 * Always acquire the lock first before accessing the julia engine instance. Also make sure commands are only executed
 * from inside the EXECUTOR thread. Otherwise julia will throw errors due to being thread bound.
 * 
 * https://cnuernber.github.io/libjulia-clj/signals.html
 */
@NotThreadSafe
public final class UnsafeJuliaEngineWrapper implements IJuliaEngineWrapper {

    public static final WrappedExecutorService EXECUTOR = Executors
            .newFixedThreadPool(UnsafeJuliaEngineWrapper.class.getSimpleName(), 1);
    public static final UnsafeJuliaEngineWrapper INSTANCE = new UnsafeJuliaEngineWrapper();

    private final IReentrantLock lock;
    private final JuliaResetContext resetContext;
    private final ObjectMapper mapper;
    private boolean initialized = false;

    private UnsafeJuliaEngineWrapper() {
        this.mapper = MarshallerJsonJackson.getInstance().getJsonMapper(false);
        this.lock = Locks.newReentrantLock(UnsafeJuliaEngineWrapper.class.getSimpleName() + "_lock");
        this.resetContext = new JuliaResetContext(new LibjuliacljScriptTaskEngineJulia(this));
    }

    private void maybeInit() {
        if (initialized) {
            return;
        }
        synchronized (this) {
            if (initialized) {
                return;
            }
            if (EXECUTOR.isExecutorThread()) {
                init();
            } else {
                if (EXECUTOR.getPendingCount() > 0) {
                    throw new IllegalStateException(
                            "Initialization should already be taking place, would deadlock if another thread tried it!");
                }
                Futures.waitNoInterrupt(EXECUTOR.submit(this::init));
            }
        }

    }

    private void init() {
        final Map<String, Object> initParams = new HashMap<String, Object>();
        //        initParams.put("n-threads", 8);
        //        initParams.put("signals-enabled?", false);
        initParams.put("julia-home", LibjuliacljProperties.JULIA_HOME.getAbsolutePath());
        final Object result = libjulia_clj.java_api.initialize(initParams);
        final String resultStr = String.valueOf(result);
        if (!":ok".equals(resultStr)) {
            throw new IllegalStateException("Initialization failed: " + resultStr);
        }
        evalUnchecked(
                "using InteractiveUtils; using Pkg; isinstalled(pkg::String) = any(x -> x.name == pkg && x.is_direct_dep, values(Pkg.dependencies())); if !isinstalled(\"JSON\"); Pkg.add(\"JSON\"); end; using JSON;");
        initialized = true;
        this.resetContext.init();
    }

    @Override
    public void eval(final String command) {
        maybeInit();
        evalUnchecked(command);
    }

    private void evalUnchecked(final String command) {
        final String adjCommand = command + "; true";
        IScriptTaskRunnerJulia.LOG.debug("> %s", command);
        final Object result = libjulia_clj.java_api.runString(adjCommand);
        IScriptTaskRunnerJulia.LOG.debug("< %s", result);
        if (!(result instanceof Boolean) || !Booleans.checkedCast(result)) {
            throw new IllegalStateException("Command [" + command + "] failed: " + result);
        }
    }

    @Override
    public JsonNode getAsJsonNode(final String variable) {
        maybeInit();
        final String command = "JSON.json(" + variable + ")";
        IScriptTaskRunnerJulia.LOG.debug("> get %s", variable);
        final Object result = libjulia_clj.java_api.runString(command);
        try {
            final JsonNode node = mapper.readTree(String.valueOf(result));
            if (node instanceof NullNode) {
                return null;
            } else {
                return node;
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        maybeInit();
        resetContext.reset();
    }

    @Override
    public IReentrantLock getLock() {
        return lock;
    }

}
