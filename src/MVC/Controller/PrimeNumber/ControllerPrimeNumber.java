package MVC.Controller.PrimeNumber;

import Practica7.Practica7;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents a controller for checking prime numbers using the Miller-Rabin
 * primality test. The implementation is based on the algorithm described in the
 * following article:
 * https://www.geeksforgeeks.org/primality-test-set-3-miller-rabin/
 *
 * This class extends Thread to enable running the algorithm in a separate
 * thread. It checks if a given number is prime and updates the simulation
 * status accordingly. It also provides utility methods for performing the
 * Miller-Rabin primality test.
 *
 * Note: The Miller-Rabin primality test provides a probabilistic result. It
 * returns true if the number is probably prime and false if it is composite.
 *
 * Note: This implementation uses a default number of trials for the
 * Miller-Rabin test. The accuracy level can be adjusted by modifying the
 * DEFAULT_NUM_TRIALS constant.
 *
 * @author Sergio
 */
public class ControllerPrimeNumber extends Thread {

    private final Practica7 PRACTICA_7;
    private static final int DEFAULT_NUM_TRIALS = 7;

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;
    private static final BigInteger FOUR = BigInteger.valueOf(4);

    /**
     * Constructor for the ControllerPrimeNumber class.
     *
     * @param practica7 an instance of the Practica7 class.
     */
    public ControllerPrimeNumber(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
    }

    /**
     * Checks if the given number is prime.
     *
     * @param number the number to be checked
     * @return true if the number is probably prime, false if it is composite
     */
    public boolean isPrime(String number) {
        return isPrime(new BigInteger(number), DEFAULT_NUM_TRIALS);
    }

    /**
     * Checks if the given number is prime.
     *
     * @param number the number to be checked
     * @return true if the number is probably prime, false if it is composite
     */
    public static boolean isPrime(BigInteger number) {
        return isPrime(number, DEFAULT_NUM_TRIALS);
    }

    /**
     * Checks if the given BigInteger is prime. It returns false if n is
     * composite and returns true if n is probably prime. numTrials is an input
     * parameter that determines accuracy level. Higher value of numTrials
     * indicates more accuracy.
     *
     * @param n the BigInteger to be checked
     * @param numTrials the number of iterations for the Miller-Rabin primality
     * test
     * @return false if the number is composite, true if it is probably prime
     */
    private static boolean isPrime(BigInteger n, int numTrials) {

        // (n <= 1 || n == 4)
        if (n.compareTo(ONE) <= 0 || n.compareTo(FOUR) == 0) {
            return false;
        }

        //(n <= 3)
        if (n.compareTo(BigInteger.valueOf(3)) <= 0) {
            return true;
        }

        // Find d such that n-1 = 2^s * d for some d >= 1
        BigInteger d = n.subtract(ONE);
        // while even
        while (!d.testBit(0)) {
            d = d.shiftRight(1); // d /= 2
        }

        // Iterate given number 'numTrials' times
        for (int i = 0; i < numTrials; i++) {
            if (!millerRabinTest(d, n)) {
                return false;
            }
        }

        return true;
    }

    /**
     * This function is called DEFAULT_NUM_TRIALS times. It returns false if n
     * is composite and true if n is probably prime. d is an odd number such
     * that d*(2^r) = n-1 for some r >= 1.
     *
     * @param d the odd number
     * @param n the BigInteger to be checked
     * @return false if the number is composite, true if it is probably prime
     */
    private static boolean millerRabinTest(BigInteger d, BigInteger n) {

        // Pick a random number in [2..n-2]
        BigInteger nMinusFour = n.subtract(FOUR);
        BigInteger random = BigDecimal.valueOf(Math.random()).toBigInteger();
        BigInteger a = TWO.add(random.mod(nMinusFour));

        // Compute a^d % n
        BigInteger x = a.modPow(d, n);

        if (x.equals(ONE) || x.equals(n.subtract(ONE))) {
            return true;
        }

        // Keep squaring x while one of the following doesn't happen
        // (i) d does not reach n-1
        // (ii) (x^2) % n is not 1
        // (iii) (x^2) % n is not n-1
        while (!d.equals(n.subtract(ONE))) {
            x = x.modPow(TWO, n);
            d = d.shiftLeft(1); // d *= 2

            if (x.equals(ONE)) {
                return false;
            }
            if (x.equals(n.subtract(ONE))) {
                return true;
            }

        }

        // Return composite
        return false;
    }

    @Override
    public void run() {
        PRACTICA_7.getModel().getPrimeNumberStatus().setSolving();

        boolean prime = isPrime(PRACTICA_7.getModel().getPrimeNumberValue());
        PRACTICA_7.notify("Solution", "PrimeNumber", prime ? "Yes" : "No");

        PRACTICA_7.getModel().getPrimeNumberStatus().setSolved();
    }

}
