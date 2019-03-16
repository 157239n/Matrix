package org.kelvinho.matrix;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
class Environment {
    static float[][] clone(@Nonnull float[][] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Input values mst have at least 1 row");
        }
        int rows = values.length;
        int columns = values[0].length;
        float[][] newValues = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(values[i], 0, newValues[i], 0, columns);
        }
        return newValues;
    }

    static double[][] clone(@Nonnull double[][] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Input values mst have at least 1 row");
        }
        int rows = values.length;
        int columns = values[0].length;
        double[][] newValues = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(values[i], 0, newValues[i], 0, columns);
        }
        return newValues;
    }

    private static float[][] convert(@Nonnull int[][] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Input values mst have at least 1 row");
        }
        int rows = values.length;
        int columns = values[0].length;
        float[][] newValues = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[i][j] = values[i][j];
            }
        }
        return newValues;
    }

    static float[][] clone(@Nonnull int[][] values) {
        return convert(values);
    }

    @SuppressWarnings("SameParameterValue")
    static boolean floatEquals(float a, float b) {
        return Math.abs(a - b) < 0.000001;
    }

    static boolean floatLooselyEquals(float a, float b) {
        return Math.abs(a - b) < 0.0001;
    }

    static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < 0.000000000000001;
    }

    @SuppressWarnings({"SameParameterValue", "BooleanMethodIsAlwaysInverted"})
    static boolean doubleLooselyEquals(double a, double b) {
        return Math.abs(a - b) < 0.000000000001;
    }

    @Deprecated static boolean isZero(float a) {
        return floatEquals(a, 0.0f);
    }

    @SuppressWarnings("SameParameterValue")
    static String round(float a, int decimalPlaces) {
        return String.format("%." + decimalPlaces + "g%n", a);
    }

    static String round(float a) {
        return round(a, 4);
    }
}
