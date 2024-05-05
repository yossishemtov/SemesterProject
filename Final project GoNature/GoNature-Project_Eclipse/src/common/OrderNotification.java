package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The OrderNotification class represents a notification associated with an order.
 * It contains information such as the order ID, date of notification, start and end time of notification,
 * and status of the notification.
 */
public class OrderNotification implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer orderId;
	private LocalDate dateOfNotification;
	private LocalTime startNotification;
	private LocalTime endNotification;
	private String status;
	
	
	/**
     * Constructs a new OrderNotification object with the specified parameters.
     *
     * @param orderId            The ID of the order associated with the notification.
     * @param dateOfNotification The date of the notification.
     * @param startNotification  The start time of the notification.
     * @param endNotification    The end time of the notification.
     * @param status             The status of the notification.
     */
	public OrderNotification(Integer orderId, LocalDate dateOfNotification, LocalTime startNotification,
			LocalTime endNotification, String status) {
		this.orderId = orderId;
		this.dateOfNotification = dateOfNotification;
		this.startNotification = startNotification;
		this.endNotification = endNotification;
		this.status = status;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public LocalDate getDateOfNotification() {
		return dateOfNotification;
	}


	public void setDateOfNotification(LocalDate dateOfNotification) {
		this.dateOfNotification = dateOfNotification;
	}


	public LocalTime getStartNotification() {
		return startNotification;
	}


	public void setStartNotification(LocalTime startNotification) {
		this.startNotification = startNotification;
	}


	public LocalTime getEndNotification() {
		return endNotification;
	}


	public void setEndNotification(LocalTime endNotification) {
		this.endNotification = endNotification;
	}
}