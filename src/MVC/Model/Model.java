package MVC.Model;

import Practica7.Practica7;

/**
 * The Model component of the MVC architecture for the program. Handles the data
 * for the application.
 */
public class Model {

    private final SimulationStatus primeNumberStatus;
    private final SimulationStatus factorizeNumberStatus;
    private final SimulationStatus performanceStatus;

    private String primeNumberValue;
    private String factorizeNumberValue;

    private final Practica7 PRACTICA_7;

    /**
     * Constructs a new Modelo object.
     *
     * @param practica7 the main application object
     */
    public Model(Practica7 practica7) {
        PRACTICA_7 = practica7;
        primeNumberStatus = new SimulationStatus();
        factorizeNumberStatus = new SimulationStatus();
        performanceStatus = new SimulationStatus();
    }

    /**
     * Gets the current status of the simulation.
     *
     * @return the current status of the simulation
     */
    public SimulationStatus getPrimeNumberStatus() {
        return primeNumberStatus;
    }

    /**
     * Gets the current status of the simulation.
     *
     * @return the current status of the simulation
     */
    public SimulationStatus getFactorizeNumberStatus() {
        return factorizeNumberStatus;
    }

    /**
     * Gets the current status of the simulation.
     *
     * @return the current status of the simulation
     */
    public SimulationStatus getPerformanceStatus() {
        return performanceStatus;
    }

    /**
     * Gets the value for the prime number simulation.
     *
     * @return the value for the prime number simulation
     */
    public String getPrimeNumberValue() {
        return primeNumberValue;
    }

    /**
     * Sets the value for the prime number simulation.
     *
     * @param primeNumberValue the value for the prime number simulation
     */
    public void setPrimeNumberValue(String primeNumberValue) {
        this.primeNumberValue = primeNumberValue;
    }

    /**
     * Gets the value for the factorization simulation.
     *
     * @return the value for the factorization simulation
     */
    public String getFactorizeNumberValue() {
        return factorizeNumberValue;
    }

    /**
     * Sets the value for the factorization simulation.
     *
     * @param factorizeNumberValue the value for the factorization simulation
     */
    public void setFactorizeNumberValue(String factorizeNumberValue) {
        this.factorizeNumberValue = factorizeNumberValue;
    }
}
