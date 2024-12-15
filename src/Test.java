import buildings.Hospital;
import people.Patient;
import people.Person;
import people.Staff;
import UI.*;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import UI.HospitalManagementUI;

public class Test {
    public static void main(String[] args) throws SQLException {
        // Hospital mehrgan = Hospital.getInstance();
        SwingUtilities.invokeLater(() -> {
            HospitalManagementUI ui = new HospitalManagementUI();
            ui.setVisible(true);
        });
    }
}

