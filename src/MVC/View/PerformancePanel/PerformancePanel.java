package MVC.View.PerformancePanel;

import Practica7.Practica7;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Sergio
 */
public class PerformancePanel extends JPanel {

    private final Practica7 practica7;
    private final JButton computerPerformance;
    private final JButton factorizePerformance;

    public PerformancePanel(Practica7 practica7) {
        this.practica7 = practica7;
        this.computerPerformance = new JButton("Get computer performance");        
        this.factorizePerformance = new JButton("Factorization performance");

        configure();
        addComponents();
    }

    private void configure() {
        computerPerformance.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                practica7.notify("ComputerPerformance");
            }

        });
        
        factorizePerformance.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("FactorizaationPerformance");
            }

        });
    }
    
    private void addComponents() {
        add(computerPerformance);
        add(factorizePerformance);
    }

}
