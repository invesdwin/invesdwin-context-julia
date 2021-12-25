package de.invesdwin.context.julia.runtime.libjuliaclj.internal;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.util.concurrent.lock.IReentrantLock;

public interface IJuliaEngineWrapper {

    void eval(String command);

    JsonNode getAsJsonNode(String variable);

    void reset();

    IReentrantLock getLock();

}
