package MVC.Controller;

import MVC.Controller.Factorize.ControllerFactorizeNumber;
import MVC.Controller.ComputerPerformance.ControllerComputerPerformance;
import MVC.Controller.PrimeNumber.ControllerPrimeNumber;
import Practica7.Practica7;

/**
 * The controller component of the Model-View-Controller architecture for
 * Practica7.
 *
 * This class is responsible for managing the multiple algorithms avaialble.
 *
 * @author Sergio
 */
public class Controller {

    private final Practica7 PRACTICA_7;
    private ControllerPrimeNumber controllerPrimeNumber;
    private ControllerFactorizeNumber controllerFactorizeNumber;
    private ControllerComputerPerformance controllerComputerPerformance;

    /**
     * Constructor for the Controller class.
     *
     * @param practica7 an instance of the Practica7 class.
     */
    public Controller(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
    }

    public void isPrimeNumber(){
        controllerPrimeNumber = new ControllerPrimeNumber(PRACTICA_7);
        controllerPrimeNumber.start();
    }
    
    public void factorize(){
        controllerFactorizeNumber = new ControllerFactorizeNumber(PRACTICA_7);
        controllerFactorizeNumber.start();
    }
    
    public void performance(){
        controllerComputerPerformance = new  ControllerComputerPerformance(PRACTICA_7);
        controllerComputerPerformance.start();
    }
    
}
