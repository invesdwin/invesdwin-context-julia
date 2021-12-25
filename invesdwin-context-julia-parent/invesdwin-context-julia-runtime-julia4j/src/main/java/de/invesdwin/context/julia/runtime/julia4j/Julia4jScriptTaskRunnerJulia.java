package de.invesdwin.context.julia.runtime.julia4j;

import java.util.concurrent.Future;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.julia4j.internal.ExecutorJuliaEngineWrapper;
import de.invesdwin.context.julia.runtime.julia4j.internal.UnsafeJuliaEngineWrapper;
import de.invesdwin.util.concurrent.WrappedExecutorService;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class Julia4jScriptTaskRunnerJulia
        implements IScriptTaskRunnerJulia, FactoryBean<Julia4jScriptTaskRunnerJulia> {

    public static final WrappedExecutorService EXECUTOR = UnsafeJuliaEngineWrapper.EXECUTOR;

    public static final Julia4jScriptTaskRunnerJulia INSTANCE = new Julia4jScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public Julia4jScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final ExecutorJuliaEngineWrapper wrapper = ExecutorJuliaEngineWrapper.INSTANCE;
        final Future<T> future = EXECUTOR.submit(() -> {
            final Julia4jScriptTaskEngineJulia engine = new Julia4jScriptTaskEngineJulia(
                    UnsafeJuliaEngineWrapper.INSTANCE);
            final ILock lock = engine.getSharedLock();
            lock.lock();
            try {
                //inputs
                scriptTask.populateInputs(engine.getInputs());

                //execute
                scriptTask.executeScript(engine);

                //results
                final T result = scriptTask.extractResults(engine.getResults());
                engine.close();

                //return
                lock.unlock();
                return result;
            } catch (final Throwable t) {
                lock.unlock();
                throw Throwables.propagate(t);
            }
        });
        return Futures.getNoInterrupt(future);
    }

    @Override
    public Julia4jScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return Julia4jScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
