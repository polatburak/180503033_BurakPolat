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
    private JButton addARowToButton;
    private JButton deleteSelectedRowButton;
    private JButton updateTheModifiedRowButton;


    public homePage() {
        add(mainPanel);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataTable.setBackground(Color.cyan);
        setStartPanel();
        setLabelIcon();
        setDataTable();
        ImageIcon refreshIcon = new ImageIcon(new ImageIcon("images/refresh-logo.png").getImage().getScaledInstance(32, 18, Image.SCALE_DEFAULT));
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
                if (tableName.equals("Benutzern") && (!getPrivilege(loginPage.benutzername).equals("admin"))) {
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
                if (getPrivilege(loginPage.benutzername).equals("admin"))
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
                updateSelectedRow();
            }
        });
        addARowToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //when clicked add a row to table
                if (getPrivilege(loginPage.benutzername).equals("admin")) {
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
    }

    public void setName() throws SQLException {
        connectionJDBC cJDBC = new connectionJDBC();
        Connection conn = cJDBC.getConn();

        String query2 = "SELECT Vorname, Nachname FROM myschema.Personalen, myschema.Benutzern WHERE Benutzern.idBenutzern = Personalen.idbenutzer";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query2);
        String Vorname = null, Nachname = null;
        while (rs.next()) {
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

    public void setStartPanel() {
        parentPanel.removeAll();
        parentPanel.add(startPanel);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    //add image next to benutzerLabel
    public void setLabelIcon() {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("images/Sample_User_Icon.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        benutzerLabel.setIcon(imageIcon);

    }


    //fill dataTable with data from database
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

    //get column names from database
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

    //add a row to the datatable
    public void addRow() throws SQLException {
        String tableName = String.format("" + tableSelector.getSelectedItem());
        String[] columnNames = getColumnNames(tableName);
        String query = String.format("INSERT INTO myschema.%s (", tableName);
        Object values = "VALUES (";
        //get datatypes from database as array
        String[] dataTypes = getColumnTypes(tableName);
        for (int i = 1; i < columnNames.length; i++) {
            String value = JOptionPane.showInputDialog(null, "Please enter a value for " + columnNames[i] + " (type: " + dataTypes[i] + ")");
            //when clicked cancel on input dialog, return
            if (value == null)
                return;
            //when given wrong datatype or empty value show error message
            if (value == null || value.equals("") || !checkDataType(dataTypes[i], value)) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid value for " + columnNames[i] + "!",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                i--;
                continue;
            }
            if (i == columnNames.length - 1) {
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
                    "Row couldn't be added! Try again!",
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

    //check if the given value is of the given datatype
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

}


