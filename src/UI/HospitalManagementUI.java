package UI;

import javax.swing.*;
import java.awt.*;

public class HospitalManagementUI extends JFrame {
    public HospitalManagementUI() {
        setTitle("Hospital Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patient Management", new PatientPanel());
        tabbedPane.addTab("Staff Management", new StaffPanel());
        tabbedPane.addTab("Resource Management", new ResourcePanel());
        tabbedPane.addTab("Reports", new ReportsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
