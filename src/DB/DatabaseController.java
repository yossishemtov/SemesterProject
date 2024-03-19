package DB;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;
import common.Counter;
import common.worker.*;
import ocsf.server.ConnectionToClient;


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
    public Park getParkDetails(Integer parkNumber) {
    	Park park = null;
		String query = "SELECT * FROM park WHERE parkNumber = ? ";

        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
            // ps.setInt(1, traveler.getId()); גרסא נכונה
			ps.setInt(1, parkNumber);  
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
            	String name = rs.getString("name");
	            Integer parkid = rs.getInt("parkNumber"); 
	            Integer maxVisitors = rs.getInt("maxVisitors");
	            Integer capacity = rs.getInt("capacity");
	            Integer currentVisitors = rs.getInt("currentVisitors");
	            String location = rs.getString("location");
	            Integer stayTime = rs.getInt("staytime");
	            Integer workersAmount = rs.getInt("workersAmount"); 
	            Integer managerId = rs.getInt("managerId");
	            Integer workingTime = rs.getInt("workingTime");
	            Integer gap = rs.getInt("gap");

	            park = new Park(name, parkid, maxVisitors, capacity, currentVisitors, location, stayTime
	            		,workersAmount, gap , managerId, workingTime);	            
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
                    traveler = new Traveler(travelerFromClient.getId(), firstName, lastName, email, phoneNum, isGrupGuide, isloggedin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider better exception handling
        }
        return traveler; // Returns the new Traveler object or null if not found
    }

	// Get GeneralParkWorkerDetails
	// check GetGeneralParkWorker login details and return the data,if not exist return empty ArrayList of type generalParkWorker
	// WorkerId | firstName | lastName | email | role | userName | password | worksAtPark
	public GeneralParkWorker getGeneralParkWorkerDetails(GeneralParkWorker worker) {
	    // Removed unused ArrayList<Order>
	    System.out.println("in db");
	    System.out.println(worker.getPassword()+worker.getUserName());
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
	            
                generalParkWorker = new GeneralParkWorker(workerId, firstName, lastName, email, role, userName, password, worksAtPark);

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
	            String parkName = rs.getString("parkName");
	            // Assuming Order constructor is updated to accept all the necessary fields including telephoneNumber
	            Order order = new Order(orderId, traveler.getId(), parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
	            orderDataForClient.add(order);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return orderDataForClient; // This will return an empty list if there were no records found or an error occurred
	}
	

	/**
	 * Inserts a new reservation (order) into the database.
	 * @param traveler The traveler making the reservation.
	 * @param order The details of the order being made.
	 * @return true if insertion was successful, false otherwise.
	 */
	public Boolean insertTravelerOrder(Order order) {
		// Adjusting the query to match the database schema order provided
		String query = "INSERT INTO `order` (orderId, travlerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder, parkName)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		
		System.out.println(order.toString());
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			// Set parameters based on the Order object fields, in the order specified
			ps.setInt(1, order.getOrderId());
			ps.setInt(2, order.getVisitorId());
			ps.setInt(3, order.getParkNumber());
			ps.setInt(4, order.getAmountOfVisitors());
			ps.setFloat(5, order.getPrice());
			ps.setString(6, order.getVisitorEmail());
			ps.setDate(7, java.sql.Date.valueOf(order.getDate()));
			ps.setString(8, order.getTelephoneNumber()); 
			ps.setTime(9, java.sql.Time.valueOf(order.getVisitTime()));
			ps.setString(10, order.getOrderStatus()); 
			ps.setString(11, order.getTypeOfOrder());
			ps.setString(12, order.getParkName());
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
	            ps.setInt(8, park.getManagerid()); 
	            ps.setInt(9, park.getWorkingTime());
	            ps.setInt(10, park.getParkNumber());

	            int affectedRows = ps.executeUpdate();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    /**
	     * Updates the status of generalparkworker to signedin
	     * @param GeneralParkWorker to sign
	     * @return true if the signedin was successful, false otherwise.
	     */
	    public Boolean changeSingedInOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
	    	String query = "UPDATE generalparkworker SET isloggedin = 1 WHERE workerid = ?";
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedParkWorker.getWorkerId());
	    		int affectedRows = ps.executeUpdate();
	    		
	    		return affectedRows > 0;
	    		
	    	}catch(SQLException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    	
	    }
	    
	    /**
	     * Updates the status of generalparkworker to signedout
	     * @param GeneralParkWorker to sign
	     * @return true if the signed out was successful, false otherwise.
	     */
	    public Boolean changeSignedOutOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
	    	String query = "UPDATE `generalparkworker` SET isloggedin = 0 WHERE workerid = ?";
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedParkWorker.getWorkerId());
	    		int affectedRows = ps.executeUpdate();
	    		
	    		return affectedRows > 0;
	    		
	    	}catch(SQLException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    	
	    }
	    
	    /**
	     * Gets the status of loggedin of generalparkworker
	     * @param GeneralParkWorker
	     * @return isloggedin of generalparkworker
	     */
	    public Boolean getSignedinStatusOfGeneralParkWorker(GeneralParkWorker signedParkWorker) {
	    	//Return the status of isloggedin of generalparkworker
	    	String query = "SELECT isloggedin FROM generalparkworker WHERE workerid = ?";
	     
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)){
	    		ps.setInt(1, signedParkWorker.getWorkerId());
	    		ResultSet rs = ps.executeQuery();
	    		
	    		if (rs.next()) {
	                // Retrieve the value of isloggedin from the ResultSet
	                int isLoggedIn = rs.getInt("isloggedin");
	                System.out.print(isLoggedIn);
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
	     * Gets the amount of visitors in the park where the parkworker works at
	     * @param parkworker the park worker information
	     * @return park information if successful and null if not found
	     */

	    public Park getAmountOfVisitorsByParkWorker(GeneralParkWorker parkworker) {
	    	//Querying for the park information with the park number associated with the park worker
	    	String query = "Select * FROM park WHERE parkNumber = ?";
	    	Park fetchedPark = null;
	    	
	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ps.setInt(1, parkworker.getWorksAtPark());
	            ResultSet rs = ps.executeQuery(); // Use executeQuery for SELECT statements


	  
	                if (rs.next()) { // Use if instead of while if you expect or require a single result
	                    fetchedPark = new Park(
	                        rs.getString("name"), 
	                        rs.getInt("parkNumber"), 
	                        rs.getInt("maxVisitors"), 
	                        rs.getInt("capacity"), 
	                        rs.getInt("currentVisitors"), 
	                        rs.getString("location"), 
	                        rs.getInt("staytime"), 
	                        rs.getInt("workersAmount"), 
	                        rs.getInt("gap"), // Assuming you have a column for gap in your DB
	                        rs.getInt("managerID"), // Assuming managerID is stored directly as an integer
	                        rs.getInt("workingTime")
	                    );
	                }
	            }
	         catch (SQLException e) {
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
	                    park = new Park(
	                        rs.getString("name"), 
	                        rs.getInt("parkNumber"), 
	                        rs.getInt("maxVisitors"), 
	                        rs.getInt("capacity"), 
	                        rs.getInt("currentVisitors"), 
	                        rs.getString("location"), 
	                        rs.getInt("staytime"), 
	                        rs.getInt("workersAmount"),
	                        rs.getInt("gap"), 
	                        rs.getInt("managerID"), 
	                        rs.getInt("workingTime")
	                    );
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	        return park;
	    }
	    
	    
	    public VisitorsReport getTotalNumberOfVisitorsReport(GeneralParkWorker worker) {
	        // Adjusted query to include filtering by park number
	 
	        String query = "SELECT typeOfOrder, SUM(amountOfVisitors) AS totalVisitors FROM `order` WHERE parkNumber = ? GROUP BY typeOfOrder";
	        VisitorsReport report=null;

	        Integer totalIndividuals = 0;
	        Integer totalGroups = 0;
	        Integer totalFamilies = 0; // Initialize total for family visitors

	        try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	            // Set the park number in the query
	            statement.setInt(1, worker.getWorksAtPark());

	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    String typeOfOrder = resultSet.getString("typeOfOrder");
	                    int totalVisitors = resultSet.getInt("totalVisitors");
	                    System.out.println("Order Type: " + typeOfOrder + ", Total Visitors: " + totalVisitors);
	                    System.out.println("Order Type Raw: [" + typeOfOrder + "], Length: " + typeOfOrder.length());
	                    
	                    if (typeOfOrder.trim().equalsIgnoreCase("GUIDEDGROUP")) {
	                        System.out.println("Match found after trimming and case-insensitive check");
	                    } else {
	                        System.out.println("No match found");
	                    }
	                    typeOfOrder=typeOfOrder.trim();
	                    switch (typeOfOrder) {
	                        case "SOLO":
	                            totalIndividuals += totalVisitors;
	                            break;
	                        case "GUIDEDGROUP":
	    	                    System.out.println("in Order Type: " );

	                            totalGroups += totalVisitors;
	                            break;
	                        case "FAMILY":
	                            totalFamilies += totalVisitors;
	                            break;
	                        default:
	                            // Handle any unexpected typeOfOrder
	                            break;
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            System.err.println("An error occurred while fetching the total number of visitors report: " + e.getMessage());
	            e.printStackTrace();
	        }
	        report=new VisitorsReport(totalIndividuals, totalGroups, totalFamilies,totalIndividuals+ totalGroups+totalFamilies);
	        System.out.println(report.toString());
	        return report; // Pass the new total as well
	    }

	    /**
	     * Checks if the order is valid based on traveler orders for the same date and hour
	     * @param order
	     * @return true if valid, else false
	     */
	    public Boolean checkIfOrderisValid(Order order) {
	        Traveler traveler = Usermanager.getCurrentTraveler();
	        Integer parkNumber;
	        LocalDate date;
	        LocalTime time;
	        ArrayList<Order> orders = new ArrayList<>();
	        String query = "SELECT date, visitTime, parkNumber FROM `order` WHERE travlerId = ?"; 

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            // ps.setInt(1, traveler.getId()); גרסא נכונה

	            ps.setInt(1, 1214214);
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                parkNumber = rs.getInt("parkNumber"); 
	                date = rs.getDate("date").toLocalDate();
	                time = rs.getTime("visitTime").toLocalTime();
	                Order existingOrder = new Order(null, null, parkNumber, null, null, null, date, time, null, null, null, null);
	                orders.add(existingOrder);
	            }
	            // Check if the new order is valid
	            for (Order existingOrder : orders) {

	                if (existingOrder.getParkNumber().equals(order.getParkNumber()) &&
	                    existingOrder.getDate().isEqual(order.getDate()) &&
	                    existingOrder.getVisitTime().equals(order.getVisitTime())) {
	                    // Order with the same park, date, and time already exists
	                    System.out.println("Conflicting Order Found: " + existingOrder.toString());
	                    return false;
	                }
	            }

	            // If no conflicting orders were found, the new order is valid
	            return true;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("SQL Exception occurred: " + e.getMessage());
	            return false; // Handle the exception according to your needs
	        }
	    }

//	    /**
//		 * Checks if there is availability for a new order in the specified park, at the given time and date.
//		 * @param order The Order object containing details of the new order to check for availability.
//		 * @return True if there is availability, false otherwise.
//		 */
//		public boolean isSpotAvailable(Order order) {
//			Integer parkNumber = order.getParkNumber(); 
//			System.out.println(parkNumber);
//			LocalTime arrivedTime = order.getVisitTime();
//			System.out.println(arrivedTime);
//			LocalDate arrivedDate = order.getDate();
//			System.out.println(arrivedDate);
//			int visitorsNumber = order.getAmountOfVisitors();
//			int totalAmount = 0;
//			int maxAmount = 0 ; 
//			String query = "SELECT amountOfVisitors FROM `order` WHERE parkNumber = ? AND date = ?"; 
//
//			
//			System.out.println("SQL Query: " + query);
//			try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) 
//			{
//				ps.setInt(1, parkNumber);
//			    ps.setString(2, arrivedDate.toString());
//			    ResultSet rs = ps.executeQuery();
//
//			    while (rs.next()) {
//			        totalAmount += Integer.parseInt(rs.getString("amountOfVisitors"));
//			    }
//			    System.out.println("Total amount of visitors: " + totalAmount);
//			} catch (SQLException e) {
//			    e.printStackTrace(); // Handle the exception
//			    System.out.println("SQL Exception: " + e.getMessage());
//			}
//			
//			String query1 = "SELECT maxVisitors FROM park WHERE parkNumber = ?";
//		    
//		    try (PreparedStatement ps = connectionToDatabase.prepareStatement(query1)) {
//		        ps.setInt(1, parkNumber);
//
//		        try (ResultSet rs = ps.executeQuery()) {
//		            if (rs.next()) {
//		                maxAmount = rs.getInt("maxVisitors");
//		            }
//				    System.out.println("max Order Visitors Amount: " + maxAmount);
//
//		        }
//		    } catch (SQLException e) {
//		        e.printStackTrace();
//		    }
//		    System.out.println("totalAmount + visitorsNumber: " + (totalAmount + visitorsNumber));
//		    System.out.println(totalAmount + visitorsNumber <= maxAmount);
//
//		    return totalAmount + visitorsNumber <= maxAmount;
//		    
//		}
		
		
		/**
		 * This query finds the Max order id
		 * @return MAX(OrderId)
		 */
		public Integer getLastOrderId() {
	        String query = "SELECT MAX(OrderId) FROM `order`";  
	        Integer lastOrder = null;

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                lastOrder=rs.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lastOrder;
	    }
		
		/**
		 * Finds the most recent traveler order based on his ID
		 * @param travelerId
		 * @return order
		 */
		public Order getTravelerRecentOrder(Integer travelerId) {
			String query = "SELECT * FROM order WHERE travlerId = ? ORDER BY orderId DESC";  
	        Order lastOrder = null;

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            // ps.setInt(1, traveler.getId()); גרסא נכונה

	            ps.setInt(1, 123);	            
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	Integer orderId = rs.getInt("orderId");
		            Integer traveler = rs.getInt("travlerId"); 
		            Integer parkNumber = rs.getInt("parkNumber");
		            Integer amountOfVisitors = rs.getInt("amountOfVisitors");
		            Float price = rs.getFloat("price");
		            String visitorEmail = rs.getString("visitorEmail");
		            LocalDate date = rs.getDate("date").toLocalDate();
		            String telephoneNumber = rs.getString("TelephoneNumber"); 
		            LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
		            String statusStr = rs.getString("orderStatus"); 
		            String typeOfOrderStr = rs.getString("typeOfOrder");
		            String parkName = rs.getString("parkName");
		            lastOrder = new Order(orderId, traveler, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lastOrder;
		}
		
		/**
		 * Finding order within given times
		 * @param parameters parkNumber, date, visitTime
		 * @return 
		 */
		public ArrayList<Order> findOrdersWithinDates(ArrayList<?> parameters) {
			ArrayList<Order> orders = new ArrayList<Order>();
			String query = "SELECT * FROM `order` WHERE parkNumber = ? AND date = ? AND visitTime >= ? AND visitTime <= ?";

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	        	ps.setString(1, (String) parameters.get(0));
	        	ps.setDate(2, java.sql.Date.valueOf(LocalDate.parse((String) parameters.get(1))));
	        	ps.setTime(3, java.sql.Time.valueOf(LocalTime.parse((String) parameters.get(2))));
	        	ps.setTime(4, java.sql.Time.valueOf(LocalTime.parse((String) parameters.get(3))));

	            
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	Integer orderId = rs.getInt("orderId");
		            Integer travelerId = rs.getInt("travlerId"); 
		            Integer parkNumber = rs.getInt("parkNumber");
		            Integer amountOfVisitors = rs.getInt("amountOfVisitors");
		            Float price = rs.getFloat("price");
		            String visitorEmail = rs.getString("visitorEmail");
		            LocalDate date = rs.getDate("date").toLocalDate();
		            String telephoneNumber = rs.getString("TelephoneNumber"); 
		            LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
		            String statusStr = rs.getString("orderStatus"); 
		            String typeOfOrderStr = rs.getString("typeOfOrder");
		            String parkName = rs.getString("parkName");

		            orders.add(new Order(orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName));
	            }
	            
	         
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return orders;
		}
		
		/**
		 * getting all parks information
		 * @return parks information
		 */
		public ArrayList<Park> getParksInfo() {
			ArrayList<Park> parks = new ArrayList<Park>();
			String query = "SELECT * FROM park";
	        
			try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	String name = rs.getString("name");
		            Integer parkNumber = rs.getInt("parkNumber"); 
		            Integer maxVisitors = rs.getInt("maxVisitors");
		            Integer capacity = rs.getInt("capacity");
		            Integer currentVisitors = rs.getInt("currentVisitors");
		            String location = rs.getString("location");
		            Integer staytime = rs.getInt("staytime");
		            Integer workersAmount = rs.getInt("workersAmount");
		            Integer managerID = rs.getInt("managerId"); 
		            Integer workingTime = rs.getInt("workingTime");
		            Integer gap = rs.getInt("gap");

		            parks.add(new Park(name, parkNumber, maxVisitors , capacity, currentVisitors, location, staytime, workersAmount, gap, managerID, workingTime));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
			return parks;

		}
		
		/**
		 * finds park info by his name
		 * @param parkName
		 * @return park's info
		 */
		public Park findParkByName(String parkName) {
			Park park = null;
			String query = "SELECT * FROM park where name = ?";
	        
			try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ps.setString(1, parkName);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	            	String name = rs.getString("name");
		            Integer parkNumber = rs.getInt("parkNumber"); 
		            Integer maxVisitors = rs.getInt("maxVisitors");
		            Integer capacity = rs.getInt("capacity");
		            Integer currentVisitors = rs.getInt("currentVisitors");
		            String location = rs.getString("location");
		            Integer staytime = rs.getInt("staytime");
		            Integer workersAmount = rs.getInt("workersAmount");
		            Integer managerID = rs.getInt("managerId"); 
		            Integer workingTime = rs.getInt("workingTime");
		            Integer gap = rs.getInt("gap");

		            park = new Park(name, parkNumber, maxVisitors , capacity, currentVisitors, location, staytime, workersAmount, gap, managerID, workingTime);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
			return park;

		}

		/**
		 * Insert traveler to waiting list
		 * @param waiting
		 * @return inserted successfully true, else false
		 */
		public Boolean insertWaitingList(WaitingList waiting) {

			String query = "INSERT INTO `waitinglist` (orderId, travlerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, "
					+ "visitTime, orderStatus, typeOfOrder,parkName, waitingListId, placeInList) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			System.out.println(waiting.toString());
			try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
				ps.setInt(1, waiting.getOrderId());
				ps.setInt(2, waiting.getVisitorId());
				ps.setInt(3, waiting.getParkNumber());
				ps.setInt(4, waiting.getAmountOfVisitors());
				ps.setFloat(5, waiting.getPrice());
				ps.setString(6, waiting.getVisitorEmail());
				ps.setDate(7, java.sql.Date.valueOf(waiting.getDate()));
				ps.setString(8, waiting.getTelephoneNumber()); 
				ps.setTime(9, java.sql.Time.valueOf(waiting.getVisitTime()));
				ps.setString(10, waiting.getOrderStatus()); 
				ps.setString(11, waiting.getTypeOfOrder()); 
				ps.setString(12, waiting.getParkName());
				ps.setInt(13, waiting.getWaitingListId());
				ps.setInt(14, waiting.getPlaceInList());

				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("Inserted successfully.");
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
		 * Insert new traveler to traveler table
		 * @param traveler
		 * @return true if inserted, false otherwise.
		 */
		public boolean insertNewTraveler(Traveler traveler) {
	     

	        String query = "INSERT INTO `travler` (id, firstName, lastName, email, phoneNumber, GroupGuide, isloggedin) VALUES (?, ?, ?, ?, ?, ?, ?)";

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	        	ps.setInt(1, traveler.getId());
	        	ps.setString(2, traveler.getFirstName());
	        	ps.setString(3, traveler.getLastName());
	        	ps.setString(4, traveler.getEmail());
	        	ps.setString(5, traveler.getPhoneNum());
	        	ps.setInt(6, traveler.getIsGroupGuide()); 
	        	ps.setInt(7, 0); 

	            int affectedRows = ps.executeUpdate();

	            // Check if the insert was successful
	            if (affectedRows > 0) {
	                return true;
	            }
	        } catch (SQLException e) {
	            System.err.println("SQLException: " + e.getMessage());
	        } 

	        return false;
	    }
		
		
		/**
		 * Finds MAX(waitingListId) in waitingList table
		 * @return MAX(waitingListId)
		 */
		public Integer getLastWaitingId() {
	        String query = "SELECT MAX(waitingListId) FROM `waitinglist`";  
	        Integer lastOrder = null;

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                lastOrder=rs.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lastOrder;
	    }
		
		/**
		 * Find MAX(placeInList) in waitingList based on parkNumber,date,visitTime
		 * @param parameters parkNumber, date, visitTime
		 * @return placeInList
		 */
		public Integer findPlaceWaiting(ArrayList<?> parameters) {
			Integer maxPlaceInList = 1;
			String query = "SELECT MAX(placeInList) AS maxPlaceInList FROM waitinglist WHERE parkName = ? AND date = ? AND visitTime = ?";


	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	        	ps.setString(1, (String) parameters.get(0));
	        	ps.setDate(2, java.sql.Date.valueOf(LocalDate.parse((String) parameters.get(1))));
	        	ps.setTime(3, java.sql.Time.valueOf(LocalTime.parse((String) parameters.get(2))));

	            
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                maxPlaceInList = rs.getInt("maxPlaceInList");
	                return ++maxPlaceInList;
	            }
	            
	         
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return maxPlaceInList;
		}
		

}
		

