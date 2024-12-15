package UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import buildings.Hospital;
import people.Staff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StaffPanel extends JPanel {
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, roleField, departmentField;

    public StaffPanel() {
        setLayout(new BorderLayout());

        // Create the table model and table
        tableModel = new DefaultTableModel(new String[]{"Name", "Role", "Department"}, 0);
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

        formPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        formPanel.add(departmentField);

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
    }

    private void addStaff() {
        String name = nameField.getText();
        String role = roleField.getText();
        String department = departmentField.getText();
        tableModel.addRow(new Object[]{name, role, department});
        Staff newStaff = new Staff(name, role, department);
        try {
                boolean added = Hospital.getInstance().addStaff(newStaff);
                if (added) {
                    System.out.println("Staff added successfully!");
                } else {
                    System.out.println("Failed to add Staff.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error adding patient to the database.");
            }
        } 
        
    

    private void editStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.setValueAt(nameField.getText(), selectedRow, 0);
            tableModel.setValueAt(roleField.getText(), selectedRow, 1);
            tableModel.setValueAt(departmentField.getText(), selectedRow, 2);
        }
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        }
    }
}
