package common;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order {
	private Integer parkNumber;
	private Integer amountOfVisitors;
	private Float price;
	private enum status{
		PENDING,
		CONFIRM,
		CANCEL
	}
	private String visitorEmail;
	private LocalDate date;
	private LocalTime visitTime;
	
	
	public Order(Integer parkNumber, Integer amountOfVisitors, Float price, String visitorEmail, LocalDate date,
			LocalTime visitTime) {
		this.parkNumber = parkNumber;
		this.amountOfVisitors = amountOfVisitors;
		this.price = price;
		this.visitorEmail = visitorEmail;
		this.date = date;
		this.visitTime = visitTime;
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
