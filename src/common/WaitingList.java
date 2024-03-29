package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The WaitingList class represents a waiting list for orders in the system.
 * It extends the Order class and adds attributes specific to the waiting list,
 * such as waiting list ID, place in the list, and waiting list status.
 */
public class WaitingList extends Order {

    private static final long serialVersionUID = 1L;
    private Integer waitingListId;
    private Integer placeInList;
    private statusWaiting waitingListStatus;
    
    /**
     * Enum representing the possible status of a waiting list.
     */
    public enum statusWaiting {
		HAS_SPOT , CANCELED, CONFIRMED, PENDING
	}
    
    /**
     * Constructs a WaitingList object with the specified parameters.
     *
     * @param orderId        The ID of the order associated with the waiting list.
     * @param visitorId      The ID of the visitor making the order.
     * @param parkNumber     The number identifying the park.
     * @param amountOfVisitors The number of visitors in the order.
     * @param price          The price of the order.
     * @param visitorEmail   The email of the visitor making the order.
     * @param date           The date of the order.
     * @param visitTime      The time of the visit.
     * @param statusStr      The status of the order as a string.
     * @param typeOfOrderStr The type of order as a string.
     * @param telephoneNumber The telephone number of the visitor.
     * @param parkName       The name of the park associated with the order.
     * @param waitingListId  The ID of the waiting list.
     * @param placeInList    The position of the order in the waiting list.
     */
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