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
		 
		 Alerts alertID = InputValidation.ValidateVisitorID(TravelerID); // get the right alert
		 Boolean validID = alertID.getAlertType().toString().equals("INFORMATION");
		 Alerts alertEmail = InputValidation.validateEmail(TravelerEmail);
		 Boolean validEmail = alertID.getAlertType().toString().equals("INFORMATION");
		 try {
			 if (validID && validEmail) {
				 
				 Traveler GroupGuideAttempt = new Traveler(Integer.parseInt(TravelerID),null,null,null,null, null);
				 // send to server in order to check if traveler exists in the system
				 ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(GroupGuideAttempt, Operation.GET_TRAVLER_INFO);
			     ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
			     // get the data return from server 
			     Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
			     // if traveler not exist in the system, register as a new group guide
			     if (TravelerFromServer == null) {
			    	 ClientServerMessage<?> RegistrateGroupGuide = new ClientServerMessage<>(GroupGuideAttempt, Operation.POST_NEW_TRAVLER_GUIDER);
			     } else {
			    	 // traveler exists therefore he need to go to "register exist guide"
//			    	 ClientServerMessage<?> RegistrateGroupGuide = new ClientServerMessage<>(GroupGuideAttempt, Operation.PATCH_TRAVELER_MAKE_GROUPGUIDE);
			     }
				 
			 }
			 else {
				 if(!validID) {
					 alertID.showAndWait();
				 }
				 if(!validEmail) {
					 alertEmail.showAndWait();
				 }
			 }
		 } catch(Exception e) {
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
