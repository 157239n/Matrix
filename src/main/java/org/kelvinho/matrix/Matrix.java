package org.kelvinho.matrix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Matrix implements Cloneable { // immutable
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
                internalSet(i, j, generator.apply(i, j));
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
    private void internalSet(int i, int j, float value) {
        values[i][j] = value;
    }

    public Matrix set(int i, int j, float value) {
        Matrix answer = (Matrix) clone();
        answer.internalSet(i, j, value);
        return answer;
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
                answer.internalSet(i, j, sum);
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
        print(15, 4);
    }

    public void print(int spacing, int decimalPlaces) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(String.format("%-" + String.valueOf(spacing) + "." + String.valueOf(decimalPlaces) + "f", get(i, j)));
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
                transposedMatrix.internalSet(j, i, get(i, j));
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
    public boolean equals(@Nonnull Object obj) {
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
                answer.internalSet(i, j, function.apply(get(i, j)));
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
                answer.internalSet(i, j, function.apply(get(i, j), matrix.get(i, j)));
            }
        }
        return answer;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Object clone() {
        Matrix answer = new Matrix(rows, columns);
        for (int i = 0; i < answer.rows; i++) {
            for (int j = 0; j < answer.columns; j++) {
                answer.internalSet(i, j, get(i, j));
            }
        }
        return answer;
    }

    public Matrix addRowToRow(int rowWithValuesToAdd, float multiple, int rowToAddTo) { // rowToAddTo += rowWithValuesToAdd * multiple
        Matrix answer = (Matrix) clone();
        for (int i = 0; i < columns; i++) {
            answer.internalSet(rowToAddTo, i, get(rowToAddTo, i) + get(rowWithValuesToAdd, i) * multiple);
        }
        return answer;
    }

    public Matrix changeRow(int rowToChange, Function<Float, Float> function) {
        Matrix answer = (Matrix) clone();
        if (rowToChange < 0 || rowToChange >= rows) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < columns; i++) {
            answer.internalSet(rowToChange, i, function.apply(get(rowToChange, i)));
        }
        return answer;
    }

    public Matrix reducedRowEchelonForm() {
        Matrix answer = (Matrix) clone();
        int pivotLocation = 0;
        // going forwards
        for (int i = 0; i < rows; i++) {
            // find in which column is the pivot, save that into pivotLocation
            boolean pivotChanged = false;
            for (int w = pivotLocation; w < columns; w++) {
                if (!Environment.isStrictZero(answer.get(i, w))) {
                    pivotLocation = w;
                    pivotChanged = true;
                    break;
                }
            }
            if (pivotChanged) {
                float factor = 1.0f / answer.get(i, pivotLocation);
                answer = answer.changeRow(i, x -> x * factor);
                // start eliminating rows below it
                for (int w = i + 1; w < rows; w++) {
                    answer = answer.addRowToRow(i, -answer.get(w, pivotLocation), w);
                }
            } else {// this means that all rows below are zeros
                break;
            }
        }
        // going backwards
        for (int i = rows - 1; i >= 0; i--) {
            pivotLocation = -1;
            for (int w = 0; w < columns; w++) {
                if (!Environment.isZero(answer.get(i, w))) {
                    pivotLocation = w;
                    break;
                }
            }
            if (pivotLocation != -1) {
                // start eliminating everything above
                for (int w = i - 1; w >= 0; w--) {
                    answer = answer.addRowToRow(i, -answer.get(w, pivotLocation), w);
                }
            }
        }
        return answer;
    }

    private int[] pivotLocations() { // array with #rows length, each index containing the column position of the pivot
        Matrix RREF = reducedRowEchelonForm();
        int[] answer = new int[rows];
        for (int i = 0; i < rows; i++) {
            answer[i] = -1;
            for (int j = 0; j < columns; j++) {
                if (!Environment.isStrictZero(RREF.get(i, j))) {
                    answer[i] = j;
                    break;
                }
            }
        }
        return answer;
    }

    public int rank() {
        int[] pivotLocations = pivotLocations();
        for (int i = 0; i < pivotLocations.length; i++) {
            if (pivotLocations[i] == -1) {
                return i;
            }
        }
        return pivotLocations.length;
    }

    public Matrix nullSpace() {
        Matrix RREF = reducedRowEchelonForm();
        int rank = rank();
        int[] pivotLocations = pivotLocations();
        Matrix answer = new Matrix(columns, columns - rank);
        int currentColumnOfNullSpace = 0;
        for (int i = 0; i < (rank == 0 ? 1 : rank); i++) { // loop through each pivots
            // these are starting and ending indices of dependent vectors, so go through each of those vectors and build up one of the null space's columns
            int initialDependency = pivotLocations[i] + 1;
            int endDependency = (i + 1 < pivotLocations.length) ? pivotLocations[i + 1] : -1;
            endDependency = endDependency == -1 ? columns : endDependency;
            for (int w = initialDependency; w < endDependency; w++) { // w is the index of a single dependent vector
                for (int k = 0; k <= i; k++) {// looping through every pivot to get the values
                    answer.internalSet(k, currentColumnOfNullSpace, -RREF.get(k, w));
                }
                answer.internalSet(w, currentColumnOfNullSpace, 1);
                currentColumnOfNullSpace++;
            }
        }
        return answer;
    }

    @Nullable
    public Matrix inverse() {
        if (rows != columns) {
            throw new MatrixNotInvertibleException();
        }
        Matrix answer = new Matrix(rows, rows * 2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                answer.internalSet(i, j, get(i, j));
            }
        }
        for (int i = 0; i < rows; i++) {
            answer.internalSet(i, columns + i, 1);
        }
        answer = answer.reducedRowEchelonForm();
        for (int w = 0; w < rows; w++) {
            if (!Environment.isZero(answer.get(rows - 1, w))) {
                Matrix croppedAnswer = new Matrix(rows, columns);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        croppedAnswer.internalSet(i, j, answer.get(i, j + columns));
                    }
                }
                return croppedAnswer;
            }
        }
        return null;
    }

    @Nonnull
    public Matrix nonNullInverse() {
        return Objects.requireNonNull(inverse());
    }

    // quality of life methods

    public Matrix T() {
        return transpose();
    }

    public Matrix negate() {
        return operate(x -> -x);
    }

    public Matrix oneOver() {
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
