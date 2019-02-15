package org.kelvinho.matrix;

import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public class MatrixTest {
    private float[][] matrix1Values = new float[][]{
            new float[]{1, 2, 3, 4},
            new float[]{5, 6, 7, 8},
            new float[]{9, 10, 11, 12}
    };
    private Matrix matrix1 = new Matrix(matrix1Values);

    private float[][] matrix2Values = new float[][]{
            new float[]{1, 2},
            new float[]{3, 4},
            new float[]{5, 6},
            new float[]{7, 8},
    };
    private Matrix matrix2 = new Matrix(matrix2Values);

    private Matrix matrix3 = new Matrix(new float[][]{
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0}
    });

    private Matrix matrix4 = new Matrix(new float[][]{
            new float[]{1, 0, 0, 0},
            new float[]{0, 1, 0, 0},
            new float[]{0, 0, 1, 0},
            new float[]{0, 0, 0, 1}
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
        float[][] transposeOfMatrix1Values = new float[][]{
                new float[]{1, 5, 9},
                new float[]{2, 6, 10},
                new float[]{3, 7, 11},
                new float[]{4, 8, 12}
        };
        Matrix transposeOfMatrix1 = new Matrix(transposeOfMatrix1Values);
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
        new Matrix(3, 4, 0).minus(matrix1).print();
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
        Matrix result = matrix1.addRowToRow(0, -1, 2);
        Matrix correctResult = new Matrix(new float[][]{
                new float[]{1, 2, 3, 4},
                new float[]{5, 6, 7, 8},
                new float[]{8, 8, 8, 8}
        });
        result.print();
        assertEquals(result, correctResult);
    }

    @Test
    public void reducedRowEchelonForm() {
        Matrix result = matrix1.reducedRowEchelonForm();
        Matrix correctResult = new Matrix(new float[][]{
                new float[]{1, 0, -1, -2},
                new float[]{0, 1, 2, 3},
                new float[]{0, 0, 0, 0}
        });
        assertEquals(result, correctResult);
    }

    @Test
    public void rank() {
        assertEquals(matrix1.rank(), 2);
        assertEquals(new Matrix(new float[][]{
                new float[]{0, 0, 0},
                new float[]{0, 0, 0}
        }).rank(), 0);
        assertEquals(new Matrix(new float[][]{
                new float[]{1, 2, 3},
                new float[]{2, 3, 4}
        }).rank(), 2);
    }

    @Test
    public void nullSpace() {
        matrix1.nullSpace().print();
        System.out.println();
        assertEquals(matrix3.nullSpace(), new Matrix(new float[][]{
                new float[]{1, 0, 0, 0},
                new float[]{0, 1, 0, 0},
                new float[]{0, 0, 1, 0},
                new float[]{0, 0, 0, 1},
        }));
        assertEquals(matrix4.nullSpace(), new Matrix(4, 0));
    }

    @Test
    public void randomTest() {/*
        new Matrix(new float[][]{
                new float[]{},
                new float[]{},
                new float[]{}
        });/**/
        Matrix A = new Matrix(new float[][]{
                new float[]{2, 4, -4, -6},
                new float[]{-4, 1, 4, 27},
                new float[]{-2, -2, 3, 9},
                new float[]{4, 1, 0, -9},
        });
        A.reducedRowEchelonForm().print();
        System.out.println();
        //A.nonNullInverse().dot(new Matrix(new float[][] {new float[] {26, -22, -19, 14}}).transpose()).print();
        A.nullSpace().print();/**/
    }

    private void println() {
        println("");
    }

    private void println(@Nonnull String string) {
        System.out.println(string);
    }
}