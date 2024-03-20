package common;

import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalTime;

public class Order implements Serializable {

	private Integer parkNumber;
	private Integer amountOfVisitors;
	private Integer orderId;
	private Integer visitorId;
	private Float price;
	private String visitorEmail;
	private LocalDate date;
	private LocalTime visitTime;
	private String telephoneNumber; // Added TelephoneNumber field
	private String parkName;
	private status orderStatus;
	private typeOfOrder orderType;

	private enum status {
		PENDING, CONFIRMED, CANCELED, NOTARRIVED, INPARK
	}

	private enum typeOfOrder {
		SOLO, FAMILY, GUIDEDGROUP
	}

	// Adjusted constructor to include typeOfOrderStr and telephoneNumber
	public Order(Integer orderId, Integer visitorId, Integer parkNumber, String parkName, Integer amountOfVisitors, Float price,
			String visitorEmail, LocalDate date, LocalTime visitTime, String statusStr, String typeOfOrderStr,
			String telephoneNumber) {

		this.orderId = orderId;
		this.visitorId = visitorId;
		this.parkNumber = parkNumber;
		this.parkName = parkName;
		this.amountOfVisitors = amountOfVisitors;
		this.visitorEmail = visitorEmail;
		this.price = price;
		this.date = date;
		this.visitTime = visitTime;
		this.telephoneNumber = telephoneNumber; // Set the telephone number
		
		  try {
	            this.orderStatus = (statusStr != null) ? Order.status.valueOf(statusStr.toUpperCase())
	                    : Order.status.PENDING;
	        } catch (IllegalArgumentException e) {
	            this.orderStatus = Order.status.PENDING;
	        }

	        try {
	            this.orderType = (typeOfOrderStr != null) ? Order.typeOfOrder.valueOf(typeOfOrderStr.toUpperCase())
	                    : Order.typeOfOrder.SOLO;
	        } catch (IllegalArgumentException e) {
	            this.orderType = Order.typeOfOrder.SOLO;
	        }
	    

		/*// Convert the String status to the enum status
		try {
			this.orderStatus = Order.status.valueOf(statusStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			this.orderStatus = Order.status.PENDING; // Default to PENDING if conversion fails
		}

		// Convert the String typeOfOrder to the enum typeOfOrder
		try {
			this.orderType = Order.typeOfOrder.valueOf(typeOfOrderStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			// Handle the error or default case here
			// For example, default to SOLO if the conversion fails
			this.orderType = Order.typeOfOrder.FAMILY;

		}*/
	}

	public void setStatus(String orderStatusStr) {
        try {
            this.orderStatus = Order.status.valueOf(orderStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.orderStatus = Order.status.PENDING;
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

	// public LocalDate getDate() {
	// return date;
	// }
	
	@Override
    public String toString() {
        return "Order{" + "parkNumber='" + parkNumber + '\'' + ", amountOfVisitors=" + amountOfVisitors + ", orderId="
                + orderId + ", visitorId=" + visitorId + ", price=" + price + ", visitorEmail='" + visitorEmail + '\''
                + ", date=" + date + ", visitTime=" + visitTime + ", telephoneNumber='" + telephoneNumber + '\''
                + ", orderStatus=" + orderStatus + ", orderType=" + orderType + '}';
    }
	
	public String getDate() {
		return date.toString(); // Convert LocalDate to String
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getVisitTime() {
		return visitTime.toString(); // Convert LocalTime to String
	}

	public void setVisitTime(LocalTime visitTime) {
		this.visitTime = visitTime;
	}

	public Integer getParkNumber() {
		// TODO Auto-generated method stub
		return parkNumber;
	}
	
	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

}
