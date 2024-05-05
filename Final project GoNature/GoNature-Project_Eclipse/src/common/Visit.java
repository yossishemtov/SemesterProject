package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Visit class represents a visit made by a traveler to a park.
 * It contains information about the visit date, entering time, exiting time,
 * park number, and order ID.
 */
public class Visit implements Serializable {
	private static final long serialVersionUID = 1L;
	private LocalDate visitDate;
	private LocalTime enteringTime;
	private LocalTime existingTime;
	private Integer orderId;
	private Integer parkNumber;
	
	/**
     * Constructs a Visit object with the specified parameters.
     *
     * @param visitDate    The date of the visit.
     * @param enteringTime The time when the traveler enters the park.
     * @param exitingTime  The time when the traveler exits the park.
     * @param parkNumber   The number identifying the park.
     * @param orderId      The ID of the order associated with the visit.
     */
	public Visit(LocalDate visitDate, LocalTime enteringTime, LocalTime existingTime,Integer parkNumber, Integer orderId) {
		this.visitDate = visitDate;
		this.enteringTime = enteringTime;
		this.existingTime = existingTime;
		this.parkNumber = parkNumber;
		this.orderId = orderId;
	}


	public Integer getParkNumber() {
		return parkNumber;
	}


	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}


	public LocalDate getVisitDate() {
		return visitDate;
	}


	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}


	public LocalTime getEnteringTime() {
		return enteringTime;
	}


	public void setEnteringTime(LocalTime enteringTime) {
		this.enteringTime = enteringTime;
	}


	public LocalTime getExistingTime() {
		return existingTime;
	}


	public void setExistingTime(LocalTime existingTime) {
		this.existingTime = existingTime;
	}


	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	
}
