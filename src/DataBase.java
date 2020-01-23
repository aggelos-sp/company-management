import java.sql.*;
import java.util.Iterator;

public class DataBase {
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
            String sql = "CREATE TABLE permanent_staff " +
                    "(id INTEGER not NULL, " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER, " +
                    " iban VARCHAR(255),"+
                    " address VARCHAR(255),"+
                    " marital_status BOOL,"+
                    " family_allowance INTEGER,"+
                    " work_years INTEGER,"+
                    " bank_name VARCHAR(255),"+
                    " children_num INTEGER,"+
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE permanent_phones"+
                " (id INTEGER not NULL,"+
                " phone INTEGER)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE contract_staff " +
                    "(id INTEGER not NULL , " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER, " +
                    " iban VARCHAR(255),"+
                    " address VARCHAR(255),"+
                    " marital_status BOOL,"+
                    " family_allowance INTEGER,"+
                    " bank_name VARCHAR(255),"+
                    " children_num INTEGER,"+
                    " start_date VARCHAR(255),"+
                    " end_date VARCHAR(255),"+
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE contract_phones"+
                    " (id INTEGER not NULL,"+
                    " phone INTEGER)";//+
                    //" PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE children"+
                "( name VARCHAR(255),"+
                " age INTEGER,"+
                " id INTEGER not NULL)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE  department"+
                " (dep_name VARCHAR (255) not NULL,"+
                " hire_date VARCHAR(255),"+
                " id INTEGER ,"+
                " PRIMARY KEY(dep_name)) ";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE allowance_permanent"+
                " (id INTEGER not NULL,"+
                " research_allowance INTEGER,"+
                " PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE allowance_contract"+
                    " (id INTEGER not NULL,"+
                    " library_allowance INTEGER,"+
                    " PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE payroll"+
                    " (id INTEGER not NULL,"+
                    " payroll INTEGER,"+
                    " department_name VARCHAR(255))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE base_money"+
                    "(date VARCHAR(255)not NULL,"+
                    " base_salary INTEGER,"+
                    " research_allowance INTEGER,"+
                    " library_allowance INTEGER,"+
                    " family_allowance INTEGER,"+
                    " per_child INTEGER)";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO base_money"+
                " VALUES ('01012020', 750, 250, 150, 100, 50)";
            stmt.executeUpdate(sql);
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
            String sql = "DROP TABLE permanent_staff ";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);

            sql = "DROP TABLE contract_staff ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE contract_phones ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE permanent_phones ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE children ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE department ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE allowance_permanent ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE allowance_contract ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE payroll ";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE base_money ";
            stmt.executeUpdate(sql);
            con.close();
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
}
