import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Iterator;

public class DataBase {
    private int user_id = 1;
    private int company_id = 1;
    private int account_id = 1;
    private int merchant_id = 1;
    private int order_no = 1;
    class salaries{
        public int  base_salary, research_allowance, library_allowance, family_allowance,per_child;
        String date;
        public salaries(String date, int base_salary, int research_allowance, int library_allowance, int family_allowance,int per_child){
            this.date = date;
            this.base_salary = base_salary;
            this.research_allowance = research_allowance;
            this.library_allowance = library_allowance;
            this.family_allowance = family_allowance;
            this.per_child = per_child;
        }
    }

    private Connection con;
    private String url, databaseName, username, password;
    private int port;
    private salaries current_salaries = null;
    private int key = 0;
    DataBase(String url, String databaseName, int port, String username, String password){
        this.url = url;
        this.databaseName = databaseName;
        this.port = port;
        this.username = username;
        this.password = password;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url + ":" + port + "/" +
                    databaseName + "?characterEncoding=UTF-8", username, password);
        }catch (SQLException se){
            System.out.println("Exception trying to establish connection with the database!!");
            se.printStackTrace();
        }catch (ClassNotFoundException ce){
            System.out.println("Exception setting driver!!");
            ce.printStackTrace();
        }
    }

    public void sql_exception_handle(SQLException se){
        System.out.println("Exception during initialization!!");
        while (se != null){
            System.out.println("Message: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("ErrorCode: " + se.getErrorCode());
            se = se.getNextException();
        }
    }
    public void db_init(){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE merchant " +
                    "(merchant_id INTEGER not NULL, " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " commision FLOAT, " +
                    " profit FLOAT,"+
                    " amount_owed FLOAT,"+
                    " account_id INTEGER,"+
                    " PRIMARY KEY ( merchant_id ))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE individual " +
                    "(user_id INTEGER not NULL , " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " expiration_date DATE, " +
                    " amount_owed FLOAT,"+
                    " amount_left FLOAT ,"+
                    " credit_limit FLOAT ,"+
                    " account_id INTEGER not NULL,"+
                    " PRIMARY KEY ( user_id ))";
            stmt.executeUpdate(sql);


            sql = "CREATE TABLE company_user " +
                    "(user_id INTEGER not NULL , " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " company_name VARCHAR(255), " +
                    " company_id INTEGER , " +
                    " expiration_date DATE, " +
                    " amount_owed FLOAT,"+
                    " amount_left FLOAT ,"+
                    " credit_limit FLOAT ,"+
                    " account_id INTEGER not NULL,"+
                    " PRIMARY KEY ( user_id ))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE transactions " +
                    "(user_id INTEGER not NULL , " +
                    " merchant_first_name VARCHAR(255), " +
                    " merchant_last_name VARCHAR(255), " +
                    " buyer_first_name VARCHAR(255), " +
                    " buyer_last_name VARCHAR(255), " +
                    " transaction_date DATE, " +
                    " transaction_amount FLOAT,"+
                    " transaction_type VARCHAR (255),"+
                    " merchant_id INTEGER not NULL,"+
                    " order_no INTEGER not NULL)";
            stmt.executeUpdate(sql);

        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    void db_open_company_account(String company_name, String[] employee_first, String[] employee_last,
                                 String expiration_date, float credit_limit){
        float credit_amount_left = credit_limit;
        String sql;
        for(int i = 0; i < employee_first.length; i++){
            try {
                Statement stmt = con.createStatement();
                sql = "INSERT INTO company_user VALUES ( "+ user_id + ",'" +employee_first[i] + "','"+
                        employee_last[i] + "','" + company_name + "'," + company_id + ",'" + expiration_date +
                        "', 0 ," + credit_amount_left + "," + credit_limit + "," + account_id +")";
                stmt.executeUpdate(sql);
                user_id++;
            }catch (SQLException se){
                sql_exception_handle(se);
            }
        }


        company_id++;
        account_id++;
    }
    void db_open_private_account(String first_name, String last_name, String expiration_date, float credit_limit){
        String sql;
        float amount_left = credit_limit;
        try{
            Statement stmt = con.createStatement();
            sql = "INSERT INTO individual VALUES (" + user_id + ",'"+first_name+"','"+last_name+"','" +
                    expiration_date + "'," + 0 + ","+ amount_left + "," + credit_limit + "," + account_id +")";
            stmt.executeUpdate(sql);
        }catch (SQLException se){
            sql_exception_handle(se);
        }
        user_id++;
        account_id++;
    }

    void db_open_merchant_account(String merchant_first, String merchant_last, float commission){
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "INSERT INTO merchant VALUES (" + merchant_id + ",'" + merchant_first + "','"+
                    merchant_last + "'," + commission + "," + 0 + "," + 0 + "," + account_id + ")";
            stmt.executeUpdate(sql);
        }catch (SQLException se){
            sql_exception_handle(se);
        }
        account_id++;
    }

    void db_close_account_merchant(int id){
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "DELETE FROM merchant WHERE (account_id = "+ id + "AND amount_owed=0)";
            stmt.executeUpdate(sql);

        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    void db_close_account_individual(int id){
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "DELETE FROM individual WHERE account_id = "+ id+ "AND amount_owed= 0)";
            stmt.executeUpdate(sql);
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    void db_close_account_company(int id){
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "SELECT SUM(amount_owed) as result FROM company_user WHERE account_id=id;";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                if (rs.getFloat("result") == 0.0) {
                    sql = "DELETE FROM company_user WHERE account_id =" + id + ")";
                    stmt.executeUpdate(sql);
                }
            }else{
                System.out.println("Can't remove company with id = "+ id +" because there is dept");
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    String db_best_users(){
        String out = "";
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "(SELECT account_id FROM merchant WHERE amount_owed = 0)" +
                    "UNION(SELECT account_id FROM individual WHERE amount_owed = 0)" +
                    "UNION(SELECT DISTINCT account_id FROM company_user WHERE amount_owed = 0)" +
                    "ORDER BY account_id ASC";
            ResultSet rs = stmt.executeQuery(sql);
            int i = 1;
            while(rs.next()){
                out += i + " -> " + rs.getInt(account_id);
                i++;
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }

        return out;
    }

    String db_bad_users(){
        String out = "";
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "(SELECT account_id,amount_owed FROM merchant WHERE amount_owed <> 0)" +
                    "UNION (SELECT account_id,amount_owed FROM individual WHERE amount_owed <> 0)" +
                    "UNION (SELECT DISTINCT account_id,amount_owed FROM company_user WHERE amount_owed <> 0)" +
                    "ORDER BY amount_owed DESC";
            ResultSet rs = stmt.executeQuery(sql);
            int i = 1;
            while (rs.next()){
                out += i + " -> " + rs.getInt("account_id") +
                        " : " + rs.getFloat("amount_owed") + "\n";
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }
        return out;
    }
    String db_merchant_of_the_month(){
        String out = "";
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "SELECT merchant_id, COUNT(*) AS sales FROM transactions AS t "+
                    "WHERE t.transaction_type = 'buy' GROUP BY merchant_id" +
                    "ORDER BY sales DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);
            int found_id = 0;
            if (rs.next()){
                rs.getInt("merchant_id") ;
                out = "Merchant of the month had ID = " + found_id + " with a total of "+
                        rs.getInt("sales");
            }else{
                out = "Something went wrong!!!!";
            }
            sql = "UPDATE merchant SET amount_owed=amount_owed-(amount_owed*0.05)"+
                    " WHERE merchant.merchant_id = " + found_id ;
            stmt.executeUpdate(sql);
        }catch (SQLException se){
            sql_exception_handle(se);
        }
        return out;
    }

    void db_buy(int user_id, int merchant_id, float buy_amount, String date){
        String sql;

        try {
            Statement stmt = con.createStatement();
            sql = "(SELECT amount_left,account_id, first_name, last_name, credit_limit FROM individual WHERE individual.user_id = "+user_id+")"+
                "UNION(SELECT amount_left,account_id, first_name, last_name, credit_limit FROM company_user WHERE company_user.user_id = " + user_id+")";
            ResultSet rs = stmt.executeQuery(sql);
            float amount_left = 0;
            int id = 0;
            String user_first = "";
            String user_last = "";
            float credit_limit = 0;
            float amount_owed = 0;
            if(rs.next()){
                amount_left = rs.getFloat("amount_left");
                id = rs.getInt("account_id");
                user_first = rs.getString("first_name");
                user_last = rs.getString("last_name");
                credit_limit = rs.getFloat("credit_limit");
                amount_owed = credit_limit - amount_left;
            }
            String merchant_first ="";
            String merchant_last = "";
            sql = "SELECT first_name, last_name FROM merchant AS m WHERE m.merchant_id = "+ merchant_id;
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                merchant_first = rs.getString("first_name");
                merchant_last = rs.getString("last_name");
            }
            if(amount_left >= buy_amount){
                /*save transaction*/
                sql = "INSERT INTO transactions VALUES ("+user_id+",'"+ merchant_first+"','"+ merchant_last +"','"+
                        user_first+"','" + user_last+"','" + date +"',"+ buy_amount+", buy)";
                stmt.executeUpdate(sql);
                float commision = 0;
                sql = "SELECT commision FROM merchant WHERE mechant_id = "+merchant_id;
                rs = stmt.executeQuery(sql);
                if (rs.next()){
                    commision = rs.getFloat("commision");
                }
                sql = "UPDATE merchant SET (profit = profit+ "+ (buy_amount * (1-commision))+",amount_owed = (amount_owed+"+
                        (buy_amount* commision)+") )WHERE merchant_id = " + merchant_id;
                stmt.executeUpdate(sql);
                amount_left -= buy_amount;
                amount_owed += buy_amount;
                sql = "UPDATE individual SET (amount_left = "+amount_left+",amount_owed ="+amount_owed+")"+
                    "WHERE (individual.account_id = "+id;
                stmt.executeUpdate(sql);
                sql = "UPDATE company_user SET (amount_left = "+amount_left+",amount_owed ="+amount_owed+")"+
                        "WHERE comapany_user.account_id = "+id;
                stmt.executeUpdate(sql);

            }
        }catch(SQLException se) {
            sql_exception_handle(se);
        }

    }

    void db_give_back_individual(int id, float amount){
        String sql;
        try{
            Statement stmt = con.createStatement();
            sql = "SELECT amount_owed, credit_limit FROM individual WHERE user_id = "+id;
            ResultSet rs = stmt.executeQuery(sql);
            float m = 0;
            float limit =0;
            if(rs.next()){
                m = rs.getFloat("amount_owed");
                limit = rs.getFloat("credit_limit");
            }
            if(m < amount){
                sql = "UPDATE individual SET (amount_owed = 0, amount_left = "+limit+") WHERE user_id = "+ id;
                stmt.executeUpdate(sql);
            }else{
                sql = "UPDATE individual SET (amount_owed  = (amount_owed -"+amount+
                        "), amount_left = (amount_left +"+amount+")) WHERE user_id = "+id;
                stmt.executeUpdate(sql);
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
     void db_give_back_merchant(int id, float amount){
         String sql;
         try{
             Statement stmt = con.createStatement();
             sql = "SELECT amount_owed FROM merchant WHERE merchant_id = "+id;
             ResultSet rs = stmt.executeQuery(sql);
             float m = 0;
             if(rs.next()){
                 m = rs.getFloat("amount_owed");
             }
             if(m < amount){
                 sql = "UPDATE merchant SET (amount_owed = 0) WHERE merchant_id = "+ id;
                 stmt.executeUpdate(sql);
             }else{
                 sql = "UPDATE merchant SET amount_owed  = (amount_owed -"+amount+
                         ") WHERE merchant_id = "+id;
                 stmt.executeUpdate(sql);
             }
         }catch (SQLException se){
             sql_exception_handle(se);
         }
     }

     void db_give_back_company(int id, float amount){
        String sql;
        try {
            Statement stmt = con.createStatement();
            sql = "SELECT DISTINCT amount_owed, credit_limit FROM company_user WHERE company_id = "+id;
            ResultSet rs = stmt.executeQuery(sql);
            float m = 0;
            float limit =0;
            if(rs.next()){
                m = rs.getFloat("amount_owed");
                limit = rs.getFloat("credit_limit");
            }
            if(m < amount){
                sql = "UPDATE company_user SET (amount_owed = 0, amount_left = "+limit+") WHERE company_id = "+ id;
                stmt.executeUpdate(sql);
            }else{
                sql = "UPDATE company_user SET (amount_owed  = (amount_owed -"+amount+
                        "), amount_left = (amount_left +"+amount+")) WHERE company_id = "+id;
                stmt.executeUpdate(sql);
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }
     }






    public void get_current_salaries(){
        try {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM base_money ORDER BY date DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                System.out.println("The return set is : "+rs.toString());
                if(current_salaries == null){
                    current_salaries = new salaries(rs.getString("date"), rs.getInt("base_salary"),
                            rs.getInt("research_allowance"), rs.getInt("library_allowance"),
                            rs.getInt("family_allowance"), rs.getInt("per_child"));
                }else{
                    current_salaries.date = rs.getString("date");
                    current_salaries.base_salary = rs.getInt("base_salary");
                    current_salaries.research_allowance = rs.getInt("research_allowance");
                    current_salaries.library_allowance = rs.getInt("library_allowance");
                    current_salaries.family_allowance = rs.getInt("family_allowance");
                    current_salaries.per_child = rs.getInt("per_child");
                }
            }
            rs.close();
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
    public void db_hire_permanent_staff(boolean isTeacher,String first, String last, int age, String iban, String address,
                        boolean marital_status, int work_years, String bank_name, int children_num, String start_date,
                                        Phones p, Children c, String dep_name){
        get_current_salaries();
        /*
        System.out.println(first);
        System.out.println(last);
        System.out.println(age);
        System.out.println(iban);
        System.out.println(address);
        System.out.println(marital_status);
        System.out.println(work_years);
        System.out.println(bank_name);
        System.out.println(children_num);
        System.out.println(start_date);*/
        try {
            Statement stmt = con.createStatement();
            key++;
            int family = current_salaries.family_allowance;
            if(marital_status){
                if(children_num!=0){
                    family += current_salaries.per_child*children_num;
                }
            }else {
                family = 0;
            }
            String sql = "INSERT INTO permanent_staff VALUES ("+ key +",'"+first+"','"+last+"',"+age+",'"+iban+"','"+address+"',"+
                    marital_status+","+ family+","+work_years+",'"+bank_name+"',"+children_num+")";


            //sql = "INSERT  INTO permanent_staff VALUES (1,'aggelos','spiliotis',27,'gr214314','pandorou 5',true,150,4,'ethniki',5)";
            stmt.executeUpdate(sql);
            Iterator iter = p.phone.iterator();
            while (iter.hasNext()){

                sql = "INSERT INTO permanent_phones VALUES("+ key +","+iter.next()+")";
                stmt.executeUpdate(sql);
            }
            if(isTeacher){
                sql = "INSERT INTO allowance_permanent VALUES ("+ key +","+current_salaries.research_allowance+")";
                stmt.executeUpdate(sql);
            }
            if(children_num !=0){
                Iterator iter_a = c.age.iterator();
                Iterator iter_n = c.name.iterator();
                while (iter_a.hasNext() && iter_n.hasNext()){
                    sql = "INSERT INTO children VALUES ('"+iter_n.next()+"',"+iter_a.next()+","+ key +")";
                    stmt.executeUpdate(sql);
                }
            }
            int salary = 0;
            salary += family;
            double bs = current_salaries.base_salary;
            for (int j = 0; j<work_years;j++){
                if(j==5){
                    break;
                }
                bs = bs*1.15;
            }
            salary += bs;
            if(isTeacher){
                salary += current_salaries.research_allowance;
            }
            sql = "INSERT INTO payroll VALUES ("+ key +","+salary+ ",'"+dep_name+"')";
            stmt.executeUpdate(sql);



        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    public void db_hire_contract_staff(boolean isTeacher,String first, String last, int age, String iban, String address,
                                        boolean marital_status, int work_years, String bank_name, int children_num, String start_date,
                                        String end_date, Phones p, Children c, String dep_name,int contract_salary){
        get_current_salaries();
        try {
            Statement stmt = con.createStatement();
            key++;
            int family = current_salaries.family_allowance;
            if(marital_status){
                if(children_num!=0){
                    family += current_salaries.per_child*children_num;
                }
            }else {
                family = 0;
            }
            String sql = "INSERT INTO contract_staff VALUES ("+ key +",'"+first+"','"+last+"',"+age+",'"+iban+"','"+address+"',"+
                    marital_status+","+ family+",'"+bank_name+"',"+children_num+",'"+start_date+"','"+end_date+"')";
            stmt.executeUpdate(sql);
            Iterator iter = p.phone.iterator();
            while (iter.hasNext()){
                sql = "INSERT INTO contract_phones VALUES("+ key +","+iter.next()+")";
                stmt.executeUpdate(sql);
            }

            if(isTeacher){
                sql = "INSERT INTO allowance_contract VALUES ("+ key +","+current_salaries.library_allowance+")";
                stmt.executeUpdate(sql);
            }
            if(children_num !=0){
                Iterator iter_a = c.age.iterator();
                Iterator iter_n = c.name.iterator();
                while (iter_a.hasNext() && iter_n.hasNext()){
                    sql = "INSERT INTO children VALUES ('"+iter_n.next()+"',"+iter_a.next()+","+ key +")";
                    stmt.executeUpdate(sql);
                }
            }
            int salary = contract_salary;
            salary += family;
            if(isTeacher){
                salary += current_salaries.library_allowance;
            }
            sql = "INSERT INTO payroll VALUES ("+ key +","+salary+",'"+dep_name+"')";
            stmt.executeUpdate(sql);



        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
    public String arbitrary(String sql){
        String r ="";
        try{
            Statement stmt = con.createStatement();
            if(sql.charAt(0) == 'I' || sql.charAt(0) == 'i'){

                stmt.executeUpdate(sql);
            }else{
                ResultSet rs = stmt.executeQuery(sql);
                r = toStringRS(rs);
            }
        }catch(SQLException se){
            sql_exception_handle(se);
        }finally {
            return r;
        }
    }
    public String toStringRS(ResultSet rs){
        try {
            String r = "";
            ResultSetMetaData md = rs.getMetaData();
            int col_num = md.getColumnCount();
            while(rs.next()){
                for (int i = 1; i <= col_num; i++){
                    if(i > 1){
                        r += ", ";
                    }
                    r += rs.getString(i);
                }
                r += "\n";
                return r;
            }
        }catch (SQLException se){
            sql_exception_handle(se);
        }
        return null;
    }

    public void db_close(){
        try {
            String sql = "DROP TABLE company_user ";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);

            sql = "DROP TABLE individual ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE merchant ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE transactions ";
            stmt.executeUpdate(sql);

            con.close();
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
}
