package MVC.Controller.Factorize;

import static MVC.Controller.Factorize.Qn.*;
import static MVC.Controller.Factorize.SieveOfEratosthenes.*;
import static MVC.Controller.Factorize.BSmoothValues.*;
import MVC.Controller.PrimeNumber.ControllerPrimeNumber;
import Practica7.Practica7;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sergio
 */
public class ControllerFactorizeNumber extends Thread {

    private final Practica7 practica7;

    public ControllerFactorizeNumber(Practica7 practica7) {
        this.practica7 = practica7;
    }

    public void factorize(String number) {
        BigInteger n = new BigInteger(number);
        if (ControllerPrimeNumber.isPrime(n)) {
            System.out.println(n + " is prime.");
        } else {
            factorize(n);
        }
    }

    private void factorize(BigInteger n) {

        // 1
        int smoothnessBound = SmoothnessBoundB.getSmoothnessBound(n);

        // 2 y 3
        BigInteger[] factorBase = sieveOfEratosthenesWithEulerCriterion(BigInteger.valueOf(50), n); // TODO: cambiar 50 por smoothnessBound
        int[] primes = sieveOfEratosthenes(BigInteger.valueOf(50), n);
        BigInteger[] primesEuler = eulerCriterion(n, primes);
        BigInteger[] bigIntegerArray = new BigInteger[primes.length];
        for (int i = 0; i < primes.length; i++) {
            bigIntegerArray[i] = BigInteger.valueOf(primes[i]);
        }

        // 4
        BigInteger[] Qn = computeQn(n, factorBase);

        // 5 y 6
        SmoothRelationObject smoothRelations = smoothRelations(Qn, bigIntegerArray);
        BigInteger[] smoothRelationsQN = smoothRelations.getQn();
        int[][] smoothRelationsFactorsMatrix = smoothRelations.getFactors();
    }

    public static BigInteger[][] solve(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        // Convert the int matrix to BigInteger matrix
        BigInteger[][] bigIntegerMatrix = new BigInteger[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                bigIntegerMatrix[i][j] = BigInteger.valueOf(matrix[i][j]);
            }
        }

        for (int col = 0; col < numCols; col++) {
            int maxRow = col;
            for (int row = col + 1; row < numRows; row++) {
                if (bigIntegerMatrix[row][col].abs().compareTo(bigIntegerMatrix[maxRow][col].abs()) > 0) {
                    maxRow = row;
                }
            }

            BigInteger[] temp = bigIntegerMatrix[col];
            bigIntegerMatrix[col] = bigIntegerMatrix[maxRow];
            bigIntegerMatrix[maxRow] = temp;

            for (int row = col + 1; row < numRows; row++) {
                BigInteger ratio = bigIntegerMatrix[row][col].divide(bigIntegerMatrix[col][col]);
                for (int i = col; i < numCols; i++) {
                    bigIntegerMatrix[row][i] = bigIntegerMatrix[row][i].subtract(ratio.multiply(bigIntegerMatrix[col][i]));
                }
            }
        }

        BigInteger[][] solution = new BigInteger[numCols - 1][1];
        for (int row = numRows - 1; row >= 0; row--) {
            BigInteger sum = BigInteger.ZERO;
            for (int col = row + 1; col < numCols - 1; col++) {
                sum = sum.add(bigIntegerMatrix[row][col].multiply(solution[col][0]));
            }
            BigInteger coefficient = bigIntegerMatrix[row][row];
            BigInteger constantTerm = bigIntegerMatrix[row][numCols - 1];
            solution[row][0] = constantTerm.subtract(sum).divide(coefficient);
        }

        return solution;
    }

    public static int[][] transposeMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        int[][] transposedMatrix = new int[columns][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    public static int[][] computeNullSpace(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        // Create a list to store the null space vectors
        List<int[]> nullSpace = new ArrayList<>();

        // Create a copy of the matrix
        int[][] copyMatrix = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(matrix[i], 0, copyMatrix[i], 0, numCols);
        }

        // Perform Gaussian elimination
        int row = 0;
        for (int col = 0; col < numCols; col++) {
            if (row >= numRows) {
                break;
            }

            // Find the next non-zero element in the column
            int pivotRow = row;
            while (pivotRow < numRows && copyMatrix[pivotRow][col] == 0) {
                pivotRow++;
            }

            if (pivotRow == numRows) {
                continue; // All elements below this row are already zero
            }

            // Swap rows if necessary
            int[] tempRow = copyMatrix[row];
            copyMatrix[row] = copyMatrix[pivotRow];
            copyMatrix[pivotRow] = tempRow;

            // Perform row operations to make all elements below the pivot zero
            for (int i = row + 1; i < numRows; i++) {
                int factor = copyMatrix[i][col] / copyMatrix[row][col];
                for (int j = col; j < numCols; j++) {
                    copyMatrix[i][j] -= factor * copyMatrix[row][j];
                }
            }

            row++;
        }

        // Find the non-zero rows (basis vectors) in the row-echelon form
        for (int r = 0; r < numRows; r++) {
            boolean isZeroRow = true;
            for (int c = 0; c < numCols; c++) {
                if (copyMatrix[r][c] != 0) {
                    isZeroRow = false;
                    break;
                }
            }
            if (!isZeroRow) {
                nullSpace.add(copyMatrix[r]);
            }
        }

        // Convert the list to a 2D array and return
        int[][] nullSpaceArray = new int[nullSpace.size()][numCols];
        for (int i = 0; i < nullSpace.size(); i++) {
            nullSpaceArray[i] = nullSpace.get(i);
        }
        return nullSpaceArray;
    }

    public static double[][] convertIntMatrixToDoubleMatrix(int[][] intMatrix) {
        int rows = intMatrix.length;
        int cols = intMatrix[0].length;

        double[][] doubleMatrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                doubleMatrix[i][j] = (double) intMatrix[i][j]; // Type casting
            }
        }

        return doubleMatrix;
    }

    @Override
    public void run() {
        practica7.getModel().getFactorizeNumberStatus().setSolving();

        factorize("227179");

        practica7.getModel().getFactorizeNumberStatus().setSolved();
    }

    /**
     * RREF contains advanced matrix math operations Created by Edie Zhou on
     * 1/17/2019.
     */
    public class RREF {

        /**
         * Puts a matrix into reduced row echelon form
         *
         * @param matrix input matrix
         *
         * @return 2D result matrix
         */
        public static int[][] rref(double[][] matrix) {
            int lead = 0;
            int i;

            // number of rows and columns in matrix
            int numRows = matrix.length;
            int numColumns = matrix[0].length;

            for (int k = 0; k < numRows; k++) {
                if (numColumns <= lead) {
                    break;
                }
                i = k;
                while (matrix[i][lead] == 0) {
                    i++;
                    if (numRows == i) {
                        i = k;
                        lead++;
                        if (numColumns == lead) {
                            break;
                        }
                    }

                }
                matrix = rowSwap(matrix, i, k);
                if (matrix[k][lead] != 0) {
                    matrix = rowScale(matrix, k, (1 / matrix[k][lead]));
                }
                for (i = 0; i < numRows; i++) {
                    if (i != k) {
                        matrix = rowAddScale(matrix, k, i, ((-1) * matrix[i][lead]));
                    }
                }
                lead++;
            }

            return convertDoubleMatrixToIntMatrix(matrix);
        }

        private static int[][] convertDoubleMatrixToIntMatrix(double[][] doubleMatrix) {
            int numRows = doubleMatrix.length;
            int numCols = doubleMatrix[0].length;

            int[][] intMatrix = new int[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    intMatrix[i][j] = (int) Math.round(doubleMatrix[i][j]);
                }
            }

            return intMatrix;
        }

        /**
         * Swap positions of 2 rows
         *
         * @param matrix matrix before row additon
         * @param rowIndex1 int index of row to swap
         * @param rowIndex2 int index of row to swap
         *
         * @return matrix after row swap
         */
        private static double[][] rowSwap(double[][] matrix, int rowIndex1,
                int rowIndex2) {
            // number of columns in matrix
            int numColumns = matrix[0].length;

            // holds number to be swapped
            double hold;

            for (int k = 0; k < numColumns; k++) {
                hold = matrix[rowIndex2][k];
                matrix[rowIndex2][k] = matrix[rowIndex1][k];
                matrix[rowIndex1][k] = hold;
            }

            return matrix;
        }

        /**
         * Adds 2 rows together row2 = row2 + row1
         *
         * @param matrix matrix before row additon
         * @param rowIndex1 int index of row to be added
         * @param rowIndex2 int index or row that row1 is added to
         *
         * @return matrix after row addition
         */
        private static double[][] rowAdd(double[][] matrix, int rowIndex1,
                int rowIndex2) {
            // number of columns in matrix
            int numColumns = matrix[0].length;

            for (int k = 0; k < numColumns; k++) {
                matrix[rowIndex2][k] += matrix[rowIndex1][k];
            }

            return matrix;
        }

        /**
         * Multiplies a row by a scalar
         *
         * @param matrix matrix before row additon
         * @param rowIndex int index of row to be scaled
         * @param scalar double to scale row by
         *
         * @return matrix after row scaling
         */
        private static double[][] rowScale(double[][] matrix, int rowIndex,
                double scalar) {
            // number of columns in matrix
            int numColumns = matrix[0].length;

            for (int k = 0; k < numColumns; k++) {
                matrix[rowIndex][k] *= scalar;
            }

            return matrix;
        }

        /**
         * Adds a row by the scalar of another row row2 = row2 + (row1 * scalar)
         *
         * @param matrix matrix before row additon
         * @param rowIndex1 int index of row to be added
         * @param rowIndex2 int index or row that row1 is added to
         * @param scalar double to scale row by
         *
         * @return matrix after row addition
         */
        private static double[][] rowAddScale(double[][] matrix, int rowIndex1,
                int rowIndex2, double scalar) {
            // number of columns in matrix
            int numColumns = matrix[0].length;

            for (int k = 0; k < numColumns; k++) {
                matrix[rowIndex2][k] += (matrix[rowIndex1][k] * scalar);
            }

            return matrix;
        }

    }

}
