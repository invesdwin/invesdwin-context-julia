package de.invesdwin.context.julia.runtime.julia4j;

import javax.annotation.concurrent.NotThreadSafe;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.context.integration.script.AScriptTaskResultsFromString;
import de.invesdwin.context.julia.runtime.contract.IScriptTaskResultsJulia;
import de.invesdwin.util.lang.string.Strings;

@NotThreadSafe
public class Julia4jScriptTaskResultsJulia extends AScriptTaskResultsFromString implements IScriptTaskResultsJulia {

    private final Julia4jScriptTaskEngineJulia engine;

    public Julia4jScriptTaskResultsJulia(final Julia4jScriptTaskEngineJulia engine) {
        this.engine = engine;
    }

    @Override
    public Julia4jScriptTaskEngineJulia getEngine() {
        return engine;
    }

    @Override
    public String getString(final String variable) {
        final JsonNode node = engine.unwrap().getAsJsonNode(variable);
        if (node == null) {
            return null;
        }
        final String str = node.asText();
        if (Strings.isBlankOrNullText(str)) {
            return null;
        } else {
            return str;
        }
    }

    @Override
    public String[] getStringVector(final String variable) {
        JsonNode strs = engine.unwrap().getAsJsonNode(variable);
        if (strs == null) {
            return null;
        }
        //unwrap array
        while (strs.size() == 1 && strs.get(0).size() > 1) {
            strs = strs.get(0);
        }
        final String[] values = new String[strs.size()];
        for (int i = 0; i < values.length; i++) {
            final String str = strs.get(i).asText();
            if (Strings.isBlankOrNullText(str)) {
                values[i] = null;
            } else {
                values[i] = str;
            }
        }
        return values;
    }

    @Override
    public String[][] getStringMatrix(final String variable) {
        //json returns the columns instead of rows
        final JsonNode strsMatrix = engine.unwrap().getAsJsonNode(variable);
        if (strsMatrix == null) {
            return null;
        }
        if (strsMatrix.size() == 0) {
            //https://stackoverflow.com/questions/23079625/extract-array-dimensions-in-julia
            final int[] dims = getIntegerVector("size(" + variable + ")");
            final int rows = dims[0];
            final String[][] emptyMatrix = new String[rows][];
            for (int i = 0; i < rows; i++) {
                emptyMatrix[i] = Strings.EMPTY_ARRAY;
            }
            return emptyMatrix;
        }
        //[11 12 13;21 22 23;31 32 33;41 42 43]
        //[[11,21,31,41],[12,22,32,42],[13,23,33,43]]
        final int columns = strsMatrix.size();
        final int rows = strsMatrix.get(0).size();
        final String[][] valuesMatrix = new String[rows][];
        for (int r = 0; r < rows; r++) {
            final String[] values = new String[columns];
            valuesMatrix[r] = values;
            for (int c = 0; c < columns; c++) {
                final String str = strsMatrix.get(c).get(r).asText();
                if (Strings.isBlankOrNullText(str)) {
                    values[c] = null;
                } else {
                    values[c] = str;
                }
            }
        }
        return valuesMatrix;
    }

}