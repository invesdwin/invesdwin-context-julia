package de.invesdwin.context.julia.runtime.libjuliaclj;

import java.util.concurrent.Future;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.libjuliaclj.internal.InitializingJuliaEngineWrapper;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.concurrent.lock.ILock;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class LibjuliacljScriptTaskRunnerJulia
        implements IScriptTaskRunnerJulia, FactoryBean<LibjuliacljScriptTaskRunnerJulia> {

    public static final LibjuliacljScriptTaskRunnerJulia INSTANCE = new LibjuliacljScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public LibjuliacljScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final LibjuliacljScriptTaskEngineJulia engine = new LibjuliacljScriptTaskEngineJulia(
                InitializingJuliaEngineWrapper.getInstance());
        final Future<T> future = engine.getSharedExecutor().submit(() -> {
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
                return result;
            } catch (final Throwable t) {
                throw Throwables.propagate(t);
            } finally {
                lock.unlock();
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
