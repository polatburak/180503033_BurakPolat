import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class homePage extends JFrame {
    public JLabel Name;
    private JPanel mainPanel;
    private JPanel selectionPanel;
    private JPanel parentPanel;
    private JButton datenbankButton;
    private JButton überDenBenutzerButton;
    private JButton suchenButton;
    private JPanel datenbankPanel;
    private JPanel benutzerPanel;
    private JPanel suchePanel;
    private JButton startseiteButton;
    private JPanel startPanel;
    private JTextArea displayedText;
    private JLabel nameLabel;
    public JLabel authority;

    public homePage(){
        add(mainPanel);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            setName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setName() throws SQLException {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = cJDBC.getConn();

        String query2 = "SELECT Vorname, Nachname FROM myschema.personal, myschema.benutzer WHERE benutzer.idbenutzer = personal.idbenutzer";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query2);
        String Vorname = null, Nachname = null;
        while(rs.next()) {
            Vorname = rs.getString(1);
            Nachname = rs.getString(2);
            nameLabel.setText(Vorname + " " + Nachname);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(185, 0, 185, getHeight());
        g2.draw(lin);
    }
}
