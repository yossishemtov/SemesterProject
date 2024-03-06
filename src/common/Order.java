package common;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
	private Integer parkNumber;
	private Integer amountOfVisitors;
	private Integer orderId;
	private Float price;
	private enum status{
		PENDING,
		CONFIRM,
		CANCEL
	}
	private String visitorEmail;
	private LocalDate date;
	private LocalTime visitTime;
	private status orderStatus;

	
	
	public Order(Integer orderID,Integer parkNumber, Integer amountOfVisitors, Float price, String visitorEmail, LocalDate date,
			LocalTime visitTime) {
		this.parkNumber = parkNumber;
		this.amountOfVisitors = amountOfVisitors;
		this.price = price;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
		this.orderStatus=status.PENDING;
	}



    // Getter for orderStatus that returns a String
    public String getOrderStatus() {
        return orderStatus.name(); // Convert the enum to String
    }

    // Setter for orderStatus that takes a String
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = status.valueOf(orderStatus); // Convert String to enum
    }

	public Integer getParkNumber() {
		return parkNumber;
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
	
	
}
