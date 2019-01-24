package org.kelvinho.matrix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Matrix { // immutable
    private float[][] values;
    private int rows;
    private int columns;
    private Matrix transposedMatrix = null;
    private Float sum = null;

    public Matrix(int numberOfRows, int numberOfColumns, BiFunction<Integer, Integer, Float> generator) {
        rows = numberOfRows;
        columns = numberOfColumns;
        values = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                set(i, j, generator.apply(i, j));
            }
        }
    }

    public Matrix(int numberOfRows, int numberOfColumns, float value) {
        this(numberOfRows, numberOfColumns, (x, y) -> 0f);
    }

    public Matrix(int numberOfRows, int numberOfColumns) {
        this(numberOfRows, numberOfColumns, 0);
    }

    public Matrix(@Nonnull float[][] values) {
        this.values = Environment.clone(values);
        rows = values.length;
        columns = values[0].length;
    }

    public Matrix(@Nonnull int[][] values) {
        this.values = Environment.clone(values);
        rows = values.length;
        columns = values[0].length;
    }

    public float get(int i, int j) {
        return values[i][j];
    }

    @SuppressWarnings("SameParameterValue")
    private void set(int i, int j, float value) {
        values[i][j] = value;
    }

    public Matrix dot(@Nonnull Matrix matrix) {
        if (columns != matrix.rows) {
            throw new MismatchDimensionException(this, matrix);
        }
        Matrix answer = new Matrix(rows, matrix.columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                float sum = 0.0f;
                for (int k = 0; k < columns; k++) {
                    sum += get(i, k) * matrix.get(k, j);
                }
                answer.set(i, j, sum);
            }
        }
        return answer;
    }

    public int numberOfRows() {
        return rows;
    }

    public int numberOfColumns() {
        return columns;
    }

    public void print() {
        print(15);
    }

    public void print(int spacing) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(String.format("%-" + String.valueOf(spacing) + "f", get(i, j)));
            }
            System.out.println();
        }
    }

    public Matrix transpose() {
        if (transposedMatrix != null) {
            return transposedMatrix;
        }
        transposedMatrix = new Matrix(columns, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transposedMatrix.set(j, i, get(i, j));
            }
        }
        return transpose();
    }

    public float sum() {
        if (sum != null) {
            return sum;
        } else {
            sum = 0.0f;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    sum += get(i, j);
                }
            }
            return sum();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean sameDimension(@Nonnull Matrix matrix) {
        return rows == matrix.rows && columns == matrix.columns;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            Matrix matrix = (Matrix) obj;
            if (!sameDimension(matrix)) {
                return false;
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (!Environment.equals(get(i, j), matrix.get(i, j))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Matrix operate(@Nullable Function<Float, Float> function) {
        if (function == null) {
            return this;
        }
        Matrix answer = new Matrix(rows, columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                answer.set(i, j, function.apply(get(i, j)));
            }
        }
        return answer;
    }

    public Matrix operate(@Nonnull BiFunction<Float, Float, Float> function, Matrix matrix) {
        if (!sameDimension(matrix)) {
            throw new MismatchDimensionException(this, matrix);
        }
        Matrix answer = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                answer.set(i, j, function.apply(get(i, j), matrix.get(i, j)));
            }
        }
        return answer;
    }

    // quality of life methods

    public Matrix T() {
        return transpose();
    }

    public Matrix negate() {
        return operate(x -> -x);
    }

    public Matrix inverse() {
        return operate(x -> 1 / x);
    }

    public Matrix mul(float value) {
        return operate(x -> x * value);
    }

    public Matrix mul(double value) {
        return mul((float) value);
    }

    public Matrix sq() {
        return operate(x -> x * x);
    }

    public Matrix sigmoid() {
        return operate(x -> (float) (1 / (1 + Math.exp(-x))));
    }

    public Matrix oneMinus() {
        return operate(x -> 1 - x);
    }

    public Matrix sigmoidDerivative() {
        return operate(x -> x * (1 - x));
    }

    public Matrix abs() {
        return operate(Math::abs);
    }

    public Matrix add(@Nonnull Matrix matrix) {
        return operate((x, y) -> x + y, matrix);
    }

    public Matrix minus(@Nonnull Matrix matrix) {
        return operate((x, y) -> x - y, matrix);
    }

    public Matrix mul(@Nonnull Matrix matrix) {
        return operate((x, y) -> x * y, matrix);
    }

    public Matrix divide(@Nonnull Matrix matrix) {
        return operate((x, y) -> x / y, matrix);
    }
}
