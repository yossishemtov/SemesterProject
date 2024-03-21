package common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

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

    public enum status {

		PENDING, NOTARRIVED, INPARK, CONFIRMED, CANCELED, COMPLETED, WAITING, PENDING_EMAIL_SENT, WAITING_HAS_SPOT
	}

	public enum typeOfOrder {
		SOLO, FAMILY, GUIDEDGROUP 
	}

	public Order(Integer orderId, Integer visitorId, Integer parkNumber, Integer amountOfVisitors, Float price,
            String visitorEmail, LocalDate date, LocalTime visitTime, String statusStr, String typeOfOrderStr,
            String telephoneNumber, String parkName) {

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
    }

	public Integer getVisitorId() {
		return visitorId;
	}

	public Integer getParkNumber() {
		return parkNumber;
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

	public String getOrderStatus() {
		return orderStatus.name();
	}

	public void setStatus(String orderStatusStr) {
		try {
			this.orderStatus = Order.status.valueOf(orderStatusStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			this.orderStatus = Order.status.PENDING;
		}
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

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	@Override
	public String toString() {
		return "Order{" + "parkNumber='" + parkNumber + '\'' + ", amountOfVisitors=" + amountOfVisitors + ", orderId="
				+ orderId + ", visitorId=" + visitorId + ", price=" + price + ", visitorEmail='" + visitorEmail + '\''
				+ ", date=" + date + ", visitTime=" + visitTime + ", telephoneNumber='" + telephoneNumber + '\''
				+ ", orderStatus=" + orderStatus + ", orderType=" + orderType + '}';
	}

	public void setParkNumber(Integer parkNumber) {
		this.parkNumber = parkNumber;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
}
