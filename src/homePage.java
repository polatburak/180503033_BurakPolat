import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel benutzerLabel;
    private JTable dataTable;
    private JComboBox tableSelector;
    private JButton refreshButton;
    private JButton addRowEntryIntoButton;
    private JButton deleteRowButton;


    public homePage(){
        add(mainPanel);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataTable.setBackground(Color.cyan);
        setStartPanel();
        setLabelIcon();
        setDataTable();
        ImageIcon refreshIcon = new ImageIcon(new ImageIcon("images/refresh-logo.png").getImage().getScaledInstance(32,18, Image.SCALE_DEFAULT));
        refreshButton.setIcon(refreshIcon);
        fillDataTable("Personalen");
        ListSelectionModel model = dataTable.getSelectionModel();

            try {
            setName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        startseiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStartPanel();
            }
        });
        datenbankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(datenbankPanel);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        überDenBenutzerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(benutzerPanel);
                parentPanel.repaint();
                parentPanel.revalidate();
            }
        });
        tableSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = null;
                tableName = String.format("" + tableSelector.getSelectedItem());
                if(tableName.equals("Benutzern") && (!getPrivilege(loginPage.benutzername).equals("admin"))) {
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    tableSelector.setSelectedItem("Personalen");
                    tableName = "Personalen";
                    fillDataTable(tableName);
                }
                else
                    fillDataTable(tableName);
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillDataTable(String.format("" + tableSelector.getSelectedItem()));
            }
        });
    }

    public void setName() throws SQLException {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = cJDBC.getConn();

        String query2 = "SELECT Vorname, Nachname FROM myschema.Personalen, myschema.Benutzern WHERE Benutzern.idbenutzer = Personalen.idbenutzer";
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

    public void setStartPanel(){
        parentPanel.removeAll();
        parentPanel.add(startPanel);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    //add image next to benutzerLabel
    public void setLabelIcon(){
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/Sample_User_Icon.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        benutzerLabel.setIcon(imageIcon);

    }



    //fill dataTable with data from database
    public void fillDataTable(String tablename){
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM myschema.%s",tablename);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            dataTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDataTable() {
        dataTable.setRowSelectionAllowed(true);
    }

    //get privilage from table Benutzern
    public String getPrivilege(String username){
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT Privileg FROM myschema.Benutzern WHERE Benutzername = '%s'",username);
        String privilege = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                privilege = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilege;
    }

}


