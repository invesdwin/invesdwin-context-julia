package de.invesdwin.context.julia.runtime.juliacaller;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskJulia;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.juliacaller.pool.ExtendedRserveSession;
import de.invesdwin.context.julia.runtime.juliacaller.pool.RsessionObjectPool;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class JuliaCallerScriptTaskRunnerJulia implements IScriptTaskRunnerJulia, FactoryBean<JuliaCallerScriptTaskRunnerJulia> {

    public static final JuliaCallerScriptTaskRunnerJulia INSTANCE = new JuliaCallerScriptTaskRunnerJulia();

    /**
     * public for ServiceLoader support
     */
    public JuliaCallerScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        //get session
        final ExtendedRserveSession rsession = RsessionObjectPool.INSTANCE.borrowObject();
        try {
            //inputs
            final JuliaCallerScriptTaskEngineJulia engine = new JuliaCallerScriptTaskEngineJulia(rsession);
            scriptTask.populateInputs(engine.getInputs());

            //execute
            scriptTask.executeScript(engine);

            //results
            final T result = scriptTask.extractResults(engine.getResults());
            engine.close();

            //return
            RsessionObjectPool.INSTANCE.returnObject(rsession);
            return result;
        } catch (final Throwable t) {
            RsessionObjectPool.INSTANCE.destroyObject(rsession);
            throw Throwables.propagate(t);
        }
    }

    @Override
    public JuliaCallerScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return JuliaCallerScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
