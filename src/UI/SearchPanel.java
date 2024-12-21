package UI;

import means.Hospital;
import means.Room;
import people.Patient;
import people.Staff;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

public class SearchPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JTextField patientSearch;

    private JTable staffTable;
    private DefaultTableModel staffTableModel;
    private JTextField staffSearch;

    public SearchPanel() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patients", createPatientsPanel());
        tabbedPane.addTab("Staff", createStaffPanel());

        add(tabbedPane, BorderLayout.CENTER);    
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        staffTableModel = new DefaultTableModel(new String[]{"Name", "nationalId", "role"}, 0);
        staffTable = new JTable(staffTableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);

        JPanel formPanel = new JPanel(new GridLayout(1, 2));

        formPanel.add(new JLabel("Entry:"));
        staffSearch = new JTextField();
        formPanel.add(staffSearch);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Search");
        buttonPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = staffTableModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    staffTableModel.removeRow(i);
                }

                String text = staffSearch.getText();
                try {
                    for (Staff s : Hospital.getInstance().searchStaff(text)) {
                        staffTableModel.addRow(new Object[]{s.getName(), s.getNationalId(), s.getRole()});
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPatientsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        patientTableModel = new DefaultTableModel(new String[]{"Name", "nationalId", "condition"}, 0);
        patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        JPanel formPanel = new JPanel(new GridLayout(1, 2));

        formPanel.add(new JLabel("Entry:"));
        patientSearch = new JTextField();
        formPanel.add(patientSearch);

        patientSearch.addPropertyChangeListener("document", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String text = patientSearch.getText();
                try {
                    for (Patient s : Hospital.getInstance().searchPatients(text)) {
                        patientTableModel.addRow(new Object[]{s.getName(), s.getNationalId(), s.getIllness()});
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Search");
        buttonPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = patientTableModel.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    patientTableModel.removeRow(i);
                }

                String text = patientSearch.getText();
                try {
                    for (Patient s : Hospital.getInstance().searchPatients(text)) {
                        patientTableModel.addRow(new Object[]{s.getName(), s.getNationalId(), s.getIllness()});
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
}
