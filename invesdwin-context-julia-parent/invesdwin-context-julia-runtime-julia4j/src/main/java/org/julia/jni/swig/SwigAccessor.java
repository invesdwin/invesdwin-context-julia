package org.julia.jni.swig;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class SwigAccessor {

    private SwigAccessor() {
    }

    public static long getPointer(final SWIGTYPE_p_void pointer) {
        return SWIGTYPE_p_void.getCPtr(pointer);
    }

}
