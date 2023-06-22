package MVC.Controller.Factorize;

import static MVC.Controller.Factorize.ExponentsMatrix.createMatrix;
import static MVC.Controller.Factorize.Qn.*;
import static MVC.Controller.Factorize.SieveOfEratosthenes.*;
import static MVC.Controller.Factorize.SmoothQn.*;
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

    private final Practica7 PRACTICA_7;

    public ControllerFactorizeNumber(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
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

        // 2 & 3
        BigInteger[] factorBase = sieveOfEratosthenesWithEulerCriterion(BigInteger.valueOf(50), n); // TODO: cambiar 50 por smoothnessBound

        // 4
        BigInteger[] Qn = computeQn(n, factorBase);

        // 5
        BigInteger[] smoothQn = sieveQn(Qn, factorBase);

        // 6
        System.out.println("exponentsMatrix");
        int[][] exponentsMatrix = createMatrix(smoothQn, factorBase);

        System.out.println("nullSpace");
        int[][] nullSpace = findRelations(factorBase, Qn, smoothQn);
        for (int[] is : nullSpace) {
            System.out.println(Arrays.toString(is));
        }

        // Step 7: Perform Gaussian elimination to reduce the null space matrix to row-echelon form
        System.out.println("rowEchelonMatrix");
        int[][] rowEchelonMatrix = gaussianElimination(nullSpace);
        for (int[] is : rowEchelonMatrix) {
            System.out.println(Arrays.toString(is));
        }

        // Step 8: Identify linearly independent rows (relations)
        System.out.println("independentRelations");
        List<int[]> independentRelations = identifyIndependentRelations(rowEchelonMatrix);
        for (int[] is : independentRelations) {
            System.out.println(Arrays.toString(is));
        }

        // Step 9: Compute the product of corresponding smooth Qn values for each independent relation
        System.out.println("productQnList");
        List<BigInteger> productQnList = computeProductQn(independentRelations, smoothQn);

        // Step 10: Compute square roots and check for non-trivial square roots modulo N
        System.out.println("factors");
        List<BigInteger> factors = new ArrayList<>();
        for (BigInteger productQn : productQnList) {
            BigInteger sqrtN = productQn.sqrt();
            if (sqrtN.multiply(sqrtN).mod(n).equals(productQn)) {
                BigInteger factor = n.gcd(sqrtN.subtract(productQn));
                if (!factor.equals(BigInteger.ONE) && !factor.equals(n)) {
                    factors.add(factor);
                }
            }
        }
        System.out.println(factors);

    }

    private static int[][] findRelations(BigInteger[] factorBase, BigInteger[] Qn, BigInteger[] smoothQn) {
        int numRelations = smoothQn.length;
        int numPrimes = factorBase.length;

        // Create a matrix to hold the exponents of the prime factors
        int[][] matrix = new int[numRelations][numPrimes];

        // Fill the matrix with the exponents of the prime factors
        for (int i = 0; i < numRelations; i++) {
            BigInteger qn = smoothQn[i];
            for (int j = 0; j < numPrimes; j++) {
                BigInteger p = factorBase[j];
                while (qn.mod(p).equals(BigInteger.ZERO)) {
                    matrix[i][j]++;
                    qn = qn.divide(p);
                }
            }
        }

        // Use the block Lanczos or block Wiedemann algorithm to compute the null space
        // Here, we'll just simulate the null space calculation
        int[][] nullSpace = new int[numRelations][numPrimes];
        boolean[] used = new boolean[numRelations];
        int nullSpaceSize = 0;

        for (int i = 0; i < numRelations; i++) {
            if (!used[i]) {
                for (int j = 0; j < numPrimes; j++) {
                    int sum = 0;
                    for (int k = i; k < numRelations; k++) {
                        sum += matrix[k][j];
                    }
                    nullSpace[nullSpaceSize][j] = sum % 2;
                }

                // Mark the related rows as used
                for (int k = i; k < numRelations; k++) {
                    if (Arrays.equals(matrix[k], matrix[i])) {
                        used[k] = true;
                    }
                }

                nullSpaceSize++;
            }
        }

        // Trim the nullSpace array to the actual size
        int[][] trimmedNullSpace = new int[nullSpaceSize][numPrimes];
        System.arraycopy(nullSpace, 0, trimmedNullSpace, 0, nullSpaceSize);

        return trimmedNullSpace;
    }

    private static int[][] findNullSpace(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        // Augment the matrix with an identity matrix
        int[][] augmentedMatrix = new int[numRows][numCols + numRows];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, numCols);
            augmentedMatrix[i][numCols + i] = 1;
        }

        // Perform Gaussian elimination
        for (int row = 0; row < numRows; row++) {
            if (augmentedMatrix[row][row] == 0) {
                int swapRow = findNonZeroRow(augmentedMatrix, row);
                if (swapRow == -1) {
                    continue;  // No pivot found, move to the next row
                }
                swapRows(augmentedMatrix, row, swapRow);
            }

            int pivot = augmentedMatrix[row][row];
            for (int col = 0; col < numCols + numRows; col++) {
                augmentedMatrix[row][col] = (augmentedMatrix[row][col] * modInverse(pivot)) % 2;
            }

            for (int otherRow = 0; otherRow < numRows; otherRow++) {
                if (otherRow != row && augmentedMatrix[otherRow][row] != 0) {
                    int multiplier = augmentedMatrix[otherRow][row];
                    for (int col = 0; col < numCols + numRows; col++) {
                        augmentedMatrix[otherRow][col] = (augmentedMatrix[otherRow][col] + multiplier * augmentedMatrix[row][col]) % 2;
                    }
                }
            }
        }

        // Extract the null space
        int[][] nullSpace = new int[numCols][numRows - numCols];
        int nullSpaceCol = 0;
        for (int col = numCols; col < numCols + numRows; col++) {
            for (int row = 0; row < numRows; row++) {
                nullSpace[row][nullSpaceCol] = augmentedMatrix[row][col];
            }
            nullSpaceCol++;
        }

        return nullSpace;
    }

    private static int findNonZeroRow(int[][] matrix, int startRow) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        for (int row = startRow + 1; row < numRows; row++) {
            if (matrix[row][startRow] != 0) {
                return row;
            }
        }
        return -1;
    }

    private static void swapRows(int[][] matrix, int row1, int row2) {
        int[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private static int modInverse(int num) {
        if (num % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private static List<int[]> identifyIndependentRelations(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        List<int[]> independentRelations = new ArrayList<>();
        boolean[] usedRows = new boolean[numRows];

        for (int r = 0; r < numRows; r++) {
            int leadingCol = -1;
            for (int c = 0; c < numCols; c++) {
                if (matrix[r][c] == 1) {
                    leadingCol = c;
                    break;
                }
            }

            if (leadingCol != -1 && !usedRows[r]) {
                independentRelations.add(matrix[r]);
                usedRows[r] = true;

                // Mark the rows with the same leading column as used
                for (int i = r + 1; i < numRows; i++) {
                    if (matrix[i][leadingCol] == 1) {
                        usedRows[i] = true;
                    }
                }
            }
        }

        return independentRelations;
    }

    private static int[][] gaussianElimination(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        int[][] rowEchelonMatrix = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            rowEchelonMatrix[i] = Arrays.copyOf(matrix[i], numCols);
        }

        int lead = 0;
        for (int r = 0; r < numRows; r++) {
            if (numCols <= lead) {
                break;
            }

            int pivotRow = r;
            while (rowEchelonMatrix[pivotRow][lead] == 0) {
                pivotRow++;
                if (numRows == pivotRow) {
                    pivotRow = r;
                    lead++;
                    if (numCols == lead) {
                        return rowEchelonMatrix; // Matrix is in row-echelon form
                    }
                }
            }

            // Swap rows
            int[] temp = rowEchelonMatrix[r];
            rowEchelonMatrix[r] = rowEchelonMatrix[pivotRow];
            rowEchelonMatrix[pivotRow] = temp;

            int pivot = rowEchelonMatrix[r][lead];
            if (pivot != 0) {
                for (int j = 0; j < numCols; j++) {
                    rowEchelonMatrix[r][j] /= pivot;
                }
            }

            for (int i = 0; i < numRows; i++) {
                if (i != r) {
                    int factor = rowEchelonMatrix[i][lead];
                    for (int j = 0; j < numCols; j++) {
                        rowEchelonMatrix[i][j] -= factor * rowEchelonMatrix[r][j];
                    }
                }
            }

            lead++;
        }

        return rowEchelonMatrix;
    }

    // Helper function to convert int[][] to double[][]
    private static double[][] convertToDoubleArray(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] doubleMatrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                doubleMatrix[i][j] = matrix[i][j];
            }
        }

        return doubleMatrix;
    }

    private static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] transposedMatrix = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    public static List<BigInteger> computeProductQn(List<int[]> independentRelations, BigInteger[] smoothQn) {
        List<BigInteger> productQnList = new ArrayList<>();

        for (int[] relation : independentRelations) {
            BigInteger productQn = BigInteger.ONE;

            for (int i = 0; i < relation.length - 1; i++) {
                if (relation[i] == 1) {
                    productQn = productQn.multiply(smoothQn[i]);
                }
            }

            productQnList.add(productQn);
        }

        return productQnList;
    }

    @Override
    public void run() {
        PRACTICA_7.getModel().getFactorizeNumberStatus().setSolving();

        factorize("21");

        PRACTICA_7.getModel().getFactorizeNumberStatus().setSolved();
    }
}
