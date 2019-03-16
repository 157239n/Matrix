package org.kelvinho.matrix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AccurateMatrix extends Matrix implements Cloneable { // immutable
    private double[][] values;
    private Integer rank = null;
    private AccurateMatrix nullSpace = null;

    public AccurateMatrix(int numberOfRows, int numberOfColumns, @Nonnull BiFunction<Integer, Integer, Double> generator) {
        rows = numberOfRows;
        columns = numberOfColumns;
        values = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                internalSet(i, j, generator.apply(i, j));
            }
        }
    }

    public AccurateMatrix(int numberOfRows, int numberOfColumns, double value) {
        this(numberOfRows, numberOfColumns, (x, y) -> value);
    }

    public AccurateMatrix(int numberOfRows, int numberOfColumns) {
        this(numberOfRows, numberOfColumns, 0);
    }

    public AccurateMatrix(@Nonnull double[][] values) {
        if (values.length == 0) {
            throw new RuntimeException("The number of rows is 0. Please use new NormalMatrix(int numberOfRows, int numberOfColumns) instead.");
        }
        this.values = Environment.clone(values);
        rows = values.length;
        columns = values[0].length;
    }

    protected AccurateMatrix template(int rows, int columns) {
        return new AccurateMatrix(rows, columns, 0.0);
    }

    @Override
    public double get(int i, int j) {
        return values[i][j];
    }

    @Override
    @SuppressWarnings("SameParameterValue")
    protected void internalSet(int i, int j, double value) {
        values[i][j] = value;
    }

    AccurateMatrix addRowToRow(int rowWithValuesToAdd, double multiple, int rowToAddTo) { // rowToAddTo += rowWithValuesToAdd * multiple
        AccurateMatrix answer = (AccurateMatrix) clone();
        for (int i = 0; i < columns; i++) {
            answer.internalSet(rowToAddTo, i, get(rowToAddTo, i) + get(rowWithValuesToAdd, i) * multiple);
        }
        return answer;
    }

    private AccurateMatrix changeRow(int rowToChange, @Nonnull Function<Double, Double> function) {
        AccurateMatrix answer = (AccurateMatrix) clone();
        if (rowToChange < 0 || rowToChange >= rows) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < columns; i++) {
            answer.internalSet(rowToChange, i, function.apply(get(rowToChange, i)));
        }
        return answer;
    }

    public AccurateMatrix reducedRowEchelonForm() {
        AccurateMatrix answer = (AccurateMatrix) clone();
        int pivotLocation = 0;
        // going forwards
        for (int i = 0; i < rows; i++) {
            // find in which column is the pivot, save that into pivotLocation
            boolean pivotChanged = false;
            for (int w = pivotLocation; w < columns; w++) {
                if (!Environment.doubleLooselyEquals(answer.get(i, w), 0.0)) {
                    pivotLocation = w;
                    pivotChanged = true;
                    break;
                }
            }
            if (pivotChanged) {
                double factor = 1.0 / answer.get(i, pivotLocation);
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
                if (!Environment.doubleLooselyEquals(answer.get(i, w), 0.0)) {
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
        AccurateMatrix RREF = reducedRowEchelonForm();
        int[] answer = new int[rows];
        for (int i = 0; i < rows; i++) {
            answer[i] = -1;
            for (int j = 0; j < columns; j++) {
                if (!Environment.doubleLooselyEquals(RREF.get(i, j), 0.0)) {
                    answer[i] = j;
                    break;
                }
            }
        }
        return answer;
    }

    public int rank() {
        if (rank != null) {
            return rank;
        } else {
            int[] pivotLocations = pivotLocations();
            for (int i = 0; i < pivotLocations.length; i++) {
                if (pivotLocations[i] == -1) {
                    return i;
                }
            }
            return (rank = pivotLocations.length);
        }
    }

    public AccurateMatrix nullSpace() {
        if (nullSpace != null) {
            return nullSpace;
        } else {
            AccurateMatrix RREF = reducedRowEchelonForm();
            int rank = rank();
            int[] pivotLocations = pivotLocations();
            nullSpace = new AccurateMatrix(columns, columns - rank);
            int currentColumnOfNullSpace = 0;
            for (int i = 0; i < (rank == 0 ? 1 : rank); i++) { // loop through each pivots
                // these are starting and ending indices of dependent vectors, so go through each of those vectors and build up one of the null space's columns
                int initialDependency = pivotLocations[i] + 1;
                int endDependency = (i + 1 < pivotLocations.length) ? pivotLocations[i + 1] : -1;
                endDependency = endDependency == -1 ? columns : endDependency;
                for (int w = initialDependency; w < endDependency; w++) { // w is the index of a single dependent vector
                    for (int k = 0; k <= i; k++) {// looping through every pivot to get the values
                        nullSpace.internalSet(k, currentColumnOfNullSpace, -RREF.get(k, w));
                    }
                    nullSpace.internalSet(w, currentColumnOfNullSpace, 1);
                    currentColumnOfNullSpace++;
                }
            }
            return nullSpace;
        }
    }

    @Nullable
    public AccurateMatrix inverse() {
        if (rows != columns) {
            throw new MatrixNotInvertibleException();
        }
        AccurateMatrix answer = new AccurateMatrix(rows, rows * 2);
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
            if (!Environment.doubleLooselyEquals(answer.get(rows - 1, w), 0.0)) {
                AccurateMatrix croppedAnswer = new AccurateMatrix(rows, columns);
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
    public AccurateMatrix nonNullInverse() {
        return Objects.requireNonNull(inverse());
    }

    @Override
    public boolean equals(@Nonnull Object obj) {
        if (obj instanceof AccurateMatrix) {
            AccurateMatrix matrix = (AccurateMatrix) obj;
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
