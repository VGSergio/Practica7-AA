package MVC.Controller.Factorize;

import java.math.BigInteger;

/**
 * Represents a class that computes the smoothness bound for finding a desired
 * number of smooth numbers.
 *
 * The smoothness bound is a value used in factoring composite numbers. It helps
 * determine the upper limit for the size of the prime factors that are
 * considered during the factorization process.
 *
 * This class provides a method to calculate the smoothness bound based on a
 * given composite number.
 *
 * Note: The smoothness bound is computed using mathematical formulas and
 * approximations. It may not be an exact value, but it provides an estimation
 * for the factorization process.
 *
 * Note: The smoothness bound is returned as an integer value.
 *
 * Note: This implementation uses the natural logarithm (base e) and the square
 * root function from the Math class.
 *
 * Formula extracted from:
 * https://medium.com/nerd-for-tech/heres-how-quadratic-sieve-factorization-works-1c878bc94f81
 *
 * @author Sergio
 */
public class SmoothnessBoundB {

    /**
     * Computes the smoothness bound for finding a desired number of smooth
     * numbers.
     *
     * @param compositeNumber The composite number for which the smoothness
     * bound is computed.
     * @return The smoothness bound as an integer.
     */
    public static int getSmoothnessBound(BigInteger compositeNumber) {
        double logN = Math.log(compositeNumber.doubleValue());
        double logLogN = Math.log(logN);
        return (int) Math.exp(0.5 * Math.sqrt(logN * logLogN));
    }
}
