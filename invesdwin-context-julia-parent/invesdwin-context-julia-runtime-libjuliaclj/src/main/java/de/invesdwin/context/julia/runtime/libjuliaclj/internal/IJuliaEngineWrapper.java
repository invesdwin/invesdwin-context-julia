package de.invesdwin.context.julia.runtime.libjuliaclj.internal;

import com.fasterxml.jackson.databind.JsonNode;

import de.invesdwin.util.concurrent.lock.IReentrantLock;

public interface IJuliaEngineWrapper {

    void eval(String command);

    JsonNode getAsJsonNode(String variable);

    void reset();

    IReentrantLock getLock();

    void putByteVector(String variable, byte[] vector);

    byte[] getByteVector(String variable);

    void putShortVector(String variable, short[] vector);

    short[] getShortVector(String variable);

    void putIntegerVector(String variable, int[] vector);

    int[] getIntegerVector(String variable);

    void putLongVector(String variable, long[] vector);

    long[] getLongVector(String variable);

    void putFloatVector(String variable, float[] vector);

    float[] getFloatVector(String variable);

    void putDoubleVector(String variable, double[] vector);

    double[] getDoubleVector(String variable);

    void putByteMatrix(String variable, byte[][] matrix);

    byte[][] getByteMatrix(String variable);

    void putShortMatrix(String variable, short[][] matrix);

    short[][] getShortMatrix(String variable);

    void putIntegerMatrix(String variable, int[][] matrix);

    int[][] getIntegerMatrix(String variable);

    void putLongMatrix(String variable, long[][] matrix);

    long[][] getLongMatrix(String variable);

    void putFloatMatrix(String variable, float[][] matrix);

    float[][] getFloatMatrix(String variable);

    void putDoubleMatrix(String variable, double[][] matrix);

    double[][] getDoubleMatrix(String variable);

    String[] getStringVectorAsJson(String variable);

    String[][] getStringMatrixAsJson(String variable);

    char[] getCharacterVectorAsJson(String variable);

    char[][] getCharacterMatrixAsJson(String variable);

    boolean[] getBooleanVectorAsJson(String variable);

    boolean[][] getBooleanMatrixAsJson(String variable);

    byte[] getByteVectorAsJson(String variable);

    byte[][] getByteMatrixAsJson(String variable);

    short[] getShortVectorAsJson(String variable);

    short[][] getShortMatrixAsJson(String variable);

    int[] getIntegerVectorAsJson(String variable);

    int[][] getIntegerMatrixAsJson(String variable);

    long[] getLongVectorAsJson(String variable);

    long[][] getLongMatrixAsJson(String variable);

    float[] getFloatVectorAsJson(String variable);

    float[][] getFloatMatrixAsJson(String variable);

    double[] getDoubleVectorAsJson(String variable);

    double[][] getDoubleMatrixAsJson(String variable);

}
