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

	private static DatabaseController DC = new DatabaseController("root","Aa123456");
	

	/**
	 * This method receives a canceled order and notifies the next order in the waiting list.
	 * 
	 * @param Order that has been canceled.
	 */
	public static void notifyPersonFromWaitingList(WaitingList waiting) {
		Park park = DC.getParkDetails(waiting.getParkNumber());
		WaitingList toNotify = getOrderFromWaitingList(waiting.getDate().toString(), waiting.getVisitTime().toString(), park);

		if (toNotify == null)
			return;

		String waitingListId = String.valueOf(toNotify.getWaitingListId()+"");
		String status = "HAS_SPOT";
		DC.updateWaitingStatusArray(new ArrayList<String>(Arrays.asList(status, waitingListId)));

	}

	private static WaitingList getOrderFromWaitingList(String date, String hour, Park park) {
		String parkId = String.valueOf(park.getParkNumber());
		String maxVisitors = String.valueOf(park.getMaxVisitors());
		String estimatedStayTime = String.valueOf(park.getStaytime());
		ArrayList<String> parameters = new ArrayList<String>(
				Arrays.asList(parkId, maxVisitors, estimatedStayTime, date, hour));
		WaitingList rightPlace = DC.findPlaceInWaiting(parameters);
		return rightPlace;
	}


}
