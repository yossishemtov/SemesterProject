package gui;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegisterNewGuideController {
	@FXML
	private Button SubmitBtn;
	@FXML
	private Button BackBtn;
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
	        return; // Exit the method early, as validation failed
	    }

	    // Perform additional validation for ID and Email
	    Alerts alertID = InputValidation.ValidateVisitorID(TravelerID);
	    Alerts alertEmail = InputValidation.validateEmail(TravelerEmail);
	    Boolean validID = alertID.getAlertType().toString().equals("INFORMATION");
		Boolean validEmail = alertID.getAlertType().toString().equals("INFORMATION");
		 
	    // Check if ID and Email validations passed
	    if (!validID) {
	        alertID.showAndWait();
	        return; // Exit the method early, as validation failed
	    }

	    if (!validEmail) {
	        alertEmail.showAndWait();
	        return; // Exit the method early, as validation failed
	    }
	    
	    // If all validations pass, proceed with registration logic
	    try {
	    	Traveler GroupGuideAttempt = new Traveler(Integer.parseInt(TravelerID), null, null, null, null, 1, 0);

			// send to server in order to check if traveler exists in the system
			ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(GroupGuideAttempt, Operation.GET_TRAVLER_INFO);
		    ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
		    // get the data return from server 
		    Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		    // if traveler not exist in the system, register as a new group guide
		    if (TravelerFromServer == null) {
		    	ClientServerMessage<?> RegistrateGroupGuide = new ClientServerMessage<>(GroupGuideAttempt, Operation.POST_NEW_TRAVLER_GUIDER);
		     } 
		    else {
		    	 // traveler exists therefore we changed him to be group guide
		    	 ClientServerMessage<?> RegistrateGroupGuide = new ClientServerMessage<>(GroupGuideAttempt, Operation.POST_EXISTS_TRAVLER_GUIDER);
		     }
	    } catch (Exception e) {
	        System.out.print("Something went wrong while clicking on submit button, trying to register new group guide, check stack trace");
	        e.printStackTrace();
	    }
	}

	 
	 public void BackBtn(ActionEvent click) throws Exception{
			//Function for opening a new scene when clicking on the Back Button
		try {
			NavigationManager.openPage("ServiceWorkerFrame.fxml", click, "Home Page", true);
		} catch(Exception e) {
				System.out.print("Something went wrong while clicking on the back button, check stack trace");
				e.printStackTrace();
			}
		}
}
