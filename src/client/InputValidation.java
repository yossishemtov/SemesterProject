package client;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Alerts;

public class InputValidation {

	
	public static Alerts ValidateVisitorID(String visitorID) {
        // Check if the visitor ID is empty
        if (visitorID.equals("")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "You need to insert your ID");
        }
        // Check if the visitor ID starts with a negative sign
        else if (visitorID.startsWith("-")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "ID must be a non-negative number");
        }

        // Check if the visitor ID does not contain 9 digits
        else if (!visitorID.matches("\\d{9}")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "Your ID must contain 9 digits");
        }
        
        // ID is valid
        else {
        	 return new Alerts(Alerts.AlertType.INFORMATION, "ID Validation", "", "Valid ID");
        }
       
    }
	

	// function for validate worker password
	 public static Alerts validatePassword(String password) {
	        // Check if password is empty
	        if (password.equals("")) {
	            return new Alerts(Alerts.AlertType.ERROR, "Invalid Password", "", "Password cannot be empty.");
	        }

//	        // Validate password strength
//	        else if (!password.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
//	            return new Alerts(Alerts.AlertType.ERROR, "Invalid Password", "", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.");
//	        }

	        // Password is valid
	        else {
	        	return new Alerts(Alerts.AlertType.INFORMATION, "Password Validation", "", "Valid password");
	        }
	        
	 }
	 
 
	// function for validate worker username
	public static Alerts validateUsername(String username) {
	    if (username.isEmpty()) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username cannot be empty");
	    } 
	    
//	    else if (!username.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
//	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "First character must be a letter, and only alphanumeric characters are allowed");
//	    } 
	    
//	    else if (username.matches("^[a-zA-Z0-9]*$")) {
//	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username cannot contain special characters");
//	    } 
	    
	    else if (username.length() < 2) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username must have at least two characters");
	    } 
	    
	    else {
	        return new Alerts(Alerts.AlertType.INFORMATION, "Valid Username", "", "Valid Username");
	    }
	}
}

