package de.invesdwin.context.julia.runtime.juliacaller;

import java.io.File;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.context.system.properties.SystemProperties;

@Immutable
public final class Julia4jProperties {

    public static final File JULIA_LIBRARY_PATH;

    static {
        final SystemProperties systemProperties = new SystemProperties(Julia4jProperties.class);
        if (systemProperties.containsValue("JULIA_LIBRARY_PATH")) {
            JULIA_LIBRARY_PATH = systemProperties.getFile("JULIA_LIBRARY_PATH");
        } else {
            JULIA_LIBRARY_PATH = null;
        }
    }

    private Julia4jProperties() {
    }

}
