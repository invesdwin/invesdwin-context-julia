package de.invesdwin.context.julia.runtime.libjuliaclj;

import java.io.File;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.context.system.properties.SystemProperties;

@Immutable
public final class LibjuliacljProperties {

    public static final File JULIA_LIBRARY_PATH;

    static {
        final SystemProperties systemProperties = new SystemProperties(LibjuliacljProperties.class);
        if (systemProperties.containsValue("JULIA_LIBRARY_PATH")) {
            JULIA_LIBRARY_PATH = systemProperties.getFile("JULIA_LIBRARY_PATH");
        } else {
            JULIA_LIBRARY_PATH = null;
        }
    }

    private LibjuliacljProperties() {
    }

}
