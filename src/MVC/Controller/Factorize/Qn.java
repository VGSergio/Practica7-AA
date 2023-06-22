package MVC.Controller.Factorize;

import java.math.BigInteger;

/**
 * This class provides a method for computing the congruence sequence of the
 * form X^2 = Y^2 (mod N) for a given modulus N and factor base. Based on:
 * https://medium.com/nerd-for-tech/heres-how-quadratic-sieve-factorization-works-1c878bc94f81
 *
 * @author Sergio
 */
public class Qn {

    /**
     * Computes the congruence sequence of the form X^2 = Y^2 (mod N) (or what
     * is the same thing) X^2 (mod N) = Y^2 for a given modulus N and factor
     * base. X^2 (mod N) is equivallent to x^2 - N in the sieving interval.
     *
     * @param n the modulus N
     * @param factorBase the factor base array
     * @return an array of BigInteger representing the congruence sequence
     */
    public static BigInteger[] computeQn(BigInteger n, BigInteger[] factorBase) {
        int sqrt = (int) Math.ceil(Math.sqrt(n.doubleValue()));

        BigInteger[] Q = new BigInteger[factorBase.length + 8]; // TODO: change "+ 8" per * 6

        for (int i = 0; i < Q.length; i++) {
            BigInteger Qn = BigInteger.valueOf(sqrt + i).pow(2).subtract(n);
            Q[i] = Qn;
        }

        return Q;
    }
}
