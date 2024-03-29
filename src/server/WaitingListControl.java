package server;

import java.util.ArrayList;
import java.util.Arrays;

import common.Order;
import common.WaitingList;

/**
 * The WaitingListControl class manages all travelers on the waiting list within the system.
 */

public class WaitingListControl {


	/**
	 * This method receives a canceled order and notifies the next order in the waiting list.
	 * 
	 * @param Order that has been canceled.
	 */
	public static void notifyPersonFromWaitingList(Order order) {
	    ArrayList<WaitingList> toNotify = getOrderFromWaitingList(order);

	    if (toNotify.isEmpty())
	        return;

	    for (WaitingList notify : toNotify) {
	        String waitingListId = String.valueOf(notify.getWaitingListId());
	        String status = "HAS_SPOT";
	        NotifyThread.getDC().updateWaitingStatusArray(new ArrayList<String>(Arrays.asList(status, waitingListId)));
	    }
	}


	private static ArrayList<WaitingList> getOrderFromWaitingList(Order order) {
		ArrayList<WaitingList> rightPlace = NotifyThread.getDC().findPlaceInWaiting(order);
		return rightPlace;
	}


}
