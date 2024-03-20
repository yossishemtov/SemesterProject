package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Order implements Serializable{

	private Integer parkNumber;
	private Integer amountOfVisitors;
	private Integer orderId;
	private Integer visitorId;
	private Float price;
	private String visitorEmail;
	private LocalDate date;
	private LocalTime visitTime;
	private String telephoneNumber; // Added TelephoneNumber field
	private status orderStatus;
	private typeOfOrder orderType;

	private enum status {

		PENDING, NOTARRIVED, INPARK, CONFIRM, CANCEL,COMPLETED
	}

	private enum typeOfOrder {
		SOLO, FAMILY, GUIDEDGROUP
	}

	// Adjusted constructor to include typeOfOrderStr and telephoneNumber
	public Order(Integer orderId, Integer visitorId, Integer parkNumber, Integer amountOfVisitors, Float price,
			String visitorEmail, LocalDate date, LocalTime visitTime, String statusStr, String typeOfOrderStr,
			String telephoneNumber) {
		
		this.orderId = orderId;
		this.visitorId = visitorId;
		this.parkNumber = parkNumber;
		this.amountOfVisitors = amountOfVisitors;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
		this.telephoneNumber = telephoneNumber; // Set the telephone number

		
		// Convert the String status to the enum status
		if(statusStr!= null) {
			try {
				this.orderStatus = Order.status.valueOf(statusStr.toUpperCase());
			} catch (IllegalArgumentException e) {
				this.orderStatus = Order.status.PENDING; // Default to PENDING if conversion fails
			}
		}else {
			this.orderStatus = null;
		}

		// Convert the String typeOfOrder to the enum typeOfOrder
		
		if(typeOfOrderStr != null) {
			
			try {
				this.orderType = Order.typeOfOrder.valueOf(typeOfOrderStr.toUpperCase().trim());
			} catch (IllegalArgumentException e) {
			
				this.orderType = Order.typeOfOrder.SOLO;
			}
			
		}else {
			this.orderType = null;
		}
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}


	public void setOrderType(String orderTypeStr) {
		try {
			this.orderType = Order.typeOfOrder.valueOf(orderTypeStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Handle the case where the provided string does not match any enum constants
			// For simplicity, defaulting to SOLO here
			this.orderType = Order.typeOfOrder.SOLO;
		}
	} 

	public String getTypeOfOrder() {
		return orderType.name();
	}

	// Getter for orderStatus that returns a String
	public String getOrderStatus() {
		return orderStatus.name(); // Convert the enum to String
	}



	public Integer getVisitorId() {
		return visitorId;
	}


	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}

	public Integer getAmountOfVisitors() {
		return amountOfVisitors;
	}

	public void setAmountOfVisitors(Integer amountOfVisitors) {
		this.amountOfVisitors = amountOfVisitors;
	}

	public Float getPrice() {
		return price;
	}

	public Integer getOrderId() {

		return orderId;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getVisitorEmail() {
		return visitorEmail;
	}

	public void setVisitorEmail(String visitorEmail) {
		this.visitorEmail = visitorEmail;
	}


	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(LocalTime visitTime) {
		this.visitTime = visitTime;
	}

	public Integer getParkNumber() {
		// TODO Auto-generated method stub
		return parkNumber;
	}

}
