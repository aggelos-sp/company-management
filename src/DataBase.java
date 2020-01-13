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
            String sql = "CREATE TABLE REGISTRATION " +
                    "(id INTEGER not NULL, " +
                    " first VARCHAR(255), " +
                    " last VARCHAR(255), " +
                    " age INTEGER, " +
                    " PRIMARY KEY ( id ))";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }

    public void db_close(){
        try {
            String sql = "DROP TABLE REGISTRATION ";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            con.close();
        }catch (SQLException se){
            sql_exception_handle(se);
        }
    }
}
