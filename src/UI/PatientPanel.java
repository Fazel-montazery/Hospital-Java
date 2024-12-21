package UI;

import people.Patient;
import means.Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class PatientPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, nationalId, conditionField;

    public PatientPanel() {
        setLayout(new BorderLayout());

        // Create the table model and table
        tableModel = new DefaultTableModel(new String[]{"Name", "NationalID", "Condition"}, 0);
        patientTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Create the form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("NationalID:"));
        nationalId = new JTextField();
        formPanel.add(nationalId);

        formPanel.add(new JLabel("Condition:"));
        conditionField = new JTextField();
        formPanel.add(conditionField);

        // Create the buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Patient");
        JButton editButton = new JButton("Edit Patient");
        JButton deleteButton = new JButton("Delete Patient");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPatient();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
            }
        });

        // Add components to the panel
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load patients from the database
        loadPatientsFromDatabase();
    }

    // Load patients from the database and populate the table
    private void loadPatientsFromDatabase() {
        try {
            List<Patient> patients = Hospital.getInstance().getPatients();
            for (Patient patient : patients) {
                tableModel.addRow(new Object[]{patient.getName(), patient.getNationalId(), patient.getIllness()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create a new Patient instance
    private void addPatient() {
        // Retrieve values from the text fields
        String name = nameField.getText();
//        String age = Agefield.getText();
        String ID = nationalId.getText();
        String condition = conditionField.getText();

        // Create a new Patient instance
        Patient newPatient = new Patient(name, ID, condition);

        if (newPatient.isValid()) {
            // Add patient data to the table
            tableModel.addRow(new Object[]{name, ID, condition});

            try {
                boolean added = Hospital.getInstance().addPatient(newPatient);
                if (added) {
                    System.out.println("Patient added successfully!");
                } else {
                    System.out.println("Failed to add patient.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adding patient to the database.");
            }
        } else {
            System.out.println("Patient information is not valid.");
        }
    }

    private void editPatient() {
        int selectedRow = patientTable.getSelectedRow();
    
        if (selectedRow >= 0) {
            // Retrieve new values from the text fields
            String newName = nameField.getText();
            String newCondition = conditionField.getText();
    
            // Get the original NationalID (primary key) from the selected row
            String originalNationalId = (String) tableModel.getValueAt(selectedRow, 1);
    
            try {
                // Call the update method in the Hospital class
                boolean updated = Hospital.getInstance().updatePatient(originalNationalId, newName, newCondition);
    
                if (updated) {
                    // Update the table with new values
                    tableModel.setValueAt(newName, selectedRow, 0);
                    tableModel.setValueAt(originalNationalId, selectedRow, 1); // If NationalID can be changed
                    tableModel.setValueAt(newCondition, selectedRow, 2);
    
                     System.out.println("Patient updated successfully.");
                } else {
                    System.out.println("Failed to update patient.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                }
        } 
    }
    

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get the nationalCode of the selected patient
            String nationalCode = (String) tableModel.getValueAt(selectedRow, 1);

            try {
                // Attempt to delete the patient from the database
                @SuppressWarnings("unused")
                boolean deleted = Hospital.getInstance().deletePatient(nationalCode);
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
             }
        }
    }

}
