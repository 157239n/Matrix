package org.kelvinho.matrix;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
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

    static boolean equals(float a, float b) {
        return Math.abs(a - b) < 0.000001;
    }

    static boolean equals(double a, double b) {
        return Math.abs(a - b) < 0.000000000000001;
    }
}