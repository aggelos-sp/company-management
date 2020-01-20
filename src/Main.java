import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main {
    public static void main(String[] args) {
        //Start database
        DataBase db = new DataBase("jdbc:mysql://localhost", "cs360", 3306,
                "root","");

        final JFrame frame = new JFrame("DataBase");
        //input output field
        final JTextArea input_output_text = new JTextArea();
        input_output_text.setBounds(360, 10, 700, 500);
        input_output_text.add(new JScrollPane(), BorderLayout.CENTER);

        //output_text.setEditable(false);
        //add initialization button
        final JButton button_initialize = new JButton("Init");
        button_initialize.setBounds(50,100,300,20);
        button_initialize.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                db.db_init();
                button_initialize.setVisible(false);
            }
        });
        //add termination button
        final JButton button_close = new JButton("Close Con");
        button_close.setBounds(50, 120, 300,20);
        button_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.db_close();
            }
        });
        //hire tenure professor button
        final JButton button_hire_tenure_professor = new JButton("Hire Tenure Professor");
        button_hire_tenure_professor.setBounds(50, 140, 300, 20);
        button_hire_tenure_professor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                String first = string_array[0];
                String last = string_array[1];
                int age =  Integer.parseInt(string_array[2]);
                String iban = string_array[3];
                String address = string_array[4];
                boolean married = false;
                if(string_array[5].equals("married")){
                    System.out.println("is married!!!");
                    married = true;
                }
                int work_years = Integer.parseInt(string_array[6]);
                String bank_name = string_array[7];
                int num_children = Integer.parseInt(string_array[8]);
                String start_date = string_array[9];
                String dep_name = string_array[10];
                Phones p = new Phones();
                Children c = new Children();
                int i = 11;
                while (i < string_array.length){
                    if(string_array[i].startsWith("p")){
                        p.phone.add(Integer.parseInt(string_array[i].replace("p","")));
                    }else if(string_array[i].startsWith("!")){
                        String[] sp = string_array[i].replace("!","").split(" ",-1);
                        c.name.add(sp[0]);
                        c.age.add(Integer.parseInt(sp[1]));
                    }
                    else{
                        System.out.println("Invalid input");
                        break;
                    }
                    i++;
                }

                db.db_hire_permanent_staff(true, first,last,age,iban,address,married,work_years,bank_name,
                        num_children,start_date,p,c,dep_name);
                input_output_text.setText("");
            }
        });
        //hire tenure staff button
        final JButton button_hire_tenure_staff = new JButton("Hire Tenure Staff");
        button_hire_tenure_staff.setBounds(50, 160, 300, 20);
        button_hire_tenure_staff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                String first = string_array[0];
                String last = string_array[1];
                int age =  Integer.parseInt(string_array[2]);
                String iban = string_array[3];
                String address = string_array[4];
                boolean married = false;
                if(string_array[5].equals("married")){
                    System.out.println("is married!!!");
                    married = true;
                }
                int work_years = Integer.parseInt(string_array[6]);
                String bank_name = string_array[7];
                int num_children = Integer.parseInt(string_array[8]);
                String start_date = string_array[9];
                String dep_name = string_array[10];
                Phones p = new Phones();
                Children c = new Children();
                int i = 11;
                while (i < string_array.length){
                    if(string_array[i].startsWith("p")){
                        p.phone.add(Integer.parseInt(string_array[i].replace("p","")));
                    }else if(string_array[i].startsWith("!")){
                        String[] sp = string_array[i].replace("!","").split(" ",-1);
                        c.name.add(sp[0]);
                        c.age.add(Integer.parseInt(sp[1]));
                    }
                    else{
                        System.out.println("Invalid input");
                        break;
                    }
                    i++;
                }

                db.db_hire_permanent_staff(false, first,last,age,iban,address,married,work_years,bank_name,
                        num_children,start_date,p,c,dep_name);
                input_output_text.setText("");
            }
        });
        //hire contract professor button
        final JButton button_hire_contract_professor = new JButton("Hire Contract Professor");
        button_hire_contract_professor.setBounds(50, 180, 300, 20);
        button_hire_contract_professor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                String first = string_array[0];
                String last = string_array[1];
                int age =  Integer.parseInt(string_array[2]);
                String iban = string_array[3];
                String address = string_array[4];
                boolean married = false;
                if(string_array[5].equals("married")){
                    System.out.println("is married!!!");
                    married = true;
                }
                int work_years = Integer.parseInt(string_array[6]);
                String bank_name = string_array[7];
                int num_children = Integer.parseInt(string_array[8]);
                String start_date = string_array[9];
                String end_date = string_array[10];
                String dep_name = string_array[11];
                Phones p = new Phones();
                Children c = new Children();
                int i = 12;
                while (i < string_array.length){
                    if(string_array[i].startsWith("p")){
                        p.phone.add(Integer.parseInt(string_array[i].replace("p","")));
                    }else if(string_array[i].startsWith("!")){
                        String[] sp = string_array[i].replace("!","").split(" ",-1);
                        c.name.add(sp[0]);
                        c.age.add(Integer.parseInt(sp[1]));
                    }
                    else{
                        System.out.println("Invalid input");
                        break;
                    }
                    i++;
                }

                db.db_hire_contract_staff(true, first,last,age,iban,address,married,work_years,bank_name,
                        num_children,start_date,end_date,p,c,dep_name);
                input_output_text.setText("");
            }
        });
        //hire contract staff button
        final JButton button_hire_contract_staff = new JButton("Hire Contract Staff");
        button_hire_contract_staff.setBounds(50,200, 300, 20);
        button_hire_contract_staff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String inp = input_output_text.getText();
                    String[] string_array = inp.split("\n", -1);
                    String first = string_array[0];
                    String last = string_array[1];
                    int age =  Integer.parseInt(string_array[2]);
                    String iban = string_array[3];
                    String address = string_array[4];
                    boolean married = false;
                    if(string_array[5].equals("married")){
                        System.out.println("is married!!!");
                        married = true;
                    }
                    int work_years = Integer.parseInt(string_array[6]);
                    String bank_name = string_array[7];
                    int num_children = Integer.parseInt(string_array[8]);
                    String start_date = string_array[9];
                    String end_date = string_array[10];
                    String dep_name = string_array[11];
                    Phones p = new Phones();
                    Children c = new Children();
                    int i = 12;
                    while (i < string_array.length){
                        if(string_array[i].startsWith("p")){
                            p.phone.add(Integer.parseInt(string_array[i].replace("p","")));
                        }else if(string_array[i].startsWith("!")){
                            String[] sp = string_array[i].replace("!","").split(" ",-1);
                            c.name.add(sp[0]);
                            c.age.add(Integer.parseInt(sp[1]));
                        }
                        else{
                            System.out.println("Invalid input");
                            break;
                        }
                        i++;
                    }

                    db.db_hire_contract_staff(false, first,last,age,iban,address,married,work_years,bank_name,
                            num_children,start_date,end_date,p,c,dep_name);
                    input_output_text.setText("");
                }
        });
        //Update Employee button
        final JButton button_update_employee = new JButton("Update Employee");
        button_update_employee.setBounds(50, 220, 300, 20);
        button_update_employee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //fire or retire button
        final JButton button_fire_retire = new JButton("Fire/Retire");
        button_fire_retire.setBounds(50, 240, 300, 20);
        button_fire_retire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //promote button
        final JButton button_promote = new JButton("Promote");
        button_promote.setBounds(50,260, 300, 20);
        button_promote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //payment button
        final JButton button_payment = new JButton("Payment");
        button_payment.setBounds(50, 280, 300, 20);
        button_payment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //payroll status button
        final JButton button_payroll_status = new JButton("Payroll Status");
        button_payroll_status.setBounds(50, 300, 300, 20);
        button_payroll_status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //min, max and median payroll button
        final JButton button_min_max_median_payroll = new JButton("Min/Max/Median Payroll");
        button_min_max_median_payroll.setBounds(50, 320, 300, 20);
        button_min_max_median_payroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //median payroll increase button
        final JButton button_median_payroll_increase = new JButton("Median Payroll Increase");
        button_median_payroll_increase.setBounds(50, 340, 300, 20);
        button_median_payroll_increase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //employee status button
        final JButton button_employee_status = new JButton("Employ Status");
        button_employee_status.setBounds(50, 360, 300, 20);
        button_employee_status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //total payroll per category button
        final JButton button_total_payroll_per_category = new JButton("Total Payroll Per Category");
        button_total_payroll_per_category.setBounds(50, 380, 300, 20);
        button_total_payroll_per_category.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //arbitrary sql questions button
        final JButton button_arbitrary_sql_questions = new JButton("Arbitrary SQL Questions");
        button_arbitrary_sql_questions.setBounds(50, 400, 300, 20);
        button_arbitrary_sql_questions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                input_output_text.setText("");
                input_output_text.setText(db.arbitrary(inp));
            }
        });
        //add everything to the frame
        frame.add(button_initialize);
        frame.add(button_close);
        frame.add(button_hire_tenure_professor);
        frame.add(button_hire_tenure_staff);
        frame.add(button_hire_contract_professor);
        frame.add(button_hire_contract_staff);
        frame.add(button_update_employee);
        frame.add(button_fire_retire);
        frame.add(button_promote);
        frame.add(button_payment);
        frame.add(button_payroll_status);
        frame.add(button_min_max_median_payroll);
        frame.add(button_median_payroll_increase);
        frame.add(button_employee_status);
        frame.add(button_total_payroll_per_category);
        frame.add(button_arbitrary_sql_questions);
        frame.add(input_output_text);
        frame.setSize(1366,768);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
