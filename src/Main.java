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
        /*Open Account*/
        final JButton account_open = new JButton("Open Account");
        account_open.setBounds(50, 140, 300, 20);
        account_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                int input_index = 0;
                String type = string_array[input_index];
                if(type.charAt(0) == 'I'){
                    String first_name = string_array[++input_index];
                    String last_name  = string_array[++input_index];
                    String expiration_date = string_array[++input_index];
                    float credit_limit = Float.parseFloat(string_array[++input_index]);
                    db.db_open_private_account(first_name,last_name,expiration_date,credit_limit);
                }else if(type.charAt(0) == 'C'){
                    String company_name = string_array[++input_index];
                    String expiration_date = string_array[++input_index];
                    float credit_limit = Float.parseFloat(string_array[++input_index]);
                    int emp_no = (string_array.length - 4) / 2;
                    String[] employee_first = new String[emp_no];
                    String[] employee_last = new String[emp_no];
                    for(int i = 0;  i < emp_no; i++){
                        employee_first[i] = string_array[++input_index];
                        employee_last[i] = string_array[++input_index];
                    }
                    db.db_open_company_account(company_name, employee_first, employee_last, expiration_date,credit_limit);
                }else if(type.charAt(0) == 'M'){
                    String merchant_first = string_array[++input_index];
                    String merchant_last = string_array[++input_index];
                    float commission = Float.parseFloat(string_array[++input_index]);
                    db.db_open_merchant_account(merchant_first,merchant_last, commission);
                }else {
                    throw new RuntimeException("First Character must always be the type of account!!!\n");
                }
                input_output_text.setText("");
            }
        });
        /*Account Closing*/
        final JButton account_close = new JButton("Close Account");
        account_close.setBounds(50, 160, 300, 20);
        account_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                int input_index = 0;
                String type = string_array[input_index];
                if(type.charAt(0) == 'I'){
                    db.db_close_account_individual(Integer.parseInt(string_array[++input_index]));
                }else if (type.charAt(0) == 'C'){
                    db.db_close_account_company(Integer.parseInt(string_array[++input_index]));
                }else if (type.charAt(0) == 'M'){
                    db.db_close_account_merchant(Integer.parseInt(string_array[++input_index]));
                }else{
                    throw new RuntimeException("First Character must always be the type of account!!!\n");
                }

                input_output_text.setText("");
            }
        });
        /*Best Users*/
        final JButton best_users = new JButton("Good Users");
        best_users.setBounds(50, 180, 300, 20);
        best_users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = db.db_best_users();
                input_output_text.setText(output);
            }
        });
        /* Worst users list*/
        final JButton worst_users = new JButton("Bad users");
        worst_users.setBounds(50,200, 300, 20);
        worst_users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String output = db.db_bad_users();
                    input_output_text.setText(output);
                }
        });
        /* Merchant of the Month*/
        final JButton best_merchant = new JButton("Merchant of The Month");
        best_merchant.setBounds(50, 220, 300, 20);
        best_merchant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = db.db_merchant_of_the_month();
                input_output_text.setText(output);
            }
        });
        /*Buy*/
        final JButton buy_button = new JButton("Buy");
        buy_button.setBounds(50, 240, 300, 20);
        buy_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                int user_id = Integer.parseInt(string_array[0]);
                int merchant_id = Integer.parseInt(string_array[1]);
                float buy_amount = Float.parseFloat(string_array[2]);
                String date = string_array[3];
                db.db_buy(user_id, merchant_id, buy_amount, date);
                input_output_text.setText("");
            }
        });
        /*Give Back*/
        final JButton give_back_button = new JButton("Give Back");
        give_back_button.setBounds(50,260, 300, 20);
        give_back_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inp = input_output_text.getText();
                String[] string_array = inp.split("\n", -1);
                int input_index = 0;
                String type = string_array[input_index];
                int id = Integer.parseInt(string_array[++input_index]);
                float amount = Float.parseFloat(string_array[++input_index]);
                if(type.charAt(0) == 'I'){
                    db.db_give_back_individual(id, amount);
                }else if (type.charAt(0) == 'C'){
                    db.db_give_back_merchant(id, amount);
                }else if (type.charAt(0) == 'M'){
                    db.db_give_back_company(id, amount);
                }else{
                    throw new RuntimeException("First Character must always be the type of account!!!\n");
                }
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
        frame.add(account_open);
        frame.add(account_close);
        frame.add(best_users);
        frame.add(worst_users);
        frame.add(best_merchant);
        frame.add(buy_button);
        frame.add(give_back_button);
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
