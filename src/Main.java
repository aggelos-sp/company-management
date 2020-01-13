import javax.swing.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        //Start database
        DataBase db = new DataBase("jdbc:mysql://localhost", "cs360", 3306,
                "root","");

        JFrame frame=new JFrame("DataBase");
        final JTextField input_text = new JTextField();
        input_text.setBounds(50,50, 150,20);
        //add initialization button
        JButton button_initialize = new JButton("Init");
        button_initialize.setBounds(50,100,95,30);
        button_initialize.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                db.db_init();
            }
        });
        //add termination button
        JButton button_close = new JButton("Close Connection");
        button_close.setBounds(50, 150, 100,20);
        button_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.db_close();
            }
        });


        frame.add(button_initialize);
        frame.add(button_close);
        frame.add(input_text);
        frame.setSize(400,400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
