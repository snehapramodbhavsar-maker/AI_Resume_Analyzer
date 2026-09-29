import database.DBConnection;
import ui.ConsoleUI;

/**
 * Main.java
 * -------------------------------------------------
 * Entry point of the Resume Analyzer application.
 *
 * How to Run:
 *   1. Run resume_analyzer.sql in MySQL first.
 *   2. Update DB credentials in DBConnection.java.
 *   3. Add mysql-connector-j-x.x.x.jar to classpath.
 *   4. Compile all .java files.
 *   5. Run Main.
 *
 * Compile command (from project root):
 *   javac -cp ".;lib/mysql-connector-j.jar" **\/*.java Main.java
 *
 * Run command:
 *   java -cp ".;lib/mysql-connector-j.jar" Main
 * -------------------------------------------------
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Test database connection at startup
            if (DBConnection.getConnection() == null) {
                System.err.println("❌ Cannot connect to the database. Please check:");
                System.err.println("   - MySQL server is running.");
                System.err.println("   - Credentials in DBConnection.java are correct.");
                System.err.println("   - You have run resume_analyzer.sql to set up the DB.");
                return;
            }

            // Launch the console UI
            ConsoleUI ui = new ConsoleUI();
            ui.start();

        } finally {
            // Always close DB connection on exit
            DBConnection.closeConnection();
        }
    }
}
