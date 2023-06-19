package MVC.View.ProblemPanel;

import java.awt.Dimension;
import javax.swing.JTextField;

/**
 * Represents a non-editable JTextField component used for displaying the
 * solution to the user.
 *
 * @author Sergio
 */
public class ProblemPanelSolution extends JTextField {

    /**
     * Constructs a new instance of PrintSolution with a default size and the
     * text set as non-editable.
     */
    public ProblemPanelSolution() {
        setEditable(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 30);
    }

}
