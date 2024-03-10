package common.worker;

import java.io.Serializable;

public class GeneralParkWorker  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer workerId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String userName;
    private String password;
    private Integer worksAtPark; // Assuming this is an ID referring to the park where the worker is employed

    public GeneralParkWorker(Integer workerId, String firstName, String lastName, String email, String role,
                             String userName, String password, Integer worksAtPark) {
        super();
        this.workerId = workerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.userName = userName;
        this.password = password;
        this.worksAtPark = worksAtPark;
    }
    
    @Override
    public String toString() {
        return "Worker ID: " + workerId +
               ", Name: " + firstName + " " + lastName +",Role: "+role+
               ", Username: " + userName +"Password : "+ password+
               // Generally, you shouldn't include password in toString for security reasons
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
