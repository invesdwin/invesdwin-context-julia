package de.invesdwin.context.julia.runtime.julia4j;

import java.io.File;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.context.system.properties.SystemProperties;

@Immutable
public final class Julia4jProperties {

    public static final File JRI_LIBRARY_PATH;

    static {
        final SystemProperties systemProperties = new SystemProperties(Julia4jProperties.class);
        if (systemProperties.containsValue("JRI_LIBRARY_PATH")) {
            JRI_LIBRARY_PATH = systemProperties.getFile("JRI_LIBRARY_PATH");
        } else {
            JRI_LIBRARY_PATH = null;
        }
    }

    private Julia4jProperties() {}

}
