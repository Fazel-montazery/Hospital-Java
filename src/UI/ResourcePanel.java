package UI;
import means.Hospital;
import means.Room;
import means.Tool;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResourcePanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel roomTableModel;
    private JTextField roomNumberField, roomCapacityField, roomTypeField;
    
    private JTable toolTable;
    private DefaultTableModel toolTableModel;
    private JTextField toolNameField;
    private JCheckBox toolStatusField;

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
        roomTableModel = new DefaultTableModel(new String[]{"Room Number", "Capacity", "Type"}, 0);
        roomTable = new JTable(roomTableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Retrieve current rooms
        try {
            for (Room r : Hospital.getInstance().getRooms()) {
                roomTableModel.addRow(new Object[]{r.getNumber(), r.getCapacity(), r.getType()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the form fields for room management
        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Capacity:"));
        roomCapacityField = new JTextField();
        formPanel.add(roomCapacityField);

        formPanel.add(new JLabel("Type:"));
        roomTypeField = new JTextField();
        formPanel.add(roomTypeField);

        // Create the buttons for room management
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Room");
        JButton deleteButton = new JButton("Delete Room");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom();
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

        // Retrieve current rooms
        try {
            for (Tool t : Hospital.getInstance().getTools()) {
                toolTableModel.addRow(new Object[]{t.getName(), (t.isAvailable()) ? "Available" : "In Use"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the form fields for tool management
        JPanel formPanel = new JPanel(new GridLayout(2, 2));

        formPanel.add(new JLabel("Tool Name:"));
        toolNameField = new JTextField();
        formPanel.add(toolNameField);

        formPanel.add(new JLabel("Is Available:"));
        toolStatusField = new JCheckBox();
        formPanel.add(toolStatusField);

        // Create the buttons for tool management
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Tool");
        JButton deleteButton = new JButton("Delete Tool");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Add action listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTool();
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
        String roomCapcity = roomCapacityField.getText();
        String roomType = roomTypeField.getText();

        try {
            boolean result = Hospital.getInstance().addRoom(new Room(Integer.parseInt(roomNumber), Integer.parseInt(roomCapcity), roomType));
            if (!result) {
                System.out.println("Couldn't add the room!");
            } else {
                System.out.println("Room added successfully!");
                roomTableModel.addRow(new Object[]{roomNumber, roomCapcity, roomType});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        try {
            if (selectedRow >= 0) {
                int roomId = (int) roomTableModel.getValueAt(selectedRow, 0);
                if (Hospital.getInstance().deleteRoom(roomId)) {
                    roomTableModel.removeRow(selectedRow);
                } else {
                    System.out.println("Error deleting the room!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTool() {
        String toolName = toolNameField.getText();
        boolean toolStat = toolStatusField.isSelected();

        try {
            boolean result = Hospital.getInstance().addTool(new Tool(toolName, toolStat));
            if (!result) {
                System.out.println("Couldn't add the tool!");
            } else {
                System.out.println("tool added successfully!");
                toolTableModel.addRow(new Object[]{toolName, (toolStat) ? "Available" : "In Use"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTool() {
        int selectedRow = toolTable.getSelectedRow();
        try {
            if (selectedRow >= 0) {
                String toolName = (String) toolTableModel.getValueAt(selectedRow, 0);
                if (Hospital.getInstance().deleteTool(toolName)) {
                    toolTableModel.removeRow(selectedRow);
                } else {
                    System.out.println("Error deleting the room!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
