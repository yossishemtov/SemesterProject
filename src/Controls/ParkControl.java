//package Controls;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import client.ClientUI;
//import client.SystemClient;
//import logic.ClientToServerRequest;
//import logic.OrderTable;
//import logic.ClientToServerRequest.Request;
//import logic.Park;
//
///**
// * ParkControl class handles all the park related functionalities
// */
//@SuppressWarnings("unchecked")
//public class ParkControl {
//
//	/**
//	 * This function gets park's id and returns Park object.
//	 * 
//	 * @param parkId - park's id
//	 * @return Park object
//	 */
//	public static Park getParkById(String parkId) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_PARK_BY_ID,
//				new ArrayList<String>(Arrays.asList(parkId)));
//		ClientUI.clientControllerInstance.accept(request);
//		Park park = (Park) SystemClient.serverResponse.getResultSet().get(0);
//		return park;
//
//	}
//
//	/**
//	 * This function gets park's name and returns Park object.
//	 * 
//	 * @param parkName The park name
//	 * @return Park object
//	 */
//	public static Park getParkByName(String parkName) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_PARK_BY_NAME,
//				new ArrayList<String>(Arrays.asList(parkName)));
//		ClientUI.clientControllerInstance.accept(request);
//		Park park = (Park) SystemClient.serverResponse.getResultSet().get(0);
//		return park;
//	}
//
//	/**
//	 * This function returns an array list with all the park's names.
//	 * 
//	 * @return ArrayList with all the park's names
//	 */
//	public static ArrayList<String> getParksNames() {
//		ArrayList<Park> parks = getAllParks();
//		ArrayList<String> parksNames = new ArrayList<String>();
//		for (Park park : parks) {
//			parksNames.add(park.getParkName());
//		}
//		return parksNames;
//
//	}
//
//	/**
//	 * This function gets park's id and returns the park name.
//	 * 
//	 * @param parkId The park id
//	 * @return String - park name
//	 */
//	public static String getParkName(String parkId) {
//		Park park = getParkById(parkId);
//		if (park != null)
//			return park.getParkName();
//		return "Park";
//
//	}
//
//	/**
//	 * This function returns an array list with all the parks in the DB.
//	 * 
//	 * @return ArrayList of Park objects
//	 */
//	public static ArrayList<Park> getAllParks() {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.GET_ALL_PARKS);
//		ClientUI.clientControllerInstance.accept(request);
//		return SystemClient.serverResponse.getResultSet();
//
//	}
//
//	/**
//	 * This function return the park Id of given order Id
//	 * 
//	 * @param orders  ArrayList of object OrderTb, containing orders
//	 * @param orderId int variable, the order we would like the park for.
//	 * 
//	 * @return park id on success, -1 if the park was not found.
//	 */
//	public static int getParkIdByOrderId(ArrayList<OrderTable> orders, int orderId) {
//		for (OrderTable o : orders)
//			if (o.getOrderId() == orderId)
//				return o.getParkId();
//		return -1;
//	}
//
//	/**
//	 * This function return the park Id of given order Id
//	 * 
//	 * @param pId int variable, the park Id to update
//	 * @param num int variable, the total number of visitors in the park
//	 * 
//	 */
//	public static void updateCurrentVisitors(int pId, int num) {
//		String cVisitors = String.valueOf(num);
//		String parkId = String.valueOf(pId);
//		ClientToServerRequest<String> request = new ClientToServerRequest<String>(Request.UPDATE_CURRENT_VISITORS_ID,
//				new ArrayList<String>(Arrays.asList(cVisitors, parkId)));
//		ClientUI.clientControllerInstance.accept(request);
//	}
//
//	/**
//	 * this function changes park's parameters if Department Manager confirmed Park Manager's request.
//	 * 
//	 * @param changeParkParameterList containing the type of parameter to be changed, the new value, and park ID.
//	 */
//	public static void changeParkParameters(ArrayList<String> changeParkParameterList) {
//
//		ClientToServerRequest<?> request = new ClientToServerRequest<>(Request.CHANGE_PARK_PARAMETERS,
//				changeParkParameterList);
//
//		ClientUI.clientControllerInstance.accept(request);
//	}
//
//	/**
//	 * This function get a date and a park id and check if the park was full at that date.
//	 * 
//	 * @param date   The date to check
//	 * @param parkID The park to check
//	 * @return ArrayList of Strings with all the comments on that dates.
//	 */
//	public static ArrayList<String> isParkIsFullAtDate(String date, String parkID) {
//		ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.CHECK_IF_PARK_FULL_AT_DATE,
//				new ArrayList<String>(Arrays.asList(date, parkID)));
//		ClientUI.clientControllerInstance.accept(request);
//		return (ArrayList<String>) SystemClient.serverResponse.getResultSet();
//	}
//
//	/**
//	 * This function insert into "fullparkdate" table when the park is full.
//	 * 
//	 * @param park The park that is full
//	 */
//	public static void updateIfParkFull(Park park) {
//
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		LocalDateTime now = LocalDateTime.now();
//		String dateAndTime = dtf.format(now);
//		String date = dateAndTime.split(" ")[0];
//		// String time = dateAndTime.split(" ")[1];
//
//		String comment = park.getParkName() + " was full at: " + dateAndTime;
//
//		String parkId = String.valueOf(park.getParkId());
//		String maxVisitors = String.valueOf(park.getMaxVisitors());
//
//		if (park.getCurrentVisitors() >= park.getMaxVisitors()) {
//			ClientToServerRequest<String> request = new ClientToServerRequest<>(Request.INSERT_TO_FULL_PARK_DATE,
//					new ArrayList<String>(Arrays.asList(parkId, date, maxVisitors, comment)));
//			ClientUI.clientControllerInstance.accept(request);
//		}
//	}
//}
