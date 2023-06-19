package MVC.Controller.Factorize;

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

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;

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
        int smoothnessBound = getSmoothnessBound(n);
        System.out.println(smoothnessBound);
        // 2 y 3
        BigInteger[] factorBase = sieveOfEratosthenesWithEulerCriterion(BigInteger.valueOf(50), n);
        System.out.println(Arrays.toString(factorBase));
        // 4
        BigInteger[] congruenceSequence = computeCongruenceSequence(n, factorBase);
        System.out.println(Arrays.toString(congruenceSequence));
        // 5

    }

    /**
     * Computes the smoothness bound for finding a desired number of smooth
     * numbers.
     *
     * @param compositeNumber The composite number for which the smoothness
     * bound is computed.
     * @return The smoothness bound as an integer.
     */
    private static int getSmoothnessBound(BigInteger compositeNumber) {
        double logN = Math.log(compositeNumber.doubleValue());
        double logLogN = Math.log(logN);
        return (int) Math.exp(0.5 * Math.sqrt(logN * logLogN));
    }

    /**
     * Finds all prime numbers less than or equal to a given number
     * smoothnessBound that passes the Euler criterion when evaluated with the
     * number n.
     *
     * @param smoothnessBound The upper limit for prime number search.
     * @param n The number to be evaluated with the Euler criterion.
     * @return An array of prime numbers less than or equal to N that satisfy
     * the Euler criterion with n.
     */
    private static BigInteger[] sieveOfEratosthenesWithEulerCriterion(BigInteger smoothnessBound, BigInteger n) {
        int maxLimit = smoothnessBound.intValueExact();
        boolean[] isComposite = new boolean[maxLimit + 1];
        List<BigInteger> primes = new ArrayList<>();

        for (BigInteger i = TWO; i.compareTo(smoothnessBound) <= 0; i = i.add(ONE)) {
            int currentNumber = i.intValueExact();
            if (!isComposite[currentNumber]) {
                if (eulerCriterion(n, i) == 1) {
                    primes.add(i);
                }

                for (int j = 2 * currentNumber; j <= maxLimit; j += currentNumber) {
                    isComposite[j] = true;
                }
            }
        }

        return primes.toArray(BigInteger[]::new);
    }

    /**
     * Applies the Euler criterion to determine the quadratic residue status of
     * a number 'n' modulo 'p'.
     *
     * @param n The number for which the quadratic residue status is determined.
     * @param p The modulus within which the quadratic residue status is
     * evaluated.
     * @return 1 if 'n' is a quadratic residue modulo 'p', -1 if it is a
     * quadratic non-residue, or 0 if 'n' is not coprime to 'p'.
     */
    private static int eulerCriterion(BigInteger n, BigInteger p) {
        BigInteger power = p.subtract(ONE).shiftRight(1);
        BigInteger result = n.modPow(power, p);

        if (result.equals(ONE)) {
            return 1;  // a is a quadratic residue modulo p
        } else if (result.equals(p.subtract(ONE))) {
            return -1; // a is a quadratic non-residue modulo p
        } else {
            return 0;  // a is not coprime to p
        }
    }

    /**
     * Computes the congruence sequence of the form X^2 = Y^2 (mod N) (or what
     * is the same thing) x^2 (mod N) = Y^2 for a given modulus N and factor
     * base.
     *
     * @param n the modulus N
     * @param factorBase the factor base array
     * @return an array of BigInteger representing the congruence sequence
     */
    private static BigInteger[] computeCongruenceSequence(BigInteger n, BigInteger[] factorBase) {
        int sqrt = (int) Math.ceil(Math.sqrt(n.doubleValue()));

        BigInteger[] sequence = new BigInteger[factorBase.length * 6];

        for (int i = 0; i < sequence.length; i++) {
            BigInteger qn = BigInteger.valueOf(sqrt + i).pow(2).subtract(n);
            sequence[i] = qn;
        }

        return sequence;
    }

    @Override
    public void run() {
        practica7.getModel().getFactorizeNumberStatus().setSolving();

        factorize("227179");

        practica7.getModel().getFactorizeNumberStatus().setSolved();
    }

}
