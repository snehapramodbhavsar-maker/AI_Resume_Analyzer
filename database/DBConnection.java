package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * -------------------------------------------------
 * Utility class for managing MySQL JDBC connections.
 * Uses Singleton pattern to reuse the connection.
 * -------------------------------------------------
 */
public class DBConnection {

    // ── JDBC Configuration ──────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/resume_analyzer_db";
    private static final String USER     = "root";        // Change if needed
    private static final String PASSWORD = "root";        // Change to your MySQL password
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    // Private constructor — no instantiation allowed
    private DBConnection() {}

    /**
     * Returns a singleton Connection instance.
     * Opens a new connection if one doesn't exist or is closed.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB ERROR] MySQL JDBC Driver not found. Add mysql-connector-j.jar to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to connect: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to close connection: " + e.getMessage());
        }
    }
}
