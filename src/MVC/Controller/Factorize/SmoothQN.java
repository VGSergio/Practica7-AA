package MVC.Controller.Factorize;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergio
 */
public class BSmoothValues {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;

    public static SmoothRelationObject smoothRelations(BigInteger[] Qn, BigInteger[] factorBase) {
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

                // just the parities
                factors[idx] = factors[idx] % 2;

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

    /**
     * Auxiliary class for saving the result of the fifth step.
     */
    public static class SmoothRelationObject {

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
}
