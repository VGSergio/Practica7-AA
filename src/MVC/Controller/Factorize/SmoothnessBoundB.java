package MVC.Controller.Factorize;

import java.math.BigInteger;

/**
 *
 * @author Sergi
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
