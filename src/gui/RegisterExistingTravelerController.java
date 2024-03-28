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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;


/**
 * Controller class for managing the registration of an existing traveler as a group guide.
 * This class handles the logic for registering an existing traveler as a group guide,
 * including input validation, communication with the server, and displaying appropriate alerts
 * based on the success or failure of the registration process.
 */
public class RegisterExistingTravelerController {

    @FXML
    private JFXTextField travelerID;

    @FXML
    private JFXButton submitBtn;
    
    
    /**
     * Handles the submission of the registration form for an existing traveler.
     * This method validates the input traveler ID, communicates with the server to check if
     * the traveler exists in the system, and registers the traveler as a guide if he is
     * not already registered as a guide. It displays appropriate alerts based on the success
     * or failure of the registration process.
     * 
     * @param click The action event triggering the method.
     * @throws Exception If an error occurs during the registration process.                 
     */
    public void SubmitForm(ActionEvent click) throws Exception {
    	String TravelerID = travelerID.getText();
    	Alerts alertID = InputValidation.ValidateVisitorID(TravelerID);
    	Boolean validID = alertID.getAlertType().toString().equals("INFORMATION");
    	if (validID) {
    		try {  
    			// create new instance of traveler that is group guide
	    		Traveler MakingTravelerGroupGuide = new Traveler(Integer.parseInt(TravelerID), null, null, null, null, 1, 0);
	    		// send to server in order to check if traveler id exists in the system
	    		ClientServerMessage<?> validTravelerID = new ClientServerMessage<>(MakingTravelerGroupGuide, Operation.GET_TRAVLER_INFO);
			    ClientUI.clientControllerInstance.sendMessageToServer(validTravelerID);
			    Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		    	// if traveler exists, send him to DB to make him a guide
			    if (TravelerFromServer != null) {
			    	// if the traveler is not a group guide
			    	if (TravelerFromServer.getIsGroupGuide() == 0) {
		    		ClientServerMessage<?> RegistrationAttempt = new ClientServerMessage<>(TravelerFromServer, Operation.POST_EXISTS_TRAVLER_GUIDER);
				    ClientUI.clientControllerInstance.sendMessageToServer(RegistrationAttempt);
				    if( ClientUI.clientControllerInstance.getData().getFlag()) {
				    	Alerts succeedRegistration = new Alerts(Alert.AlertType.CONFIRMATION, "Succeed to registrate","","Succeed to registrate");
				    	succeedRegistration.showAndWait();
				    	// Clear the field after registered
					    travelerID.setText(""); 
				    	}
				    }
			    	// traveler is already a guide, show appropriate alert.
			    	else {
			    		Alerts AlreadyGroupGuide = new Alerts (Alerts.AlertType.ERROR, "Traveler is already a guop guide", "", "Traveler is already a guop guide");
			    		AlreadyGroupGuide.showAndWait();
			    		travelerID.setText(""); 
			    	}
			    }
			    // if traveler not exists , means he can't register here.
			    else {
			    	Alerts notATraveler = new Alerts(Alerts.AlertType.ERROR, "Traveler not exist", "", "Traveler does no exist in the system, go to register new guide");
			    	notATraveler.showAndWait();
			    	travelerID.setText("");
			    }
			    
    		} catch(Exception e) {
    	        e.printStackTrace();
    	        Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on submit register exist traveler as guide button");
    			errorAlert.showAndWait();
    		}
    	}
    	// ID is not valid.
    	else {
    		alertID.showAndWait();
    	}
    }
}
