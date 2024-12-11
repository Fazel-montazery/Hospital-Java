package UI;
import javax.swing.*;


public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Reports"));
        JButton generateReportButton = new JButton("Generate Report");
        add(generateReportButton);
    }
}
