package MVC.View.ProblemPanel;

import Practica7.Practica7;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that contains two JTextField for retrieveng or displaying info about
 * an algorithm. The left one is the input for the algorithm. The right one
 * displays the solution found.
 *
 * @author Sergio
 */
public class ProblemPanel extends JPanel {

    private final JLabel label;
    private final ProblemPanelValue value;
    private final ProblemPanelSolution solution;

    /**
     * Constructor for the ProblemPanel class.
     *
     * @param practica7 The instance of the Practica7 class.
     * @param label The text to be displayed on the panel's JLabel.
     * @param algorithm The algorithm associated with the panel.
     */
    public ProblemPanel(Practica7 practica7, String label, String algorithm) {
        this.label = new JLabel(label);
        this.value = new ProblemPanelValue(practica7, algorithm);
        this.solution = new ProblemPanelSolution();

        addComponents();
    }

   /**
     * Adds the components to the panel.
     * Automatically called in the constructor.
     */
    private void addComponents() {
        add(label);
        add(value);
        add(solution);
    }

     /**
     * Gets the solution text field from the panel.
     *
     * @return The ProblemPanelSolution object representing the solution text field of the panel.
     */
    public ProblemPanelSolution getSolutionTextField() {
        return solution;
    }
}
