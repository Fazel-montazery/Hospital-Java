package UI;

import people.Patient;
import buildings.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PatientPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, Agefield, nationalId, conditionField;

    public PatientPanel() {
        setLayout(new BorderLayout());

        // Create the table model and table
        tableModel = new DefaultTableModel(new String[]{"Name", "Age", "NationalID", "Condition"}, 0);
        patientTable = new JTable(tableModel);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Create the form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Age"));
        Agefield = new JTextField();
        formPanel.add(Agefield);

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
    }

    // Create a new Patient instance
    private void addPatient() {
        // Retrieve values from the text fields
        String name = nameField.getText();
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
            tableModel.setValueAt(nameField.getText(), selectedRow, 0);
            tableModel.setValueAt(nationalId.getText(), selectedRow, 1);
            tableModel.setValueAt(conditionField.getText(), selectedRow, 2);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        }
    }
}
