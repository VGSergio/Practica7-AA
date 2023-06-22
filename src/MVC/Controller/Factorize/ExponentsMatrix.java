package MVC.Controller.Factorize;

import java.math.BigInteger;

/**
 * The ExponentsMatrix class provides a method for creating an exponents matrix
 * based on the given smoothQn array and factorBase array. The exponents matrix
 * represents the exponents of prime factors in the factorization of the
 * smoothQn values.
 *
 * Each row of the matrix corresponds to a smoothQn value, and each column
 * corresponds to a prime factor from the factorBase. The value at the (i, j)
 * position in the matrix represents the exponent of the j-th prime factor in
 * the factorization of the i-th smoothQn value.
 *
 * The matrix is created by iterating over the smoothQn values and the prime
 * factors, counting the number of times each prime factor divides each smoothQn
 * value. The resulting matrix provides the exponents required for the
 * factorization process.
 *
 * Note: The exponents matrix can be further processed, such as taking the
 * modulo 2 of each element to convert the exponents to binary values (0 or 1).
 * *
 *
 * @author Sergio
 */
public class ExponentsMatrix {

    /**
     * Creates an exponents matrix based on the given smoothQn array and
     * factorBase array.
     *
     * @param smoothQn the array of BigInteger representing the smoothQn values
     * @param factorBase the array of BigInteger representing the factor base
     * @return the created exponents matrix as a 2D array of integers
     */
    public static int[][] createMatrix(BigInteger[] smoothQn, BigInteger[] factorBase) {
        int numRows = smoothQn.length;
        int numCols = factorBase.length;
        int[][] matrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            BigInteger q = smoothQn[i];

            for (int j = 0; j < numCols; j++) {
                BigInteger prime = factorBase[j];

                while (q.mod(prime).equals(BigInteger.ZERO)) {
                    q = q.divide(prime);
                    matrix[i][j]++;
                }
                // matrix[i][j] = matrix[i][j] % 2; // Just the parities
            }
        }

        return matrix;
    }
}
