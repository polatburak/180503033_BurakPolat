import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginPage extends JFrame {
    private JTextField benutzernameField1;
    private JPasswordField passwordField1;
    private JButton logInButton;
    private JPanel panel1;

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

                returnPass rP = new returnPass();
                String actualPass = null;

                try {
                    actualPass = rP.returnPass(benutzername);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if(passwort.equals(actualPass)){
                    homePage hp = new homePage();
                    hp.setVisible(true);
                    hp.Name.setText("Burak Polat");
                    hp.authority.setText("(Admin)");
                    setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(null,
                            "Wrong Password!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public static class returnPass {

        public static String returnPass(String benutzername) throws SQLException {
            connectionJDBC cJDBC = new connectionJDBC();
            Connection conn = cJDBC.getConn();

            String query1 = String.format("SELECT passwort FROM myschema.benutzer WHERE benutzername = '%s'",benutzername);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query1);
            String pass = null;
            while (rs.next())
                pass = rs.getString(1);

            return pass;

        }
    }
}
