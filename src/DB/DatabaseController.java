package DB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import common.*;
import common.worker.*;

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
	
	
	// Get GeneralParkWorkerDetails
	// check GetGeneralParkWorker login details and return the data,if not exist return empty ArrayList
	public ArrayList<GeneralParkWorker> getGeneralParkWorkerDetails(GeneralParkWorker worker) {
		ArrayList<Order> orderDataForClient = new ArrayList<>();
		String query = "SELECT * FROM `generalparkworker` WHERE userName = ? AND password= ?";
		ArrayList<GeneralParkWorker> GeneralParkWorkerList = new ArrayList<>();

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, worker.getUserName());
			ps.setString(2, worker.getPassword());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Integer workerId = rs.getInt(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);
				String email = rs.getString(4);
				String role = rs.getString(5);
				String userName = rs.getString(6);
				String password = rs.getString(7);

				switch (role) {

				case "Department manager":

					DepartmentManager departmentManager = new DepartmentManager(workerId, firstName, lastName, email,
							role, userName, password);
					GeneralParkWorkerList.add(departmentManager);

				case "Park manager":

					ParkManager ParkManager = new ParkManager(workerId, firstName, lastName, email, role, userName,
							password);
					GeneralParkWorkerList.add(ParkManager);

				case "Worker":

					ParkManager GeneralParkWorker = new ParkManager(workerId, firstName, lastName, email, role,
							userName, password);
					GeneralParkWorkerList.add(GeneralParkWorker);

				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return GeneralParkWorkerList; // This will return an empty list if there were no records found
	}
	
	
	   /**
     * Retrieves all orders for a given traveler from the database.
     * @param traveler The traveler whose orders are to be retrieved.
     * @return An ArrayList of Order objects.
     */
	public ArrayList<Order> getOrdersDataFromDatabase(Traveler traveler) {
		ArrayList<Order> orderDataForClient = new ArrayList<>();
		String query = "SELECT * FROM `order` WHERE travelerId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, traveler.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Integer orderID = rs.getInt(1);
				Integer parkNumber = rs.getInt(2);
				Integer amountOfVisitors = rs.getInt(3);
				Float price = rs.getFloat(4);
				String visitorEmail = rs.getString(5);
				LocalDate date = LocalDate.parse(rs.getString(6));
				LocalTime visitTime = LocalTime.parse(rs.getString(7));
				String status = rs.getString(9);

				Order order = new Order(orderID, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime,
						status);
				orderDataForClient.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return orderDataForClient; // This will return an empty list if there were no records found
	}
	
	


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

		 /**
	     * Inserts a new reservation (order) into the database.
	     * @param traveler The traveler making the reservation.
	     * @param order The details of the order being made.
	     * @return true if insertion was successful, false otherwise.
	     */
		public Boolean insertTravelerOrder(Order order) {
		    // Assuming the database schema aligns with the fields of the Order class
		    String query = "INSERT INTO orders (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, orderStatus, typeOfOrder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		    try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
		        // Set parameters based on the Order object fields
		        ps.setInt(1, order.getOrderId());
		        ps.setInt(2, order.getVisitorId());
		        ps.setInt(3, order.getParkNumber());
		        ps.setInt(4, order.getAmountOfVisitors());
		        ps.setFloat(5, order.getPrice());
		        ps.setString(6, order.getVisitorEmail());
		        ps.setDate(7, java.sql.Date.valueOf(order.getDate()));
		        ps.setTime(8, java.sql.Time.valueOf(order.getVisitTime()));
		        ps.setString(9, order.getOrderStatus()); // Directly use the enum's name as the DB value
		        ps.setString(10, order.getTypeOfOrder()); // Same here

		        int affectedRows = ps.executeUpdate();
		        if (affectedRows > 0) {
		            System.out.println("Reservation inserted successfully.");
		            return true;
		        } else {
		            System.out.println("A problem occurred and the reservation was not inserted.");
		            return false;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		}


	    /**
	     * Updates the status of an existing order in the database.
	     * @param order The order object containing the order ID and the new status.
	     * @return true if the update was successful, false otherwise.
	     */
		public Boolean updateOrderStatus(Order order) {
			String updateQuery = "UPDATE 'order' SET status = ? WHERE orderId = ?";

			try (PreparedStatement ps = connectionToDatabase.prepareStatement(updateQuery)) {
				ps.setString(1, order.getOrderStatus()); // Assuming getStatus() returns the new status of the order
				ps.setInt(2, order.getOrderId()); // Assuming getOrderId() returns the ID of the order to be updated

				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("Order status updated successfully.");
					return true;
				} else {
					System.out.println(
							"No order was found with the provided ID, or the status is already set to the new value.");
				}
			} catch (SQLException e) {
				System.out.println("An error occurred while updating the order status:");
				e.printStackTrace();
			}
			return false;
		}
		
		
	    /**
	     * Deletes an existing order from the database based on its ID.
	     * @param orderId The ID of the order to delete.
	     * @return true if the deletion was successful, false otherwise.
	     */
	    public Boolean deleteOrder(int orderId) {
	        String deleteQuery = "DELETE FROM `order` WHERE orderId = ?";

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(deleteQuery)) {
	            ps.setInt(1, orderId);

	            int affectedRows = ps.executeUpdate();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            System.out.println("An error occurred while deleting the order:");
	            e.printStackTrace();
	            return false;
	        }
	    }

}

	/**
	 * Retrieves specific order data from the database based on the order number.
	 *
	 * @param orderId The order number to search for.
	 * @return An ArrayList containing the park name and telephone number for the
	 *         specified order.
	 */
	/*
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
	*/




	/**
	 * Updates the telephone number and park name for a specific order in the
	 * database.
	 *
	 * @param orderId                 The ID of the order to update.
	 * @param parkNameToChange        The new park name.
	 * @param telephoneNumberToChange The new telephone number.
	 */
	/*
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

	
}
/*
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

*/
