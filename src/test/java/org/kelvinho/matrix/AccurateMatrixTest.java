package org.kelvinho.matrix;

import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public class AccurateMatrixTest {
    private double[][] matrix1Values = new double[][]{
            new double[]{1, 2, 3, 4},
            new double[]{5, 6, 7, 8},
            new double[]{9, 10, 11, 12}
    };
    private AccurateMatrix matrix1 = new AccurateMatrix(matrix1Values);

    private double[][] matrix2Values = new double[][]{
            new double[]{1, 2},
            new double[]{3, 4},
            new double[]{5, 6},
            new double[]{7, 8},
    };
    private AccurateMatrix matrix2 = new AccurateMatrix(matrix2Values);

    private AccurateMatrix matrix3 = new AccurateMatrix(new double[][]{
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0}
    });

    private AccurateMatrix matrix4 = new AccurateMatrix(new double[][]{
            new double[]{1, 0, 0, 0},
            new double[]{0, 1, 0, 0},
            new double[]{0, 0, 1, 0},
            new double[]{0, 0, 0, 1}
    });

    @Test
    public void constructor() {
        assertEquals(matrix1.numberOfRows(), 3);
        assertEquals(matrix1.numberOfColumns(), 4);
        assertEquals(matrix2.numberOfRows(), 4);
        assertEquals(matrix2.numberOfColumns(), 2);
    }

    @Test
    public void dot() {
        Matrix matrix3 = matrix1.dot(matrix2);
        matrix3.print();
        assertEquals(matrix3.numberOfRows(), 3);
        assertEquals(matrix3.numberOfColumns(), 2);
    }

    @Test
    public void transpose() {
        double[][] transposeOfMatrix1Values = new double[][]{
                new double[]{1, 5, 9},
                new double[]{2, 6, 10},
                new double[]{3, 7, 11},
                new double[]{4, 8, 12}
        };
        AccurateMatrix transposeOfMatrix1 = new AccurateMatrix(transposeOfMatrix1Values);
        assertEquals(matrix1.transpose(), transposeOfMatrix1);
    }

    @Test
    public void qualityOfLifeMethods() {
        println("matrix: ");
        matrix1.print();
        println();
        println("- matrix: ");
        matrix1.negate().print();
        println();
        println("0 - matrix: ");
        new AccurateMatrix(3, 4, 0).minus(matrix1).print();
        println();
        println("1 - matrix: ");
        matrix1.oneMinus().print();
        println();
        println("matrix * 2: ");
        matrix1.mul(2).print();
        println();
        println("sigmoid(matrix): ");
        matrix1.sigmoid().print();
    }

    @Test
    public void addRowToRow() {
        AccurateMatrix result = matrix1.addRowToRow(0, -1, 2);
        AccurateMatrix correctResult = new AccurateMatrix(new double[][]{
                new double[]{1, 2, 3, 4},
                new double[]{5, 6, 7, 8},
                new double[]{8, 8, 8, 8}
        });
        result.print();
        assertEquals(result, correctResult);
    }

    @Test
    public void reducedRowEchelonForm() {
        AccurateMatrix result = matrix1.reducedRowEchelonForm();
        AccurateMatrix correctResult = new AccurateMatrix(new double[][]{
                new double[]{1, 0, -1, -2},
                new double[]{0, 1, 2, 3},
                new double[]{0, 0, 0, 0}
        });
        assertEquals(result, correctResult);
    }

    @Test
    public void rank() {
        assertEquals(matrix1.rank(), 2);
        assertEquals(new AccurateMatrix(new double[][]{
                new double[]{0, 0, 0},
                new double[]{0, 0, 0}
        }).rank(), 0);
        assertEquals(new AccurateMatrix(new double[][]{
                new double[]{1, 2, 3},
                new double[]{2, 3, 4}
        }).rank(), 2);
    }

    @Test
    public void nullSpace() {
        matrix1.nullSpace().print();
        System.out.println();
        assertEquals(matrix3.nullSpace(), new AccurateMatrix(new double[][]{
                new double[]{1, 0, 0, 0},
                new double[]{0, 1, 0, 0},
                new double[]{0, 0, 1, 0},
                new double[]{0, 0, 0, 1},
        }));
        assertEquals(matrix4.nullSpace(), new AccurateMatrix(4, 0));
    }

    @Test
    public void randomTest() {/*
        new Matrix(new double[][]{
                new double[]{},
                new double[]{},
                new double[]{}
        });/*
        Matrix A = new Matrix(new double[][]{
                new double[]{2, 4, -4, -6},
                new double[]{-4, 1, 4, 27},
                new double[]{-2, -2, 3, 9},
                new double[]{4, 1, 0, -9},
        });
        A.reducedRowEchelonForm().print();
        System.out.println();
        //A.nonNullInverse().dot(new Matrix(new double[][] {new double[] {26, -22, -19, 14}}).transpose()).print();
        A.nullSpace().print();/**/

        AccurateMatrix A = new AccurateMatrix(new double[][]{
                new double[]{1, 1, 1},
                new double[]{1, 2, 4},
                new double[]{1, 3, 9}
        });
        A.reducedRowEchelonForm().print();
    }

    private void println() {
        println("");
    }

    private void println(@Nonnull String string) {
        System.out.println(string);
    }
}