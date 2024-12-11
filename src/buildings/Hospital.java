package buildings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import people.Patient;
import people.Staff;

public final class Hospital {
    // The internal instance
    private static Hospital instance;

    // Sqlite connection
    private Connection connectionDB;

    private Hospital() {
        try {
            // Initialize SQLite connection
            connectionDB = DriverManager.getConnection("jdbc:sqlite:db/hospital.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database.");
        }
    }

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }

    private void initializeDatabase() throws SQLException {
        try (Statement stmt = connectionDB.createStatement()) {
            // Create tables with UNIQUE constraint on nationalCode
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Patient (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nationalCode TEXT UNIQUE NOT NULL,
                        name TEXT NOT NULL,
                        illness TEXT NOT NULL
                    );
                """);

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Staff (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nationalCode TEXT UNIQUE NOT NULL,
                        name TEXT NOT NULL,
                        role TEXT NOT NULL
                    );
                """);
        }
    }

    public boolean addPatient(Patient patient) throws SQLException {
        if (!patient.isValid())
            return false;

        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "INSERT INTO Patient (nationalCode, name, illness) VALUES (?, ?, ?)")) {
            pstmt.setString(1, patient.getNationalId());
            pstmt.setString(2, patient.getName());
            pstmt.setString(3, patient.getIllness());
            pstmt.executeUpdate();
            return true;
        }
    }

    public boolean addStaff(Staff staff) throws SQLException {
        if (!staff.isValid())
            return false;

        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "INSERT INTO Patient (nationalCode, name, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, staff.getNationalId());
            pstmt.setString(2, staff.getName());
            pstmt.setString(3, staff.getRole());
            pstmt.executeUpdate();
            return true;
        }
    }

    public boolean deletePatient(String nationalCode) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "DELETE FROM Patient WHERE nationalCode = ?")) {
            pstmt.setString(1, nationalCode);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No patient found with nationalCode: " + nationalCode);
                return false;
            } else {
                System.out.println("Patient with nationalCode " + nationalCode + " deleted successfully.");
                return true;
            }
        }
    }

    public boolean deleteStaff(String nationalCode) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "DELETE FROM Staff WHERE nationalCode = ?")) {
            pstmt.setString(1, nationalCode);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No staff found with nationalCode: " + nationalCode);
                return false;
            } else {
                System.out.println("Staff with nationalCode " + nationalCode + " deleted successfully.");
                return true;
            }
        }
    }

    public boolean updatePatient(String nationalCode, String newName, String newIllness) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "UPDATE Patient SET name = ?, illness = ? WHERE nationalCode = ?")) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newIllness);
            pstmt.setString(3, nationalCode);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No patient found with nationalCode: " + nationalCode);
                return false;
            } else {
                System.out.println("Patient with nationalCode " + nationalCode + " updated successfully.");
                return true;
            }
        }
    }

    public boolean updateStaff(String nationalCode, String newName, String newRole) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "UPDATE Staff SET name = ?, role = ? WHERE nationalCode = ?")) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newRole);
            pstmt.setString(3, nationalCode);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No staff found with nationalCode: " + nationalCode);
                return false;
            } else {
                System.out.println("Staff with nationalCode " + nationalCode + " updated successfully.");
                return true;
            }
        }
    }

    public List<Patient> getPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        try (Statement stmt = connectionDB.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Patient")) {
            while (rs.next()) {
                patients.add(new Patient(rs.getString("name"), rs.getString("nationalCode"), rs.getString("illness")));
            }
        }
        return patients;
    }

    public List<Staff> getStaff() throws SQLException {
        List<Staff> staff = new ArrayList<>();
        try (Statement stmt = connectionDB.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Staff")) {
            while (rs.next()) {
                staff.add(new Staff(rs.getString("name"), rs.getString("nationalCode"), rs.getString("role")));
            }
        }
        return staff;
    }
}

