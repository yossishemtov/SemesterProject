package DB;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is responsible for managing database operations related to orders.
 */
public class DatabaseController {
    private MySqlConnector connector;
    private Connection connectionToDatabase;

    /**
     * Constructs a DatabaseController object with specified user credentials.
     *
     * @param username the database username
     * @param password the database password
     */
    public DatabaseController(String username, String password) {
        this.connector = new MySqlConnector(username, password);
        connectionToDatabase = this.connector.getDbConnection();
    }

    /**
     * Retrieves specific order data from the database based on the order number.
     *
     * @param orderNumber The order number to search for.
     * @return An ArrayList containing the park name and telephone number for the specified order.
     */
    public ArrayList<String> getSpecificDataFromDB(String orderNumber) {
        ArrayList<String> orderDataForClient = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE OrderNumber=?";

        try {
            PreparedStatement ps = connectionToDatabase.prepareStatement(query);
            ps.setString(1, orderNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String parkName = rs.getString("ParkName");
                String telephoneNumber = rs.getString("TelephoneNumber");

                orderDataForClient.add(parkName);
                orderDataForClient.add(telephoneNumber);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDataForClient;
    }

    /**
     * Retrieves all order data from the database.
     *
     * @return An ArrayList of strings, where each string contains concatenated order information.
     */
    public ArrayList<String> getOrderDataFromDatabase() {
        ArrayList<String> orderDataForClient = new ArrayList<>();
        String query = "SELECT * FROM `order`";
        
        try {
            Statement st = connectionToDatabase.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String parkName = rs.getString("ParkName");
                String orderNumber = rs.getString("OrderNumber");
                String timeOfVisit = rs.getString("TimeOfVisit");
                String numberOfVisitors = rs.getString("NumberOfVisitors");
                String telephoneNumber = rs.getString("TelephoneNumber");
                String email = rs.getString("Email");

                String rowInDB = parkName + " " + orderNumber + " " + timeOfVisit +
                                 " " + numberOfVisitors + " " + telephoneNumber + " " + email;
                orderDataForClient.add(rowInDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDataForClient;
    }

    /**
     * Updates the telephone number and park name for a specific order in the database.
     *
     * @param orderId                The ID of the order to update.
     * @param parkNameToChange       The new park name.
     * @param telephoneNumberToChange The new telephone number.
     */
    public void setOrderDataOnDatabase_TelphoneParkNameChange(String orderId, String parkNameToChange, String telephoneNumberToChange) {
        String updateQuery = "UPDATE `order` SET TelephoneNumber=?, ParkName=? WHERE OrderNumber=?";

        try {
            PreparedStatement ps = connectionToDatabase.prepareStatement(updateQuery);

            ps.setString(1, telephoneNumberToChange);
            ps.setString(2, parkNameToChange);
            ps.setString(3, orderId);
            ps.executeUpdate();
            ps.close();
            System.out.println("Order data updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
