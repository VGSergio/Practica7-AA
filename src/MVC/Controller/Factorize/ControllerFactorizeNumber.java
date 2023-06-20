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
        System.out.println("Smoothness Bound " + smoothnessBound);

        // 2 y 3
        BigInteger[] factorBase = sieveOfEratosthenesWithEulerCriterion(BigInteger.valueOf(50), n); // TODO: cambiar 50 por smoothnessBound
        System.out.println("Factor base " + Arrays.toString(factorBase));

        // 4
        BigInteger[] Qn = computeQn(n, factorBase);
        System.out.println("Qn's " + Arrays.toString(Qn));

        // 5 y 6
        SmoothRelationObject smoothRelations = smoothRelations(Qn, factorBase);
        BigInteger[] smoothRelationsQN = smoothRelations.getQn();
        int[][] smoothRelationsFactorsMatrix = smoothRelations.getFactors();
        System.out.println("Smooth values" + Arrays.toString(smoothRelationsQN));
        for (int[] is : smoothRelationsFactorsMatrix) {
            System.out.println(Arrays.toString(is));
        }
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
     * Optimized and adapted for smoothnessBound and Euler's Criterion, based on
     * https://www.geeksforgeeks.org/sieve-of-eratosthenes/
     *
     * @param smoothnessBound The upper limit for prime number search.
     * @param n The number to be evaluated with the Euler criterion.
     * @return An array of prime numbers less than or equal to N that satisfy
     * the Euler criterion with n.
     */
    private static BigInteger[] sieveOfEratosthenesWithEulerCriterion(BigInteger smoothnessBound, BigInteger n) {
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

        return primes.toArray(new BigInteger[0]);
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
        int power = (p - 1) >> 1;  // Integer division by 2
        BigInteger result = n.modPow(BigInteger.valueOf(power), BigInteger.valueOf(p));

        if (result.equals(ONE)) {
            return 1;  // a is a quadratic residue modulo p
        } else if (result.equals(BigInteger.valueOf(p - 1))) {
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
    private static BigInteger[] computeQn(BigInteger n, BigInteger[] factorBase) {
        int sqrt = (int) Math.ceil(Math.sqrt(n.doubleValue()));

        BigInteger[] Q = new BigInteger[factorBase.length + 8]; // TODO: cambiar "+ 8" por * 6

        for (int i = 0; i < Q.length; i++) {
            BigInteger Qn = BigInteger.valueOf(sqrt + i).pow(2).subtract(n);
            Q[i] = Qn;
        }

        return Q;
    }

    private static SmoothRelationObject smoothRelations(BigInteger[] Qn, BigInteger[] factorBase) {
        List<BigInteger> smoothRelations = new ArrayList<>();
        List<int[]> factorsMatrix = new ArrayList<>();

        for (BigInteger Q : Qn) {
            int idx = 0;
            BigInteger qn = Q;
            int[] factors = new int[factorBase.length];
            for (BigInteger p : factorBase) {
                BigInteger roots = TonelliShanks.STonelli(qn, p);
                if (roots == null) {
                    // Q is not B-smooth, skip to the next Qₙ
                    break;
                }

                // Divide Q by p until it is no longer divisible
                while (qn.mod(p).equals(ZERO)) {
                    qn = qn.divide(p);
                    factors[idx]++;
                    // Store the factor p for Qₙ
                }
                idx++;
            }

            // Check if Q is fully factored into primes from the factor base
            if (qn.equals(ONE)) {
                smoothRelations.add(Q);
                factorsMatrix.add(factors);
                // Q is B-smooth
                // Continue with the next Qₙ
            }
        }

        BigInteger[] smoothRelationsArray = smoothRelations.toArray(BigInteger[]::new);
        int[][] array = new int[factorsMatrix.size()][];
        for (int i = 0; i < factorsMatrix.size(); i++) {
            int[] innerArray = factorsMatrix.get(i);
            array[i] = innerArray;
        }

        SmoothRelationObject help = new SmoothRelationObject(smoothRelationsArray, array);

        return help;
    }
    
    @Override
    public void run() {
        practica7.getModel().getFactorizeNumberStatus().setSolving();

        factorize("227179");

        practica7.getModel().getFactorizeNumberStatus().setSolved();
    }

    /**
     * Auxiliary class for saving the result of the fifth step.
     */
    private static class SmoothRelationObject {

        private final BigInteger[] Qn;
        private final int[][] factors;

        public BigInteger[] getQn() {
            return Qn;
        }

        public int[][] getFactors() {
            return factors;
        }

        public SmoothRelationObject(BigInteger[] Qn, int[][] factors) {
            this.Qn = Qn;
            this.factors = factors;
        }
    }

    /**
     * https://www.geeksforgeeks.org/find-square-root-modulo-p-set-2-shanks-tonelli-algorithm/
     * Refactored and adaptated for BigIntegers
     */
    private static class TonelliShanks {

        static int z = 0;
        private static final BigInteger MONE = BigInteger.valueOf(-1);

        // Returns k such that b^k = 1 (mod p)
        static BigInteger order(BigInteger p, BigInteger b) {
            if (!p.gcd(b).equals(ONE)) {
                return MONE;
            }

            // Initializing k with first odd prime number
            BigInteger k = BigInteger.valueOf(3);
            while (true) {
                if (b.modPow(k, p).equals(ONE)) {
                    return k;
                }
                k = k.add(ONE);
            }
        }

        // function to update e and return x
        static BigInteger convertx2e(BigInteger x, int[] e) {
            z = 0;
            while (x.mod(TWO).equals(ZERO)) {
                x = x.shiftRight(1);
                z++;
            }
            e[0] = z;
            return x;
        }

        // Main function for finding the modular square root
        static BigInteger STonelli(BigInteger n, BigInteger p) {
            // a and p should be coprime for finding the modular square root
            if (!n.gcd(p).equals(ONE)) {
                return MONE;
            }

            // If below expression returns (p - 1), then modular square root is not possible
            if (n.modPow(p.subtract(ONE).shiftRight(1), p).equals(p.subtract(ONE))) {
                return MONE;
            }

            // expressing p - 1, in terms of s * 2^e, where s is odd number
            int[] e = new int[1];
            BigInteger s = convertx2e(p.subtract(ONE), e);
            int z = e[0];

            // finding smallest q such that q ^ ((p - 1) / 2) (mod p) = p - 1
            BigInteger q;
            for (q = TWO;; q = q.add(ONE)) {
                // q - 1 is in place of (-1 % p)
                if (q.modPow(p.subtract(ONE).shiftRight(1), p).equals(p.subtract(ONE))) {
                    break;
                }
            }

            // Initializing variables x, b, and g
            BigInteger x = n.modPow(s.add(ONE).shiftRight(1), p);
            BigInteger b = n.modPow(s, p);
            BigInteger g = q.modPow(s, p);

            BigInteger r = BigInteger.valueOf(z);

            // keep looping until b becomes 1 or m becomes 0
            while (true) {
                int m;
                for (m = 0; m < r.intValue(); m++) {
                    if (order(p, b).equals(MONE)) {
                        return MONE;
                    }
                    // finding m such that b^(2^m) = 1
                    if (order(p, b).equals(BigInteger.valueOf((int) Math.pow(2, m)))) {
                        break;
                    }
                }
                if (m == 0) {
                    return x;
                }

                // updating value of x, g, and b according to the algorithm
                x = x.multiply(g.modPow(BigInteger.valueOf((long) Math.pow(2, r.intValue() - m - 1)), p)).mod(p);
                g = g.modPow(BigInteger.valueOf((long) Math.pow(2, r.intValue() - m)), p);
                b = b.multiply(g).mod(p);

                if (b.equals(ONE)) {
                    return x;
                }
                r = BigInteger.valueOf(m);
            }
        }
    }

}
