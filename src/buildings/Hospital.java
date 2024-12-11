package buildings;

import java.sql.*;
import people.Patient;
import people.Staff;

import java.util.ArrayList;

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
}

