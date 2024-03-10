package common;

public class Order {
<<<<<<< HEAD
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
		PENDING, CONFIRM, CANCEL
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
=======
	private int amountOfVisitors;
	private String orderId;
	private String parkId;
	private Integer travelerId;
	private String orderDate;
	private String orderTime;
	private Double price;
	private String visitorEmail;
	private String date;
	private String visitTime;
	private OrderStatus orderstatus;
	private OrderType ordertype;
	
	public enum OrderStatus {
		CONFIRMED, PENDING , CANCELED, PENDING_EMAIL_SENT, WAITING, WAITING_HAS_SPOT, ENTERED_THE_PARK,COMPLETED;

		@Override
		public String toString() {
			
			
			switch (this) {
			case CONFIRMED:
				return "Order Confirmed";
			case PENDING:
				return "Order is Pending";
			case CANCELED:
				return "Order Canceled";
			case WAITING:
				return "Waiting";
			case ENTERED_THE_PARK:
				return "Entered the park";
			case PENDING_EMAIL_SENT:
				return "Pending, email has been sent";
			case WAITING_HAS_SPOT:
				return "Waiting has available spot";
			case COMPLETED:
				return "Visit has been completed";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
	
	public enum OrderType {
		SINGLE, FAMILY, GROUP;

		
		@Override
		public String toString() {
			switch (this) {
			case SINGLE:
				return "Single Visit";
			case FAMILY:
				return "Family Visit";
			case GROUP:
				return "Group Visit";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
	

	public Order(String orderId,String parkId, int amountOfVisitors, 
			String visitorEmail, String date, String visitTime, OrderStatus orderstatus, OrderType ordertype) {
		this.orderId=orderId;
		this.parkId = parkId;
>>>>>>> emanuelbranch
		this.amountOfVisitors = amountOfVisitors;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
<<<<<<< HEAD
		this.telephoneNumber = telephoneNumber; // Set the telephone number

		// Convert the String status to the enum status
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
			this.orderType = Order.typeOfOrder.SOLO;
		}
=======
		this.orderstatus=orderstatus;
		this.ordertype=ordertype;
>>>>>>> emanuelbranch
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

<<<<<<< HEAD
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
=======
    // Getter for orderStatus that returns a String
    public OrderStatus getOrderStatus() {
        return orderstatus; // Convert the enum to String
    }

    // Setter for orderStatus that takes a String
    public void setOrderStatus(String orderStatus) {
        this.orderstatus = orderstatus; 
    }
>>>>>>> emanuelbranch


	public Integer getVisitorId() {
		return visitorId;
	}

<<<<<<< HEAD
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
=======
	public int getAmountOfVisitors() {
		return amountOfVisitors;
	}


	public void setAmountOfVisitors(int amountOfVisitors) {
		this.amountOfVisitors = amountOfVisitors;
	}


	public Double getPrice() {
		return price;
	}
	
	public String getOrderId() {
>>>>>>> emanuelbranch
		return orderId;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getVisitorEmail() {
		return visitorEmail;
	}

	public void setVisitorEmail(String visitorEmail) {
		this.visitorEmail = visitorEmail;
	}

<<<<<<< HEAD
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
=======

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getVisitTime() {
		return visitTime;
	}


	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}



	public String getParkId() {
		return parkId;
	}



	public void setParkId(String parkId) {
		this.parkId = parkId;
	}



	public OrderType getOrdertype() {
		return ordertype;
	}



	public void setOrdertype(OrderType ordertype) {
		this.ordertype = ordertype;
	}



	public Integer getTravelerId() {
		return travelerId;
	}



	public void setTravelerId(Integer travelerId) {
		this.travelerId = travelerId;
	}



	public String getOrderDate() {
		return orderDate;
	}



	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}



	public String getOrderTime() {
		return orderTime;
	}



	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}



	public void setOrderStatus(OrderStatus orderstatus) {
		this.orderstatus = orderstatus;
	}
	
	
}
>>>>>>> emanuelbranch
