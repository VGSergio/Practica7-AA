package MVC.Controller.ComputerPerformance;

import Practica7.Practica7;
import mesurament.Mesurament;

/**
 * Represents the controller for retrieving the computers performance. This
 * class extends Thread to enable running the algorithm in a separate thread. It
 * measures the computer performance and updates the simulation status
 * accordingly.
 *
 * @author Sergio
 */
public class ControllerComputerPerformance extends Thread {

    private final Practica7 PRACTICA_7;

    /**
     * Constructor for the ComputerPerformance class.
     *
     * @param practica7 an instance of the Practica7 class.
     */
    public ControllerComputerPerformance(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
    }

    @Override
    public void run() {
        PRACTICA_7.getModel().getPerformanceStatus().setSolving();

        Mesurament.mesura();

        PRACTICA_7.getModel().getPerformanceStatus().setSolved();
    }

}
