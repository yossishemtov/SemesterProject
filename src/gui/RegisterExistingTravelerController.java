package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Traveler;
import javafx.fxml.FXML;

public class RegisterExistingTravelerController {

    @FXML
    private JFXTextField travelerID;

    @FXML
    private JFXButton submitBtn;
    
    public void SubmitForm() {
    	String TravelerID = travelerID.getText();
    	Alerts alertID = InputValidation.ValidateVisitorID(TravelerID);
    	Boolean validID = alertID.getAlertType().toString().equals("INFORMATION");
    	if (validID) {
    		try {
    		Traveler MakingTravelerGroupGuide = new Traveler(Integer.parseInt(TravelerID), null, null, null, null, 1, 0);
    		ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(MakingTravelerGroupGuide, Operation.POST_EXISTS_TRAVLER_GUIDER);
		    ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
    		} catch(Exception e) {
    			System.out.print("Something went wrong while clicking on submit button, trying to register existing group guide, check stack trace");
    	        e.printStackTrace();
    		}
    	}
    	
    }
}
