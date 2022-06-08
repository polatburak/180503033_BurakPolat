import javax.swing.*;

public class homePage extends JFrame {
    public JLabel Name;
    private JPanel mainPanel;
    private JPanel selectionPanel;
    private JPanel parentPanel;
    public JLabel authority;

    public homePage(){
        add(mainPanel);
        setSize(900, 600);
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
}
