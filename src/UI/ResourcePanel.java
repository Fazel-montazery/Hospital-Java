package UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResourcePanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel roomTableModel;
    private JTextField roomNumberField, roomStatusField;
    
    private JTable toolTable;
    private DefaultTableModel toolTableModel;
    private JTextField toolNameField, toolStatusField;

    public ResourcePanel() {
        setLayout(new BorderLayout());

        // Create tabs for Rooms and Tools management
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Rooms", createRoomPanel());
        tabbedPane.addTab("Tools", createToolPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create the room table model and table
        roomTableModel = new DefaultTableModel(new String[]{"Room Number", "Status"}, 0);
        roomTable = new JTable(roomTableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Create the form fields for room management
        JPanel formPanel = new JPanel(new GridLayout(2, 2));

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Status:"));
        roomStatusField = new JTextField();
        formPanel.add(roomStatusField);

        // Create the buttons for room management
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Room");
        JButton editButton = new JButton("Edit Room");
        JButton deleteButton = new JButton("Delete Room");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRoom();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });

        // Add components to the room panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createToolPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create the tool table model and table
        toolTableModel = new DefaultTableModel(new String[]{"Tool Name", "Status"}, 0);
        toolTable = new JTable(toolTableModel);
        JScrollPane scrollPane = new JScrollPane(toolTable);

        // Create the form fields for tool management
        JPanel formPanel = new JPanel(new GridLayout(2, 2));

        formPanel.add(new JLabel("Tool Name:"));
        toolNameField = new JTextField();
        formPanel.add(toolNameField);

        formPanel.add(new JLabel("Status:"));
        toolStatusField = new JTextField();
        formPanel.add(toolStatusField);

        // Create the buttons for tool management
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Tool");
        JButton editButton = new JButton("Edit Tool");
        JButton deleteButton = new JButton("Delete Tool");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTool();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTool();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTool();
            }
        });

        // Add components to the tool panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText();
        String status = roomStatusField.getText();
        roomTableModel.addRow(new Object[]{roomNumber, status});
    }

    private void editRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            roomTableModel.setValueAt(roomNumberField.getText(), selectedRow, 0);
            roomTableModel.setValueAt(roomStatusField.getText(), selectedRow, 1);
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            roomTableModel.removeRow(selectedRow);
        }
    }

    private void addTool() {
        String toolName = toolNameField.getText();
        String status = toolStatusField.getText();
        toolTableModel.addRow(new Object[]{toolName, status});
    }

    private void editTool() {
        int selectedRow = toolTable.getSelectedRow();
        if (selectedRow >= 0) {
            toolTableModel.setValueAt(toolNameField.getText(), selectedRow, 0);
            toolTableModel.setValueAt(toolStatusField.getText(), selectedRow, 1);
        }
    }

    private void deleteTool() {
        int selectedRow = toolTable.getSelectedRow();
        if (selectedRow >= 0) {
            toolTableModel.removeRow(selectedRow);
        }
    }
}
