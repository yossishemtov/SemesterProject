package common.worker;

import java.io.Serializable;

/**
 * Represents a general worker in a park management system. This class
 * encapsulates all the relevant information about a worker, including their
 * personal details, role within the park, and login credentials.
 */
public class GeneralParkWorker implements Serializable {

	private static final long serialVersionUID = 1L;

	// Fields representing the worker's details
	private Integer workerId;
	private String firstName;
	private String lastName;
	private String email;
	private String role; // Worker's role in the park (e.g., Administrator, Manager, etc.)
	private String userName; // For system login
	private String password; // For system login, should be stored securely
	private Integer worksAtPark; // Park identifier where the worker is employed

	/**
	 * Constructs a new GeneralParkWorker with specified details.
	 *
	 * @param workerId    Unique identifier for the worker.
	 * @param firstName   Worker's first name.
	 * @param lastName    Worker's last name.
	 * @param email       Worker's email address.
	 * @param role        Worker's role within the park.
	 * @param userName    Worker's username for system access.
	 * @param password    Worker's password for system access.
	 * @param worksAtPark Identifier of the park where the worker is employed.
	 */
	public GeneralParkWorker(Integer workerId, String firstName, String lastName, String email, String role,
			String userName, String password, Integer worksAtPark) {
		this.workerId = workerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.userName = userName;
		this.password = password;
		this.worksAtPark = worksAtPark;
	}

	// toString method provides a string representation of the worker, excluding
	// sensitive information like password
	@Override
	public String toString() {
		return "Worker ID: " + workerId + ", Name: " + firstName + " " + lastName + ", Role: " + role + ", Username: "
				+ userName +
				// Password is intentionally excluded from the toString() method for security
				// reasons
				", Works at Park ID: " + worksAtPark;
	}

	// Getters and setters
	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getWorksAtPark() {
		return worksAtPark;
	}

	public void setWorksAtPark(Integer worksAtPark) {
		this.worksAtPark = worksAtPark;
	}
}
