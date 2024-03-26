package common;

import java.util.ArrayList;
import java.util.Arrays;

import client.ClientUI;

public class WaitingChecker {
	
	public static Integer GetLastWaiting() {
		ClientServerMessage<?> getLast = new ClientServerMessage<>(null, Operation.GET_LAST_WAITINGLIST);
		ClientUI.clientControllerInstance.sendMessageToServer(getLast);
		ClientServerMessage<?> waitingIdMsg = ClientUI.clientControllerInstance.getData();
		Integer waitingId = (Integer) waitingIdMsg.getDataTransfered();
		return waitingId;
	}
	


	public static Integer FindRightPlaceInWaiting(String visitDate, String visitTime, String parkName) {
		ClientServerMessage<?> findPlace = new ClientServerMessage<>(new ArrayList<String>
		(Arrays.asList(parkName, visitDate, visitTime)), Operation.FIND_PLACE_IN_WAITING_LIST);
		ClientUI.clientControllerInstance.sendMessageToServer(findPlace);
	    ClientServerMessage<?> placeMsg = ClientUI.clientControllerInstance.getData();
	    Integer rightPlace = (Integer) placeMsg.getDataTransfered();
	    return rightPlace;
	}



	public static Boolean PostNewWaitingList(WaitingList waiting) {
		ClientServerMessage<?> waitingAttempt = new ClientServerMessage<>(waiting, Operation.POST_NEW_WAITING_LIST);
		ClientUI.clientControllerInstance.sendMessageToServer(waitingAttempt);
	    ClientServerMessage<?> isNewWaitingMsg = ClientUI.clientControllerInstance.getData();
    	Boolean isNewWaiting = (Boolean) isNewWaitingMsg.getFlag();
    	return isNewWaiting;
	}
}
