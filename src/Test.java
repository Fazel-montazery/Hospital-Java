import buildings.Hospital;
import people.Patient;
import people.Staff;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import UI.HospitalManagementUI;

public class Test {
    public static void main(String[] args) throws SQLException {
        Hospital mehrgan = Hospital.getInstance();
        for (Patient p : mehrgan.getPatients()) {
            System.out.println(p);
        }
        for (Staff staff : mehrgan.getStaff()) {
            System.out.println(staff);
        }
        SwingUtilities.invokeLater(() -> {
            HospitalManagementUI ui = new HospitalManagementUI();
            ui.setVisible(true);
        });
    }
}

