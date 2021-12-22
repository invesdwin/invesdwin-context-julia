package de.invesdwin.context.julia.runtime.contract;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;

import de.invesdwin.context.system.properties.SystemProperties;
import de.invesdwin.util.lang.Strings;
import de.invesdwin.util.lang.reflection.Reflections;

/**
 * This instance will use the IScriptTaskRunner that was chosen by the user either by including the appropriate runtime
 * module in the classpath or by defining the class to be used as a system property.
 */
@Immutable
@Named
public final class ProvidedScriptTaskRunnerJulia implements IScriptTaskRunnerJulia, FactoryBean<ProvidedScriptTaskRunnerJulia> {

    public static final String PROVIDED_INSTANCE_KEY = IScriptTaskRunnerJulia.class.getName();

    public static final ProvidedScriptTaskRunnerJulia INSTANCE = new ProvidedScriptTaskRunnerJulia();

    @GuardedBy("ProvidedScriptTaskRunnerR.class")
    private static IScriptTaskRunnerJulia providedInstance;

    private ProvidedScriptTaskRunnerJulia() {}

    public static synchronized IScriptTaskRunnerJulia getProvidedInstance() {
        if (providedInstance == null) {
            final SystemProperties systemProperties = new SystemProperties();
            if (systemProperties.containsValue(PROVIDED_INSTANCE_KEY)) {
                try {
                    final String runner = systemProperties.getString(PROVIDED_INSTANCE_KEY);
                    return (IScriptTaskRunnerJulia) Reflections.classForName(runner).newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                final Map<String, IScriptTaskRunnerJulia> runners = new LinkedHashMap<String, IScriptTaskRunnerJulia>();
                for (final IScriptTaskRunnerJulia runner : ServiceLoader.load(IScriptTaskRunnerJulia.class)) {
                    final IScriptTaskRunnerJulia existing = runners.put(runner.getClass().getName(), runner);
                    if (existing != null) {
                        throw new IllegalStateException("Duplicate service provider found for [" + PROVIDED_INSTANCE_KEY
                                + "=" + existing.getClass().getName()
                                + "]. Please make sure you have only one provider for it in the classpath.");
                    }
                }
                if (runners.isEmpty()) {
                    throw new IllegalStateException("No service provider found for [" + PROVIDED_INSTANCE_KEY
                            + "]. Please add one provider for it to the classpath.");
                }
                if (runners.size() > 1) {
                    final StringBuilder runnersStr = new StringBuilder("(");
                    for (final String runner : runners.keySet()) {
                        runnersStr.append(runner);
                        runnersStr.append("|");
                    }
                    Strings.removeEnd(runnersStr, "|");
                    runnersStr.append(")");
                    throw new IllegalStateException(
                            "More than one service provider found for [" + PROVIDED_INSTANCE_KEY + "=" + runnersStr
                                    + "] to choose from. Please remove unwanted ones from the classpath or choose a "
                                    + "specific one by defining a system property for the preferred one. E.g. on the command line with -D"
                                    + PROVIDED_INSTANCE_KEY + "=" + runners.keySet().iterator().next());
                }
                setProvidedInstance(runners.values().iterator().next());
            }
        }
        return providedInstance;
    }

    public static synchronized void setProvidedInstance(final IScriptTaskRunnerJulia providedInstance) {
        ProvidedScriptTaskRunnerJulia.providedInstance = providedInstance;
        final SystemProperties systemProperties = new SystemProperties();
        if (providedInstance == null) {
            systemProperties.setString(PROVIDED_INSTANCE_KEY, null);
        } else {
            systemProperties.setString(PROVIDED_INSTANCE_KEY, providedInstance.getClass().getName());
        }
    }

    @Override
    public <T> T run(final AScriptTaskJulia<T> scriptTask) {
        return getProvidedInstance().run(scriptTask);
    }

    @Override
    public Class<?> getObjectType() {
        return ProvidedScriptTaskRunnerJulia.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public ProvidedScriptTaskRunnerJulia getObject() throws Exception {
        return INSTANCE;
    }

}
