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
import javafx.scene.control.Alert;

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
    			// create new instance of traveler that is group guide
	    		Traveler MakingTravelerGroupGuide = new Traveler(Integer.parseInt(TravelerID), null, null, null, null, 1, 0);
	    		System.out.println(TravelerID);
	    		// send to server in order to check if traveler id exists in the system
	    		ClientServerMessage<?> validTravelerID = new ClientServerMessage<>(MakingTravelerGroupGuide, Operation.GET_TRAVLER_INFO);
			    ClientUI.clientControllerInstance.sendMessageToServer(validTravelerID);
			    Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		    	// if traveler exists, send him to DB to make him a guide
			    if (TravelerFromServer != null) {
		    		ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(TravelerFromServer, Operation.POST_EXISTS_TRAVLER_GUIDER);
				    ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
				    if( ClientUI.clientControllerInstance.getData().getFlag()) {
				    	Alerts succeedRegistration = new Alerts(Alert.AlertType.CONFIRMATION, "Succeed to registrate","","Succeed to registrate");
				    	succeedRegistration.showAndWait();
				    	// Clear the field after registrate
					    travelerID.setText(""); 
				    }
			    }
			    else {
			    	// if traveler not exists 
			    	Alerts notATraveler = new Alerts(Alerts.AlertType.ERROR, "Traveler not exist", "", "Entered wrong ID");
			    	notATraveler.showAndWait();
			    	travelerID.setText("");
			    }
    		} catch(Exception e) {
    			System.out.print("Something went wrong while clicking on submit button, trying to register existing group guide, check stack trace");
    	        e.printStackTrace();
    		}
    	} else {
    		alertID.showAndWait();
    	}
    	
    }
}
