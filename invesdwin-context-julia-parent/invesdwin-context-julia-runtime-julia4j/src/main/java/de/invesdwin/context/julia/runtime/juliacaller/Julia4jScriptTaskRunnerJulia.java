package de.invesdwin.context.julia.runtime.juliacaller;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.juliacaller.pool.ExtendedJuliaCaller;
import de.invesdwin.context.julia.runtime.juliacaller.pool.Julia4jObjectPool;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class Julia4jScriptTaskRunnerJulia
        implements IScriptTaskRunnerJulia, FactoryBean<Julia4jScriptTaskRunnerJulia> {

    public static final Julia4jScriptTaskRunnerJulia INSTANCE = new Julia4jScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public Julia4jScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final ExtendedJuliaCaller juliaCaller = Julia4jObjectPool.INSTANCE.borrowObject();
        try {
            //inputs
            final Julia4jScriptTaskEngineJulia engine = new Julia4jScriptTaskEngineJulia(juliaCaller);
            scriptTask.populateInputs(engine.getInputs());

            //execute
            scriptTask.executeScript(engine);

            //results
            final T result = scriptTask.extractResults(engine.getResults());
            engine.close();

            //return
            Julia4jObjectPool.INSTANCE.returnObject(juliaCaller);
            return result;
        } catch (final Throwable t) {
            Julia4jObjectPool.INSTANCE.destroyObject(juliaCaller);
            throw Throwables.propagate(t);
        }
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
