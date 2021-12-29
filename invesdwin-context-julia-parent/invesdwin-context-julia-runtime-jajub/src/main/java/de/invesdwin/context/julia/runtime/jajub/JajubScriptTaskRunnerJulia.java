package de.invesdwin.context.julia.runtime.jajub;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.jajub.pool.ExtendedJuliaBridge;
import de.invesdwin.context.julia.runtime.jajub.pool.JajubObjectPool;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class JajubScriptTaskRunnerJulia
        implements IScriptTaskRunnerJulia, FactoryBean<JajubScriptTaskRunnerJulia> {

    public static final JajubScriptTaskRunnerJulia INSTANCE = new JajubScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public JajubScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final ExtendedJuliaBridge juliaCaller = JajubObjectPool.INSTANCE.borrowObject();
        try {
            //inputs
            final JajubScriptTaskEngineJulia engine = new JajubScriptTaskEngineJulia(juliaCaller);
            scriptTask.populateInputs(engine.getInputs());

            //execute
            scriptTask.executeScript(engine);

            //results
            final T result = scriptTask.extractResults(engine.getResults());
            engine.close();

            //return
            JajubObjectPool.INSTANCE.returnObject(juliaCaller);
            return result;
        } catch (final Throwable t) {
            //we have to destroy instances on exceptions, otherwise e.g. SFrontiers.jl might get stuck with some inconsistent state
            JajubObjectPool.INSTANCE.destroyObject(juliaCaller);
            throw Throwables.propagate(t);
        }
    }

    @Override
    public JajubScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return JajubScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
