package MVC.View.ProblemPanel;

import Practica7.Practica7;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 * Represents a JTextField component used for entering the desired values for
 * running the algorithms.
 *
 * @author Sergio
 */
public class ProblemPanelValue extends JTextField implements KeyListener {

    private final Practica7 practica7;
    private final String id;

    public ProblemPanelValue(Practica7 practica7, String id) {
        this.practica7 = practica7;
        this.id = id;

        configure();
    }

    private void configure(){
        addKeyListener(this);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 30);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !"".equals(getText().trim())) {
            practica7.notify("Solve", id, getText());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            e.consume(); // Ignore the event
        }
    }

}
