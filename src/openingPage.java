import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class openingPage extends JFrame {
    private JTextField benutzernameField1;
    private JPasswordField passwordField1;
    private JButton logInButton;
    private JPanel panel1;

    public openingPage(){
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
