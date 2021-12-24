package de.invesdwin.context.julia.runtime.juliacaller.pool;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import de.invesdwin.context.julia.runtime.contract.JuliaResetContext;
import de.invesdwin.context.julia.runtime.juliacaller.Julia4jScriptTaskEngineJulia;

@NotThreadSafe
public class ExtendedJuliaCaller extends ModifiedJuliaCaller {

    private final JuliaResetContext resetContext;

    public ExtendedJuliaCaller(final String pathToJulia, final int port) {
        super(pathToJulia, port);
        this.resetContext = new JuliaResetContext(new Julia4jScriptTaskEngineJulia(this));
    }

    @Override
    public void connect() throws IOException {
        super.connect();
        resetContext.init();
    }

    public void reset() throws IOException {
        getWatcher().clearLog();
        resetContext.reset();
        getWatcher().clearLog();
    }

}
