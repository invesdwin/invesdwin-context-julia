package de.invesdwin.context.julia.runtime.julia4j.internal;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;

import org.rosuda.JRI.Rengine;

import de.invesdwin.context.julia.runtime.julia4j.Julia4jProperties;
import de.invesdwin.instrument.DynamicInstrumentationReflections;
import de.invesdwin.util.concurrent.lock.IReentrantLock;
import de.invesdwin.util.concurrent.lock.Locks;

/**
 * Always acquire the lock first before accessing the rengine instance.
 */
@NotThreadSafe
public final class RengineWrapper {

    public static final RengineWrapper INSTANCE = new RengineWrapper();

    @GuardedBy("lock")
    private final Rengine rengine;
    private final IReentrantLock lock;

    private RengineWrapper() {
        DynamicInstrumentationReflections.addPathToJavaLibraryPath(Julia4jProperties.JRI_LIBRARY_PATH);
        if (Rengine.getMainEngine() != null) {
            rengine = Rengine.getMainEngine();
        } else {
            rengine = new Rengine(new String[] { "--vanilla" }, false, null);
        }
        if (!rengine.waitForR()) {
            throw new IllegalStateException("Cannot load R");
        }
        rengine.addMainLoopCallbacks(LoggingRMainLoopCallbacks.INSTANCE);
        lock = Locks.newReentrantLock(RengineWrapper.class.getSimpleName() + "_lock");
    }

    public Rengine getRengine() {
        return rengine;
    }

    public IReentrantLock getLock() {
        return lock;
    }

}
