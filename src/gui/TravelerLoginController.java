package gui;

import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Traveler;
import common.Usermanager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TravelerLoginController {

	 @FXML
	 private Button LoginBtn;

	 @FXML
	 private Button BackBtn;

	 @FXML
	 private JFXTextField TravelerID;
	 
	
	public void LoginBtn(ActionEvent click) throws Exception {
	    String visitorID = TravelerID.getText(); // get the id
	    Alerts alertID = InputValidation.ValidateVisitorID(visitorID); // get the right alert
	    Boolean isSuccessful = alertID.getAlertType().toString().equals("INFORMATION");
	    
	    if (isSuccessful) { // if entered right ID with 9 digits
	        try {
	        	// creating a traveler instance to send to server controller for validation 

		    	Traveler TryLoginVistor = new Traveler(Integer.parseInt(visitorID), null, null, null, null, null,null);

		    	
		        ClientServerMessage TravelerLoginAttemptInformation = new ClientServerMessage(TryLoginVistor, Operation.GET_TRAVLER_INFO);
		        ClientUI.clientControllerInstance.sendMessageToServer(TravelerLoginAttemptInformation);
		        // Get traveler data from the server
		        Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		        Usermanager.setCurrentTraveler(TravelerFromServer);
		       
		        // if traveler has an order in the system
		        if(TravelerFromServer instanceof Traveler) {
	        		//Open visitor screen if traveler is registered and not logged in
		        	ClientServerMessage checkIfNotLoggedIn = new ClientServerMessage(TravelerFromServer, Operation.GET_TRAVELER_SIGNED);
		        	ClientUI.clientControllerInstance.sendMessageToServer(checkIfNotLoggedIn);
		        	
		        	
		        	Boolean isloggedin = ClientUI.clientControllerInstance.getData().getFlag();
		        	if(isloggedin == false) {
		        		//Send a login request to server
		        		ClientServerMessage travelerLoginRequest = new ClientServerMessage(TravelerFromServer, Operation.PATCH_TRAVELER_SIGNEDIN);
		        		ClientUI.clientControllerInstance.sendMessageToServer(travelerLoginRequest);
		        		
		        		NavigationManager.openPage("TravelerFrame.fxml", click, "Traveler Screen", true);
		        	}else {
		        		
		        		Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Already Logged In",
								"Traveler already logged in!", "Traveler already logged in!");
						nullResponseAlert.showAndWait();
		        	}
		        }
		        else {
		        	// if traveler does not have an order in the system
		        	if (TravelerFromServer == null) {
		        		// open order a visit screen 
		        		Usermanager.setNewTraveler(false);
		        		NavigationManager.openPage("OrderVisit.fxml", click, "order A visit Screen", true);
		        	}	
		        }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error",
						"Something went wrong!", "Something went wrong!");
				nullResponseAlert.showAndWait();
	        }
	    } else {
	    	// Display the error alert to the user
	        alertID.showAndWait();
	    }
	}
	
	public void BackBtn(ActionEvent click) throws Exception{
		//Function for opening a new scene when clicking on the Back Button
	try {
		NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true);
	} catch(Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}
}
