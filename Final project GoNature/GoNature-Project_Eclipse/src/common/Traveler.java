package common;

import java.io.Serializable;

/**
 * The Traveler class represents a traveler in the system.
 * It contains attributes such as ID, first name, last name, email, phone number,
 * group guide status, and login status.
 */
public class Traveler implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum; // This is named phoneNumber in the DB
    private Integer isGrupGuide;
    private Integer isLoggedIn; // This field corresponds to `isloggedin` in the DB


    /**
     * Constructs a new Traveler object with the specified attributes.
     *
     * @param id          The unique identifier of the traveler.
     * @param firstName   The first name of the traveler.
     * @param lastName    The last name of the traveler.
     * @param email       The email address of the traveler.
     * @param phoneNum    The phone number of the traveler.
     * @param isGroupGuide The status indicating whether the traveler is a group guide.
     * @param isLoggedIn  The status indicating whether the traveler is logged in.
     */
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
