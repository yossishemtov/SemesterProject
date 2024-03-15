package client;

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
        else if (!visitorID.matches("\\d{7}")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "Your ID must contain 7 digits");
        }
        // ID is valid
        else {
        	 return new Alerts(Alerts.AlertType.INFORMATION, "ID Validation", "", "Valid ID");
        }
    }
	
	// NEED TO IMPLEMENT
	public static Alerts validateEmail(String email) {
		 return new Alerts(Alerts.AlertType.INFORMATION, "ID Validation", "", "Valid ID");
	}
	
	// function for validate worker password
	 public static Alerts validatePassword(String password) {
	        // Check if password is empty
	        if (password.equals("")) {
	            return new Alerts(Alerts.AlertType.ERROR, "Invalid Password", "", "Password cannot be empty.");
	        }
	        
	        // Password does not contain both letters and numbers
	        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).*$")) {
	            return new Alerts(Alerts.AlertType.ERROR, "Invalid Password", "", "Password must contain both letters and numbers.");
	        }
	        
	        // Password is valid
	        else {
	        	return new Alerts(Alerts.AlertType.INFORMATION, "Password Validation", "", "Valid password");
	        }
	 }
	 
	 public static Alerts validateOrderNumber(String orderId) {
		 if(!orderId.matches("[1-9]\\d{0,4}|0")) {
	            return new Alerts(Alerts.AlertType.ERROR, "Invalid Order Id", "", "Order id must only contain numbers and not exceed 8 characters");
		 }else {
	        	return new Alerts(Alerts.AlertType.INFORMATION, "Order Id Validation", "", "Order Id Validation");
	        }
	 }
	 
 
	// function for validate worker username
	 public static Alerts validateUsername(String username) {
	    // Check if the username is empty
	    if (username.isEmpty()) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username cannot be empty");
	    } 
	    
	    // Check if the first character of the username is not a letter
	    else if (!Character.isLetter(username.charAt(0))) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username must start with a letter");
	    } 

	    // Check if the username has fewer than two characters
	    else if (username.length() < 2) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username must have at least two characters");
	    } 
	    
	    // Check if the username contains special characters
	    else if (!username.matches("^[a-zA-Z0-9]*$")) {
	        return new Alerts(Alerts.AlertType.ERROR, "Invalid Username", "", "Username cannot contain special characters");
	    } 

	    // username is valid
	    else {
	        return new Alerts(Alerts.AlertType.INFORMATION, "Valid Username", "", "Valid Username");
	    }
	}

}