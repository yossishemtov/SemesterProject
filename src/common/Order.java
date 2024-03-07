package common;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
	private Integer parkNumber;
	private Integer amountOfVisitors;
	private Integer orderId;
	private Integer visitorId;
	private Float price;
	private enum status{
		PENDING,
		CONFIRM,
		CANCEL
	}
	private enum typeOfOrder{
		SOLO,
		FAMILY,
		GUIDEDGROUP
	}
	private String visitorEmail;
	private LocalDate date;
	private LocalTime visitTime;
	private status orderStatus;
	private typeOfOrder orderType;

	
<<<<<<< HEAD
	public Order(Integer orderID,Integer visitorId, Integer parkNumber, Integer amountOfVisitors, Float price, String visitorEmail, LocalDate date,
            LocalTime visitTime, String statusStr) { // Renamed the parameter to avoid confusion
   this.parkNumber = parkNumber;
   this.visitorId=visitorId;
   this.amountOfVisitors = amountOfVisitors;
   this.orderId = orderID;
   this.price = price;
   this.visitorEmail = visitorEmail;
   this.date = date;
   this.visitTime = visitTime;

   // Convert the String status to the enum type status before assigning
   try {
       this.orderStatus = Order.status.valueOf(statusStr.toUpperCase());
   } catch (IllegalArgumentException e) {
       this.orderStatus = Order.status.PENDING; // Default to PENDING if conversion fails
       // Log or handle the exception as needed
   }
}

//Adjusted to correctly use the enum for setting
public void setOrderStatus(String orderStatusStr) {
   try {
       this.orderStatus = Order.status.valueOf(orderStatusStr.toUpperCase());
   } catch (IllegalArgumentException e) {
       // Handle the case where the provided string does not match any enum constants
       // This could log an error, throw a custom exception, or set a default value
       this.orderStatus = Order.status.PENDING; // Example default handling
   }
}
=======
	
	public Order(Integer orderID,Integer parkNumber, Integer amountOfVisitors, Float price, String visitorEmail, LocalDate date,
			LocalTime visitTime, typeOfOrder orderType) {
		this.parkNumber = parkNumber;
		this.amountOfVisitors = amountOfVisitors;
		this.price = price;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
		this.orderStatus=status.PENDING;
		this.orderType = orderType;
	}
>>>>>>> ItayBranch



    // Getter for orderStatus that returns a String
    public String getOrderStatus() {
        return orderStatus.name(); // Convert the enum to String
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
