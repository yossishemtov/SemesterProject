package common;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import client.ClientUI;

public class OrderChecker {


	/**
	 * Checking if the date available based on stayTime, numberOfVisitors in order
	 * and gap, maxVisitors in park.
	 * @param order
	 * @return true if the date is available else false.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isDateAvailable(Order order) {
		String parkId = String.valueOf(order.getParkNumber());
		Park park = getPark(order.getParkNumber());
		LocalTime timeToCheck = order.getVisitTime();
		int estimatedStayTime = park.getStaytime();


		String visitDate = order.getDate().toString();
		String startTime = timeToCheck.minusHours(estimatedStayTime-1).toString();
		String endTime = timeToCheck.plusHours(estimatedStayTime-1).toString();
		
		ClientServerMessage<?> findDates = new ClientServerMessage<>(new ArrayList<String>
		(Arrays.asList(parkId, visitDate, startTime, endTime)), Operation.FIND_ORDERS_WITHIN_DATES);
		ClientUI.clientControllerInstance.sendMessageToServer(findDates);
	    ClientServerMessage<?> findDatesMsg = ClientUI.clientControllerInstance.getData();
	    ArrayList<Order> orders = (ArrayList<Order>) findDatesMsg.getDataTransfered();
    	
		int numberOfVisitors = 0; 
		for (Order ord : orders) {
			numberOfVisitors += ord.getAmountOfVisitors();
		}

		if (numberOfVisitors + order.getAmountOfVisitors() >= park.getMaxVisitors()) {
			return false;
		}

		return true;
	}
	
	
	public static Park getPark(Integer parkId) {
		ClientServerMessage<?> findPark = new ClientServerMessage<>(parkId, Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(findPark);
	    ClientServerMessage<?> findParkMsg = ClientUI.clientControllerInstance.getData();
	    Park park = (Park) findParkMsg.getDataTransfered();
	    return park;
	}
	
	/**
	 * Finds the Max order id and increment it to prevent adding same orderId to DB
	 * @return MAX(orderId)+1
	 */
	public static Integer getLastNumber() {
	    Integer lastOrder = null;
	    Integer newOrderId = 1;

	    // Create a message to request the last order ID
	    ClientServerMessage<?> getLastOrderID = new ClientServerMessage<>(null, Operation.GET_LAST_ORDER_ID);

	    // Send the message to the server
	    ClientUI.clientControllerInstance.sendMessageToServer(getLastOrderID);

	    // Receive the response from the server
	    ClientServerMessage<?> receivedLastOrderMessage = ClientUI.clientControllerInstance.getData();

	    // Check if the received message is of the correct type
	    if (receivedLastOrderMessage.getCommand().equals(Operation.GET_LAST_ORDER_ID)) {
	        // Extract the data from the message
	        lastOrder = (Integer) receivedLastOrderMessage.getDataTransfered();

	        // Check if the data is not null and extract the new order ID
	        if (lastOrder != null) {
	        	newOrderId = lastOrder + 1;
	        }

	    }

	    return newOrderId;
	}
	
	/**
	 This function returns the names of all the parks
	 */
	public static ArrayList<String> getParksNames() {
		ClientServerMessage<?> checkPark = new ClientServerMessage<>(null, Operation.GET_PARKS_INFO);
		ClientUI.clientControllerInstance.sendMessageToServer(checkPark);
		// Receive the response from the server
	    ClientServerMessage<?> ParksMsg = ClientUI.clientControllerInstance.getData();
		ArrayList<Park> parks = (ArrayList<Park>) ParksMsg.getDataTransfered();
		
		ArrayList<String> parksNames = new ArrayList<String>();
		for (Park park : parks) {
			parksNames.add(park.getName());
		}
		return parksNames;
	}
}
