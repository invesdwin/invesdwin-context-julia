package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.util.concurrent.pool.timeout.ATimeoutObjectPool;
import de.invesdwin.util.time.date.FTimeUnit;
import de.invesdwin.util.time.duration.Duration;

@ThreadSafe
@Named
public final class JajubObjectPool extends ATimeoutObjectPool<ExtendedJuliaBridge>
        implements FactoryBean<JajubObjectPool> {

    public static final JajubObjectPool INSTANCE = new JajubObjectPool();

    private JajubObjectPool() {
        //julia compilation is a lot of overhead, thus keep instances open longer
        super(new Duration(10, FTimeUnit.MINUTES), new Duration(10, FTimeUnit.SECONDS));
    }

    @Override
    public void destroyObject(final ExtendedJuliaBridge obj) {
        obj.close();
    }

    @Override
    protected ExtendedJuliaBridge newObject() {
        final ExtendedJuliaBridge session = new ExtendedJuliaBridge();
        try {
            session.open();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    @Override
    protected void passivateObject(final ExtendedJuliaBridge obj) {
        try {
            obj.reset();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JajubObjectPool getObject() throws Exception {
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
