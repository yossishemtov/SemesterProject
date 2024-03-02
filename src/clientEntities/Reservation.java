package clientEntities;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;

public class Reservation implements Serializable{
	//Class for reservation entitiy
	
	private SimpleStringProperty parkName;
	private SimpleStringProperty orderNumber;
	private SimpleStringProperty timeOfVisit;
	private SimpleStringProperty numberOfVisitors;
	private SimpleStringProperty telephoneNumber;
	private SimpleStringProperty emailaddress;
	
	public Reservation(
			String parkName,
			String orderNumber,
			String timeOfVisit,
			String numberOfVisitors,
			String telephoneNumber,
			String emailaddress) {
		
		this.parkName = new SimpleStringProperty(parkName);
		this.orderNumber = new SimpleStringProperty(orderNumber);
		this.timeOfVisit = new SimpleStringProperty(timeOfVisit);
		this.numberOfVisitors = new SimpleStringProperty(numberOfVisitors);
		this.telephoneNumber = new SimpleStringProperty(telephoneNumber);
		this.emailaddress = new SimpleStringProperty(emailaddress);
		
	}
	public SimpleStringProperty parkNameProperty() {
        return this.parkName;
    }

    public SimpleStringProperty orderNumberProperty() {
        return this.orderNumber;
    }

    public SimpleStringProperty timeOfVisitProperty() {
        return this.timeOfVisit;
    }

    public SimpleStringProperty numberOfVisitorsProperty() {
        return this.numberOfVisitors;
    }

    public SimpleStringProperty telphoneNumberProperty() {
        return this.telephoneNumber;
    }

    public SimpleStringProperty emailProperty() {
        return this.emailaddress;
    }
	
	public String toString() {
		String reservationString = "";
		
		reservationString = "" + this.parkName + this.orderNumber + this.timeOfVisit +this.numberOfVisitors + this.telephoneNumber + this.emailaddress;
		
		return reservationString;
	}
}
