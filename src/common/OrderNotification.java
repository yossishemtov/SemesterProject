package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderNotification implements Serializable{
	private Integer orderId;
	private LocalDate dateOfNotification;
	private LocalTime startNotification;
	private LocalTime endNotification;
	
	
	public OrderNotification(Integer orderId, LocalDate dateOfNotification, LocalTime startNotification,
			LocalTime endNotification) {
		this.orderId = orderId;
		this.dateOfNotification = dateOfNotification;
		this.startNotification = startNotification;
		this.endNotification = endNotification;
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
