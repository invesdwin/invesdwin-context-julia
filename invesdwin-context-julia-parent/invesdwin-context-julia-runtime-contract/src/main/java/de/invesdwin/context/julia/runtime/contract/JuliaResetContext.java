package de.invesdwin.context.julia.runtime.contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.context.integration.marshaller.MarshallerJsonJackson;
import de.invesdwin.context.integration.script.IScriptTaskEngine;
import de.invesdwin.util.lang.Objects;
import de.invesdwin.util.lang.Strings;

public class JuliaResetContext {

    private final IScriptTaskEngine engine;

    private Set<String> protectedVariables;
    private final Map<String, String> variable_size = new HashMap<>();

    public JuliaResetContext(final IScriptTaskEngine engine) {
        this.engine = engine;
    }

    public void init() throws IOException {
        final Set<String> names = new HashSet<>();
        engine.eval("use JSON");
        final String[] array = engine.getResults().getStringVector("names(Main)");
        for (int i = 0; i < array.length; i++) {
            final String str = array[i];
            names.add(Strings.removeStart(str.trim(), ":"));
        }
    }

    public void reset() throws IOException {
        final JsonNode varinfo = varinfo();
        Set<String> changed = null;
        for (int i = 0; i < varinfo.size(); i++) {
            final JsonNode row = varinfo.get(i);
            final String name = row.get(0).asText();
            if (protectedVariables.contains(name)) {
                continue;
            }
            final String summary = row.get(2).asText();
            if ("DataType".equals(summary)) {
                //can not redefine data types
                continue;
            }
            if (summary.startsWith("typeof(")) {
                /*
                 * we don't know the arguments to functions here so we can not redifne them properly. They should not
                 * take too much memory, so it should be fine to just leave them
                 */
                continue;
            }
            final String size = row.get(1).asText();
            final String existingSize = variable_size.get(size);
            if (Objects.equals(size, existingSize)) {
                continue;
            }
            if ("Module".equals(summary)) {
                engine.eval("module " + name + " end");
            } else {
                //Variables, Arrays, Methods
                engine.eval(name + " = nothing");
            }
            if (changed == null) {
                changed = new HashSet<>();
            }
            changed.add(name);
        }
        if (changed != null) {
            updateSizeMap(changed);
        }
    }

    private void updateSizeMap(final Set<String> changed) throws IOException {
        final JsonNode varinfo = varinfo();
        for (int i = 0; i < varinfo.size(); i++) {
            final JsonNode row = varinfo.get(i);
            final String name = row.get(0).asText();
            if (changed.contains(name)) {
                final String size = row.get(1).asText();
                variable_size.put(name, size);
            }
        }
    }

    /**
     * returns a two dimensional array with columns at index 0: name, size, summary
     * 
     * Thus iterate from index 1 to access the actual values.
     */
    public JsonNode varinfo() throws IOException {
        final String json = engine.getResults().getString("JSON.json(varinfo())");
        final JsonNode node = MarshallerJsonJackson.getInstance().getJsonMapper(false).readTree(json);
        final JsonNode rows = node.get("content").get(0).get("rows");
        return rows;
    }

}
