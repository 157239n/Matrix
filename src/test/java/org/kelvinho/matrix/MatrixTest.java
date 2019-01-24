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

    private void println() {
        println("");
    }

    private void println(@Nonnull String string) {
        System.out.println(string);
    }
}