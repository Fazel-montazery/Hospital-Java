package buildings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import people.Patient;
import people.Person;
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

            // Create Room table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Room (
                    roomNumber INTEGER PRIMARY KEY,
                    capacity INTEGER DEFAULT 1,
                    type TEXT NOT NULL
                );
            """);

            // Create RoomAssignment table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS RoomAssignment (
                    roomNumber INTEGER NOT NULL,
                    nationalCode TEXT NOT NULL,
                    PRIMARY KEY (roomNumber, nationalCode),
                    FOREIGN KEY (roomNumber) REFERENCES Room (roomNumber),
                    FOREIGN KEY (nationalCode) REFERENCES (
                        SELECT nationalCode FROM (
                            SELECT nationalCode FROM Patient
                            UNION ALL
                            SELECT nationalCode FROM Staff
                        )
                    )
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
                "INSERT INTO Staff (nationalCode, name, role) VALUES (?, ?, ?)")) {
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

    public List<Patient> searchPatients(String query) throws SQLException {
        List<Patient> results = new ArrayList<>();
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "SELECT * FROM Patient WHERE name LIKE ? OR nationalCode LIKE ?")) {
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Patient(rs.getString("name"), rs.getString("nationalCode"), rs.getString("illness")));
                }
            }
        }
        return results;
    }

    public List<Staff> searchStaff(String query) throws SQLException {
        List<Staff> results = new ArrayList<>();
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "SELECT * FROM Staff WHERE name LIKE ? OR nationalCode LIKE ?")) {
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Staff(rs.getString("name"), rs.getString("nationalCode"), rs.getString("role")));
                }
            }
        }
        return results;
    }

    public List<Person> searchPeople(String query) throws SQLException {
        List<Person> results = new ArrayList<>();

        results.addAll(searchPatients(query));
        results.addAll(searchStaff(query));

        return results;
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

    public List<Person> getPeople() throws SQLException {
        List<Person> people = new ArrayList<>();

        people.addAll(getPatients());
        people.addAll(getStaff());

        return people;
    }

    public boolean addRoom(Room room) throws SQLException {
        if (!room.isValid()) {
            return false;
        }

        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "INSERT INTO Room (roomNumber, capacity, type) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, room.getNumber());
            pstmt.setInt(2, room.getCapacity());
            pstmt.setString(3, room.getType());
            pstmt.executeUpdate();
            System.out.println("Room " + room.getNumber() + " added successfully.");
            return true;
        }
    }

    public void assignToRoom(int roomNumber, String nationalCode) throws SQLException {
        // Check if the room exists
        try (PreparedStatement roomCheck = connectionDB.prepareStatement(
                "SELECT COUNT(*) AS count FROM Room WHERE roomNumber = ?")) {
            roomCheck.setInt(1, roomNumber);
            try (ResultSet rs = roomCheck.executeQuery()) {
                if (rs.getInt("count") == 0) {
                    throw new SQLException("Room " + roomNumber + " does not exist.");
                }
            }
        }

        // Assign the person to the room
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "INSERT INTO RoomAssignment (roomNumber, nationalCode) VALUES (?, ?)")) {
            pstmt.setInt(1, roomNumber);
            pstmt.setString(2, nationalCode);
            pstmt.executeUpdate();
            System.out.println("Assigned nationalCode " + nationalCode + " to room " + roomNumber);
        }
    }

}