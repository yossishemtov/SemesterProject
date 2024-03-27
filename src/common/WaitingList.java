package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WaitingList extends Order {

    private static final long serialVersionUID = 1L;
    private Integer waitingListId;
    private Integer placeInList;
    private statusWaiting waitingListStatus;
    
    public enum statusWaiting {
		HAS_SPOT , CANCELED, CONFIRMED, PENDING
	}
    
    public WaitingList(Integer orderId, Integer visitorId, Integer parkNumber, Integer amountOfVisitors, Float price,
            String visitorEmail, LocalDate date, LocalTime visitTime, String statusStr, String typeOfOrderStr,
            String telephoneNumber, String parkName, Integer waitingListId, Integer placeInList) {
        super(orderId, visitorId, parkNumber, amountOfVisitors, price, visitorEmail, date, visitTime, statusStr, typeOfOrderStr,
                telephoneNumber, parkName);

        this.waitingListId = waitingListId;
        this.placeInList = placeInList;
        
        try {
            this.waitingListStatus = (statusStr != null) ? statusWaiting.valueOf(statusStr.toUpperCase())
                    : statusWaiting.PENDING;
        } catch (IllegalArgumentException e) {
            this.waitingListStatus = statusWaiting.PENDING;
        }
    }

    public statusWaiting getWaitingListStatus() {
        return waitingListStatus;
    }

    public void setWaitingListStatus(String waitingListStatusStr) {
        try {
            this.waitingListStatus = (waitingListStatusStr != null) ? statusWaiting.valueOf(waitingListStatusStr.toUpperCase())
                    : statusWaiting.PENDING;
        } catch (IllegalArgumentException e) {
            this.waitingListStatus = statusWaiting.PENDING;
        }
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