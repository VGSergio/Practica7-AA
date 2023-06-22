package MVC.Controller.Factorize;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a method for sieving the Qn array to filter out the
 * smooth numbers.
 *
 * @author Sergio
 */
public class SmoothQn {

    /**
     * Sieves the Qn array to filter out the smooth numbers using the factor
     * base.
     *
     * @param Qn the congruence sequence array
     * @param factorBase the factor base array
     * @return an array of BigInteger representing the smooth Qn values
     */
    public static BigInteger[] sieveQn(BigInteger[] Qn, BigInteger[] factorBase) {
        List<BigInteger> smoothQn = new ArrayList<>();

        for (BigInteger q : Qn) {
            if (isSmooth(q, factorBase)) {
                smoothQn.add(q);
            }
        }

        return smoothQn.toArray(BigInteger[]::new);
    }

    /**
     * Checks if a number is B-smooth, where B is the factor base.
     *
     * @param number the number to check
     * @param factorBase the factor base array
     * @return true if the number is B-smooth, false otherwise
     */
    private static boolean isSmooth(BigInteger number, BigInteger[] factorBase) {
        for (BigInteger prime : factorBase) {
            while (number.mod(prime).equals(BigInteger.ZERO)) {
                number = number.divide(prime);
            }
        }
        return number.equals(BigInteger.ONE);
    }
}
