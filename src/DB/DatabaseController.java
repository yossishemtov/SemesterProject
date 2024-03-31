package DB;

import java.sql.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Manages database operations for the user management system.
 * This controller handles the interaction between the user management
 * system and the database, including inserting new employees into
 * the database.
 */
public class DatabaseController {
    private Connection connectionToDatabase;

    /**
     * Constructs a DatabaseController object. It initializes the database connection
     * using provided user credentials and inserts existing employees from the 
     * user management system into the database.
     *
     * @param connectionToDB The connection to the database.
     * @param userManagementSystemDB An instance of UserManagementSystemDB that provides
     *                               access to employee data.
     */
    public DatabaseController(Connection connectionToDB, UserManagementSystemDB userManagementSystemDB) {
        connectionToDatabase = connectionToDB;
        insertEmployees(userManagementSystemDB.getAllEmployees());
        userManagementSystemDB.closeConnection();
    }

    /**
     * Inserts a list of employees into the database. This method takes
     * the list of employees retrieved from the user management system
     * and persists them into the database.
     * 
     * Implementation note: This method's body should contain the logic
     * to convert the list of employees into appropriate SQL INSERT statements
     * and execute them using the established database connection. Error handling
     * and transaction management should also be considered.
     *
     * @param employees The list of employees to be inserted into the database.
     */
	public int insertEmployees(ArrayList<GeneralParkWorker> employees) {
	    int insertedRows = 0;
	    String query = "INSERT INTO `generalparkworker` (workerId, firstName, lastName, email, role, userName, password, worksAtPark, isloggedin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
	        for (GeneralParkWorker worker : employees) {
	            try {
	                statement.setInt(1, worker.getWorkerId());
	                statement.setString(2, worker.getFirstName());
	                statement.setString(3, worker.getLastName());
	                statement.setString(4, worker.getEmail());
	                statement.setString(5, worker.getRole());
	                statement.setString(6, worker.getUserName());
	                statement.setString(7, worker.getPassword());
	                statement.setInt(8, worker.getWorksAtPark());
	                statement.setInt(9, 0); 
	                
	                insertedRows += statement.executeUpdate(); // Execute the insertion for each employee.
	            } catch (SQLIntegrityConstraintViolationException e) {
	                break;
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("SQL Exception: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return insertedRows;
	}

	/**
	 * Finds the most recent traveler order based on his ID
	 * 
	 * @param travelerId
	 * @return order
	 */
	public Order getTravelerRecentOrder(Integer travelerId) {
		String query = "SELECT * FROM order WHERE travelerId = ? ORDER BY orderId DESC";
		Order lastOrder = null;

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			// ps.setInt(1, traveler.getId()); גרסא נכונה

			ps.setInt(1, 123);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer traveler = rs.getInt("travelerId");
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
				lastOrder = new Order(orderId, traveler, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastOrder;
	}

	/**
	 * Finding order within given times
	 * 
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
				Integer travelerId = rs.getInt("travelerId");
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

				orders.add(new Order(orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * getting all parks information
	 * 
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

				parks.add(new Park(name, parkNumber, maxVisitors, capacity, currentVisitors, location, staytime,
						workersAmount, gap, managerID, workingTime));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parks;

	}

	/**
	 * finds park info by his name
	 * 
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

				park = new Park(name, parkNumber, maxVisitors, capacity, currentVisitors, location, staytime,
						workersAmount, gap, managerID, workingTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return park;

	}

	/**
	 * Insert traveler to waiting list
	 * 
	 * @param waiting
	 * @return inserted successfully true, else false
	 */
	public Boolean insertWaitingList(WaitingList waiting) {

		String query = "INSERT INTO `waitinglist` (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, "
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
	 * 
	 * @param traveler
	 * @return true if inserted, false otherwise.
	 */
	public boolean insertNewTraveler(Traveler traveler) {

		String query = "INSERT INTO `traveler` (id, firstName, lastName, email, phoneNumber, GroupGuide, isloggedin) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
	 * 
	 * @return MAX(waitingListId)
	 */
	public Integer getLastWaitingId() {
		String query = "SELECT MAX(waitingListId) FROM `waitinglist`";
		Integer lastOrder = null;

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lastOrder = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastOrder;
	}

	/**
	 * Find MAX(placeInList) in waitingList based on parkNumber,date,visitTime
	 * 
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

	/**
	 * This query finds the Max order id
	 * 
	 * @return MAX(OrderId)
	 */
	public Integer getLastOrderId() {
		String query = "SELECT MAX(OrderId) FROM `order`";
		Integer lastOrder = null;

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lastOrder = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastOrder;
	}

	/**
	 * Checks if the order is valid based on traveler orders for the same date and
	 * hour
	 * 
	 * @param order
	 * @return true if valid, else false
	 */
	public Boolean checkIfOrderisValid(Order order) {
		Integer parkNumber;
		LocalDate date;
		LocalTime time;
		ArrayList<Order> orders = new ArrayList<>();
		String query = "SELECT date, visitTime, parkNumber FROM `order` WHERE travelerId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, order.getVisitorId());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				parkNumber = rs.getInt("parkNumber");
				date = rs.getDate("date").toLocalDate();
				time = rs.getTime("visitTime").toLocalTime();
				Order existingOrder = new Order(null, null, parkNumber, null, null, null, date, time, null, null, null,
						null);
				orders.add(existingOrder);
			}
			// Check if the new order is valid
			for (Order existingOrder : orders) {

				if (existingOrder.getParkNumber().equals(order.getParkNumber())
						&& existingOrder.getDate().isEqual(order.getDate())
						&& existingOrder.getVisitTime().equals(order.getVisitTime())) {
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
	
	/**
     * Retrieves visit report data for a specified park and month.
     *
     * @param report The VisitReport object representing the report.
     * @return The VisitReport object populated with visit data, or null if no data is found.
     */
	public VisitReport getVisitReport(VisitReport report) {
		String query = "SELECT v.enteringTime, TIMESTAMPDIFF(MINUTE, v.enteringTime, v.exitingTime) AS duration, o.typeOfOrder "
				+ "FROM `visit` v JOIN `order` o ON v.orderNumber = o.orderId "
				+ "WHERE MONTH(v.visitDate) = ? AND v.parkNumber = ? AND o.orderStatus = 'COMPLETED'";

		boolean dataFound = false; // Flag to check if any data is found

		try (PreparedStatement ps = this.connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, report.getMonthNumber());
			ps.setInt(2, report.getParkNumber());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				dataFound = true; // Data is found, set the flag to true

				String enteringTimeStr = rs.getString("enteringTime");
				long duration = rs.getLong("duration");
				String typeOfOrderStr = rs.getString("typeOfOrder");

				report.addVisit(enteringTimeStr, duration, typeOfOrderStr);
				// Print each row to see what we're getting back
				System.out.println("Visit data: Entering Time: " + enteringTimeStr + ", Duration: " + duration
						+ ", Type of Order: " + typeOfOrderStr);
			}

			if (dataFound) {
				System.out.println("Report successfully populated with visits data for park number "
						+ report.getParkNumber() + " and month " + report.getMonthNumber() + ". " + report);
				return report;
			} else {
				System.out.println("No visit data found for park number " + report.getParkNumber() + " and month "
						+ report.getMonthNumber());
				return null; // No data found, return null
			}
		} catch (SQLException e) {
			System.err.println("An error occurred while populating the report: " + e.getMessage());
			e.printStackTrace();
			return null; // In case of an exception, you might also want to consider returning null or
							// handling it differently
		}
	}

	/**
     * Inserts a change request into the database.
     *
     * @param request The ChangeRequest object representing the change request to be inserted.
     * @return True if the change request is successfully inserted, false otherwise.
     */
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

	/**
     * Retrieves change requests waiting for approval based on the role of the worker.
     *
     * @param worker The GeneralParkWorker object representing the worker.
     * @return An ArrayList of ChangeRequest objects representing the change requests waiting for approval,
     *         or null if no requests are found.
     */
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

	/**
     * Updates the status of a change request.
     *
     * @param request The ChangeRequest object representing the change request to be updated.
     * @return True if the status of the change request is successfully updated, false otherwise.
     */
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
     * Updates the number of unordered visits for a park.
     *
     * @param parkWithIdAndNewUnorderedVisits The Park object containing the park number and the new unordered visits count.
     * @return True if the update is successful, false otherwise.
     */
	public Boolean patchParkUnorderedVisits(Park parkWithIdAndNewUnorderedVisits) {
		// change the unordered visits amount

		String query = "UPDATE `park` SET unorderedvisits = ? WHERE parkNumber = ?";
		System.out.print(parkWithIdAndNewUnorderedVisits.getUnorderedVisits());
		try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
			pstmt.setInt(1, parkWithIdAndNewUnorderedVisits.getUnorderedVisits());
			pstmt.setInt(2, parkWithIdAndNewUnorderedVisits.getParkNumber());

			int affectedRows = pstmt.executeUpdate();

			// Check if the update was successful
			return affectedRows > 0;

		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			return false;
		}
	}

	/**
     * Retrieves details of a park based on the park number.
     *
     * @param parkNumber The park number for which details are to be retrieved.
     * @return A Park object containing the details of the park, or null if no park is found with the specified number.
     */
	public Park getParkDetails(Integer parkNumber) {
		Park park = null;
		String query = "SELECT * FROM `park` WHERE parkNumber = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, parkNumber);

			ResultSet resultSet = ps.executeQuery();

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
				Integer unorderedvisits = resultSet.getInt("unorderedvisits");
				park = new Park(name, parkNumber, maxVisitors, capacity, currentVisitors, location, staytime,
						workersAmount, gap, managerID, workingTime);
				park.setUnorderedVisits(unorderedvisits);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return park;
	}

	/**
	 * Get amount of unordered visits from a park
	 *
	 * @param parkid of a Park
	 * @return Integer unorderedvisits allowed
	 */
	public Integer getUnorderedVists(int parkNumber) {
		Integer numberOfUnorderedVisits = null;
		String query = "SELECT unorderedvisits FROM `park` WHERE parkNumber = ?";

		try (PreparedStatement preparedStatement = connectionToDatabase.prepareStatement(query)) {
			preparedStatement.setInt(1, parkNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				numberOfUnorderedVisits = resultSet.getInt("unorderedvisits");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return numberOfUnorderedVisits;

	}

	/**
	 * Get order information and change its state to COMPLETED
	 * 
	 * @param Order object that contains a valid orderId
	 * @return True if success or False if not
	 */
	public Boolean patchOrderStatusToCompleted(Order receivedOrder) {
		String query = "UPDATE `order` SET orderStatus = 'COMPLETED' WHERE orderId = ?";

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
	 * 
	 * @param Park object with new amount of visitors
	 * @return True if success or False if not
	 */
	public Boolean patchParkVisitorsNumber(Park parkToAppend) {
		// Append the visitors to the park currentvisitors

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
	 * Get order information based on the orderId, park number and date provided
	 * 
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

			if (rs.next()) {

				// Parsing special object such of type localDate and LocalTime
				LocalDate orderDate = rs.getObject("date", LocalDate.class);
				LocalTime visitTime = rs.getObject("visitTime", LocalTime.class);
				float price = rs.getFloat("price");

				orderInformation = new Order(rs.getInt("orderId"), rs.getInt("travelerId"), rs.getInt("parkNumber"),
						rs.getInt("amountOfVisitors"), price, rs.getString("visitorEmail"), orderDate, visitTime,
						rs.getString("orderStatus"), // Assuming managerID is stored directly as an integer
						rs.getString("typeOfOrder"), rs.getString("TelephoneNumber"), rs.getString("parkName"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return orderInformation;
			// Consider logging this exception or handling it more gracefully
		}

		return orderInformation;
	}

	/**
	 * Retrieves all waiting lists for a given traveler from the database.
	 * 
	 * @param traveler The traveler whose orders are to be retrieved.
	 * @return An ArrayList of waiting list objects.
	 */
	public ArrayList<WaitingList> getWaitingListsDataFromDatabase(Traveler traveler) {
		ArrayList<WaitingList> waitingListDataForClient = new ArrayList<>();
		// Assuming the table and column names are corrected to match Java conventions
		String query = "SELECT orderId, waitingListId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, "
				+ "typeOfOrder, parkName, placeInList FROM waitinglist WHERE travelerId = ? AND orderStatus != 'CONFIRMED'";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, traveler.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer waitingListId = rs.getInt("waitingListId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");
				Integer placeInList = rs.getInt("placeInList");

				WaitingList waitingList = new WaitingList(orderId, traveler.getId(), parkNumber, amountOfVisitors,
						price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName,
						waitingListId, placeInList);
				waitingListDataForClient.add(waitingList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return waitingListDataForClient;
	}

	/**
	 * 
	 * Deletes an existing waiting list from the database based on its ID.
	 * 
	 * @param waitingListId The ID of the waiting list to delete.
	 * @return true if the deletion was successful, false otherwise.
	 */
	public Boolean deleteWaitingList(Integer waitingListId) {
		String deleteQuery = "DELETE FROM waitingList WHERE waitingListId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(deleteQuery)) {
			ps.setInt(1, waitingListId);

			int affectedRows = ps.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			System.out.println("An error occurred while deleting the waiting list:");
			e.printStackTrace();
			return false;
		}
	}
	
	 /**
     * Retrieves details of a traveler from the database based on the traveler's ID.
     *
     * @param travelerFromClient The Traveler object representing the traveler whose details are to be retrieved.
     * @return A Traveler object containing the details of the traveler, or null if no traveler is found with the specified ID.
     */
	public Traveler getTravelerDetails(Traveler travelerFromClient) {
		String query = "SELECT firstName, lastName, email, phoneNumber, GroupGuide, isloggedin FROM `traveler` WHERE id = ?";
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

	/**
     * Retrieves details of a general park worker from the database based on the worker's credentials.
     *
     * @param worker The GeneralParkWorker object representing the worker whose details are to be retrieved.
     * @return A GeneralParkWorker object containing the details of the worker, or null if no worker is found with the specified credentials.
     */
	public GeneralParkWorker getGeneralParkWorkerDetails(GeneralParkWorker worker) {
		System.out.println("Starting to fetch GeneralParkWorker details from DB.");
		System.out.println("Credentials provided: " + worker.getUserName());

		GeneralParkWorker generalParkWorker = null;
		String query = "SELECT * FROM `generalparkworker` WHERE userName = ? AND password= ?";

		try (PreparedStatement preparedStatementInstance = connectionToDatabase.prepareStatement(query)) {
			// Set a timeout for the query to prevent it from running indefinitely
			preparedStatementInstance.setQueryTimeout(30); // Timeout in seconds

			preparedStatementInstance.setString(1, worker.getUserName());
			preparedStatementInstance.setString(2, worker.getPassword());

			ResultSet returnedStatement = preparedStatementInstance.executeQuery();

			if (!returnedStatement.next()) {
				System.out.println("No matching GeneralParkWorker found.");
				return null; // Immediately return null if no data is found
			} else {
				// Move the cursor back to the beginning of the ResultSet
				returnedStatement.beforeFirst();
			}

			while (returnedStatement.next()) {
				Integer workerId = returnedStatement.getInt(1);
				String firstName = returnedStatement.getString(2);
				String lastName = returnedStatement.getString(3);
				String email = returnedStatement.getString(4);
				String role = returnedStatement.getString(5);
				String userName = returnedStatement.getString(6);
				String password = returnedStatement.getString(7);
				Integer worksAtPark = returnedStatement.getInt(8);

				generalParkWorker = new GeneralParkWorker(workerId, firstName, lastName, email, role, userName,
						password, worksAtPark);
				System.out.println("Fetched details for: " + userName);
			}
		} catch (SQLException e) {
			System.err.println("SQLException occurred while fetching GeneralParkWorker details: " + e.getMessage());
			e.printStackTrace();
		}

		if (generalParkWorker != null) {
			System.out.println("Successfully fetched GeneralParkWorker details.");
		} else {
			System.out.println("Failed to fetch GeneralParkWorker details.");
		}

		return generalParkWorker;
	}

	/**
	 * Retrieves all orders for a given traveler from the database.
	 * 
	 * @param traveler The traveler whose orders are to be retrieved.
	 * @return An ArrayList of Order objects.
	 */
	public ArrayList<Order> getOrdersDataFromDatabase(Traveler traveler) {
		ArrayList<Order> orderDataForClient = new ArrayList<>();// Ensure the query reflects your actual database table
																// and column names
		String query = "SELECT orderId, parkName, amountOfVisitors, price, date, visitTime, orderStatus, typeOfOrder, TelephoneNumber FROM `order` WHERE travelerId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, traveler.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				String parkName = rs.getString("parkName");

				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder").trim();
				String Telephone = rs.getString("TelephoneNumber");

				// Assuming Order constructor is updated to accept all the necessary fields
				// including telephoneNumber
				Order order = new Order(orderId, null, null, amountOfVisitors, price, null, date, visitTime, statusStr,
						typeOfOrderStr, Telephone, parkName);
				orderDataForClient.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (orderDataForClient.isEmpty()) {
			return null;
		}
		return orderDataForClient; // This will return an empty list if there were no records found or an error
									// occurred
	}

	/**
	 * 
	 * Inserts a new reservation (order) into the database.
	 * 
	 * @param traveler The traveler making the reservation.
	 * @param order    The details of the order being made.
	 * @return true if insertion was successful, false otherwise.
	 */
	public Boolean insertTravelerOrder(Order order) {// Adjusting the query to match the database schema order provided
		String query = "INSERT INTO `order` (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder, parkName)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		System.out.println(order.toString());
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {// Set parameters based on the Order
																					// object fields, in the order
																					// specified
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
	 * 
	 * @param order The order object containing the order ID and the new status.
	 * @return true if the update was successful, false otherwise.
	 */
	public Boolean updateOrderStatus(Order order) {
		String updateQuery = "UPDATE `order` SET orderStatus = ? WHERE orderId = ?";

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

	/**
	 * Updates the parameters of a park based on a change request.
	 *
	 * @param changeRequest The ChangeRequest object representing the details of the requested changes.
	 * @return True if the update is successful, false otherwise.
	 */
	public Boolean patchParkParameters(ChangeRequest changeRequest) {
		// Assuming capacity is a column in your database that should be updated based
		// on gap
		// Calculate the new capacity based on the provided gap and maxVisitors
		Integer newMaxVisitor = changeRequest.getCapacity() - changeRequest.getGap();
		// Update only the fields that are affected by a change request
		String query = "UPDATE `park` SET capacity = ?, stayTime = ? ,gap = ? , maxVisitors = ? , unorderedvisits = unorderedvisits + ? WHERE parkNumber = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, changeRequest.getCapacity());
			ps.setInt(2, changeRequest.getStaytime());
			ps.setInt(3, changeRequest.getGap());
			ps.setInt(4, newMaxVisitor);
			ps.setInt(5, changeRequest.getGap()-changeRequest.getOldGap());

			
			ps.setInt(6, changeRequest.getParkNumber());
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
	 * 
	 * @param Traveler to signin
	 * @return true if the signedin was successful, false otherwise.
	 */
	public Boolean changedSignedInOfTraveler(Traveler signedTraveler) {
		String query = "UPDATE traveler SET isloggedin = 1 WHERE id = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedTraveler.getId());
			int affectedRows = ps.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Updates the status of Traveler to signout
	 * 
	 * @param Traveler to signout
	 * @return true if the signedout was successful, false otherwise.
	 */
	public Boolean changedSignedOutOfTraveler(Traveler signedTraveler) {
		String query = "UPDATE traveler SET isloggedin = 0 WHERE id = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedTraveler.getId());
			int affectedRows = ps.executeUpdate();

			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets the status of loggedin of Traveler
	 * 
	 * @param GeneralParkWorker
	 * @return Boolean isloggedin of Traveler
	 */
	public Boolean getSignedinStatusOfTraveler(Traveler signedTraveler) {
		// Return the status of isloggedin of Traveler
		String query = "SELECT isloggedin FROM traveler WHERE id = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, signedTraveler.getId());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				// Retrieve the value of isloggedin from the ResultSet
				int isLoggedIn = rs.getInt("isloggedin");

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
	 * Get order information and change its state to INPARK
	 * 
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
	 * Insert a visit based on provided Visit object
	 * 
	 * @param Visit object to insert
	 * @return True if success or False if not
	 */
	public Boolean addNewVisit(Visit newVisitToAdd) {
		String query = "INSERT INTO `visit` (visitDate, enteringTime, exitingTime, parkNumber, orderNumber) VALUES (?, ?, ?, ?, ?)";

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
//	    	
//	    /**
//	     * Get order information based on the orderId, park number and date provided
//	     * @param Order object that contains a valid orderId, parknumber and date
//	     * @return Order object containing all the information about the order
//	     */	
//	    public Order getOrderInformationByOrderIdAndParkNumber(Order receivedOrderId) {
//	    	String query = "SELECT * FROM `order` WHERE orderId = ? AND parkNumber = ? AND date = ?";
//	    	Order orderInformation = null;
//	    	
//	    	try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
//	    		ps.setInt(1, receivedOrderId.getOrderId());
//	    		ps.setInt(2, receivedOrderId.getParkNumber());
//	    		ps.setObject(3, java.sql.Date.valueOf(receivedOrderId.getDate()));
//	            ResultSet rs = ps.executeQuery(); // Use executeQuery for SELECT statements
//	            
//	            
//	  
//	                if (rs.next()) { // Use if instead of while if you expect or require a single result
//	                	
//	                	//Parsing special object such of type localDate and LocalTime
//	    	            LocalDate orderDate = rs.getObject("date", LocalDate.class);
//	    	            LocalTime visitTime = rs.getObject("visitTime", LocalTime.class);
//	    	            float price = rs.getFloat("price");
//	    	            
//	                	orderInformation = new Order(
//	                        rs.getInt("orderId"), 
//	                        rs.getInt("travelerId"), 
//	                        rs.getInt("parkNumber"), 
//	                        rs.getInt("amountOfVisitors"), 
//	                        price, 
//	                        rs.getString("visitorEmail"), 
//	                        orderDate, 
//	                        visitTime, // Assuming you have a column for gap in your DB
//	                        rs.getString("orderStatus"), // Assuming managerID is stored directly as an integer
//	                        rs.getString("typeOfOrder"),
//	                        rs.getString("TelephoneNumber"), query
//	                    );
//	                }
//	            }
//	         catch (SQLException e) {
//	            e.printStackTrace();
//	            return orderInformation;
//	            // Consider logging this exception or handling it more gracefully
//	        }
//	    	
//	    	return orderInformation;
//	    }
//	    

	/**
	 * Gets the amount of visitors in the park where the parkworker works at
	 * 
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

	/**
	 * Retrieves the details of a park based on the park number associated with a general park worker.
	 *
	 * @param worker The GeneralParkWorker object containing the park number.
	 * @return A Park object containing the details of the specified park, or null if no park is found.
	 */
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

	/**
	 * Generates a new visitors report based on the specified criteria.
	 *
	 * @param visitReport The VisitorsReport object representing the criteria for generating the report.
	 * @return A VisitorsReport object containing the newly generated report, or null if no data is available.
	 */
	public VisitorsReport getNewVisitorsReport(VisitorsReport visitReport) {
		boolean haveData = false;
		String query = "SELECT typeOfOrder, SUM(amountOfVisitors) AS totalVisitors " + "FROM `order` "
				+ "WHERE parkNumber = ? AND YEAR(date) = ? AND MONTH(date) = ? AND orderStatus = 'COMPLETED' "
				+ "GROUP BY typeOfOrder";

		// Initialize variables for the report
		Integer totalIndividuals = 0, totalGroups = 0, totalFamilies = 0, totalVisitors = 0;
		Integer parkNumber = visitReport.getParkID(); // Assuming VisitReport has this method
		LocalDate reportDate = visitReport.getDate(); // Assuming VisitReport has this method
		System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: "
				+ reportDate.getYear() + ", Month: " + reportDate.getMonthValue());

		// Prepare the database query
		try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {

			statement.setInt(1, parkNumber);
			statement.setInt(2, reportDate.getYear());
			statement.setInt(3, visitReport.getMonth());
			// statement.setInt(3, reportDate.getMonthValue());
			System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: "
					+ reportDate.getYear() + ", Month: " + reportDate.getMonthValue());

			// Execute the query and process the results
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					haveData = true;
					String typeOfOrder = resultSet.getString("typeOfOrder").trim();
					int visitors = resultSet.getInt("totalVisitors");

					switch (typeOfOrder.toUpperCase().trim()) {
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
						System.out.println(typeOfOrder.toUpperCase().trim());
						break;
					}
				}
				totalVisitors = totalIndividuals + totalGroups + totalFamilies;
			}
		} catch (SQLException e) {
			System.err
					.println("An error occurred while fetching the total number of visitors report: " + e.getMessage());
			e.printStackTrace();
		}
		if (!haveData) {
			System.out.println("not have data...");

			return null;
		}

		// Construct and return the report
		int month = visitReport.getMonth(); // Extract month from reportDate
		String comment = "Automatically generated report based on month and park ID.";

		// Corrected to match the constructor's parameters
		VisitorsReport report = new VisitorsReport(-1, Report.ReportType.VISITOR, parkNumber, reportDate, month,
				comment, totalIndividuals, totalGroups, totalFamilies, totalVisitors);
		System.out.println(report.toString());
		return report;
	}

	/**
	 * Inserts a new visitor report into the database.
	 *
	 * @param report The VisitorsReport object representing the report to be inserted.
	 * @return True if the insertion is successful, false otherwise.
	 */
	public boolean insertVisitorReport(VisitorsReport report) {
		// First, insert into the 'report' table.
		System.out.println("in db insert...");

		String insertReportSql = "INSERT INTO `report` (reportType, parkID, date, month, comment) VALUES (?, ?, ?, ?, ?)";
		// Assuming 'visitorsreports' is the correct table name and it also includes a
		// reference to 'reportID'.
		String insertVisitorsReportSql = "INSERT INTO `VisitorsReport` (reportID, parkNumber, totalIndividualVisitors, totalGroupVisitors, totalFamilyVisitors, totalVisitors) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement statementReport = connectionToDatabase.prepareStatement(insertReportSql,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementVisitorsReport = connectionToDatabase
						.prepareStatement(insertVisitorsReportSql)) {

			// Set values for the report table insert
			statementReport.setString(1, report.getReportType().toString()); // Assuming you have a method to get the
																				// report type as String
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

						// Set values for the visitorsreports table insert, using the generated
						// reportID.
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

	/**
	 * Retrieves a visitors report based on the report ID.
	 *
	 * @param inputReport The Report object containing the report ID.
	 * @return A VisitorsReport object containing the details of the specified report, or null if not found.
	 */
	public VisitorsReport getVisitorsReportByReportId(Report inputReport) {
		System.out.println("in db getVisitorsReportByReportId...");

		VisitorsReport visitorsReport = null;
		// Query to select visitor report details based on the reportID from the Report
		// object
		String query = "SELECT vr.reportID, vr.parkNumber, vr.totalIndividualVisitors, vr.totalGroupVisitors, vr.totalFamilyVisitors, vr.totalVisitors, r.reportType, r.date, r.month, r.comment "
				+ "FROM `VisitorsReport` vr JOIN `report` r ON vr.reportID = r.reportID " + "WHERE vr.reportID = ?";
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
					visitorsReport = new VisitorsReport(reportID, reportType, parkNumber, date, month, comment,
							totalIndividualVisitors, totalGroupVisitors, totalFamilyVisitors, totalVisitors);
				} else {
					System.out
							.println("No VisitorsReport found in database for Report ID: " + inputReport.getReportID());
				}
			}
		} catch (SQLException e) {
			System.err.println("An error occurred while fetching the visitors report: " + e.getMessage());
			e.printStackTrace();
		}

		return visitorsReport;
	}

	/**
	 * Retrieves a list of general reports for a specific park ID.
	 *
	 * @param parkId The ID of the park for which reports are to be retrieved.
	 * @return A list of Report objects containing the general reports for the specified park.
	 */
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
					Report.ReportType reportType = Report.ReportType.valueOf(reportTypeStr); // Convert the string to
																								// enum
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
		if (reports.isEmpty())
		{
			return null;
		}

		return reports;
	}

	/**
	 * Generates an updated cancellation report based on the provided criteria.
	 *
	 * @param cancellationReport The CancellationReport object containing the criteria for generating the report.
	 * @return A CancellationReport object containing the updated cancellation report, or null if no data is available.
	 */
	public CancellationReport getCancellationReport(CancellationReport cancellationReport) {
		System.out.println("In db getUpdatedCancellationReport...");
		System.out.println(cancellationReport.toString());

		Map<Integer, Integer> dailyCancellations = new HashMap<>();
		Map<Integer, Integer> dailyUnfulfilledOrders = new HashMap<>();

		Integer parkNumber = cancellationReport.getParkNumber();
		Integer monthNumber = cancellationReport.getMonthNumber();

		// Initialize both maps with all days of the month set to 0
		LocalDate reportDate = LocalDate.of(Year.now().getValue(), monthNumber, 1);
		LocalDate startOfMonth = reportDate.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate endOfMonth = reportDate.with(TemporalAdjusters.lastDayOfMonth());
		for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
			dailyCancellations.put(date.getDayOfMonth(), 0);
			dailyUnfulfilledOrders.put(date.getDayOfMonth(), 0);
		}

		// Query to fetch daily cancellations
		String cancellationQuery = "SELECT DAY(date) AS dayOfMonth, COUNT(*) AS count " + "FROM `order` "
				+ "WHERE parkNumber = ? AND YEAR(date) = ? AND MONTH(date) = ? " + "AND (orderStatus = 'CANCELED' OR orderStatus = 'CANCELEDBYSERVER')"
				+ "GROUP BY DAY(date)";

		String unfulfilledQuery = "SELECT DAY(date) AS dayOfMonth, COUNT(*) AS count " + "FROM `order` "
				+ "WHERE parkNumber = ? AND YEAR(date) = ? AND MONTH(date) = ? "
				+ "AND (orderStatus = 'CONFIRMED' OR orderStatus = 'NOTARRIVED') AND date < CURRENT_DATE() "
				+ "GROUP BY DAY(date)";

		// Process cancellations
		processQuery(dailyCancellations, parkNumber, reportDate, cancellationQuery);

		// Process unfulfilled orders
		processQuery(dailyUnfulfilledOrders, parkNumber, reportDate, unfulfilledQuery);

		// Check if both maps only contain zeros
		boolean cancellationsEmpty = dailyCancellations.values().stream().allMatch(count -> count == 0);
		boolean unfulfilledOrdersEmpty = dailyUnfulfilledOrders.values().stream().allMatch(count -> count == 0);

		if (cancellationsEmpty && unfulfilledOrdersEmpty) {
			System.out.println("No data available for the given criteria.");
			return null; // Indicate no data is available
		}

		// Set the updated maps in the report
		cancellationReport.setDailyCancellations(dailyCancellations);
		cancellationReport.setDailyUnfulfilledOrders(dailyUnfulfilledOrders);

		System.out.println(cancellationReport.toString());
		return cancellationReport;
	}

	/**
	 * Processes the specified SQL query to populate a daily map with data retrieved from the database.
	 *
	 * @param dailyMap    The Map<Integer, Integer> to populate with daily data, where the key is the day of the month
	 *                     and the value is the count of the corresponding metric.
	 * @param parkNumber  The ID of the park for which the data is being retrieved.
	 * @param reportDate  The LocalDate representing the date for which the data is being retrieved.
	 * @param query       The SQL query to execute for fetching the data.
	 */
	private void processQuery(Map<Integer, Integer> dailyMap, Integer parkNumber, LocalDate reportDate, String query) {
		try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
			statement.setInt(1, parkNumber);
			statement.setInt(2, reportDate.getYear());
			statement.setInt(3, reportDate.getMonthValue());

			System.out.println("Query Prepared: " + statement.toString()); // Diagnostic

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int dayOfMonth = resultSet.getInt("dayOfMonth");
					int count = resultSet.getInt("count");

					System.out.println("Day: " + dayOfMonth + ", Count: " + count); // Diagnostic print

					dailyMap.put(dayOfMonth, count);
				}
			}
		} catch (SQLException e) {
			System.err.println("An error occurred while fetching the report: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a new usage report based on the specified criteria.
	 *
	 * @param usageReport The UsageReport object representing the criteria for generating the report.
	 * @return A UsageReport object containing the newly generated report, or null if no data is available.
	 */
	public UsageReport getNewUsageReport(UsageReport usageReport) {

		UsageReport newReport = null;
		String query = "SELECT DAY(date) AS dayOfMonth, SUM(amountOfVisitors) AS dailyVisitors " + "FROM `order` "
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
		System.out.println("Executing query with parameters - Park Number: " + parkNumber + ", Year: "
				+ reportDate.getYear() + ", Month: " + usageReport.getMonth());

		try (PreparedStatement statement = connectionToDatabase.prepareStatement(query)) {
			statement.setInt(1, parkNumber);
			statement.setInt(2, reportDate.getYear());
			statement.setInt(3, usageReport.getMonth());

			System.out.println("Query Prepared: " + statement.toString()); // This line helps to see the prepared
																			// statement.

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int dayOfMonth = resultSet.getInt("dayOfMonth");
					int dailyVisitors = resultSet.getInt("dailyVisitors");

					System.out.println("Day: " + dayOfMonth + ", Visitors: " + dailyVisitors); // Diagnostic print

					// Check if park was fully occupied on this day
					boolean isFull = dailyVisitors >= parkCapacity;
					// Instead of storing a boolean, store the visitor count or 0 to indicate
					// full/not full
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
		newReport = new UsageReport(-1, UsageReport.ReportType.USAGE, parkNumber, reportDate, month, comment,
				dailyUsage, parkCapacity);
		System.out.println(newReport.toString());
		return newReport;
	}

	/**
	 * Inserts a new usage report into the database.
	 * This method first inserts data into the 'report' table and then into the 'UsageReport' table.
	 * It converts the daily usage map to a JSON string for storage in the 'UsageReport' table.
	 * @param report The UsageReport object containing the report data to be inserted.
	 * @return true if the insertion was successful, false otherwise.
	 */
	public boolean insertUsageReport(UsageReport report) {
		// First, insert into the 'report' table.
		System.out.println("in db insert...");

		String insertReportSql = "INSERT INTO `report` (reportType, parkID, date, month, comment) VALUES (?, ?, ?, ?, ?)";
		// Assuming 'usagereport' is the correct table name and it also includes a
		// reference to 'reportID'.
		String insertUsageReportSql = "INSERT INTO `UsageReport` (reportID, parkNumber, parkCapacity, dailyUsage) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statementReport = connectionToDatabase.prepareStatement(insertReportSql,
				Statement.RETURN_GENERATED_KEYS);
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

	/**
	 * Retrieves a usage report from the database based on the given Report ID.
	 *
	 * @param inputReport The Report object containing the ID of the report to retrieve.
	 * @return The UsageReport object retrieved from the database, or null if no report is found.
	 */
	public UsageReport getUsageReportByReportId(Report inputReport) {
		System.out.println("in db getUsageReportByReportId...");

		UsageReport usageReport = null;
		String query = "SELECT ur.reportID, ur.parkNumber, ur.parkCapacity, ur.dailyUsage, r.reportType, r.date, r.month, r.comment "
				+ "FROM `UsageReport` ur JOIN `report` r ON ur.reportID = r.reportID " + "WHERE ur.reportID = ?";

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
					Map<Integer, Integer> dailyUsage = gson.fromJson(dailyUsageJson,
							new TypeToken<Map<Integer, Integer>>() {
							}.getType());

					// Construct a new UsageReport object using the fetched data
					usageReport = new UsageReport(reportID, reportType, parkNumber, date, month, comment, dailyUsage,
							parkCapacity);
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

	/**
	 * Return's all the orders that their status is orderStatus within 24hours from
	 * now.
	 * 
	 * @return ArrayList of orders.
	 */
	public ArrayList<Order> getOrdersByStatusInLastTwentyFourHours(String orderStatus) {
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "SELECT * FROM `order` WHERE orderStatus = ? AND CONCAT(date, ' ', visitTime) BETWEEN ? AND ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, orderStatus);
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime twentyFourHoursAhead = LocalDateTime.now().plusHours(24);

			// Convert LocalDateTime to Timestamp
			Timestamp nowTimestamp = Timestamp.valueOf(now);
			Timestamp twentyFourHoursAheadTimestamp = Timestamp.valueOf(twentyFourHoursAhead);

			ps.setTimestamp(2, nowTimestamp);
			ps.setTimestamp(3, twentyFourHoursAheadTimestamp);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer travelerId = rs.getInt("travelerId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");

				Order order = new Order(orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
				orders.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	
	/**
	 * Get's an existing order from the database based on its ID.
	 * 
	 * @param orderId The ID of the order to get.
	 * @return Order information
	 */
	public Order getOrderbyId(Integer orderId) {
		Order order = null;
		String deleteQuery = "SELECT * FROM `order` WHERE orderId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(deleteQuery)) {
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Integer id = rs.getInt("orderId");
				Integer travelerId = rs.getInt("travelerId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");

				order = new Order(id, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime,
						statusStr, typeOfOrderStr, telephoneNumber, parkName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/** 
	  * This function return orders in waiting list that can replace the can canceled order. 
	  *  
	  * @param parameters ArrayList containing: parkId,maxVisitors in the park, 
	  *                   estimatedStayTime in the park, date of the canceled order, timeToCheck of the canceled order, 
	  *                    
	  * @return WaitingList object containing matching order. 
	  */ 
	 public ArrayList<WaitingList> findPlaceInWaiting(Order order) { 
	  ArrayList<WaitingList> waitingArray = new ArrayList<WaitingList>(); 
	  Order orderToCheck = null; 
	  WaitingList result; 
	  Integer parkNumber = order.getParkNumber(); 
	  Park park = getParkDetails(parkNumber); 
	  LocalDate dateToCancel = order.getDate(); 
	  LocalTime visitTimeToCheck = order.getVisitTime(); 
	  Integer amtOfVisitors = order.getAmountOfVisitors(); 
	  int estimatedStayTime = park.getStaytime(); 
	   
	  LocalTime startTime = visitTimeToCheck.minusHours(estimatedStayTime-1); 
	  LocalTime endTime = visitTimeToCheck.plusHours(estimatedStayTime-1); 
	 
	  orderToCheck = new Order(null, null, parkNumber, amtOfVisitors, null, null 
	    ,dateToCancel, visitTimeToCheck, null, null, null, null); 
	 
	  try (PreparedStatement ps = connectionToDatabase.prepareStatement( 
	    "SELECT * FROM waitinglist WHERE parkNumber = ? AND " 
	                  + "date = ? AND visitTime BETWEEN ? AND ? AND orderStatus NOT IN (?)")) { 
	         ps.setInt(1, parkNumber); 
	         ps.setDate(2, java.sql.Date.valueOf(dateToCancel)); 
	         ps.setTime(3, java.sql.Time.valueOf(startTime)); 
	         ps.setTime(4, java.sql.Time.valueOf(endTime)); 
	         ps.setString(5,"HAS_SPOT"); 
	 
	         try (ResultSet rs = ps.executeQuery()) { 
	             while (rs.next()) { 
	                 Integer orderId = rs.getInt("orderId"); 
	                 Integer travelerId = rs.getInt("travelerId"); 
	                 Integer waitingListId = rs.getInt("waitingListId"); 
	                 Integer parkNum = rs.getInt("parkNumber"); 
	                 Integer amtVisitorsWaiting = rs.getInt("amountOfVisitors"); 
	                 Float price = rs.getFloat("price"); 
	                 String visitorEmail = rs.getString("visitorEmail"); 
	                 LocalDate date = rs.getDate("date").toLocalDate(); 
	                 LocalTime visitTime = rs.getTime("visitTime").toLocalTime(); 
	                 String statusStr = rs.getString("orderStatus"); 
	                 String typeOfOrderStr = rs.getString("typeOfOrder"); 
	                 String telephoneNumber = rs.getString("TelephoneNumber"); 
	                 String parkName = rs.getString("parkName"); 
	                 Integer placeInList = rs.getInt("placeInList"); 
	                  
	                 orderToCheck.setAmountOfVisitors(amtVisitorsWaiting); 
	                  
	                 if (findOrdersWithinDates(orderToCheck,true)) { 
	                     result = new WaitingList(orderId, travelerId, parkNum, amtVisitorsWaiting, price, visitorEmail, 
	                             date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName, waitingListId, 
	                             placeInList); 
	                     waitingArray.add(result); 
	                 } 
	             } 
	         } 
	     } catch (SQLException e) { 
	         e.printStackTrace(); 
	     } 
	 
	     return waitingArray; 
	 }

	 /**
	  * Retrieves a list of pending notification orders for a given traveler ID.
	  * This method fetches orders from the database that belong to the specified traveler and have a status of "PENDING_EMAIL_SENT".
	  * It returns an ArrayList containing the retrieved orders.
	  * @param travelerIdToCheckPendingNotifications The ID of the traveler for whom pending notification orders are to be retrieved.
	  * @return An ArrayList of Order objects representing the pending notification orders for the specified traveler.
	  */
	public ArrayList<Order> getPendingNotificationsOrdersByID(Integer travelerIdToCheckPendingNotifications) {
		// Checks if traveler has any pending notifications and return their orders
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "SELECT * FROM `order` WHERE travelerId = ? AND orderStatus = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, travelerIdToCheckPendingNotifications);
			ps.setString(2, "PENDING_EMAIL_SENT");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer travelerId = rs.getInt("travelerId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");

				Order order = new Order(orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
				orders.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Updates the status of an existing waitinglist in the database.
	 * 
	 * @param containing the waitingListId and the new status.
	 * @return true if the update was successful, false otherwise.
	 */
	public Boolean updateWaitingStatusArray(ArrayList<?> info) {
		String query = "UPDATE `waitinglist` SET orderStatus = ? WHERE waitingListId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, (String) info.get(0));
			ps.setInt(2, Integer.parseInt((String) info.get(1)));

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("waitinglist status updated successfully.");
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
	 * Get's an existing waitinglist from the database based on its ID.
	 * 
	 * @param orderId The ID of the order to get.
	 * @return Order information
	 */
	public WaitingList getWaitingbyId(Integer waitingListId) {
		WaitingList waiting = null;
		String deleteQuery = "SELECT FROM `waitinglist` WHERE orderId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(deleteQuery)) {
			ps.setInt(1, waitingListId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer waitingId = rs.getInt("waitingListId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");
				Integer placeInList = rs.getInt("placeInList");

				waiting = new WaitingList(orderId, waitingId, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName, waitingId, placeInList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return waiting;
	}

	/**
	 * Return's all the waitingList that their status is "HAS_SPOT" .
	 * 
	 * @return ArrayList of waitingList.
	 */
	public ArrayList<WaitingList> getHasSpotOrders() {
		ArrayList<WaitingList> waitingArray = new ArrayList<WaitingList>();
		String query = "SELECT * FROM `waitinglist` WHERE orderStatus = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, "HAS_SPOT");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer travelerId = rs.getInt("travelerId");
				Integer waitingId = rs.getInt("waitingListId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");
				Integer placeInList = rs.getInt("placeInList");

				WaitingList waiting = new WaitingList(orderId, travelerId, parkNumber, amountOfVisitors, price,
						visitorEmail, date, visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName, waitingId,
						placeInList);
				waitingArray.add(waiting);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return waitingArray;
	}

	/**
	 * Inserts a new reservation (order) into the database.
	 * 
	 * @param traveler The traveler making the reservation.
	 * @param order    The details of the order being made.
	 * @return true if insertion was successful, false otherwise.
	 */
	public Boolean insertWaitingOrder(WaitingList waiting) {// Adjusting the query to match the database schema order
															// provided
		String query = "INSERT INTO `order` (orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder, parkName)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {// Set parameters based on the Order
																					// object fields, in the order
																					// specified
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
	 * Return's all the orders that already been notified today
	 * 
	 * @return ArrayList of orders.
	 */
	public ArrayList<OrderNotification> getTodayNotificationsWithStatus(String status) {

		// Gets all the notifications for orders of today by getting them from
		// orderNotifications table
		ArrayList<OrderNotification> ordersAlreadyNotified = new ArrayList<OrderNotification>();
		String query = "SELECT * FROM orderNotifications " + "WHERE dateOfNotification = ?  AND status = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {

			ps.setDate(1, Date.valueOf(LocalDate.now()));
			ps.setString(2, status);

			ResultSet rs = ps.executeQuery();
			// Process the results
			while (rs.next()) {
				// Retrieve data from the result set
				int orderId = rs.getInt("orderId");
				LocalDate dateOfNotification = rs.getDate("dateOfNotification").toLocalDate();
				LocalTime startNotification = rs.getTime("startNotification").toLocalTime();
				LocalTime endNotification = rs.getTime("endNotification").toLocalTime();
				String notificationStatus = rs.getString("status");

				// Add notification to arraylist
				ordersAlreadyNotified.add(new OrderNotification(orderId, dateOfNotification, startNotification,
						endNotification, notificationStatus));
			}
		}	
		 catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
		}
		 catch (SQLException e) {
			e.printStackTrace();
			return ordersAlreadyNotified;
		}

		return ordersAlreadyNotified;

	}


	/**
	 * Posts an order notification to the database.
	 * @param notificationForAnOrder The OrderNotification object containing the information to be posted.
	 */
	public void postOrderNotification(OrderNotification notificationForAnOrder) {
		// Add an order notification to the ordernotifications table
		String query = "INSERT INTO `ordernotifications` (orderId, dateOfNotification, startNotification, endNotification, status) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
			// Set values for the parameters
			pstmt.setInt(1, notificationForAnOrder.getOrderId());
			pstmt.setDate(2, Date.valueOf(notificationForAnOrder.getDateOfNotification()));
			pstmt.setTime(3, Time.valueOf(notificationForAnOrder.getStartNotification()));
			pstmt.setTime(4, Time.valueOf(notificationForAnOrder.getEndNotification()));
			pstmt.setString(5, notificationForAnOrder.getStatus());

			// Execute the query
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change the status of a notification based on the orderid and the status we
	 * want to change to
	 * 
	 * @return Boolean if succeeded or not
	 */
	public Boolean changeStatusOfNotification(Integer orderId, String statusToChangeTo) {
		String query = "UPDATE `orderNotifications` SET  " + "status = ? WHERE orderId = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, statusToChangeTo);
			ps.setInt(2, orderId);

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Notification status updated successfully.");
				return true;
			}

		} catch (SQLException e) {
			System.out.println("An error occurred while updating the order status:");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves the orders that have been canceled by the server for a given traveler ID.
	 * @param travelerIdToCheckCanceledNotifications The ID of the traveler for whom canceled notifications are to be checked.
	 * @return An ArrayList of Order objects representing the canceled orders for the specified traveler ID.
	 */
	public ArrayList<Order> getCanceledNotificationsOrdersByID(Integer travelerIdToCheckCanceledNotifications) {
		// Checks if traveler has any Canceled notifications and return their orders
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "SELECT * FROM `order` WHERE travelerId = ? AND orderStatus = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setInt(1, travelerIdToCheckCanceledNotifications);
			ps.setString(2, "CANCELEDBYSERVER");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer orderId = rs.getInt("orderId");
				Integer travelerId = rs.getInt("travelerId");
				Integer parkNumber = rs.getInt("parkNumber");
				Integer amountOfVisitors = rs.getInt("amountOfVisitors");
				Float price = rs.getFloat("price");
				String visitorEmail = rs.getString("visitorEmail");
				LocalDate date = rs.getDate("date").toLocalDate();
				LocalTime visitTime = rs.getTime("visitTime").toLocalTime();
				String statusStr = rs.getString("orderStatus");
				String typeOfOrderStr = rs.getString("typeOfOrder");
				String telephoneNumber = rs.getString("TelephoneNumber");
				String parkName = rs.getString("parkName");

				Order order = new Order(orderId, travelerId, parkNumber, amountOfVisitors, price, visitorEmail, date,
						visitTime, statusStr, typeOfOrderStr, telephoneNumber, parkName);
				orders.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Updates the order status for today in the database.
	 * @return True if the order status update was successful; false otherwise.
	 */
	public Boolean updateOrderStatusForToday() {
		// Format today's date in the same format as stored in the database
		LocalDate today = LocalDate.now();
		String todayStr = today.toString(); // Converts today's date to String in the format yyyy-MM-dd

		// SQL query to update the status of orders
		String query = "UPDATE `order` SET orderStatus = ? WHERE date = ? AND orderStatus = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			// Set the parameters for the query
			ps.setString(1, "NOTARRIVED"); // New status to set
			ps.setString(2, todayStr); // Today's date
			ps.setString(3, "CONFIRMED"); // Current status to look for

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println(affectedRows + " order(s) updated successfully.");
				return true;
			} else {
				System.out.println("No orders found with status CONFIRMED for today's date, or an error occurred.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates the waiting list status for today in the database.
	 * @return True if the waiting list status update was successful; false otherwise.
	 */
	public Boolean updateWaitingListStatusForToday() {
		// Format today's date in the same format as stored in the database
		LocalDate today = LocalDate.now();
		String todayStr = today.toString(); // Converts today's date to String in the format yyyy-MM-dd

		// SQL query to update the status of waiting list entries
		String query = "UPDATE `waitinglist` SET orderStatus = ? WHERE date = ? AND orderStatus = ?";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			// Set the parameters for the query
			ps.setString(1, "CANCELED"); // New status to set
			ps.setString(2, todayStr); // Today's date
			ps.setString(3, "PENDING"); // Current status to look for

			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println(affectedRows + " waiting list entry(ies) updated successfully.");
				return true;
			} else {
				System.out.println(
						"No waiting list entries found with status PENDING for today's date, or an error occurred.");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Updates a traveler's record to mark them as a guide.
	 *
	 * @param travelerId the ID of the traveler to be updated.
	 * @return true if the update is successful, false otherwise.
	 */
	public boolean ChangeTravelerToGuide(Traveler traveler) {

		String query = "UPDATE `traveler` SET GroupGuide = 1 WHERE id = ?";

		try (PreparedStatement pstmt = connectionToDatabase.prepareStatement(query)) {
			pstmt.setInt(1, traveler.getId());

			int affectedRows = pstmt.executeUpdate();

			// Check if the update was successful
			if (affectedRows > 0) {
				traveler.setIsGroupGuide(1);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Inserts a new group guide into the traveler table of the database.
	 * @param traveler The Traveler object representing the new group guide to be inserted into the database.
	 * @return True if the insertion was successful and the traveler is now considered a group guide; false otherwise.
	 */
	public boolean insertNewGroupGuide(Traveler traveler) {
		// Assuming you have a method getConnection() that returns a Connection object.

		String query = "INSERT INTO `traveler` (id, firstName, lastName, email, phoneNumber, GroupGuide, isloggedin) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
				traveler.setIsGroupGuide(1);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}

		return false;
	}

	/**
	 * Finding order within given times
	 * 
	 * @param parameters parkNumber, date, visitTime
	 * @return
	 */
	public Boolean findOrdersWithinDates(Order order, Boolean flag) {
		String query = "SELECT SUM(amountOfVisitors) AS SumVisitors FROM `order` WHERE parkNumber = ? AND date = ? AND visitTime >= ? AND visitTime <= ? "
				+ "AND orderStatus NOT IN (?, ?)";
		String parkId = String.valueOf(order.getParkNumber());
		Park park = getParkDetails(order.getParkNumber());
		LocalTime timeToCheck = order.getVisitTime();
		int estimatedStayTime = park.getStaytime();

		LocalDate visitDate = order.getDate();
		LocalTime startTime = timeToCheck.minusHours(estimatedStayTime - 1);
		LocalTime endTime = timeToCheck.plusHours(estimatedStayTime - 1);

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, parkId);
			ps.setDate(2, java.sql.Date.valueOf(visitDate));
			ps.setTime(3, java.sql.Time.valueOf(startTime));
			ps.setTime(4, java.sql.Time.valueOf(endTime));
			ps.setString(5, "CANCELED");
			ps.setString(6, "CANCELEDBYSERVER");

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int numberOfVisitors = rs.getInt("SumVisitors");

				if (numberOfVisitors + order.getAmountOfVisitors() <= park.getMaxVisitors() && flag) {
					return true;
				}
				if (numberOfVisitors + order.getAmountOfVisitors() >= park.getMaxVisitors() && !flag) {
					return false;
				}
				if (flag) {
					return false;
				} else {
					return true;
				}

			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Retrieves a list of available dates and visit times for scheduling an order.
	 * @param order The Order object representing the order for which available dates and visit times are to be determined.
	 * @return An ArrayList containing strings representing available dates and visit times in the format "YYYY-MM-DD, HH:MM".
	 */
	public ArrayList<String> getAvailableDatesList(Order order) {
		Order tempOrder = new Order(order.getOrderId(), order.getVisitorId(), order.getParkNumber(),
				order.getAmountOfVisitors(), order.getPrice(), order.getVisitorEmail(), order.getDate(),
				order.getVisitTime(), order.getOrderStatus(), order.getTypeOfOrder(), order.getTelephoneNumber(),
				order.getParkName());
		LocalDate originalDate = order.getDate();

		LocalTime currentTime = LocalTime.parse("08:00");
		LocalDate currentDate = originalDate;
		int counter = 0;
		ArrayList<String> availableDatesList = new ArrayList<String>();
		while (counter != 70) {
			tempOrder.setDate(currentDate);
			tempOrder.setVisitTime(currentTime);
			;

			if (findOrdersWithinDates(tempOrder, false)) {
				availableDatesList.add(tempOrder.getDate().toString() + ", " + tempOrder.getVisitTime().toString());
				counter++;
			}

			LocalTime hour = currentTime.plusHours(1);

			if (hour.isAfter(LocalTime.of(17, 0))) {
				currentTime = LocalTime.parse("08:00");

				// Date after adding the days to the given date
				currentDate = currentDate.plusDays(1);
			} else {
				currentTime = currentTime.plusHours(1);
			}
		}
		return availableDatesList;
	}

	/**
	 * Deletes expired order alerts from the ordernotifications table.
	 */
	public void deleteExpiredOrderAlerts() {
		String query = "DELETE FROM ordernotifications " + "WHERE dateOfNotification = ? AND endNotification < ? ";

		try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
			ps.setString(1, LocalDate.now().toString());
			ps.setString(2, LocalTime.now().toString());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Could not execute deleteExpiredOrderAlerts query");
			e.printStackTrace();
		}

	}

	/**
	 * Updates the order status for a single order identified by its order ID.
	 * @param info An ArrayList containing information about the new order status and the order ID. The first element of the ArrayList 
	 * 		  is the status, and the second element is the orderId
	 * @return true if the order status was successfully updated, false otherwise.
	 */
	public Boolean updateOrderStatusArray(ArrayList<?> info) {
	    String query = "UPDATE `order` SET orderStatus = ? WHERE orderId = ?";

	        try (PreparedStatement ps = connectionToDatabase.prepareStatement(query)) {
	            ps.setString(1, (String) info.get(0));
	            ps.setInt(2, Integer.parseInt((String) info.get(1))); 

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
	
	
}