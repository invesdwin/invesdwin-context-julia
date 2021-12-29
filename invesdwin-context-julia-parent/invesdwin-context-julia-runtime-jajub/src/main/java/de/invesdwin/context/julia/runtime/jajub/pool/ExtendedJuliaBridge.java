package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import de.invesdwin.context.integration.marshaller.MarshallerJsonJackson;
import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskEngineJulia;
import de.invesdwin.util.error.Throwables;
import de.invesdwin.util.time.date.FTimeUnit;

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
    public void open() throws IOException {
        super.open();
        resetContext.init();
    }

    public void reset() throws IOException {
        getErrWatcher().clearLog();
        resetContext.reset();
        getErrWatcher().clearLog();
    }

    @Override
    public void exec(final String jcode) {
        super.exec(jcode);
        checkError();
    }

    public JsonNode getAsJsonNode(final String variable) {
        final String result = (String) get("JSON.json(" + variable + ")");
        try {
            final JsonNode node = mapper.readTree(result);
            checkError();
            if (result == null) {
                checkErrorDelayed();
            }
            if (node instanceof NullNode) {
                return null;
            } else {
                return node;
            }
        } catch (final Throwable t) {
            checkErrorDelayed();
            throw Throwables.propagate(t);
        }
    }

    private void checkErrorDelayed() {
        //give a bit of time to read the actual error
        try {
            FTimeUnit.MILLISECONDS.sleep(10);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        checkError();
    }

}
