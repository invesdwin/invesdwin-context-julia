package de.invesdwin.context.julia.runtime.libjuliaclj.internal;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.IReentrantLock;

/**
 * Always acquire the lock first before accessing the julia engine instance. Also make sure commands are only executed
 * from inside the EXECUTOR thread. Otherwise julia will throw errors due to being thread bound.
 * 
 * https://cnuernber.github.io/libjulia-clj/signals.html
 */
@NotThreadSafe
public final class InitializingJuliaEngineWrapper implements IJuliaEngineWrapper {

    private static final InitializingJuliaEngineWrapper INSTANCE = new InitializingJuliaEngineWrapper();
    private boolean initialized = false;

    private InitializingJuliaEngineWrapper() {
    }

    public static boolean isInitialized() {
        return INSTANCE.initialized;
    }

    private void maybeInit() {
        if (initialized) {
            return;
        }
        synchronized (this) {
            if (initialized) {
                return;
            }
            if (UncheckedJuliaEngineWrapper.EXECUTOR.isExecutorThread()) {
                UncheckedJuliaEngineWrapper.INSTANCE.init();
            } else {
                if (UncheckedJuliaEngineWrapper.EXECUTOR.getPendingCount() > 0) {
                    throw new IllegalStateException(
                            "Initialization should already be taking place, would deadlock if another thread tried it!");
                }
                Futures.waitNoInterrupt(
                        UncheckedJuliaEngineWrapper.EXECUTOR.submit(UncheckedJuliaEngineWrapper.INSTANCE::init));
            }
            initialized = true;
        }

    }

    @Override
    public void eval(final String command) {
        maybeInit();
        UncheckedJuliaEngineWrapper.INSTANCE.eval(command);
    }

    @Override
    public JsonNode getAsJsonNode(final String variable) {
        maybeInit();
        return UncheckedJuliaEngineWrapper.INSTANCE.getAsJsonNode(variable);
    }

    @Override
    public void reset() {
        maybeInit();
        UncheckedJuliaEngineWrapper.INSTANCE.reset();
    }

    @Override
    public IReentrantLock getLock() {
        return UncheckedJuliaEngineWrapper.INSTANCE.getLock();
    }

    public static IJuliaEngineWrapper getInstance() {
        INSTANCE.maybeInit();
        return UncheckedJuliaEngineWrapper.INSTANCE;
    }
}
