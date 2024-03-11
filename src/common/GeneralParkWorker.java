package common.worker;

public class GeneralParkWorker {
	private Integer workerId;
	private String firstName;
	private String lastName;
	private String email;
	private String role;
	private String userName;
	private String password;
	
	public GeneralParkWorker(Integer workerNumber, String firstName, String lastName, String email, String role,
			String userName, String password) {
		super();
		this.workerId = workerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.userName = userName;
		this.password = password;
	}
	
	public Integer getWorkerNumber() {
		return workerId;
	}
	public void setWorkerNumber(Integer workerNumber) {
		this.workerId = workerNumber;
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
	
}