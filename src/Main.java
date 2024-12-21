import java.sql.SQLException;
import javax.swing.SwingUtilities;
import UI.HospitalManagementUI;

public class Main {
    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(() -> {
            HospitalManagementUI ui = new HospitalManagementUI();
            ui.setVisible(true);
        });
    }
}

