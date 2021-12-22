package de.invesdwin.context.julia.runtime.jajub;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import com.github.rcaller.rstuff.RCaller;

import de.invesdwin.context.julia.runtime.contract.AScriptTaskR;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerR;
import de.invesdwin.context.julia.runtime.jajub.pool.JajubObjectPool;
import de.invesdwin.util.error.Throwables;

@Immutable
@Named
public final class JajubScriptTaskRunnerJulia implements IScriptTaskRunnerR, FactoryBean<JajubScriptTaskRunnerJulia> {

    public static final JajubScriptTaskRunnerJulia INSTANCE = new JajubScriptTaskRunnerJulia();

    public static final String INTERNAL_RESULT_VARIABLE = JajubScriptTaskRunnerJulia.class.getSimpleName() + "_result";

    /**
     * public for ServiceLoader support
     */
    public JajubScriptTaskRunnerJulia() {
    }

    @Override
    public <T> T run(final AScriptTaskR<T> scriptTask) {
        //get session
        final RCaller rcaller = JajubObjectPool.INSTANCE.borrowObject();
        try {
            //inputs
            rcaller.getRCode().clearOnline();
            final JajubScriptTaskEngineJulia engine = new JajubScriptTaskEngineJulia(rcaller);
            scriptTask.populateInputs(engine.getInputs());

            //execute
            scriptTask.executeScript(engine);

            //results
            final T result = scriptTask.extractResults(engine.getResults());
            engine.close();

            //return
            JajubObjectPool.INSTANCE.returnObject(rcaller);
            return result;
        } catch (final Throwable t) {
            JajubObjectPool.INSTANCE.destroyObject(rcaller);
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
