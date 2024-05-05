package common;

import java.util.ArrayList;

import client.ClientUI;

/**
 * The OrderChecker class provides utility methods for checking orders and parks.
 */
public class OrderChecker {

    /**
     * Checking if the date is available based on stayTime, numberOfVisitors in order
     * and gap, maxVisitors in park.
     *
     * @param order The order to be checked for date availability.
     * @return True if the date is available, else false.
     */
    public static boolean isDateAvailable(Order order) {
        ClientServerMessage<?> findDates = new ClientServerMessage<>(order, Operation.FIND_ORDERS_WITHIN_DATES);
        ClientUI.clientControllerInstance.sendMessageToServer(findDates);
        ClientServerMessage<?> findDatesMsg = ClientUI.clientControllerInstance.getData();
        Boolean isAvailable = (Boolean) findDatesMsg.getFlag();
        return isAvailable;
    }

    /**
     * Retrieves park details by park ID.
     *
     * @param parkId The ID of the park.
     * @return The Park object containing details of the park.
     */
    public static Park getParkById(Integer parkId) {
        ClientServerMessage<?> findPark = new ClientServerMessage<>(parkId, Operation.GET_PARK_DETAILS);
        ClientUI.clientControllerInstance.sendMessageToServer(findPark);
        ClientServerMessage<?> findParkMsg = ClientUI.clientControllerInstance.getData();
        Park park = (Park) findParkMsg.getDataTransfered();
        return park;
    }

    /**
     * Retrieves park details by park name.
     *
     * @param parkName The name of the park.
     * @return The Park object containing details of the park.
     */
    public static Park getParkByName(String parkName) {
        ClientServerMessage<?> checkPark = new ClientServerMessage<>(parkName, Operation.GET_PARK_BY_NAME);
        ClientUI.clientControllerInstance.sendMessageToServer(checkPark);
        // Receive the response from the server
        ClientServerMessage<?> ParkMsg = ClientUI.clientControllerInstance.getData();
        Park park = (Park) ParkMsg.getDataTransfered();
        return park;
    }

    /**
     * Finds the maximum order ID and increments it to prevent adding the same orderId to the database.
     *
     * @return The maximum order ID incremented by 1.
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
     * Returns the names of all the parks.
     *
     * @return An ArrayList containing the names of all parks.
     */
    @SuppressWarnings("unchecked")
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
