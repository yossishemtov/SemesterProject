package common;

import java.io.Serializable;

public class Traveler implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum; // This is named phoneNumber in the DB
    private Integer isGrupGuide;
    private Integer isLoggedIn; // This field corresponds to `isloggedin` in the DB

    // Adjusted constructor to include isLoggedIn
    public Traveler(Integer id, String firstName, String lastName, String email, String phoneNum, Integer isGroupGuide, Integer isLoggedIn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.isGrupGuide = isGroupGuide;
        this.isLoggedIn = isLoggedIn;
    }

    // Getter and setter for isLoggedIn
    public Integer getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Integer isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    // Adjusted to Boolean to better represent the tinyint(1)
    public Integer getIsGroupGuide() {
        return isGrupGuide;
    }

    public void setIsGroupGuide(Integer isGroupGuide) {
        this.isGrupGuide = isGroupGuide;
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
