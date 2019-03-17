package org.kelvinho.matrix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Matrix {
    private Matrix transposedMatrix = null;
    protected int rows;
    protected int columns;
    private Double sum = null;

    // fundamentals
    public abstract double get(int i, int j);

    protected abstract Matrix template(int rows, int columns);

    public abstract void mutableSet(int i, int j, double value);

    // near fundamentals, but can generalize
    public Matrix set(int i, int j, double value) {
        Matrix answer = (Matrix) clone();
        answer.mutableSet(i, j, value);
        return answer;
    }

    public int numberOfRows() {
        return rows;
    }

    public int numberOfColumns() {
        return columns;
    }

    public Matrix transpose() {
        if (transposedMatrix != null) {
            return transposedMatrix;
        }
        transposedMatrix = template(columns, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transposedMatrix.mutableSet(j, i, get(i, j));
            }
        }
        return transpose();
    }

    public Matrix dot(@Nonnull Matrix matrix) {
        if (columns != matrix.rows) {
            throw new MismatchDimensionException(this, matrix);
        }
        Matrix answer = template(rows, matrix.columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                double sum = 0.0;
                for (int k = 0; k < columns; k++) {
                    sum += get(i, k) * matrix.get(k, j);
                }
                answer.mutableSet(i, j, sum);
            }
        }
        return answer;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final Object clone() {
        Matrix answer = template(rows, columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                answer.mutableSet(i, j, get(i, j));
            }
        }
        return answer;
    }

    public double sum() {
        if (sum != null) {
            return sum;
        } else {
            sum = 0.0;
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
        return rows == matrix.numberOfRows() && columns == matrix.numberOfColumns();
    }

    public void print() {
        print(15, 4);
    }

    public void print(int spacing, int decimalPlaces) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(String.format("%-" + spacing + "." + decimalPlaces + "f", get(i, j)));
            }
            System.out.println();
        }
    }

    public Matrix operate(@Nullable Function<Double, Double> function) {
        if (function == null) {
            return this;
        }
        Matrix answer = template(rows, columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                answer.mutableSet(i, j, function.apply(get(i, j)));
            }
        }
        return answer;
    }

    public Matrix operate(@Nonnull Matrix matrix, @Nonnull BiFunction<Double, Double, Double> function) {
        if (!sameDimension(matrix)) {
            throw new MismatchDimensionException(this, matrix);
        }
        Matrix answer = template(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                answer.mutableSet(i, j, function.apply(get(i, j), matrix.get(i, j)));
            }
        }
        return answer;
    }

    // quality of life methods, but all of these should be possible by operate() methods

    public Matrix negate() {
        return operate(x -> -x);
    }

    public Matrix oneOver() {
        return operate(x -> 1 / x);
    }

    public Matrix mul(double value) {
        return operate(x -> x * value);
    }

    public Matrix sq() {
        return operate(x -> x * x);
    }

    public Matrix sigmoid() {
        return operate(x -> (1 / (1 + Math.exp(-x))));
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
        return operate(matrix, (x, y) -> x + y);
    }

    public Matrix minus(@Nonnull Matrix matrix) {
        return operate(matrix, (x, y) -> x - y);
    }

    public Matrix mul(@Nonnull Matrix matrix) {
        return operate(matrix, (x, y) -> x * y);
    }

    public Matrix divide(@Nonnull Matrix matrix) {
        return operate(matrix, (x, y) -> x / y);
    }

    public abstract boolean equals(@Nonnull Object object);
}
