package logic;

import java.io.Serializable;

/**
 * This class represents a traveler
 */
@SuppressWarnings("serial")
public class Traveler implements Serializable {
	private String travelerId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;

	public Traveler(String travelerId, String firstName, String lastName, String email, String phoneNumber) {
		this.travelerId = travelerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
