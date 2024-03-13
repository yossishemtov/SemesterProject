package common;

import java.io.Serializable;

public class Traveler implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNum;
	private Integer isGrupGuide;
	public Traveler(Integer id, String firstName, String lastName, String email, String phoneNum,Integer isGrupGuide) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNum = phoneNum;
		this.isGrupGuide=isGrupGuide;
	}
	public Integer getIsGrupGuide() {
		return isGrupGuide;
	}

	public void setIsGrupGuide(Integer isGrupGuide) {
		this.isGrupGuide = isGrupGuide;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public String getPhoneNum() {
		return phoneNum;
	}
	
	public void setPhoneNum(String phoneNumber) {
		 this.phoneNum=phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
