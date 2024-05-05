package common;

import java.util.ArrayList;
import java.util.Arrays;

import client.ClientUI;

/**
 * The WaitingChecker class provides utility methods for checking and managing waiting lists.
 */
public class WaitingChecker {
    
    /**
     * Retrieves the ID of the last waiting list entry.
     *
     * @return The ID of the last waiting list entry.
     */
    public static Integer GetLastWaiting() {
        ClientServerMessage<?> getLast = new ClientServerMessage<>(null, Operation.GET_LAST_WAITINGLIST);
        ClientUI.clientControllerInstance.sendMessageToServer(getLast);
        ClientServerMessage<?> waitingIdMsg = ClientUI.clientControllerInstance.getData();
        Integer waitingId = (Integer) waitingIdMsg.getDataTransfered();
        return waitingId;
    }
    

    /**
     * Finds the right place in the waiting list for a new entry based on visit date, visit time, and park name.
     *
     * @param visitDate The date of the visit.
     * @param visitTime The time of the visit.
     * @param parkName  The name of the park.
     * @return The position in the waiting list where the new entry should be placed.
     */
    public static Integer FindRightPlaceInWaiting(String visitDate, String visitTime, String parkName) {
        ClientServerMessage<?> findPlace = new ClientServerMessage<>(new ArrayList<String>
                (Arrays.asList(parkName, visitDate, visitTime)), Operation.FIND_PLACE_IN_WAITING_LIST);
        ClientUI.clientControllerInstance.sendMessageToServer(findPlace);
        ClientServerMessage<?> placeMsg = ClientUI.clientControllerInstance.getData();
        Integer rightPlace = (Integer) placeMsg.getDataTransfered();
        return rightPlace;
    }


    /**
     * Posts a new entry to the waiting list.
     *
     * @param waiting The WaitingList object representing the new entry.
     * @return True if the new entry was successfully added to the waiting list, else false.
     */
    public static Boolean PostNewWaitingList(WaitingList waiting) {
        ClientServerMessage<?> waitingAttempt = new ClientServerMessage<>(waiting, Operation.POST_NEW_WAITING_LIST);
        ClientUI.clientControllerInstance.sendMessageToServer(waitingAttempt);
        ClientServerMessage<?> isNewWaitingMsg = ClientUI.clientControllerInstance.getData();
        Boolean isNewWaiting = (Boolean) isNewWaitingMsg.getFlag();
        return isNewWaiting;
    }
}
