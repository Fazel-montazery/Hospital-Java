package UI;

import javax.swing.SwingUtilities;

public class HospitalManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalManagementUI ui = new HospitalManagementUI();
            ui.setVisible(true);
        });
    }
}
