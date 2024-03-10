package common;

public class Order {
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
		this.amountOfVisitors = amountOfVisitors;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
		this.orderstatus=orderstatus;
		this.ordertype=ordertype;
	}



    // Getter for orderStatus that returns a String
    public OrderStatus getOrderStatus() {
        return orderstatus; // Convert the enum to String
    }

    // Setter for orderStatus that takes a String
    public void setOrderStatus(String orderStatus) {
        this.orderstatus = orderstatus; 
    }



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