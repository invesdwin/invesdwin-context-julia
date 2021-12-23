package de.invesdwin.context.julia.runtime.jajub.pool;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import org.arl.jajub.JuliaBridge;

import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskEngineJulia;

@NotThreadSafe
public class ExtendedJuliaBridge extends JuliaBridge {

    private final JuliaResetContext resetContext;

    public ExtendedJuliaBridge() {
        super();
        this.resetContext = new JuliaResetContext(new JajubScriptTaskEngineJulia(this));
    }

    @Override
    public void open(final long timeout) throws IOException, InterruptedException {
        super.open(timeout);
        resetContext.init();
    }

    public void reset() throws IOException {
        resetContext.reset();
    }

}
