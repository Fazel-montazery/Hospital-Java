package UI;

import java.util.List;

import buildings.Hospital;
import people.Staff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StaffPanel extends JPanel {
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, roleField, NationalIDField;

    public StaffPanel() {
        setLayout(new BorderLayout());

        // Create the table model and table
        tableModel = new DefaultTableModel(new String[]{"Name", "Role", "NationalId"}, 0);
        staffTable = new JTable(tableModel);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(staffTable);

        // Create the form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Role:"));
        roleField = new JTextField();
        formPanel.add(roleField);

        formPanel.add(new JLabel("NationalId:"));
        NationalIDField = new JTextField();
        formPanel.add(NationalIDField);

        // Create the buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Staff");
        JButton editButton = new JButton("Edit Staff");
        JButton deleteButton = new JButton("Delete Staff");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStaff();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
            }
        });

        // Add components to the panel
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        loadStaffFromDatabase();
    }

    private void loadStaffFromDatabase() {
        try {
            List<Staff> staffList = Hospital.getInstance().getStaff();
            for (Staff staff : staffList) {
                tableModel.addRow(new Object[]{staff.getName(), staff.getRole(), staff.getNationalId()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStaff() {
        String name = nameField.getText();
        String role = roleField.getText();
        String NationalId = NationalIDField.getText();
        tableModel.addRow(new Object[]{name, role, NationalId});
        Staff newStaff = new Staff(name, role, NationalId);
        try {
                boolean added = Hospital.getInstance().addStaff(newStaff);
                if (added) {
                    System.out.println("Staff added successfully!");
                } else {
                    System.out.println("Failed to add Staff.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adding Staff to the database.");
            }
        } 
        
    

    private void editStaff() {
        int selectedRow = staffTable.getSelectedRow();

        if (selectedRow >= 0) {
           String newName = nameField.getText();
           String newRole = roleField.getText();
           String newNationalId = NationalIDField.getText();
    
          String originalNationalId = (String) tableModel.getValueAt(selectedRow, 2);
    
       try {
           boolean updated = Hospital.getInstance().updateStaff(originalNationalId, newName, newRole);
    
           if (updated) {
               tableModel.setValueAt(newName, selectedRow, 0);
               tableModel.setValueAt(newRole, selectedRow, 1);
               tableModel.setValueAt(newNationalId, selectedRow, 2);
    
                System.out.println("Staff updated successfully.");
            } else {
                System.out.println("Failed to update staff.");
            }
        } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating staff in the database.");
            }
        }
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
    
        if (selectedRow >= 0) {
            String nationalId = (String) tableModel.getValueAt(selectedRow, 2);
    
            try {
                boolean deleted = Hospital.getInstance().deleteStaff(nationalId);

                if (deleted) {
                    tableModel.removeRow(selectedRow);
                    System.out.println("Staff deleted successfully.");
                } else {
                   System.out.println("Failed to delete staff.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error deleting staff from the database.");
            }
        } 
   }
}

