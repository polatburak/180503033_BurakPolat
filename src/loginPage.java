import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginPage extends JFrame {
    private JTextField benutzernameField1;
    private JPasswordField passwordField1;
    private JButton logInButton;
    private JPanel panel1;

    final String DB_URL = "jdbc:mysql://127.0.0.1:3306";
    final String USERNAME = "root";
    final String PASSWORD = "";

    Connection conn = null;

    {
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public loginPage(){
        add(panel1);
        setSize(400,200);
        setTitle("Log-in");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String benutzername;
                String passwort;

                benutzername = benutzernameField1.getText();
                passwort = String.valueOf(passwordField1.getPassword());

                System.out.println(benutzername+":"+passwort);

                if(true){
                    homePage hp = new homePage();
                    hp.setVisible(true);
                    hp.Name.setText("Burak Polat");
                    hp.authority.setText("(Admin)");
                    setVisible(false);
                }
            }
        });
    }
}
