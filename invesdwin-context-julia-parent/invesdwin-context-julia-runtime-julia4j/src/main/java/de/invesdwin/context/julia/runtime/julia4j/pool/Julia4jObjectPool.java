package de.invesdwin.context.julia.runtime.julia4j.pool;

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
public final class Julia4jObjectPool extends ATimeoutObjectPool<ExtendedJuliaCaller>
        implements FactoryBean<Julia4jObjectPool> {

    public static final Julia4jObjectPool INSTANCE = new Julia4jObjectPool();

    private Julia4jObjectPool() {
        super(Duration.ONE_MINUTE, new Duration(10, FTimeUnit.SECONDS));
    }

    @Override
    public void destroyObject(final ExtendedJuliaCaller obj) {
        try {
            obj.shutdownServer();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ExtendedJuliaCaller newObject() {
        final int port = NetworkUtil.findAvailableTcpPort();
        final ExtendedJuliaCaller session = new ExtendedJuliaCaller("julia", port);
        try {
            session.startServer();
            session.connect();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    @Override
    protected void passivateObject(final ExtendedJuliaCaller obj) {
        try {
            obj.reset();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Julia4jObjectPool getObject() throws Exception {
        return INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return Julia4jObjectPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
