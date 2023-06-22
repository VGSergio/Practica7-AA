package MVC.Controller.Factorize;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for finding prime numbers and applying the Euler
 * criterion using the Sieve of Eratosthenes algorithm. Adapted from
 * https://www.geeksforgeeks.org/sieve-of-eratosthenes/ and
 * https://www.geeksforgeeks.org/eulers-criterion-check-if-square-root-under-modulo-p-exists/
 * with BigInteger support.
 *
 * @author Sergio
 */
public class SieveOfEratosthenes {

    /**
     * Finds all prime numbers less than or equal to a given number
     * smoothnessBound that passes the Euler criterion when evaluated with the
     * number n.
     *
     * Optimized and adapted for smoothnessBound and Euler's Criterion, based on
     * https://www.geeksforgeeks.org/sieve-of-eratosthenes/
     *
     * @param smoothnessBound The upper limit for prime number search.
     * @param n The number to be evaluated with the Euler criterion.
     * @return An array of prime numbers less than or equal to N that satisfy
     * the Euler criterion with n.
     */
    public static BigInteger[] sieveOfEratosthenesWithEulerCriterion(BigInteger smoothnessBound, BigInteger n) {
        int maxLimit = smoothnessBound.intValue();
        boolean[] isComposite = new boolean[maxLimit + 1];

        List<BigInteger> primes = new ArrayList<>();

        for (int p = 2; p <= maxLimit; p++) {
            if (!isComposite[p]) {
                if (eulerCriterion(n, p) == 1) {
                    primes.add(BigInteger.valueOf(p));
                }
                for (int i = p * p; i <= maxLimit; i += p) {
                    isComposite[i] = true;
                }
            }
        }

        return primes.toArray(BigInteger[]::new);
    }

    /**
     * Applies the Euler criterion to determine the quadratic residue status of
     * a number 'n' modulo 'p'.
     *
     * Adaptation of
     * https://www.geeksforgeeks.org/eulers-criterion-check-if-square-root-under-modulo-p-exists/
     * using BigInteger
     *
     * @param n The number for which the quadratic residue status is determined.
     * @param p The modulus within which the quadratic residue status is
     * evaluated.
     * @return 1 if 'n' is a quadratic residue modulo 'p', -1 if it is a
     * quadratic non-residue, or 0 if 'n' is not coprime to 'p'.
     */
    private static int eulerCriterion(BigInteger n, int p) {
        int power = (p - 1) >> 1; // Integer division by 2
        BigInteger result = n.modPow(BigInteger.valueOf(power), BigInteger.valueOf(p));

        if (result.equals(BigInteger.ONE)) {
            return 1; // a is a quadratic residue modulo p
        } else if (result.equals(BigInteger.valueOf(p - 1))) {
            return -1; // a is a quadratic non-residue modulo p
        } else {
            return 0; // a is not coprime to p
        }
    }

    /**
     * Finds all prime numbers less than or equal to a given smoothnessBound.
     *
     * @param smoothnessBound The upper limit for prime number search.
     * @return An array of prime numbers less than or equal to smoothnessBound.
     */
    public static int[] sieveOfEratosthenes(BigInteger smoothnessBound, BigInteger n) {
        int maxLimit = smoothnessBound.intValue();
        boolean[] isComposite = new boolean[maxLimit + 1];

        List<Integer> primes = new ArrayList<>();

        for (int p = 2; p <= maxLimit; p++) {
            if (!isComposite[p]) {
                primes.add(p);
                for (int i = p * p; i <= maxLimit; i += p) {
                    isComposite[i] = true;
                }
            }
        }

        return primes.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Applies the Euler criterion to determine the quadratic residue status of
     * a number 'n' modulo 'p'.
     *
     * Adaptation of
     * https://www.geeksforgeeks.org/eulers-criterion-check-if-square-root-under-modulo-p-exists/
     * using BigInteger
     *
     * @param n The number for which the quadratic residue status is determined.
     * @param primes The modulus within which the quadratic residue status is
     * evaluated.
     * @return 1 if 'n' is a quadratic residue modulo 'p', -1 if it is a
     * quadratic non-residue, or 0 if 'n' is not coprime to 'p'.
     */
    public static BigInteger[] eulerCriterion(BigInteger n, int[] primes) {
        List<BigInteger> primesEuler = new ArrayList<>();
        for (int p : primes) {
            if (eulerCriterion(n, p) == 1) {
                primesEuler.add(BigInteger.valueOf(p));
            }
        }
        return primesEuler.toArray(BigInteger[]::new);
    }

}
