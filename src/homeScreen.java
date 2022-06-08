import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Thu Jun 09 00:04:25 TRT 2022
 */



/**
 * @author unknown
 */
public class homeScreen extends JPanel {
    public homeScreen() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        menuBar1 = new JMenuBar();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("NameGoesHere");
        add(label1, "cell 0 0 6 1");

        //======== scrollPane1 ========
        {

            //---- textArea1 ----
            textArea1.setText("kl\u015fasfkl\u015fasf");
            scrollPane1.setViewportView(textArea1);
        }
        add(scrollPane1, "cell 3 5 7 6");
        add(menuBar1, "cell 2 11");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JMenuBar menuBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
