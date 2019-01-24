package org.kelvinho.matrix;

import javax.annotation.Nonnull;

class MismatchDimensionException extends RuntimeException {
    MismatchDimensionException(@Nonnull Matrix m, @Nonnull Matrix n) {
        super("Matrix 1: " + matrixDimension(m) + ", Matrix 2: " + matrixDimension(n));
    }

    @Nonnull
    private static String matrixDimension(@Nonnull Matrix matrix) {
        return "(" + String.valueOf(matrix.numberOfRows()) + ", " + String.valueOf(matrix.numberOfColumns()) + ")";
    }
}
