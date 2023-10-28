package de.invesdwin.context.julia.runtime.contract.callback;

import java.io.Closeable;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;

@NotThreadSafe
public class ScriptTaskParametersJuliaFromJson extends AScriptTaskParametersJuliaFromJson implements Closeable {

    private JsonNode parameters;

    public void setParameters(final JsonNode parameters) {
        this.parameters = parameters;
    }

    @Override
    public int size() {
        return parameters.size();
    }

    @Override
    protected JsonNode getAsJsonNode(final int index) {
        return parameters.get(index);
    }

    @Override
    public void close() {
        parameters = null;
    }

}
