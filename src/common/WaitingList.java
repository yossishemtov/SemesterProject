package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WaitingList extends Order {
	
	private static final long serialVersionUID = 1L;
	private Integer waitingListId;
	private Integer placeInList;

	public WaitingList(Integer waitingListId, Integer placeInList, Integer orderId, Integer visitorId, Integer parkNumber, String parkName, Integer amountOfVisitors, Float price,
			String visitorEmail, LocalDate date, LocalTime visitTime, String statusStr, String typeOfOrderStr,
			String telephoneNumber) {
		super(orderId, visitorId, parkNumber, parkName, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr,
				telephoneNumber);
		
		this.waitingListId = waitingListId;
		this.placeInList = placeInList;
	}

	public void setWaitingListId(Integer waitingListId) {
		this.waitingListId = waitingListId;
	}
	
	public void setPlaceInList(Integer placeInList) {
		this.placeInList = placeInList;
	}
	
	public Integer getWaitingListId() {
		return waitingListId;
	}
	
	public Integer getPlaceInList() {
		return placeInList;
	}

}
