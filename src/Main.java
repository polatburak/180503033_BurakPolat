import javax.swing.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        String DB_URL = "jdbc:mysql://127.0.0.1:3306/myschema";
        String USERNAME = "root";
        String PASSWORD = "root123";


        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            System.out.println("Connection Established");
        } catch (ClassNotFoundException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }


        String query1 = "SELECT passwort FROM myschema.benutzer";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query1);
        System.out.println(rs.getString(2));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginPage oP = new loginPage();
                oP.setVisible(true);
            }
        });

    }

}
