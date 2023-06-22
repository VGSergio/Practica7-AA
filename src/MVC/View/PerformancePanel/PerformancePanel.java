package MVC.View.PerformancePanel;

import Practica7.Practica7;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Represents a panel for displaying performance-related options in the user
 * interface.
 *
 * @author Sergio
 */
public class PerformancePanel extends JPanel {

    private final Practica7 PRACTICA_7;
    private final JButton computerPerformance;
    private final JButton factorizePerformance;

    /**
     * Constructor for the PerformancePanel class.
     *
     * @param practica7 The instance of the Practica7 class.
     */
    public PerformancePanel(Practica7 practica7) {
        this.PRACTICA_7 = practica7;
        this.computerPerformance = new JButton("Get computer performance");
        this.factorizePerformance = new JButton("Factorization performance");

        configure();
        addComponents();
    }

    /**
     * Configures the PerformancePanel. Sets up the mouse listeners for the
     * buttons.
     */
    private void configure() {
        computerPerformance.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                PRACTICA_7.notify("ComputerPerformance");
            }

        });

        factorizePerformance.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("FactorizaationPerformance");
            }

        });
    }

    /**
     * Adds the components to the PerformancePanel.
     */
    private void addComponents() {
        add(computerPerformance);
        add(factorizePerformance);
    }

}
