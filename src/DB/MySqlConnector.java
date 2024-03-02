package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is responsible for establishing a connection to a MySQL database.
 */
public class MySqlConnector {
    private Connection dbConnection;

    /**
     * Constructs a MySqlConnector object and initializes a database connection.
     *
     * @param username the username required to connect to the database
     * @param password the password required to connect to the database
     */
    public MySqlConnector(String username, String password) {
        dbConnection = connectToDB(username, password);
    }

    /**
     * Attempts to establish a connection to the MySQL database.
     *
     * @param username the username required for the database connection
     * @param password the password required for the database connection
     * @return a Connection object to the database; null if the connection fails
     */
    private Connection connectToDB(String username, String password) {
        try {
            // Attempt to load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            System.out.println("Driver definition failed: " + ex);
            return null; // Return null if driver loading fails
        }

        try {
            // Attempt to establish a connection to the specified MySQL database
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/project?serverTimezone=IST", username, password);
            System.out.println("SQL connection succeed");
            return conn;
        } catch (SQLException ex) {
            // Handle SQL exceptions by printing error details
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null; // Return null if connection fails
        }
    }

    /**
     * Returns the database connection.
     *
     * @return the Connection object to the database
     */
    public Connection getDbConnection() {
        return dbConnection;
    }

}
