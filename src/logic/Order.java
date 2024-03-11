package logic;

import java.io.Serializable;

/**
 * This class is responsible for the system's order
 */
@SuppressWarnings("serial")
public class Order implements Serializable{
	private int orderId;
	private String travelerId;
	private int parkId;
	private String orderDate;
	private String orderTime;
	private String orderType;
	private int numberOfParticipants;
	private String email;
	private double price;
	private String orderStatus;

	public Order(int orderId, String travelerId, int parkId, String orderDate, String orderTime, String orderType,
			int numberOfParticipants, String email, double price, String orderStatus) {
		super();
		this.orderId = orderId;
		this.travelerId = travelerId;
		this.parkId = parkId;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.orderType = orderType;
		this.numberOfParticipants = numberOfParticipants;
		this.email = email;
		this.price = price;
		this.orderStatus = orderStatus;
	}
	
	// Ofir Avraham Vaknin v2.
	public Order(String travelerId, int parkId, String orderDate, String orderTime, String orderType,
			int numberOfParticipants, String email, double price, String orderStatus) {
		super();
		this.orderId = 0;
		this.travelerId = travelerId;
		this.parkId = parkId;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.orderType = orderType;
		this.numberOfParticipants = numberOfParticipants;
		this.email = email;
		this.price = price;
		this.orderStatus = orderStatus;
	}
	
	// Ofir Avraham Vaknin v2.
		public Order(OrderTable o) {
			super();
			this.orderId = 0;
			this.travelerId = o.getTravelerId();
			this.parkId = o.getParkId();
			this.orderDate = o.getOrderDate();
			this.orderTime = o.getOrderTime();
			this.orderType = o.getOrderType();
			this.numberOfParticipants = o.getNumberOfParticipants();
			this.email = o.getEmail();
			this.price = o.getPrice();
			this.orderStatus = o.getOrderStatus();
		}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(int numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


}
