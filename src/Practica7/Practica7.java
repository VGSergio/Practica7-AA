package Practica7;

import MVC.Controller.Controller;
import MVC.Model.Model;
import MVC.View.View;
import mesurament.Mesurament;

/**
 * The class "Practica7" is the main class of the application which follows the
 * MVC architecture (Model-View-Controller). It is responsible for initializing
 * the objects. required for the application to run and provides methods for
 * interact between the model, the view and the controller.
 *
 * @author Sergio
 */
public class Practica7 {

    private final View VIEW;
    private final Model MODEL;
    private Controller CONTROLLER;

    /**
     * The main method of the application. Initializes the time measurement and
     * creates an instance of the "Practice7" class.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Mesurament.mesura();
        new Practica7();
    }

    /**
     * Constructor of the "Practica7" class. Initialize the instances of the
     * controller and model.
     */
    public Practica7() {
        CONTROLLER = new Controller(this);
        MODEL = new Model(this);

        Practica7.this.notify("GetAvailableLanguages");

        VIEW = new View(this);
    }

    /**
     * Method that returns the instance of the model.
     *
     * @return The instance of the model.
     */
    public Model getModel() {
        return MODEL;
    }

    /**
     * Method that notifies that an event has occurred.
     *
     * @param event The event to notify.
     */
    public void notify(String event) {
        switch (event) {
            case "ComputerPerformance" -> {
                if (!MODEL.getPerformanceStatus().isSolving()) {
                    CONTROLLER.performance();
                } else {
                    System.err.println("Already measuring");
                }
            }
        }
    }

    /**
     * Method that notifies that an event has occurred.
     *
     * @param event The event to notify.
     * @param algorithm The value associated with the event.
     * @param value
     */
    public void notify(String event, String algorithm, String value) {
        switch (event) {
            case "Solve" -> {
                switch (algorithm) {
                    case "PrimeNumber" -> {
                        if (!MODEL.getPrimeNumberStatus().isSolving()) {
                            notify("Solution", "PrimeNumber", "Calculating...");
                            MODEL.setPrimeNumberValue(value);
                            CONTROLLER.isPrimeNumber();
                        } else {
                            System.err.println("Already calculating prime number");
                        }
                    }
                    case "FactorizeNumber" -> {
                        if (!MODEL.getFactorizeNumberStatus().isSolving()) {
                            notify("Solution", "FactorizeNumber", "Calculating...");
                            MODEL.setFactorizeNumberValue(value);
                            CONTROLLER.factorize();
                        } else {
                            System.err.println("Already calculating factorize number");
                        }
                    }
                }
            }
            case "Solution" -> {
                switch (algorithm) {
                    case "PrimeNumber" -> {
                        VIEW.getPrimeNumberPanel().getSolutionTextField().setText(value);
                    }
                    case "FactorizeNumber" -> {
                        VIEW.getFactorizeNumberPanel().getSolutionTextField().setText(value);
                    }
                }
            }
        }
    }
}
