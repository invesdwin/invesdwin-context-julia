package de.invesdwin.context.julia.runtime.libjuliaclj.internal;

import java.util.concurrent.Future;

import javax.annotation.concurrent.ThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.util.concurrent.WrappedExecutorService;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.IReentrantLock;

@ThreadSafe
public final class ExecutorJuliaEngineWrapper implements IJuliaEngineWrapper {

    private static final ExecutorJuliaEngineWrapper UNCHECKED = new ExecutorJuliaEngineWrapper(
            UncheckedJuliaEngineWrapper.INSTANCE);

    private final IJuliaEngineWrapper delegate;
    private final WrappedExecutorService executor;

    private ExecutorJuliaEngineWrapper(final IJuliaEngineWrapper delegate) {
        this.delegate = InitializingJuliaEngineWrapper.getInstance();
        this.executor = UncheckedJuliaEngineWrapper.EXECUTOR;
    }

    public IJuliaEngineWrapper getDelegate() {
        return delegate;
    }

    public WrappedExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void eval(final String command) {
        final Future<?> future = executor.submit(() -> delegate.eval(command));
        Futures.waitNoInterrupt(future);
    }

    @Override
    public JsonNode getAsJsonNode(final String variable) {
        final Future<JsonNode> future = executor.submit(() -> delegate.getAsJsonNode(variable));
        return Futures.getNoInterrupt(future);
    }

    @Override
    public void reset() {
        final Future<?> future = executor.submit(() -> delegate.reset());
        Futures.waitNoInterrupt(future);
    }

    @Override
    public IReentrantLock getLock() {
        return delegate.getLock();
    }

    public static ExecutorJuliaEngineWrapper getInstance() {
        if (InitializingJuliaEngineWrapper.isInitialized()) {
            return UNCHECKED;
        } else {
            return new ExecutorJuliaEngineWrapper(InitializingJuliaEngineWrapper.getInstance());
        }
    }

}
