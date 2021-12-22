package de.invesdwin.context.julia.runtime.ju4ja;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.ju4ja.internal.LoggingRMainLoopCallbacks;
import de.invesdwin.context.julia.runtime.ju4ja.internal.RengineWrapper;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class Ju4jaScriptTaskRunnerJulia implements IScriptTaskRunnerJulia, FactoryBean<Ju4jaScriptTaskRunnerJulia> {

    public static final Ju4jaScriptTaskRunnerJulia INSTANCE = new Ju4jaScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public Ju4jaScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final Ju4jaScriptTaskEngineJulia engine = new Ju4jaScriptTaskEngineJulia(RengineWrapper.INSTANCE);
        engine.getSharedLock().lock();
        try {
            //inputs
            scriptTask.populateInputs(engine.getInputs());

            //execute
            scriptTask.executeScript(engine);

            //results
            final T result = scriptTask.extractResults(engine.getResults());
            engine.close();

            //return
            engine.getSharedLock().unlock();
            return result;
        } catch (final Throwable t) {
            engine.getSharedLock().unlock();
            throw Throwables.propagate(t);
        } finally {
            LoggingRMainLoopCallbacks.INSTANCE.reset();
        }
    }

    @Override
    public Ju4jaScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return Ju4jaScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
