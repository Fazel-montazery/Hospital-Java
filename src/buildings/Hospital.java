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
                        name TEXT NOT NULL
                    );
                """);

            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Staff (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nationalCode TEXT UNIQUE NOT NULL,
                        name TEXT NOT NULL
                    );
                """);
        }
    }
}

