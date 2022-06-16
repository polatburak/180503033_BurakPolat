import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginPage extends JFrame {
    private JTextField benutzernameField1;
    private JPasswordField passwordField1;
    private JButton logInButton;
    private JPanel panel1;
    public static String benutzername;

    public loginPage(){
        add(panel1);
        setSize(400,200);
        setLocationRelativeTo(null);
        setTitle("Log-in");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SwingUtilities.getRootPane(logInButton).setDefaultButton(logInButton);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                benutzername = null;
                String passwort = null;

                benutzername = benutzernameField1.getText();
                passwort = String.valueOf(passwordField1.getPassword());

                checkUsername cU = new checkUsername();
                boolean usernameCorrect = true;
                try {
                    if (cU.checkUsername(benutzername) == false) {
                        JOptionPane.showMessageDialog(null,
                                "Wrong Username!",
                                "Error",
                                JOptionPane.WARNING_MESSAGE);
                        usernameCorrect = false;
                    }
                } catch (SQLException ex) {
                    System.out.println("Failed connecting to server");
                    ex.printStackTrace();
                }

                if (usernameCorrect == true) {
                    returnPass rP = new returnPass();
                    String actualPass = null;

                    try {
                        actualPass = rP.returnPass(benutzername);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (passwort.equals(actualPass)) {
                        homePage hp = new homePage();
                        hp.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Wrong Password!",
                                "Error",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }

    public static class returnPass {

        public static String returnPass(String benutzername) throws SQLException {
            connectionJDBC cJDBC = new connectionJDBC();
            Connection conn = cJDBC.getConn();

            String query1 = String.format("SELECT passwort FROM myschema.Benutzern WHERE benutzername = '%s'",benutzername);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query1);
            String pass = null;
            while (rs.next())
                pass = rs.getString(1);

            return pass;

        }
    }

    public static class checkUsername{
        public static boolean checkUsername(String benutzername) throws SQLException{

            connectionJDBC cJDBC = new connectionJDBC();
            Connection conn = cJDBC.getConn();

            String query2 = String.format("SELECT benutzername FROM myschema.Benutzern WHERE benutzername = '%s'",benutzername);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query2);
            if(rs.next() == false)
                return false;
            else return true;
        }
    }
}
