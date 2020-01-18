import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    private Connection con;
    private String url, databaseName, username, password;
    private int port;
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
                " phone INTEGER,"+
                " PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE contract_staff " +
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
                    " start_date VARCHAR(255),"+
                    " end_date VARCHAR(255),"+
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE contract_phones"+
                    " (id INTEGER not NULL,"+
                    " phone INTEGER,"+
                    " PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE children"+
                "( name VARCHAR(255),"+
                " age INTEGER,"+
                " id INTEGER not NULL,"+
                " PRIMARY KEY (id))";
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
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    public void db_hire_permanent_staff(boolean isTeacher,String first, String last, int age, String iban, String address,
                        boolean marital_status, int work_years, String bank_name, int children_num, String start_date){

    }

    public void db_hire_contract_staff(boolean isTeacher,String first, String last, int age, String iban, String address,
                                        boolean marital_status, int work_years, String bank_name, int children_num, String start_date){

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

            sql = "DROP TABLE contract ";
            stmt.executeUpdate(sql);
            con.close();
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
}
