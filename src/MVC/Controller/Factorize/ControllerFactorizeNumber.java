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
 * The ControllerFactorizeNumber class is responsible for factorizing a given
 * number using the Quadratic Sieve algorithm.
 *
 * It extends the Thread class to perform the factorization in a separate
 * thread.
 *
 * Steps:
 *
 * 1. Compute the smoothness bound B for the number.
 *
 * 2. Get all the prime numbers up to B. Done with Sieve of Eratosthenes.
 *
 * 3. Use Euler’s criterion to determine whether N is a quadratic residue modulo
 * each of the prime in step 2. Save the primes that pass the test in the factor
 * base array. Combined with step 2 so it's done in a single loop.
 *
 * 4. Compute a sequence of numbers Qₙ = (x +n)² — N for n = 0, 1, 2, 3…, where
 * x is the ceiling of the square root of N.
 *
 * 5. Solve for n in the congruence (x + n)² — N ≡ 0(mod p) for each p in the
 * factor base.
 *
 * 6. Form a matrix with exponents of the prime factors of the smooth Qₙ’s
 * obtained in step 5. Find the (left) null space of the matrix — use that to
 * determine the set of relations that will combine to give an even parity.
 *
 * 7. Combine the relations to obtain a relation of the form a² ≡ b² (mod N).
 * Use the Euclidean algorithm to compute gcd(a - b, N) and gcd(a + b, N) to
 * obtain the factors of N.
 *
 * Based on:
 * https://medium.com/nerd-for-tech/heres-how-quadratic-sieve-factorization-works-1c878bc94f81
 *
 * @author Sergio
 */
public class ControllerFactorizeNumber extends Thread {

    private final Practica7 PRACTICA_7;

    /**
     * Constructs a ControllerFactorizeNumber object
     *
     * @param practica7 the Practica7 instance
     */
    public ControllerFactorizeNumber(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
    }

    /**
     * Factorizes the given number.
     *
     * @param number the number to factorize
     */
    public void factorize(String number) {
        BigInteger n = new BigInteger(number);
        if (ControllerPrimeNumber.isPrime(n)) {
            System.out.println(n + " is prime.");
        } else {
            factorize(n);
        }
    }

    /**
     * Performs the Quadratic Sieve algorithm to factorize the given number.
     *
     * @param n the number to factorize
     */
    private void factorize(BigInteger n) {

        // 1
        int smoothnessBound = SmoothnessBoundB.getSmoothnessBound(n);

        // 2 & 3
        BigInteger[] factorBase = sieveOfEratosthenesWithEulerCriterion(BigInteger.valueOf(smoothnessBound), n);

        // 4
        BigInteger[] Qn = computeQn(n, factorBase);

        // 5
        BigInteger[] smoothQn = sieveQn(Qn, factorBase);

        // 6
        int[][] exponentsMatrix = createMatrix(smoothQn, factorBase);
        // TODO: Compute exponentsMatrix left null space and determine the set of relations that will combine to give an even parity.

        // 7 
        /**
         * Combining relations in step 6 will produce a single relation of the
         * form a² ≡ b²(mod N). From this we get the much sought-after factors
         * of N by computing gcd(a — b, N) and gcd(a + b, N) using the Euclidean
         * algorithm.
         */
    }

    @Override
    public void run() {
        PRACTICA_7.getModel().getFactorizeNumberStatus().setSolving();

        factorize("21");

        PRACTICA_7.getModel().getFactorizeNumberStatus().setSolved();
    }
}
