package gui;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import com.jfoenix.controls.JFXTextField;
import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;


/**
 * Controller class for managing the registration of a new group guide.
 * This class handles the logic for registering a new group guide, including input validation,
 * communicating with the server for registration, and displaying appropriate alerts
 * based on the success or failure of the registration process.
 */
public class RegisterNewGuideController {
	@FXML
	private Button SubmitBtn;
	@FXML
	private JFXTextField ID;
	@FXML
	private JFXTextField FirstName;
	@FXML
	private JFXTextField LastName;
	@FXML
	private JFXTextField Email;
	@FXML
	private JFXTextField PhoneNumber;
	
	  
	
	/**
     * Handles the registration process for a new group guide.
     * This method performs input validation for the provided traveler information,
     * communicates with the server to check if the traveler already exists,
     * and registers the traveler as a guide if he is new to the system.
     * 
     * @param click The action event triggering the method.
     * @throws Exception If an error occurs during the registration process.              
     */
	public void SubmitRegister(ActionEvent click) throws Exception {
	    String TravelerID = ID.getText();
	    String TravelerFirstName = FirstName.getText();
	    String TravelerLastName = LastName.getText();
	    String TravelerEmail = Email.getText();
	    String TravelerPhoneNumber = PhoneNumber.getText();

	    // Check if any of the fields are empty
	    if (TravelerID.isEmpty() || TravelerFirstName.isEmpty() || TravelerLastName.isEmpty() ||
	            TravelerEmail.isEmpty() || TravelerPhoneNumber.isEmpty()) {
	        // Show an alert indicating that all fields are required
	        Alerts alert = new Alerts(Alerts.AlertType.ERROR, "All fields are required", "", "Please fill in all the required fields");
	        alert.showAndWait();
	        return; // Exit the method, as validation failed
	    }

	    // Perform additional validation for ID and Email
	    Alerts alertID = InputValidation.ValidateVisitorID(TravelerID);
	    Alerts alertEmail = InputValidation.validateEmail(TravelerEmail);
	    Alerts alertPhone = InputValidation.validatePhoneNumber(TravelerPhoneNumber);
	    Boolean validID = alertID.getAlertType().toString().equals("INFORMATION");
		Boolean validEmail = alertEmail.getAlertType().toString().equals("INFORMATION");
		Boolean validPhone = alertPhone.getAlertType().toString().equals("INFORMATION");
		 
	    // Check if ID and Email validations passed
	    if (!validID) {
	        alertID.showAndWait();
	        return; // Exit the method, as validation failed
	    }

	    if (!validEmail) {
	        alertEmail.showAndWait();
	        return; // Exit the method, as validation failed
	    }
	    
	    if(!validPhone) {
	    	alertPhone.showAndWait();
	    	return;
	    }
	    // If all validations pass, proceed with registration logic
	    try {
	    	
	    	Traveler MakingTravelerGroupGuide = new Traveler(Integer.parseInt(TravelerID), null, null, null, null, 1, 0);
	    	ClientServerMessage<?> validTravelerID = new ClientServerMessage<>(MakingTravelerGroupGuide, Operation.GET_TRAVLER_INFO);
		    ClientUI.clientControllerInstance.sendMessageToServer(validTravelerID);
		    Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		    // if traveler exists in the system, he is not new to the system
		    if (TravelerFromServer != null) {
		    	Alerts failedToRegister = new Alerts(Alert.AlertType.ERROR, "Error in Registration","","Traveler already exists in the system, go to register existing traveler!");
		    	failedToRegister.showAndWait();
		    	// Clear the fields after registrate
		    	ID.setText(""); 
		    	FirstName.setText("");
		    	LastName.setText("");
		    	Email.setText(""); 
		    	PhoneNumber.setText("");
		    }
		    else {
		    	Traveler GroupGuideAttempt = new Traveler(Integer.parseInt(TravelerID), TravelerFirstName, TravelerLastName, TravelerEmail, TravelerPhoneNumber, 1, 0);
				// send to server in order to register new group guide
				ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(GroupGuideAttempt, Operation.POST_NEW_TRAVLER_GUIDER);
			    ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
			    if(ClientUI.clientControllerInstance.getData().getFlag()) {
			    	 Alerts succeedRegistration = new Alerts(Alert.AlertType.CONFIRMATION, "Succeed to registrate","","Succeed to registrate");
			    	 succeedRegistration.showAndWait();
			    	 // Clear the fields after registrate
			    	 ID.setText(""); 
			    	 FirstName.setText("");
			    	 LastName.setText("");
			    	 Email.setText(""); 
			    	 PhoneNumber.setText("");
			    }
			    else {
			    	Alerts failedToRegister = new Alerts(Alert.AlertType.ERROR, "Error in Registration","","Error in Registration");
			    	failedToRegister.showAndWait();
			    	// Clear the fields after registrate
			    	ID.setText(""); 
			    	FirstName.setText("");
			    	LastName.setText("");
			    	Email.setText(""); 
			    	PhoneNumber.setText("");
			    }
		    }
	    } catch (Exception e) {
	        e.printStackTrace();
	        Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on submit register new guide button");
			errorAlert.showAndWait();
	    }
	}
}
