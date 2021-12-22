package de.invesdwin.context.julia.runtime.jajub.pool;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import com.github.rcaller.rstuff.RCaller;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerR;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.jajub.pool.internal.ModifiedRCaller;
import de.invesdwin.util.concurrent.pool.timeout.ATimeoutObjectPool;
import de.invesdwin.util.time.date.FTimeUnit;
import de.invesdwin.util.time.duration.Duration;

@ThreadSafe
@Named
public final class JajubObjectPool extends ATimeoutObjectPool<RCaller> implements FactoryBean<JajubObjectPool> {

    public static final JajubObjectPool INSTANCE = new JajubObjectPool();

    private JajubObjectPool() {
        super(Duration.ONE_MINUTE, new Duration(10, FTimeUnit.SECONDS));
    }

    @Override
    public void destroyObject(final RCaller obj) {
        obj.StopRCallerOnline();
    }

    @Override
    protected RCaller newObject() {
        return new ModifiedRCaller();
    }

    @Override
    protected void passivateObject(final RCaller obj) {
        obj.getRCode().clear();
        obj.getRCode().getCode().insert(0, IScriptTaskRunnerR.CLEANUP_SCRIPT + "\n");
        obj.getRCode().addRCode(JajubScriptTaskRunnerJulia.INTERNAL_RESULT_VARIABLE + " <- c()");
        obj.runAndReturnResultOnline(JajubScriptTaskRunnerJulia.INTERNAL_RESULT_VARIABLE);
        obj.deleteTempFiles();
    }

    @Override
    public JajubObjectPool getObject() {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return JajubObjectPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
