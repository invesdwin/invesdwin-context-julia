package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.integration.network.NetworkUtil;
import de.invesdwin.util.concurrent.pool.timeout.ATimeoutObjectPool;
import de.invesdwin.util.time.date.FTimeUnit;
import de.invesdwin.util.time.duration.Duration;

@ThreadSafe
@Named
public final class JajubObjectPool extends ATimeoutObjectPool<ExtendedJuliaBridge>
        implements FactoryBean<JajubObjectPool> {

    public static final JajubObjectPool INSTANCE = new JajubObjectPool();

    private JajubObjectPool() {
        super(Duration.ONE_MINUTE, new Duration(10, FTimeUnit.SECONDS));
    }

    @Override
    public void destroyObject(final ExtendedJuliaBridge obj) {
        try {
            obj.shutdownServer();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ExtendedJuliaBridge newObject() {
        final int port = NetworkUtil.findAvailableTcpPort();
        final ExtendedJuliaBridge session = new ExtendedJuliaBridge("julia", port);
        try {
            session.startServer();
            session.connect();
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
