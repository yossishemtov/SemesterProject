package common;

public class GroupGuide extends Traveler{
	private String username;
	private String password;
	
	public GroupGuide(Integer id, String firstName, String lastName, String email, String username,
			String password) {
		super(id, firstName, lastName, email, password,1);

		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
