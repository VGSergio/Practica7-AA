package MVC.View.ProblemPanel;

import Practica7.Practica7;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Sergio
 */
public class ProblemPanel extends JPanel {

    private final JLabel label;
    private final ProblemPanelValue value;
    private final ProblemPanelSolution solution;

    public ProblemPanel(Practica7 practica7, String label, String algorithm) {
        this.label = new JLabel(label);
        this.value = new ProblemPanelValue(practica7, algorithm);
        this.solution = new ProblemPanelSolution();

        addComponents();
    }

    private void addComponents() {
        add(label);
        add(value);
        add(solution);
    }

    public ProblemPanelSolution getSolutionTextField() {
        return solution;
    }
}
