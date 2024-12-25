package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        // Layout setup
        setLayout(new GridLayout(3, 2));
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);

        // Add action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!validatePassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkCredentials(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Open main application
            dispose(); // Close login window
            new HospitalManagementUI().setVisible(true); // Replace 'HospitalManagementUI' with your main class name
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validatePassword(String password) {
        // Regex for password: at least 8 characters, 1 uppercase, 1 lowercase, 1 number
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean checkCredentials(String username, String password) {
        // boolean isValid = false;

        // try {
        //     // Connect to database
        //     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "root", "password");
        //     String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        //     PreparedStatement stmt = conn.prepareStatement(query);
        //     stmt.setString(1, username);
        //     stmt.setString(2, password);

        //     ResultSet rs = stmt.executeQuery();
        //     if (rs.next()) {
        //         isValid = true;
        //     }

        //     rs.close();
        //     stmt.close();
        //     conn.close();
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}