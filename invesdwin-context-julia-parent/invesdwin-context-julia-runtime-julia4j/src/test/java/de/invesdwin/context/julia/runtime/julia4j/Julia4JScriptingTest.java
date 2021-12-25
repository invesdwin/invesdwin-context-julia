package de.invesdwin.context.julia.runtime.julia4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.julia.jni.swig.Julia4J;
import org.julia.jni.swig.SWIGTYPE_p_jl_value_t;
import org.julia.scripting.JuliaScriptEngineFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test of jsr223 Julia binding
 * <p>
 * Created by rss on 25/08/2018
 */
@NotThreadSafe
@SuppressWarnings("WeakerAccess")
public class Julia4JScriptingTest {

    @BeforeAll
    public static void checkJuliaEngine() {
        final List<String> engineList = listScriptingEngines();

        assertTrue(engineList.contains(JuliaScriptEngineFactory.ENGINE_NAME_JULIA));
    }

    private static List<String> listScriptingEngines() {
        final List<String> result = new ArrayList<>();
        final ScriptEngineManager mgr = new ScriptEngineManager();
        for (final ScriptEngineFactory factory : mgr.getEngineFactories()) {
            result.add(factory.getEngineName());
            //CHECKSTYLE:OFF
            System.out.println("ScriptEngineFactory Info");
            System.out.printf("\tScript Engine: %s (%s)\n", factory.getEngineName(), factory.getEngineVersion());
            System.out.printf("\tLanguage: %s (%s)\n", factory.getLanguageName(), factory.getLanguageVersion());
            for (final String name : factory.getNames()) {
                System.out.printf("\tEngine Alias: %s\n", name);
            }
            //CHECKSTYLE:ON
        }
        return result;
    }

    @Test
    public void testBasic() throws ScriptException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("julia");

        // evaluate Julia code
        engine.eval("dump(:(x^2 + y^2))");

        final Object result = engine.eval("2 ^ 10");
        final long checkedResult = Julia4J.jl_unbox_int64((SWIGTYPE_p_jl_value_t) result);
        assertEquals(1024, checkedResult);
    }

    @Test
    public void testReturnvalue() throws ScriptException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("julia");

        engine.put("x", "hello");
        // print global variable "x"
        engine.eval("println(x);");
        // the above line prints "hello"

        // Now, pass a different script context
        final ScriptContext newContext = new SimpleScriptContext();
        final Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        // add new variable "x" to the new engineScope
        engineScope.put("x", "world");

        // execute the same script - but this time pass a different script context
        engine.eval("println(x);", newContext);
        // the above line prints "world"

        // Add some numeric values
        engineScope.put("a", 2);
        engineScope.put("b", 10);
        engine.eval("for i in 1:5 println(\"Value: $(a * b * i)\"); end", newContext);

        final Object result = engine.eval("a + b", newContext);
        assertEquals(12, Julia4J.jl_unbox_int64((SWIGTYPE_p_jl_value_t) result));
    }
}
