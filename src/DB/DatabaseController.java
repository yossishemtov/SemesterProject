package DB;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import common.*;
import common.worker.*;
import common.worker.Report.ReportType;

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

	public List<HourlyVisitData> getHourlyVisitDataForPark(int parkNumber) {
		// Initialize a List to hold HourlyVisitData objects
		List<HourlyVisitData> hourlyDataList = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			hourlyDataList.add(new HourlyVisitData(String.format("%02d:00", i), 0, 0, 0));
		}

		String query = "SELECT HOUR(v.enteringTime) AS hour, o.typeOfOrder, COUNT(*) AS count "
				+ "FROM visit v JOIN `order` o ON v.orderNumber = o.orderId "
				+ "WHERE v.parkNumber = ? AND o.parkNumber = ? " + "GROUP BY HOUR(v.enteringTime), o.typeOfOrder";

		try (PreparedStatement preparedStatement = connectionToDatabase.prepareStatement(query)) {
			preparedStatement.setInt(1, parkNumber);
			preparedStatement.setInt(2, parkNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int hour = resultSet.getInt("hour");
				String typeOfOrder = resultSet.getString("typeOfOrder");
				int count = resultSet.getInt("count");

				// Get the corresponding HourlyVisitData object from the list
				HourlyVisitData hourlyData = hourlyDataList.get(hour);

				// Update the HourlyVisitData object based on typeOfOrder
				switch (typeOfOrder) {
				case "GUIDEDGROUP":
					hourlyData.setGuidedGroupCount(count);
					break;
				case "FAMILY":
					hourlyData.setFamilyCount(count);
					break;
				case "SOLO":
					hourlyData.setSoloCount(count);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Optionally, handle exception, e.g., log error or throw a custom exception
		}

		return hourlyDataList;
	}

	public boolean insertChangeRequest(ChangeRequest request) {
		String query = "INSERT INTO `changerequests` (parkName, parkNumber, capacity, gap, staytime, approved) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {

			ps.setString(1, request.getParkName());
			ps.setInt(2, request.getParkNumber());
			ps.setInt(3, request.getCapacity());
			ps.setInt(4, request.getGap());
			ps.setInt(5, request.getStaytime());
			ps.setString(6, request.getApproved()); // Assuming you handle the enum to string conversion

			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<ChangeRequest> getChangeRequestsWaitingForApproval(GeneralParkWorker worker) {
		ArrayList<ChangeRequest> requests = new ArrayList<>();
		String query = "SELECT * FROM `changerequests` WHERE parkNumber = ? AND approved = 'WAITING_FOR_APPROVAL'";

		// Determine the query based on the worker's role
		if ("Department Manager".equals(worker.getRole())) {
			// For department managers: fetch requests waiting for approval for a specific
			// park
			query = "SELECT * FROM `changerequests` WHERE parkNumber = ? AND approved = 'WAITING_FOR_APPROVAL'";
		} else if ("Park Manager".equals(worker.getRole())) {
			// For park managers: fetch all requests, regardless of approval status
			query = "SELECT * FROM `changerequests` WHERE parkNumber = ?";
		}
		System.out.println(query);
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {

			ps.setInt(1, worker.getWorksAtPark());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ChangeRequest request = new ChangeRequest(rs.getInt("id"), rs.getString("parkName"),
							rs.getInt("parkNumber"), rs.getInt("capacity"), rs.getInt("gap"), rs.getInt("staytime"),
							rs.getString("approved"));
					requests.add(request);
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException occurred");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General exception occurred");
			e.printStackTrace();
		}

		System.out.println("not error in");

		return requests.isEmpty() ? null : requests; // Return null if the list is empty
	}

	public boolean updateChangeRequestStatus(ChangeRequest request) {
		String query = "UPDATE `changerequests` SET approved = ? WHERE id = ?";
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {

			ps.setString(1, request.getApproved()); // Use the request's approved status
			ps.setInt(2, request.getId()); // Use the request's id

			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Updates a traveler's record to mark them as a guide.
	 *
	 * @param travelerId the ID of the traveler to be updated.
	 * @return true if the update is successful, false otherwise.
	 */
	public boolean ChangeTravelerToGuide(Traveler traveler) {

		String query = "UPDATE `travler` SET GroupGuide = 1 WHERE id = ?";

		try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
			pstmt.setInt(1, traveler.getId());

			int affectedRows = pstmt.executeUpdate();

			// Check if the update was successful
			return affectedRows > 0;
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			return false;
		}
	}

	public boolean insertNewGroupGuide(Traveler traveler) {
		// Assuming you have a method getConnection() that returns a Connection object.

		String query = "INSERT INTO `travler` (id, firstName, lastName, email, phoneNumber, GroupGuide, isloggedin) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
			pstmt.setInt(1, traveler.getId());
			pstmt.setString(2, traveler.getFirstName());
			pstmt.setString(3, traveler.getLastName());
			pstmt.setString(4, traveler.getEmail());
			pstmt.setString(5, traveler.getPhoneNum());
			pstmt.setInt(6, traveler.getIsGroupGuide()); // Assuming 1 for guide, 0 for not a guide.
			pstmt.setInt(7, 0); // Assuming isloggedin default to 0.

			int affectedRows = pstmt.executeUpdate();

			// Check if the insert was successful
			if (affectedRows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}

		return false;
	}

	// Method to get a Park object by parkNumber
	public Park getParkDetails(int parkNumber) {
		Park park = null;
		String query = "SELECT * FROM `park` WHERE parkNumber = ?";

		try (PreparedStatement preparedStatement = connectionToDatabase.prepareStatement(query)) {
			preparedStatement.setInt(1, parkNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				System.out.println("in");

				String name = resultSet.getString("name");
				Integer maxVisitors = resultSet.getInt("maxVisitors");
				Integer capacity = resultSet.getInt("capacity");
				Integer currentVisitors = resultSet.getInt("currentVisitors");
				String location = resultSet.getString("location");
				Integer staytime = resultSet.getInt("staytime");
				Integer workersAmount = resultSet.getInt("workersAmount");
				Integer managerID = resultSet.getInt("managerId");
				Integer workingTime = resultSet.getInt("workingTime");
				Integer gap = resultSet.getInt("gap"); // Retrieve gap from resultSet

				park = new Park(name, parkNumber, maxVisitors, capacity, currentVisitors, location, staytime,
						workersAmount, gap, managerID, workingTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return park;
	}

	public Traveler getTravelerDetails(Traveler travelerFromClient) {
		String query = "SELECT firstName, lastName, email, phoneNumber, GroupGuide, isloggedin FROM `travler` WHERE id = ?";
		Traveler traveler = null; // Initialize to null

		try (PreparedStatement preparedStatement = connectionToDatabase.prepareStatement(query)) {
			preparedStatement.setInt(1, travelerFromClient.getId());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					// Instantiate a new Traveler object with the retrieved details
					String firstName = resultSet.getString("firstName");
					String lastName = resultSet.getString("lastName");
					String email = resultSet.getString("email");
					String phoneNum = resultSet.getString("phoneNumber");
					Integer isGrupGuide = resultSet.getInt("GroupGuide");
					Integer isloggedin = resultSet.getInt("isloggedin"); // Fetch isloggedin status
					traveler = new Traveler(travelerFromClient.getId(), firstName, lastName, email, phoneNum,
							isGrupGuide, isloggedin);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Consider better exception handling
		}
		return traveler; // Returns the new Traveler object or null if not found
	}

	// Get GeneralParkWorkerDetails
	// check GetGeneralParkWorker login details and return the data,if not exist
	// return empty ArrayList of type generalParkWorker
	// WorkerId | firstName | lastName | email | role | userName | password |
	// worksAtPark
	public GeneralParkWorker getGeneralParkWorkerDetails(GeneralParkWorker worker) {
		// Removed unused ArrayList<Order>
		System.out.println("in db");
		System.out.println(worker.getPassword() + worker.getUserName());
		GeneralParkWorker generalParkWorker = null;

		String query = "SELECT * FROM `generalparkworker` WHERE userName = ? AND password= ?";

		try (PreparedStatement preparedStatementInstance = connectionToDatabase.prepareStatement(query)) {
			preparedStatementInstance.setString(1, worker.getUserName());
			preparedStatementInstance.setString(2, worker.getPassword());
			ResultSet returnedStatement = preparedStatementInstance.executeQuery();

			while (returnedStatement.next()) {

				Integer workerId = returnedStatement.getInt(1);
				String firstName = returnedStatement.getString(2);
				String lastName = returnedStatement.getString(3);
				String email = returnedStatement.getString(4);
				String role = returnedStatement.getString(5);
				String userName = returnedStatement.getString(6);
				String password = returnedStatement.getString(7);
				Integer worksAtPark = returnedStatement.getInt(8);
				System.out.println(role);

				generalParkWorker = new GeneralParkWorker(workerId, firstName, lastName, email, role, userName,
						password, worksAtPark);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null; // Consider throwing a custom exception
		}

		System.out.println(generalParkWorker.toString());

		return generalParkWorker; // This will return an empty list if there were no records found
	}

	/**
	 * Retrieves all orders for a given traveler from the database.
	 * 
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
				// Integer travelerId = rs.getInt("travelerId"); // Not used in the Order
				// constructor directly
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				String telephoneNumber = rs.getString("TelephoneNumber"); // Assuming you have a field to store this in
																			// Order
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");

				// Assuming Order constructor is updated to accept all the necessary fields
				// including telephoneNumber
				Order order = new Order(orderId, traveler.getId(), parkNumber, amountOfVisitors, price, visitorEmail,
						date, visitTime, statusStr, typeOfOrderStr, telephoneNumber);
				orderDataForClient.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderDataForClient; // This will return an empty list if there were no records found or an error
									// occurred
	}

	/**
	 * Inserts a new reservation (order) into the database.
	 * 
	 * @param traveler The traveler making the reservation.
	 * @param order    The details of the order being made.
	 * @return true if insertion was successful, false otherwise.
	 */
	public Boolean insertTravelerOrder(Order order) {
		// Adjusting the query to match the database schema order provided
		String query = "INSERT INTO `order` (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
	 * 
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
	 * 
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

	public Boolean patchParkParameters(ChangeRequest changeRequest) {
		// Assuming capacity is a column in your database that should be updated based
		// on gap
		// Calculate the new capacity based on the provided gap and maxVisitors
		Integer newMaxVisitor=changeRequest.getCapacity()-changeRequest.getGap();
		// Update only the fields that are affected by a change request
		String query = "UPDATE `park` SET capacity = ?, stayTime = ? ,gap = ? , maxVisitors = ? WHERE parkNumber = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, changeRequest.getCapacity());
			ps.setInt(2, changeRequest.getStaytime());
			ps.setInt(3, changeRequest.getGap());
			ps.setInt(4, newMaxVisitor);
			ps.setInt(5, changeRequest.getParkNumber());
		
		       // Print query for debugging
	        System.out.println("Executing update: " + ps);
	        
	        int affectedRows = ps.executeUpdate();

	        // Check and print the number of affected rows
	        System.out.println(affectedRows + " rows affected.");

	        return affectedRows > 0;
	    } catch (SQLException e) {
	        System.err.println("SQL Exception: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Updates the status of generalparkworker to signedin
	 * 
	 * @param GeneralParkWorker to sign
	 * @return true if the signedin was successful, false otherwise.
	 */
	public Boolean changeSingedInOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
		String query = "UPDATE generalparkworker SET isloggedin = 1 WHERE workerid = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedParkWorker.getWorkerId());
			int affectedRows = ps.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Updates the status of generalparkworker to signedout
	 * 
	 * @param GeneralParkWorker to sign
	 * @return true if the signed out was successful, false otherwise.
	 */
	public Boolean changeSignedOutOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
		String query = "UPDATE `generalparkworker` SET isloggedin = 0 WHERE workerid = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedParkWorker.getWorkerId());
			int affectedRows = ps.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Gets the status of loggedin of generalparkworker
	 * 
	 * @param GeneralParkWorker
	 * @return isloggedin of generalparkworker
	 */
	public Boolean getSignedinStatusOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
		// Return the status of isloggedin of generalparkworker
		String query = "SELECT isloggedin FROM generalparkworker WHERE workerid = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedParkWorker.getWorkerId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				// Retrieve the value of isloggedin from the ResultSet
				int isLoggedIn = rs.getInt("isloggedin");
				System.out.print(isLoggedIn);
				if (isLoggedIn == 0)
					return false;

				return true;
			} else {
				// No rows returned, worker not found or not signed in
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
	    
	   
	    
	    /**
	     * Updates the status of Traveler to signedin
	     * @param Traveler to signin
	     * @return true if the signedin was successful, false otherwise.
	     */
	    public Boolean changedSignedInOfTraveler(Traveler signedTraveler) {
	    	String query = "UPDATE travler SET isloggedin = 1 WHERE id = ?";
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedTraveler.getId());
	    		int affectedRows = ps.executeUpdate();
	    		
	    		return affectedRows > 0;
	    		
	    	}catch(SQLException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    }
	    
	    
	    /**
	     * Updates the status of Traveler to signout
	     * @param Traveler to signout
	     * @return true if the signedout was successful, false otherwise.
	     */
	    public Boolean changedSignedOutOfTraveler(Traveler signedTraveler) {
	    	String query = "UPDATE travler SET isloggedin = 0 WHERE id = ?";
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedTraveler.getId());
	    		int affectedRows = ps.executeUpdate();
	    		
	    		return affectedRows > 0;
	    		
	    	}catch(SQLException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    }
	    
	    
	    
	    
	    /**
	     * Gets the status of loggedin of Traveler
	     * @param GeneralParkWorker
	     * @return Boolean isloggedin of Traveler
	     */  	
	    public Boolean getSignedinStatusOfTraveler(Traveler signedTraveler) {
	    	//Return the status of isloggedin of Traveler
	    	String query = "SELECT isloggedin FROM travler WHERE id = ?";
	     
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedTraveler.getId());
	    		ResultSet rs = ps.executeQuery();
	    		
	    		if (rs.next()) {
	                // Retrieve the value of isloggedin from the ResultSet
	                int isLoggedIn = rs.getInt("isloggedin");
	                
	                 if(isLoggedIn == 0)
	                	 return false;
	                 
	                 return true;
	            } else {
	                // No rows returned, worker not found or not signed in
	                return true;
	            }
	    		
	    	}catch(SQLException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    	
	    	
	    }
	    
	    /**
	     * Get order information and change its state to INPARK
	     * @param Order object that contains a valid orderId
	     * @return True if success or False if not
	     */	
	    public Boolean patchOrderStatusToInpark(Order receivedOrder) {
	    	String query = "UPDATE `order` SET orderStatus = 'INPARK' WHERE orderId = ?";
	    	
	    	
	    	try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
	            pstmt.setInt(1, receivedOrder.getOrderId());

	            int affectedRows = pstmt.executeUpdate();

	            // Check if the update was successful
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            System.err.println("SQLException: " + e.getMessage());
	            return false;
	        } 

	    }
	    
	    /**
	     * Change park current amount of visitors
	     * @param Park object with new amount of visitors
	     * @return True if success or False if not
	     */	
	    public Boolean patchParkVisitorsNumberAppend(Park parkToAppend) {
	    	//Append the visitors to the park currentvisitors
	    	
	    	String query = "UPDATE `park` SET currentVisitors = ? WHERE parkNumber = ?";
	    	
	    	try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
	            pstmt.setInt(1, parkToAppend.getCurrentVisitors());
	            pstmt.setInt(2, parkToAppend.getParkNumber());

	            int affectedRows = pstmt.executeUpdate();

	            // Check if the update was successful
	            return affectedRows > 0;
	            
	        } catch (SQLException e) {
	            System.err.println("SQLException: " + e.getMessage());
	            return false;
	        } 
	    }
	    
	    /**
	     * Insert a visit based on provided Visit object
	     * @param Visit object to insert
	     * @return True if success or False if not
	     */	
	    public Boolean addNewVisit(Visit newVisitToAdd) {
	    	String query = "INSERT INTO `visit` (visitDate, enteringTime, exitingTime, parkNumber, orderId) VALUES (?, ?, ?, ?, ?)";

		    try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
		        // Set parameters based on the Order object fields, in the order specified
		        ps.setDate(1, java.sql.Date.valueOf(newVisitToAdd.getVisitDate()));
		        ps.setTime(2, java.sql.Time.valueOf(newVisitToAdd.getEnteringTime()));
		        ps.setTime(3, java.sql.Time.valueOf(newVisitToAdd.getExistingTime()));
		        ps.setInt(4, newVisitToAdd.getParkNumber());
		        ps.setFloat(5, newVisitToAdd.getOrderId());

		        int affectedRows = ps.executeUpdate();
		        if (affectedRows > 0) {
		            return true;
		        } else {
		            return false;
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
	    }
	    	
	    /**
	     * Get order information based on the orderId, park number and date provided
	     * @param Order object that contains a valid orderId, parknumber and date
	     * @return Order object containing all the information about the order
	     */	
	    public Order getOrderInformationByOrderIdAndParkNumber(Order receivedOrderId) {
	    	String query = "SELECT * FROM `order` WHERE orderId = ? AND parkNumber = ? AND date = ?";
	    	Order orderInformation = null;
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	    		ps.setInt(1, receivedOrderId.getOrderId());
	    		ps.setInt(2, receivedOrderId.getParkNumber());
	    		ps.setObject(3, java.sql.Date.valueOf(receivedOrderId.getDate()));
	            ResultSet rs = ps.executeQuery(); // Use executeQuery for SELECT statements
	            
	            
	  
	                if (rs.next()) { // Use if instead of while if you expect or require a single result
	                	
	                	//Parsing special object such of type localDate and LocalTime
	    	            LocalDate orderDate = rs.getObject("date", LocalDate.class);
	    	            LocalTime visitTime = rs.getObject("visitTime", LocalTime.class);
	    	            float price = rs.getFloat("price");
	    	            
	                	orderInformation = new Order(
	                        rs.getInt("orderId"), 
	                        rs.getInt("travlerId"), 
	                        rs.getInt("parkNumber"), 
	                        rs.getInt("amountOfVisitors"), 
	                        price, 
	                        rs.getString("visitorEmail"), 
	                        orderDate, 
	                        visitTime, // Assuming you have a column for gap in your DB
	                        rs.getString("orderStatus"), // Assuming managerID is stored directly as an integer
	                        rs.getString("typeOfOrder"),
	                        rs.getString("TelephoneNumber")
	                    );
	                }
	            }
	         catch (SQLException e) {
	            e.printStackTrace();
	            return orderInformation;
	            // Consider logging this exception or handling it more gracefully
	        }
	    	
	    	return orderInformation;
	    }
	    
	    
	    /**
	     * Gets the amount of visitors in the park where the parkworker works at
	     * @param parkworker the park worker information
	     * @return park information if successful and null if not found
	     */                    
	public Park getAmountOfVisitorsByParkWorker(GeneralParkWorker parkworker) {
		// Querying for the park information with the park number associated with the
		// park worker
		String query = "Select * FROM park WHERE parkNumber = ?";
		Park fetchedPark = null;

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, parkworker.getWorksAtPark());
			ResultSet rs = ps.executeQuery(); // Use executeQuery for SELECT statements

			if (rs.next()) { // Use if instead of while if you expect or require a single result
				fetchedPark = new Park(rs.getString("name"), rs.getInt("parkNumber"), rs.getInt("maxVisitors"),
						rs.getInt("capacity"), rs.getInt("currentVisitors"), rs.getString("location"),
						rs.getInt("staytime"), rs.getInt("workersAmount"), rs.getInt("gap"), // Assuming you have a
																								// column for gap in
																								// your DB
						rs.getInt("managerID"), // Assuming managerID is stored directly as an integer
						rs.getInt("workingTime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Consider logging this exception or handling it more gracefully
		}

		return fetchedPark;
	}

	public Park getAmountOfVisitors(GeneralParkWorker worker) {
		String query = "SELECT * FROM park WHERE parkNumber = ?";

		Park park = null;
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, worker.getWorksAtPark());
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					park = new Park(rs.getString("name"), rs.getInt("parkNumber"), rs.getInt("maxVisitors"),
							rs.getInt("capacity"), rs.getInt("currentVisitors"), rs.getString("location"),
							rs.getInt("staytime"), rs.getInt("workersAmount"), rs.getInt("gap"), rs.getInt("managerID"),
							rs.getInt("workingTime"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return park;
	}

	public VisitorsReport getNewVisitorsReport(VisitorsReport visitReport) {
        System.out.println("in db getNewVisitorsReport...");
        System.out.println(visitReport.toString());

        String query = "SELECT typeOfOrder, SUM(amountOfVisitors) AS totalVisitors "
                + "FROM `order` "
                + "WHERE parkNumber = ? AND YEAR(date) = ? AND MONTH(date) = ? AND orderStatus = 'COMPLETED' "
                + "GROUP BY typeOfOrder";


	    // Initialize variables for the report
	    Integer totalIndividuals = 0, totalGroups = 0, totalFamilies = 0, totalVisitors = 0;
	    Integer parkNumber = visitReport.getParkID(); // Assuming VisitReport has this method
	    LocalDate reportDate = visitReport.getDate(); // Assuming VisitReport has this method
        System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: " + reportDate.getYear() + ", Month: " + reportDate.getMonthValue());

	    // Prepare the database query
	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        statement.setInt(1, parkNumber);
	        statement.setInt(2, reportDate.getYear());
	        statement.setInt(3, visitReport.getMonth());
	        //statement.setInt(3, reportDate.getMonthValue());
	        System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: " + reportDate.getYear() + ", Month: " + reportDate.getMonthValue());


	        // Execute the query and process the results
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                String typeOfOrder = resultSet.getString("typeOfOrder").trim();
	                int visitors = resultSet.getInt("totalVisitors");
	                
	                switch (typeOfOrder.toUpperCase()) {
	                    case "SOLO":
	                        totalIndividuals += visitors;
	                        break;
	                    case "GUIDEDGROUP":
	                        totalGroups += visitors;
	                        break;
	                    case "FAMILY":
	                        totalFamilies += visitors;
	                        break;
	                    default:
	                        // Optionally handle unexpected values
	                        break;
	                }
	            }
	            totalVisitors = totalIndividuals + totalGroups + totalFamilies;
	        }
	    } catch (SQLException e) {
	        System.err.println("An error occurred while fetching the total number of visitors report: " + e.getMessage());
	        e.printStackTrace();
	    }

	    // Construct and return the report
	    int month = reportDate.getMonthValue(); // Extract month from reportDate
	    String comment = "Automatically generated report based on month and park ID.";
	    
	    // Corrected to match the constructor's parameters
	    VisitorsReport report = new VisitorsReport(-1, Report.ReportType.VISITOR, parkNumber, reportDate, month, comment, totalIndividuals, totalGroups, totalFamilies, totalVisitors);
	    System.out.println(report.toString());
	    return report;
	}
	
	



	public boolean insertVisitorReport(VisitorsReport report) {
	    // First, insert into the 'report' table.
        System.out.println("in db insert...");

        String insertReportSql = "INSERT INTO `report` (reportType, parkID, date, month, comment) VALUES (?, ?, ?, ?, ?)";
	    // Assuming 'visitorsreports' is the correct table name and it also includes a reference to 'reportID'.
	    String insertVisitorsReportSql = "INSERT INTO `VisitorsReport` (reportID, parkNumber, totalIndividualVisitors, totalGroupVisitors, totalFamilyVisitors, totalVisitors) VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement statementReport = connectionToDatabase.prepareStatement(insertReportSql, Statement.RETURN_GENERATED_KEYS);
	         PreparedStatement statementVisitorsReport = connectionToDatabase.prepareStatement(insertVisitorsReportSql)) {
 
	    	// Set values for the report table insert
	    	statementReport.setString(1, report.getReportType().toString()); // Assuming you have a method to get the report type as String
	    	statementReport.setInt(2, report.getParkID());
	    	statementReport.setDate(3, java.sql.Date.valueOf(report.getDate()));
	    	statementReport.setInt(4, report.getMonth()); // Assuming you have a method to get the month as an int
	    	statementReport.setString(5, report.getComment());


	        System.out.println("Executing report table insert...");
	        // Execute the report table insert and get the generated key.
	        int rowsInsertedReport = statementReport.executeUpdate();

	        if (rowsInsertedReport > 0) {
	            try (ResultSet generatedKeys = statementReport.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    long reportId = generatedKeys.getLong(1);
	                    System.out.println("Report inserted with ID: " + reportId);

	                    // Set values for the visitorsreports table insert, using the generated reportID.
	                    statementVisitorsReport.setLong(1, reportId);
	                    statementVisitorsReport.setInt(2, report.getParkID());
	                    statementVisitorsReport.setInt(3, report.getTotalIndividualVisitors());
	                    statementVisitorsReport.setInt(4, report.getTotalGroupVisitors());
	                    statementVisitorsReport.setInt(5, report.getTotalFamilyVisitors());
	                    statementVisitorsReport.setInt(6, report.getTotalVisitors());

	                    System.out.println("Executing visitorsreports table insert...");
	                    // Execute the visitorsreports table insert.
	                    int rowsInsertedVisitorsReport = statementVisitorsReport.executeUpdate();
	                    if (rowsInsertedVisitorsReport > 0) {
	                        System.out.println("A new visitor report was inserted successfully into both tables!");
	                        return true;
	                    } else {
	                        System.out.println("Failed to insert into visitorsreports table.");
	                    }
	                } else {
	                    System.out.println("No ID was generated for the report insert.");
	                }
	            }
	        } else {
	            System.out.println("Failed to insert into report table.");
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL exception occurred during the insertVisitorReport operation: ");
	        e.printStackTrace();
	    }
	    return false;
	}



	public VisitorsReport getVisitorsReportByReportId(Report inputReport) {
	    System.out.println("in db getVisitorsReportByReportId...");

	    VisitorsReport visitorsReport = null;
	    // Query to select visitor report details based on the reportID from the Report object
	    String query = "SELECT vr.reportID, vr.parkNumber, vr.totalIndividualVisitors, vr.totalGroupVisitors, vr.totalFamilyVisitors, vr.totalVisitors, r.reportType, r.date, r.month, r.comment "
	            + "FROM `VisitorsReport` vr JOIN `report` r ON vr.reportID = r.reportID "
	            + "WHERE vr.reportID = ?";
	    System.out.println("Attempting to retrieve VisitorsReport for Report ID: " + inputReport.getReportID());

	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        // Set the reportID from the Report object into the query
	        statement.setInt(1, inputReport.getReportID());

	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                Integer reportID = resultSet.getInt("reportID");
	                Integer parkNumber = resultSet.getInt("parkNumber");
	                Integer totalIndividualVisitors = resultSet.getInt("totalIndividualVisitors");
	                Integer totalGroupVisitors = resultSet.getInt("totalGroupVisitors");
	                Integer totalFamilyVisitors = resultSet.getInt("totalFamilyVisitors");
	                Integer totalVisitors = resultSet.getInt("totalVisitors");
	                String reportTypeStr = resultSet.getString("reportType");
	                Report.ReportType reportType = Report.ReportType.valueOf(reportTypeStr); // Convert string to enum
	                LocalDate date = resultSet.getDate("date").toLocalDate();
	                int month = resultSet.getInt("month"); // Assuming `month` is stored as an INT in the database
	                String comment = resultSet.getString("comment");

	                // Construct a new VisitorsReport object using the fetched data
	                visitorsReport = new VisitorsReport(reportID, reportType, parkNumber, date, month, comment, totalIndividualVisitors, totalGroupVisitors, totalFamilyVisitors, totalVisitors);
	            } else {
	                System.out.println("No VisitorsReport found in database for Report ID: " + inputReport.getReportID());
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("An error occurred while fetching the visitors report: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return visitorsReport;
	}

	
	public List<Report> getGeneralReportsByParkId(int parkId) {
	    List<Report> reports = new ArrayList<>();
	    // Include 'month' in your SELECT query
	    String query = "SELECT reportID, reportType, parkID, date, month, comment FROM `Report` WHERE parkID = ?";

	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        statement.setInt(1, parkId);

	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                int reportID = resultSet.getInt("reportID");
	                String reportTypeStr = resultSet.getString("reportType");
	                Report.ReportType reportType = Report.ReportType.valueOf(reportTypeStr); // Convert the string to enum
	                int parkID = resultSet.getInt("parkID");
	                LocalDate date = resultSet.getDate("date").toLocalDate();
	                int month = resultSet.getInt("month");
	                String comment = resultSet.getString("comment");

	                // Create a new Report object with the fetched data, including month
	                Report report = new Report(reportID, reportType, parkID, date, month, comment);
	                reports.add(report);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("An error occurred while fetching reports: " + e.getMessage());
	        e.printStackTrace();
	    } catch (IllegalArgumentException e) {
	        System.err.println("An error occurred with enum conversion: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return reports;
	}
	
	
	public UsageReport getNewUsageReport(UsageReport usageReport) {
	    System.out.println("In db getNewUsageReport...");
	    System.out.println(usageReport.toString());

	    UsageReport newReport = null;
	    String query = "SELECT DAY(date) AS dayOfMonth, SUM(amountOfVisitors) AS dailyVisitors "
	            + "FROM `order` "
	            + "WHERE parkNumber = ? AND YEAR(date) = ? AND MONTH(date) = ? AND orderStatus = 'COMPLETED' "
	            + "GROUP BY DAY(date)";

	    Map<Integer, Integer> dailyUsage = new HashMap<>();
	    Integer parkNumber = usageReport.getParkID();
	    LocalDate reportDate = usageReport.getDate();

	    // Initialize the dailyUsage map with all days of the month set to 0
	    LocalDate startOfMonth = LocalDate.of(reportDate.getYear(), usageReport.getMonth(), 1);
	    LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
	    for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
	        dailyUsage.put(date.getDayOfMonth(), 0);
	    }

	    int parkCapacity = usageReport.getParkCapacity();
	    System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: " + reportDate.getYear() + ", Month: " + usageReport.getMonth());

	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        statement.setInt(1, parkNumber);
	        statement.setInt(2, reportDate.getYear());
	        statement.setInt(3, usageReport.getMonth());

	        System.out.println("Query Prepared: " + statement.toString()); // This line helps to see the prepared statement.

	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                int dayOfMonth = resultSet.getInt("dayOfMonth");
	                int dailyVisitors = resultSet.getInt("dailyVisitors");

	                System.out.println("Day: " + dayOfMonth + ", Visitors: " + dailyVisitors); // Diagnostic print

	                // Check if park was fully occupied on this day
	                boolean isFull = dailyVisitors >= parkCapacity;
	                // Instead of storing a boolean, store the visitor count or 0 to indicate full/not full
	                dailyUsage.put(dayOfMonth, isFull ? parkCapacity : dailyVisitors);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("An error occurred while fetching the usage report: " + e.getMessage());
	        e.printStackTrace();
	    }

	    // Diagnostic print to check map contents after attempting to populate it
	    System.out.println("Daily Usage Map: " + dailyUsage);

	    // Prepare additional fields for the report
	    int month = usageReport.getMonth();
	    String comment = "Usage report generated for the month, showing daily visitor count and full occupancy days.";

	    // Use corrected fields and constructor
	    newReport = new UsageReport(-1, UsageReport.ReportType.USAGE, parkNumber, reportDate, month, comment, dailyUsage, parkCapacity);
	    System.out.println(newReport.toString());
	    return newReport;
	}

	
	public boolean insertUsageReport(UsageReport report) {
	    // First, insert into the 'report' table.
	    System.out.println("in db insert...");

	    String insertReportSql = "INSERT INTO `report` (reportType, parkID, date, month, comment) VALUES (?, ?, ?, ?, ?)";
	    // Assuming 'usagereport' is the correct table name and it also includes a reference to 'reportID'.
	    String insertUsageReportSql = "INSERT INTO `UsageReport` (reportID, parkNumber, parkCapacity, dailyUsage) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement statementReport = connectionToDatabase.prepareStatement(insertReportSql, Statement.RETURN_GENERATED_KEYS);
	         PreparedStatement statementUsageReport = connectionToDatabase.prepareStatement(insertUsageReportSql)) {

	        // Set values for the report table insert
	        statementReport.setString(1, report.getReportType().toString());
	        statementReport.setInt(2, report.getParkID());
	        statementReport.setDate(3, java.sql.Date.valueOf(report.getDate()));
	        statementReport.setInt(4, report.getMonth());
	        statementReport.setString(5, report.getComment());

	        System.out.println("Executing report table insert...");
	        // Execute the report table insert and get the generated key.
	        int rowsInsertedReport = statementReport.executeUpdate();

	        if (rowsInsertedReport > 0) {
	            try (ResultSet generatedKeys = statementReport.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    long reportId = generatedKeys.getLong(1);
	                    System.out.println("Report inserted with ID: " + reportId);

	                    // Convert dailyUsage map to JSON string for storage
	                    String dailyUsageJson = new Gson().toJson(report.getDailyUsage());
	            	    System.out.println(dailyUsageJson);

	                    // Set values for the usagereport table insert, using the generated reportID.
	                    statementUsageReport.setLong(1, reportId);
	                    statementUsageReport.setInt(2, report.getParkID());
	                    statementUsageReport.setInt(3, report.getParkCapacity());
	                    statementUsageReport.setString(4, dailyUsageJson);

	                    System.out.println("Executing usagereport table insert...");
	                    // Execute the usagereport table insert.
	                    int rowsInsertedUsageReport = statementUsageReport.executeUpdate();
	                    if (rowsInsertedUsageReport > 0) {
	                        System.out.println("A new usage report was inserted successfully into both tables!");
	                        return true;
	                    } else {
	                        System.out.println("Failed to insert into usagereport table.");
	                    }
	                } else {
	                    System.out.println("No ID was generated for the report insert.");
	                }
	            }
	        } else {
	            System.out.println("Failed to insert into report table.");
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL exception occurred during the insertUsageReport operation: ");
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public UsageReport getUsageReportByReportId(Report inputReport) {
	    System.out.println("in db getUsageReportByReportId...");

	    UsageReport usageReport = null;
	    String query = "SELECT ur.reportID, ur.parkNumber, ur.parkCapacity, ur.dailyUsage, r.reportType, r.date, r.month, r.comment "
	            + "FROM `UsageReport` ur JOIN `report` r ON ur.reportID = r.reportID "
	            + "WHERE ur.reportID = ?";

	    System.out.println("Attempting to retrieve UsageReport for Report ID: " + inputReport.getReportID());

	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        statement.setInt(1, inputReport.getReportID());

	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                Integer reportID = resultSet.getInt("reportID");
	                Integer parkNumber = resultSet.getInt("parkNumber");
	                LocalDate date = resultSet.getDate("date").toLocalDate();
	                int month = resultSet.getInt("month");
	                int parkCapacity = resultSet.getInt("parkCapacity");
	                String dailyUsageJson = resultSet.getString("dailyUsage");
	                String reportTypeStr = resultSet.getString("reportType");
	                Report.ReportType reportType = Report.ReportType.valueOf(reportTypeStr); // Convert string to enum
	                String comment = resultSet.getString("comment");

	                Gson gson = new Gson();
	                // Assuming dailyUsage is a Map<Integer, Integer>
	                Map<Integer, Integer> dailyUsage = gson.fromJson(dailyUsageJson, new TypeToken<Map<Integer, Integer>>() {}.getType());

	                // Construct a new UsageReport object using the fetched data
	                usageReport = new UsageReport(reportID, reportType, parkNumber, date, month, comment, dailyUsage, parkCapacity);
	            } else {
	                System.out.println("No UsageReport found in database for Report ID: " + inputReport.getReportID());
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("An error occurred while fetching the usage report: " + e.getMessage());
	        e.printStackTrace();
	    } catch (JsonSyntaxException e) {
	        System.err.println("An error occurred while parsing the daily usage JSON: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return usageReport;
	}






}
