package de.invesdwin.context.julia.runtime.libjuliaclj;

import java.util.concurrent.Future;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.libjuliaclj.internal.UnsafeJuliaEngineWrapper;
import de.invesdwin.util.concurrent.WrappedExecutorService;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class LibjuliacljScriptTaskRunnerJulia
        implements IScriptTaskRunnerJulia, FactoryBean<LibjuliacljScriptTaskRunnerJulia> {

    public static final WrappedExecutorService EXECUTOR = UnsafeJuliaEngineWrapper.EXECUTOR;

    public static final LibjuliacljScriptTaskRunnerJulia INSTANCE = new LibjuliacljScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public LibjuliacljScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final Future<T> future = EXECUTOR.submit(() -> {
            final LibjuliacljScriptTaskEngineJulia engine = new LibjuliacljScriptTaskEngineJulia(
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
    public LibjuliacljScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return LibjuliacljScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
