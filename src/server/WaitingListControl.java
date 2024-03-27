package server;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;


import DB.DatabaseController;
import common.Park;
import common.WaitingList;

/**
 * The WaitingListControl class manages all travelers on the waiting list within the system.
 */

public class WaitingListControl {
	private  static DatabaseController DC;

	public WaitingListControl(DatabaseController DBController) {
		WaitingListControl.DC = DBController;
     
	}


	


	/**
	 * This method receives a canceled order and notifies the next order in the waiting list.
	 * 
	 * @param Order that has been canceled.
	 */
	public static void notifyPersonFromWaitingList(WaitingList waiting) {
	    Park park = DC.getParkDetails(waiting.getParkNumber());
	    ArrayList<WaitingList> toNotify = getOrderFromWaitingList(waiting);

	    if (toNotify.isEmpty())
	        return;

	    for (WaitingList notify : toNotify) {
	        String waitingListId = String.valueOf(notify.getWaitingListId());
	        String status = "HAS_SPOT";
	        DC.updateWaitingStatusArray(new ArrayList<String>(Arrays.asList(status, waitingListId)));
	    }
	}


	private static ArrayList<WaitingList> getOrderFromWaitingList(WaitingList waiting) {
		ArrayList<WaitingList> rightPlace = DC.findPlaceInWaiting(waiting);
		return rightPlace;
	}


}
