package client;


import common.Alerts;

public class InputValidation {

	
	public static Alerts ValidateVisitorID(String visitorID) {
        // Check if the visitor ID is empty
        if (visitorID.equals("")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "You need to insert your ID.");
        }

        // Check if the visitor ID does not contain 9 digits
        if (!visitorID.matches("\\d{9}")) {
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "Your ID must contain 9 digits.");
        }

        try {
            // Convert the ID to a long for additional checks
            long idNumber = Long.parseLong(visitorID);

            // Check if the ID is a positive number
            if (idNumber <= 0) {
                return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "ID must be a positive number.");
            }

            // Check if the ID is within a specific range
            if (idNumber < 100_000_000 || idNumber > 999_999_999) {
                return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "ID must be between 100,000,000 and 999,999,999.");
            }

        } catch (NumberFormatException e) {
            // Catch exception if the ID is not a numeric value
            return new Alerts(Alerts.AlertType.ERROR, "Invalid ID", "", "ID must be a numeric value.");
        }

        // ID is valid
        return new Alerts(Alerts.AlertType.INFORMATION, "ID Validation", "", "Valid ID");
    }
}
