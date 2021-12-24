package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import de.invesdwin.context.integration.marshaller.MarshallerJsonJackson;
import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskEngineJulia;
import de.invesdwin.util.time.duration.Duration;

@NotThreadSafe
public class ExtendedJuliaBridge extends ModifiedJuliaBridge {

    private final JuliaResetContext resetContext;
    private final ObjectMapper mapper;

    public ExtendedJuliaBridge() {
        super();
        this.resetContext = new JuliaResetContext(new JajubScriptTaskEngineJulia(this));
        this.mapper = MarshallerJsonJackson.getInstance().getJsonMapper(false);
    }

    @Override
    public void open(final Duration timeout) throws IOException, InterruptedException {
        super.open(timeout);
        resetContext.init();
    }

    public void reset() throws IOException {
        resetContext.reset();
    }

    public JsonNode getAsJsonNode(final String variable) {
        final String result = (String) get("JSON.json(" + variable + ")");
        try {
            final JsonNode node = mapper.readTree(result);
            if (node instanceof NullNode) {
                return null;
            } else {
                return node;
            }
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
