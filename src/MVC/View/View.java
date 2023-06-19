package MVC.View;

import MVC.View.PerformancePanel.PerformancePanel;
import MVC.View.ProblemPanel.ProblemPanel;
import Practica7.Practica7;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * The view component of the Model-View-Controller architecture for Practica5.
 * This class represents the graphical user interface (GUI) of the application.
 *
 * A JFrame that lets the user choose a the problem to solve "Compare languages"
 * or "Guess the language"
 *
 * @author Sergio
 */
public class View extends JFrame {

    private final Practica7 PRACTICA7;

    private final ProblemPanel primeNumberPanel;
    private final ProblemPanel factorizeNumberPanel;
    private final PerformancePanel performancePanel;

    /**
     * Constructs a new instance of the Vista class and initializes the
     * components of the GUI.
     *
     * @param practica7 an instance of the Practica7 class
     */
    public View(Practica7 practica7) {
        PRACTICA7 = practica7;

        primeNumberPanel = new ProblemPanel(practica7, "Prime number:", "PrimeNumber");
        factorizeNumberPanel = new ProblemPanel(practica7, "Factorize number:", "FactorizeNumber");
        performancePanel = new PerformancePanel(practica7);

        configure();
        addComponents();
        showView();
    }

    /**
     * Method for configuring the frame.
     */
    private void configure() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
    }

    /**
     * Method for adding the components to the frame.
     */
    private void addComponents() {
        add(primeNumberPanel);
        add(factorizeNumberPanel);
        add(performancePanel);
    }

    /**
     * Configures the default view options for the frame.
     */
    private void showView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public ProblemPanel getPrimeNumberPanel(){
        return this.primeNumberPanel;
    }
    
    public ProblemPanel getFactorizeNumberPanel(){
        return this.factorizeNumberPanel;
    }
}
