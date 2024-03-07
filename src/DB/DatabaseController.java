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
	    // Ensure the query reflects your actual database table and column names
	    String query = "SELECT orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder FROM orders WHERE travelerId = ?";

	    try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	        ps.setInt(1, traveler.getId());
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Integer orderId = rs.getInt("orderId");
	            // Integer travelerId = rs.getInt("travelerId"); // Not used in the Order constructor directly
	            Integer parkNumber = rs.getInt("parkNumber");
	            Integer amountOfVisitors = rs.getInt("amountOfVisitors");
	            Float price = rs.getFloat("price");
	            String visitorEmail = rs.getString("visitorEmail");
	            LocalDate date = rs.getDate("date").toLocalDate();
	            String telephoneNumber = rs.getString("TelephoneNumber"); // Assuming you have a field to store this in Order
	            LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
	            String statusStr = rs.getString("orderStatus");
	            String typeOfOrderStr = rs.getString("typeOfOrder");

	            // Assuming Order constructor is updated to accept all the necessary fields including telephoneNumber
	            Order order = new Order(orderId, traveler.getId(), parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber);
	            orderDataForClient.add(order);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return orderDataForClient; // This will return an empty list if there were no records found or an error occurred
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
		    // Adjusting the query to match the database schema order provided
		    String query = "INSERT INTO orders (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		    try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
		        // Set parameters based on the Order object fields, in the order specified
		        ps.setInt(1, order.getOrderId());
		        ps.setInt(2, order.getVisitorId());
		        ps.setInt(3, order.getParkNumber());
		        ps.setInt(4, order.getAmountOfVisitors());
		        ps.setFloat(5, order.getPrice());
		        ps.setString(6, order.getVisitorEmail());
		        ps.setDate(7, java.sql.Date.valueOf(order.getDate()));
		        ps.setString(8, order.getTelephoneNumber()); // Assuming getTelephoneNumber() method exists
		        ps.setTime(9, java.sql.Time.valueOf(order.getVisitTime()));
		        ps.setString(10, order.getOrderStatus()); // Using the enum's name as the DB value
		        ps.setString(11, order.getTypeOfOrder()); // Similarly here

		        int affectedRows = ps.executeUpdate();
		        if (affectedRows > 0) {
		            System.out.println("Order inserted successfully.");
		            return true;
		        } else {
		            System.out.println("A problem occurred and the order was not inserted.");
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
	    
	    
	    public Boolean patchParkParameters(Park park) {
	        String query = "UPDATE parks SET name = ?, maxVisitors = ?, capacity = ?, currentVisitors = ?, location = ?, stayTime = ?, workersAmount = ?, managerId = ?, workingTime = ? WHERE parkNumber = ?";

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ps.setString(1, park.getName());
	            ps.setInt(2, park.getMaxVisitors());
	            ps.setInt(3, park.getCapacity());
	            ps.setInt(4, park.getCurrentVisitors());
	            ps.setString(5, park.getLocation());
	            ps.setInt(6, park.getStaytime());
	            ps.setInt(7, park.getWorkersAmount());
	            ps.setInt(8, park.getManager().getWorkerId()); 
	            ps.setInt(9, park.getWorkingTime());
	            ps.setInt(10, park.getParkNumber());

	            int affectedRows = ps.executeUpdate();
	            return affectedRows > 0;
	        } catch (SQLException e) {
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
