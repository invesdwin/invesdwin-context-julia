package de.invesdwin.context.julia.runtime.contract;

import de.invesdwin.context.integration.script.IScriptTaskInputs;
import de.invesdwin.util.lang.Strings;

public interface IScriptTaskInputsJulia extends IScriptTaskInputs {

    @Override
    default void putCharacter(final String variable, final char value) {
        final String stringValue = Strings.checkedCast(value);
        putString(variable, stringValue);
    }

    @Override
    default void putCharacterVector(final String variable, final char[] value) {
        final String[] stringValue = Strings.checkedCastVector(value);
        putStringVector(variable, stringValue);
    }

    @Override
    default void putCharacterMatrix(final String variable, final char[][] value) {
        final String[][] stringValue = Strings.checkedCastMatrix(value);
        putStringMatrix(variable, stringValue);
    }

    @Override
    default void putExpression(final String variable, final String expression) {
        getEngine().eval(variable + " = " + expression);
    }

    @Override
    default void putNull(final String variable) {
        putExpression(variable, "nothing");
    }

    @Override
    default void remove(final String variable) {
        putNull(variable);
    }

}
