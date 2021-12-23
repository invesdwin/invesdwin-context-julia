package de.invesdwin.context.julia.runtime.contract;

import de.invesdwin.context.integration.script.IScriptTaskResults;
import de.invesdwin.util.math.Characters;

public interface IScriptTaskResultsJulia extends IScriptTaskResults {

    @Override
    default char getCharacter(final String variable) {
        final String doubleValue = getString(variable);
        return Characters.checkedCast(doubleValue);
    }

    @Override
    default char[] getCharacterVector(final String variable) {
        final String[] doubleValue = getStringVector(variable);
        return Characters.checkedCastVector(doubleValue);
    }

    @Override
    default char[][] getCharacterMatrix(final String variable) {
        final String[][] doubleValue = getStringMatrix(variable);
        return Characters.checkedCastMatrix(doubleValue);
    }

    @Override
    default boolean isDefined(final String variable) {
        return getBoolean("@isdefined " + variable);
    }

    @Override
    default boolean isNull(final String variable) {
        return getBoolean("isnothing(" + variable + ")");
    }

    default boolean isEmpty(final String variable) {
        return getBoolean("isempty(" + variable + ")");
    }

}
