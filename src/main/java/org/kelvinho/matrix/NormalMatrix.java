package org.kelvinho.matrix;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

@SuppressWarnings({"unused", "WeakerAccess"})
public class NormalMatrix extends Matrix implements Cloneable {
    private float[][] values;

    public NormalMatrix(int numberOfRows, int numberOfColumns, @Nonnull BiFunction<Integer, Integer, Double> generator) {
        rows = numberOfRows;
        columns = numberOfColumns;
        values = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                internalSet(i, j, generator.apply(i, j));
            }
        }
    }

    public NormalMatrix(int numberOfRows, int numberOfColumns, double value) {
        this(numberOfRows, numberOfColumns, (x, y) -> value);
    }

    public NormalMatrix(int numberOfRows, int numberOfColumns) {
        this(numberOfRows, numberOfColumns, 0);
    }

    public NormalMatrix(@Nonnull float[][] values) {
        if (values.length == 0) {
            throw new RuntimeException("The number of rows is 0. Please use new NormalMatrix(int numberOfRows, int numberOfColumns) instead.");
        }
        this.values = Environment.clone(values);
        rows = values.length;
        columns = values[0].length;
    }

    @Override
    public double get(int i, int j) {
        return 0;
    }

    @Override
    public Matrix set(int i, int j, double value) {
        return null;
    }

    @Override
    protected Matrix template(int rows, int columns) {
        return new NormalMatrix(rows, columns, 0.0);
    }

    @Override
    protected void internalSet(int i, int j, double value) {
        values[i][j] = (float) value;
    }

    @Override
    public boolean equals(@Nonnull Object object) {
        if (object instanceof NormalMatrix) {
            NormalMatrix matrix = (NormalMatrix) object;
            if (!sameDimension(matrix)) {
                return false;
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (!Environment.doubleLooselyEquals(get(i, j), matrix.get(i, j))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
