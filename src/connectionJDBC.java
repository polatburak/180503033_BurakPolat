import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionJDBC {

    String DB_URL = "jdbc:mysql://127.0.0.1:3306/myschema";
    String USERNAME = "root";
    String PASSWORD = "root123";

    Connection conn = null;

    public Connection getConn() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Data Transmission Successful!");
            //print out the connection information
            System.out.println("Connection: " + conn.getMetaData().getURL() + conn.getMetaData().getConnection());
        } catch (ClassNotFoundException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }
}
