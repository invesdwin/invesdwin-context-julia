package de.invesdwin.context.julia.runtime.ju4ja;

import java.io.File;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.context.system.properties.SystemProperties;

@Immutable
public final class Ju4jaProperties {

    public static final File JRI_LIBRARY_PATH;

    static {
        final SystemProperties systemProperties = new SystemProperties(Ju4jaProperties.class);
        if (systemProperties.containsValue("JRI_LIBRARY_PATH")) {
            JRI_LIBRARY_PATH = systemProperties.getFile("JRI_LIBRARY_PATH");
        } else {
            JRI_LIBRARY_PATH = null;
        }
    }

    private Ju4jaProperties() {}

}
