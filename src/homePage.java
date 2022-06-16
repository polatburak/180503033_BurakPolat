import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class homePage extends JFrame {
    private JLabel Name;
    private JPanel mainPanel;
    private JPanel selectionPanel;
    private JPanel parentPanel;
    private JButton datenbankButton;
    private JButton überDenBenutzerButton;
    private JButton zahlungButton;
    private JPanel datenbankPanel;
    private JPanel benutzerPanel;
    private JPanel zahlungPanel;
    private JButton startseiteButton;
    private JPanel startPanel;
    private JTextArea displayedText;
    private JLabel nameLabel;
    private JLabel benutzerLabel;
    public JTable dataTable;
    private JComboBox tableSelector;
    private JButton refreshButton;
    private JButton addARowToButton;
    private JButton deleteSelectedRowButton;
    private JButton updateTheModifiedRowButton;
    private JLabel bnLabel;
    private JLabel usernameArea;
    private JLabel nameArea;
    private JLabel dateArea;
    private JLabel privilegArea;
    private JLabel fahrschulsysIcon;
    private JList eingabeList;
    private JList ausgabeList;
    private JLabel gesamtLabel;
    private JLabel bezahlenLabel;
    private JLabel nettoLabel;
    private JLabel einDatum;
    private JLabel ausDatum;
    private JButton searchButton;
    private JTextField searchBox;
    private JButton logOutButton;


    public homePage() {

        add(mainPanel);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataTable.setBackground(Color.cyan);
        setStartPanel();
        setLabelIcon();


        try {
            setNameF(loginPage.benutzername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ImageIcon fahrschulSys = new ImageIcon(new ImageIcon("images/fahrschulsys.png").getImage().getScaledInstance(550, 123, Image.SCALE_AREA_AVERAGING));
        fahrschulsysIcon.setIcon(fahrschulSys);

        setDataTable();
        ImageIcon refreshIcon = new ImageIcon(new ImageIcon("images/refresh.png").getImage().getScaledInstance(28, 28, Image.SCALE_AREA_AVERAGING));
        refreshButton.setIcon(refreshIcon);
        fillDataTable("Personalen");
        ListSelectionModel model = dataTable.getSelectionModel();

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
                SwingUtilities.getRootPane(searchButton).setDefaultButton(searchButton);
            }
        });
        überDenBenutzerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentPanel.removeAll();
                parentPanel.add(benutzerPanel);
                parentPanel.repaint();
                parentPanel.revalidate();
                setDateArea();
                usernameArea.setText(loginPage.benutzername);
                privilegArea.setText(getPrivilege(loginPage.benutzername));
                if(!privilegArea.getText().equals("Admin"))
                    privilegArea.setText(privilegArea.getText() + " (Eingeschränkte Funktionalität)");
            }
        });
        tableSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = null;
                tableName = String.format("" + tableSelector.getSelectedItem());
                if (tableName.equals("Benutzern") && (!getPrivilege(loginPage.benutzername).equals("Admin"))) {
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    tableSelector.setSelectedItem("Personalen");
                    tableName = "Personalen";
                    fillDataTable(tableName);
                } else
                    fillDataTable(tableName);
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillDataTable(String.format("" + tableSelector.getSelectedItem()));
            }
        });
        deleteSelectedRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPrivilege(loginPage.benutzername).equals("Admin"))
                    deleteSelectedRow();
                else
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);

            }
        });
        updateTheModifiedRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPrivilege(loginPage.benutzername).equals("Admin"))
                    updateSelectedRow();
                else
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
            }
        });
        addARowToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getPrivilege(loginPage.benutzername).equals("Admin")) {
                    try {
                        addRow();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);

            }
        });
        zahlungButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if not admin, show error message
                if(!getPrivilege(loginPage.benutzername).equals("Admin"))
                    JOptionPane.showMessageDialog(null,
                            "Permission Denied!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                else{
                    DefaultListModel model1 = new DefaultListModel();
                    eingabeList.setModel(model1);
                    DefaultListModel model2 = new DefaultListModel();
                    ausgabeList.setModel(model2);
                    int Eingabe = 0;
                    int Ausgabe = 0;
                    parentPanel.removeAll();
                    parentPanel.add(zahlungPanel);
                    parentPanel.repaint();
                    parentPanel.revalidate();
                    Eingabe = setEinkommenList(model1,Eingabe);
                    Ausgabe = setAusgabenList(model2,Ausgabe);
                    gesamtLabel.setText(Integer.toString(Eingabe) + "€");
                    bezahlenLabel.setText(Integer.toString(Ausgabe) + "€");
                    nettoLabel.setText(Integer.toString(Eingabe-Ausgabe) + "€");
                    einDatum.setText(getMinDateStudenten());
                    ausDatum.setText(getMinDatePersonal());
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchBox.getText();
                System.out.println(text);
                search(text, String.format("" + tableSelector.getSelectedItem()));

            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePage.this.dispose();
                loginPage lp = new loginPage();
                lp.setVisible(true);
            }
        });
    }

    public void setNameF(String benutzername) throws SQLException{
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = cJDBC.getConn();

        String query2 = String.format("SELECT Vorname, Nachname FROM myschema.Personalen, myschema.Benutzern WHERE Benutzern.benutzername = '%s' AND Benutzern.idBenutzern = Personalen.idbenutzer",benutzername);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query2);
        String Vorname = null, Nachname = null;
        while (rs.next()) {
            Vorname = rs.getString(1);
            Nachname = rs.getString(2);
            nameLabel.setText(Vorname + " " + Nachname);
            nameArea.setText(Vorname + " " + Nachname);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(185, 0, 185, getHeight());
        g2.draw(lin);
    }

    public void setStartPanel() {
        parentPanel.removeAll();
        parentPanel.add(startPanel);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    //add image next to benutzerLabel in mainPanel
    public void setLabelIcon() {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/Sample_User_Icon.png").getImage().getScaledInstance(32, 32, Image.SCALE_AREA_AVERAGING));
        benutzerLabel.setIcon(imageIcon);

    }


    //fill dataTable in Datenbank scene with data from database
    public void fillDataTable(String tablename) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM myschema.%s", tablename);
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
    public String getPrivilege(String username) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT Privileg FROM myschema.Benutzern WHERE Benutzername = '%s'", username);
        String privilege = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                privilege = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return privilege;
    }

    //delete selected row from dataTable in database
    public void deleteSelectedRow() {
        int selectedRow = dataTable.getSelectedRow();

        if (selectedRow >= 0) {
            String tableName = String.format("" + tableSelector.getSelectedItem());
            String id = dataTable.getValueAt(selectedRow, 0).toString();
            String query = String.format("DELETE FROM myschema.%s WHERE id%s = %s", tableName, tableName, id);
            connectionJDBC cJDBC = new connectionJDBC();
            Connection conn = null;
            try {
                conn = cJDBC.getConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            fillDataTable(tableName);
        } else if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                    "Please select a row to delete!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    //update the database with the modified row in datatable
    public void updateSelectedRow() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            String tableName = String.format("" + tableSelector.getSelectedItem());
            String id = dataTable.getValueAt(selectedRow, 0).toString();
            String query = String.format("UPDATE myschema.%s SET ", tableName);
            String[] columnNames = getColumnNames(tableName);

            for (int i = 1; i < columnNames.length; i++) {
                Object value = dataTable.getValueAt(selectedRow, i);
                if (i == columnNames.length - 1)
                    query += String.format("`%s` = '%s' WHERE id%s = %s", columnNames[i], value, tableName, id);
                else
                    query += String.format("`%s` = '%s', ", columnNames[i], value);

            }

            connectionJDBC cJDBC = new connectionJDBC();
            Connection conn = null;
            try {
                conn = cJDBC.getConn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Row couldn't be updated! Try again!",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);

            }
            fillDataTable(tableName);
        } else if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                    "Please select a row to update!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public String[] getColumnNames(String tableName) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM myschema.%s", tableName);
        String[] columnNames = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            columnNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < columnNames.length; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    //add a row entry to the databasee
    public void addRow() throws SQLException {
        String tableName = String.format("" + tableSelector.getSelectedItem());
        String[] columnNames = getColumnNames(tableName);
        String query = String.format("INSERT INTO myschema.%s (", tableName);
        Object values = "VALUES (";
        int columnCount = 0;

        //Because the creationTime column is auto-generated, we don't want our loop to ask us to assign a value manually
        if (tableSelector.getSelectedItem().equals("Benutzern"))
            columnCount = columnNames.length - 1;
        else
            columnCount = columnNames.length;

        //get datatypes from database as array
        String[] dataTypes = getColumnTypes(tableName);
        for (int i = 1; i < columnCount; i++) {
            String value = JOptionPane.showInputDialog(null, "Bitte geben Sie einen Wert für " + columnNames[i] + " (type: " + dataTypes[i] + ")");
            //when clicked cancel on input dialog, return
            if (value == null)
                return;
            //when given wrong datatype or empty value show error message
            if (value == null || value.equals("") || !checkDataType(dataTypes[i], value)) {
                JOptionPane.showMessageDialog(null,
                        "Bitte geben Sie einen Wert für " + columnNames[i] + "!",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                i--;
                continue;
            }
            if (i == columnCount - 1) {
                query += String.format("`%s`) ", columnNames[i]);
                values += String.format("'%s')", value);
            } else {
                query += String.format("`%s`, ", columnNames[i]);
                values += String.format("'%s', ", value);
            }
        }
        query += values;
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Zeile konnte nicht hinzugefügt werden! Versuchen Sie es bitte noch einmal!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
        fillDataTable(tableName);
    }

    //get datatypes of columns from database
    public String[] getColumnTypes(String tableName) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM myschema.%s", tableName);
        String[] columnNames = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            columnNames = new String[rsmd.getColumnCount()];
            for (int i = 0; i < columnNames.length; i++) {
                columnNames[i] = rsmd.getColumnTypeName(i + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    //check if the given value is of the given datatype (used while adding a row entry)
    public boolean checkDataType(String dataType, String value) {
        if (dataType.equals("INT")) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (dataType.equals("VARCHAR")) {
            if (value.length() > 255)
                return false;
        } else if (dataType.equals("DATE")) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                df.parse(value);
            } catch (ParseException e) {
                return false;
            }
        } else if (dataType.equals("DATETIME")) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setLenient(false);
                df.parse(value);
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    //set timeArea to creationTime column from database
    public void setDateArea() {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT creationDate FROM myschema.Benutzern";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dateArea.setText(rs.getTimestamp("creationDate").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setUsernameArea(String username) {
        usernameArea.setText(username);
    }

    //add to einkommenList the Vorname + Nachname, leftPayment from table "Studenten" where leftpayment is not 0
    public int setEinkommenList(DefaultListModel model1, int Einkommen) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT Vorname, Nachname, leftPayment, dateOfPayment FROM myschema.Studenten WHERE leftPayment != 0";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String date = null;
            String date2 = null;
            String status = "";
            while (rs.next()) {
                Einkommen+= rs.getInt("leftPayment");
                date = rs.getString("dateOfPayment");
                date2 = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
                if (date.compareTo(date2) >= 0)
                    status = (" (Zu zahlen)");
                else if (date.compareTo(date2) < 0)
                    status = (" (Überfällig!)");
                model1.addElement(String.format(rs.getString("Vorname") + " " + rs.getString("Nachname") + ": " + rs.getString("leftPayment") + "€ - " + rs.getString("dateOfPayment") + "%s", status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Einkommen;
    }
    //set AusgabenList the Vorname + Nachname, leftPayment from table "Studenten" where leftpayment isn't 0
    public int setAusgabenList(DefaultListModel model2,int Ausgabe) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT Vorname, Nachname, leftPayment, dateOfPayment FROM myschema.Personalen WHERE leftPayment != 0";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String date = null;
            String date2 = null;
            String status = "";
            while (rs.next()) {
                Ausgabe+= rs.getInt("leftPayment");
                date = rs.getString("dateOfPayment");
                date2 = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
                if (date.compareTo(date2) >= 0)
                    status = (" (Zu zahlen)");
                else if (date.compareTo(date2) < 0)
                    status = (" (Überfällig!)");
                model2.addElement(String.format(rs.getString("Vorname") + " " + rs.getString("Nachname") + ": " + rs.getString("leftPayment") + "€ - " + rs.getString("dateOfPayment") + "%s",status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Ausgabe;
    }

    //select min(Date) from column "dateOfPayment" from table "Studenten" (for calculating the next income)
    public String getMinDateStudenten() {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT MIN(dateOfPayment) FROM myschema.Studenten";
        String minDate = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                minDate = rs.getString("MIN(dateOfPayment)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minDate;
    }
    //select min(dateOfPayment) from table "Personalen" (for calculating the next outcome)
    public String getMinDatePersonal() {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT MIN(dateOfPayment) FROM myschema.Personalen";
        String minDate = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                minDate = rs.getString("MIN(dateOfPayment)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return minDate;
    }

    public void search(String text, String tableName) {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = null;
        try {
            conn = cJDBC.getConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(tableName.equals("Personalen")) {
          String query1 = "SELECT * FROM myschema.Personalen WHERE Vorname LIKE '%" + text + "%' OR Nachname LIKE '%" + text + "%' OR fatherName LIKE '%" + text + "%' OR Geburtsdatum LIKE '%" + text + "%' OR Adresse LIKE '%" + text + "%' OR Gehalt LIKE '%" + text + "%' OR leftPayment LIKE '%" + text + "%' OR dateOfPayment LIKE '%" + text + "%' OR Geschlechter LIKE '%" + text + "%' OR idTC LIKE '%" + text + "%' OR Telefonnummer LIKE '%" + text + "%' OR Beruf LIKE '%" + text + "%'";
          try {
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(query1);
              dataTable.setModel(DbUtils.resultSetToTableModel(rs));
          } catch (SQLException e) {
              e.printStackTrace();
          }

        } else if(tableName.equals("Studenten")) {
            String query2 = "SELECT * FROM myschema.Studenten WHERE Vorname LIKE '%" + text + "%' OR Nachname LIKE '%" + text + "%' OR fatherName LIKE '%" + text + "%' OR Geburtsdatum LIKE '%" + text + "%' OR Adresse LIKE '%" + text + "%' OR Geschlechter LIKE '%" + text + "%' OR gewünschteFührerSchein LIKE '%" + text + "%' OR Zahlungstatus LIKE '%" + text + "%' OR leftPayment LIKE '%" + text + "%' OR dateOfPayment LIKE '%" + text + "%' OR idTC LIKE '%" + text + "%' OR Telefonnumer LIKE '%" + text + "%' OR Klassen LIKE '%" + text + "%'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query2);
                dataTable.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(tableName.equals("Benutzern")) {
            String query3 = "SELECT * FROM myschema.Benutzern WHERE benutzername LIKE '%" + text + "%' OR Privileg LIKE '%" + text + "%'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query3);
                dataTable.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(tableName.equals("Fahrzeuge")){
            String query4 = "SELECT * FROM myschema.Fahrzeuge WHERE Markenname LIKE '%" + text + "%' OR Modell LIKE '%" + text + "%' OR Getriebtyp LIKE '%" + text + "%' OR Modelljahr LIKE '%" + text + "%' OR Karossiertyp LIKE '%" + text + "%' OR zuordneteStudenten LIKE '%" + text + "%'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query4);
                dataTable.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(tableName.equals("Klassen")){
            String query5 = "SELECT * FROM myschema.Klassen WHERE Klassenname LIKE '%" + text + "%' OR angemeldetenSchüler LIKE '%" + text + "%' OR Klasselehrer LIKE '%" + text + "%' OR Klassengröße LIKE '%" + text + "%' OR requiredKlassen LIKE '%" + text + "%'";
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query5);
                dataTable.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}


