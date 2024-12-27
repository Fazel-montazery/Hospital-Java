package UI;

import means.Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;

public class LoginAndSignUpPage extends JFrame {

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;

    private JTextField signUpUsernameField;
    private JPasswordField signUpPasswordField;
    private JButton signUpButton;

    public LoginAndSignUpPage() {
        setTitle("Login and SignUp Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Login Panel
        JPanel loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);

        // Add SignUp Panel
        JPanel signUpPanel = createSignUpPanel();
        tabbedPane.addTab("SignUp", signUpPanel);

        // Add tabbedPane to frame
        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        loginUsernameField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(loginUsernameField);
        panel.add(passwordLabel);
        panel.add(loginPasswordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(e -> handleLogin());

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        signUpUsernameField = new JTextField(20);
        signUpPasswordField = new JPasswordField(20);
        signUpButton = new JButton("SignUp");

        panel.add(usernameLabel);
        panel.add(signUpUsernameField);
        panel.add(passwordLabel);
        panel.add(signUpPasswordField);
        panel.add(new JLabel());
        panel.add(signUpButton);

        signUpButton.addActionListener(e -> handleSignUp());

        return panel;
    }

    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        if (!validatePassword(password)) {
            JOptionPane.showMessageDialog(this, "Password not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Hospital.getInstance().signInUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Can't signIn, try again later!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Success!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new HospitalManagementUI().setVisible(true);
    }

    private void handleSignUp() {
        String username = signUpUsernameField.getText();
        String password = new String(signUpPasswordField.getPassword());

        if (!validatePassword(password)) {
            JOptionPane.showMessageDialog(this, "Password not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Hospital.getInstance().signUpUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Can't signUp, try again later!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Success!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new HospitalManagementUI().setVisible(true);
    }

    private boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
