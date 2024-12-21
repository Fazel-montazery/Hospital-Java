package means;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import people.Patient;
import people.Person;
import people.Staff;

public final class Hospital {
    // The internal instance
    private static Hospital instance;

    // Sqlite connection
    private static Connection connectionDB;

    // Writer for log files
    private static FileWriter writer;

    private Hospital() {
        try {
            // Initialize SQLite connection
            connectionDB = DriverManager.getConnection("jdbc:sqlite:db/hospital.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database.");
        }

        try {
            // Initialize the log writer
            writer = new FileWriter(getNextLogFileName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize log writer.");
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    personType TEXT NOT NULL CHECK (personType IN ('Patient', 'Staff')),
                    nationalCode TEXT NOT NULL,
                    roomId INTEGER NOT NULL,
                    FOREIGN KEY (roomId) REFERENCES Room(roomNumber),
                    UNIQUE(personType, nationalCode, roomId)
                );
            """);

            // Create Tools table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Tool (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE NOT NULL,
                    status TEXT NOT NULL
                );
            """);

        }
    }

    private static String getNextLogFileName() {
        int logNumber = 0;
        String fileName;

        while (true) {
            fileName = String.format("log/log%02d.txt", logNumber);
            File file = new File(fileName);

            if (!file.exists()) {
                break;
            }

            logNumber++;
        }
        return fileName;
    }

    private static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + currentDateTime.format(formatter) + "]";
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
            writer.write(getCurrentDateTime() + "=> addPatient => " + patient + "\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            writer.write(getCurrentDateTime() + "=> addStaff => " + staff + "\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                writer.write(getCurrentDateTime() + "=> deletePatient => " + nationalCode + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                writer.write(getCurrentDateTime() + "=> deleteStaff => " + nationalCode + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                writer.write(getCurrentDateTime() + "=> updatePatient => " + nationalCode + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                writer.write(getCurrentDateTime() + "=> updateStaff => " + nationalCode + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        if (query.isBlank()) {
            return getPeople();
        } else {
            List<Person> results = new ArrayList<>();
            results.addAll(searchPatients(query));
            results.addAll(searchStaff(query));
            return results;
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
            writer.write(getCurrentDateTime() + "=> addRoom => " + room + "\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            writer.write(getCurrentDateTime() + "=> assignToRoom[" + roomNumber + "] => " + nationalCode + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> getRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try (Statement stmt = connectionDB.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Room")) {
             while (rs.next()) {
                 rooms.add(new Room(rs.getInt("roomNumber"),rs.getInt("capacity"), rs.getString("type")));
             }
        }
        return rooms;
    }

    public boolean deleteRoom(int roomId) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "DELETE FROM Room WHERE roomNumber = ?")) {
            pstmt.setInt(1, roomId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No room found with roomNumber: " + roomId);
                return false;
            } else {
                System.out.println("Room with roomNumber " + roomId + " deleted successfully.");
                writer.write(getCurrentDateTime() + "=> deleteRoom => " + roomId + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addTool(Tool tool) throws SQLException {
        if (!tool.isValid()) {
            return false;
        }

        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "INSERT INTO Tool (name, status) VALUES (?, ?)")) {
            pstmt.setString(1, tool.getName());
            pstmt.setString(2, (tool.isAvailable()) ? "Available" : "In Use");
            pstmt.executeUpdate();
            System.out.println("Tool " + tool.getName() + " added successfully.");
            writer.write(getCurrentDateTime() + "=> addTool => " + tool + "\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Tool> getTools() throws SQLException {
        List<Tool> tools = new ArrayList<>();
        try (Statement stmt = connectionDB.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Tool")) {
            while (rs.next()) {
                String status = rs.getString("status");
                boolean ava = status.equals("Available");
                tools.add(new Tool(rs.getString("name"), ava));
            }
        }
        return tools;
    }

    public boolean deleteTool(String name) throws SQLException {
        try (PreparedStatement pstmt = connectionDB.prepareStatement(
                "DELETE FROM Tool WHERE name = ?")) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                return false;
            } else {
                writer.write(getCurrentDateTime() + "=> deleteTool => " + name + "\n");
                writer.flush();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}