import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
    private JPanel MyPanel;
    private JButton Initialize;

    public Gui() {
        Initialize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Hello World!!!");
            }
        });
    }

    public JPanel getMyPanel(){
        return MyPanel;
    }


}
