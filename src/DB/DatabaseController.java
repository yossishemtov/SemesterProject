package DB;

import java.sql.*;
import java.util.ArrayList;
import common.*;

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
	 * @param orderId The order number to search for.
	 * @return An ArrayList containing the park name and telephone number for the
	 *         specified order.
	 */
	public ArrayList<String> getSpecificDataFromDB(String orderId) {

		ArrayList<String> orderDataForClient = new ArrayList<>();
		String query = "SELECT * FROM `order` WHERE OrderNumber=?";

		try {
			PreparedStatement ps = connectionToDatabase.prepareStatement(query);
			ps.setString(1, orderId);
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
	 * @return An ArrayList of strings, where each string contains concatenated
	 *         order information.
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

				String rowInDB = parkName + " " + orderNumber + " " + timeOfVisit + " " + numberOfVisitors + " "
						+ telephoneNumber + " " + email;
				orderDataForClient.add(rowInDB);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderDataForClient;
	}

	/**
	 * Updates the telephone number and park name for a specific order in the
	 * database.
	 *
	 * @param orderId                 The ID of the order to update.
	 * @param parkNameToChange        The new park name.
	 * @param telephoneNumberToChange The new telephone number.
	 */
	public void setOrderDataOnDatabase_TelphoneParkNameChange(String orderId, String parkNameToChange,
			String telephoneNumberToChange) {
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

	// Existing class content...

	/**
	 * Adds a new group guide to the database.
	 *
	 * @param groupGuide The GroupGuide object to add to the database.
	 */
	public void addNewGroupGuide(GroupGuide groupGuide) {
		String query = "INSERT INTO GroupGuides (id, firstName, lastName, email, username, password) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			// Set parameters for the prepared statement based on the GroupGuide object
			ps.setInt(1, groupGuide.getId());
			ps.setString(2, groupGuide.getFirstName());
			ps.setString(3, groupGuide.getLastName());
			ps.setString(4, groupGuide.getEmail());
			ps.setString(5, groupGuide.getUsername());
			ps.setString(6, groupGuide.getPassword());

			// Execute the update
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("A new group guide was added successfully!");
			} else {
				System.out.println("A problem occurred and the group guide was not added.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL Exception: Could not add the group guide to the database.");
		}
	}

	// Existing constructor and methods...

	/**
	 * Inserts a new reservation into the database.
	 * 
	 * @param traveler The traveler making the reservation.
	 * @param order    The details of the order being made.
	 */
	public void insertTravelerOrder(Traveler traveler, Order order) {
		String query = "INSERT INTO orders (id, firstName, lastName, email,orderID parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime) VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, traveler.getId());
			ps.setString(2, traveler.getFirstName());
			ps.setString(3, traveler.getLastName());
			ps.setString(4, traveler.getEmail());
			ps.setInt(5, order.getOrderId());
			ps.setInt(6, order.getParkNumber());
			ps.setInt(7, order.getAmountOfVisitors());
			ps.setFloat(8, order.getPrice());
			ps.setString(9, order.getVisitorEmail());
			ps.setDate(10, java.sql.Date.valueOf(order.getDate())); // Convert LocalDate to sql.Date
			ps.setTime(11, java.sql.Time.valueOf(order.getVisitTime())); // Convert LocalTime to sql.Time

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Reservation inserted successfully.");
			} else {
				System.out.println("A problem occurred and the reservation was not inserted.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the status of an existing order in the database.
	 * 
	 * @param order The order object containing the order ID and the new status.
	 */
	public void updateOrderStatus(Order order) {
		String updateQuery = "UPDATE orders SET status = ? WHERE orderId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(updateQuery)) {
			ps.setString(1, order.getOrderStatus()); // Assuming getStatus() returns the new status of the order
			ps.setInt(2, order.getOrderId()); // Assuming getOrderId() returns the ID of the order to be updated

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Order status updated successfully.");
			} else {
				System.out.println(
						"No order was found with the provided ID, or the status is already set to the new value.");
			}
		} catch (SQLException e) {
			System.out.println("An error occurred while updating the order status:");
			e.printStackTrace();
		}
	}
	
	public void GetTravelerID(Traveler traveler) {
//		String query = "SELECT * FROM traveler WHERE id=?";
//		try {
//			PreparedStatement ps = connectionToDatabase.prepareStatement(query);
//			ps.setInt(1, traveler.getId());
//		}catch() {
//			
//		}
		
//		ClientServerMessage message = new ClientServerMessage(,guery);
		
	}

}
