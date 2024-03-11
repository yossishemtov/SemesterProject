package gui;

import java.util.ArrayList;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VisitorLoginController {
	@FXML
    private Button LoginBtn;
	@FXML
    private Button BackBtn;
	@FXML
    private TextField VisitorID;
	
	
	public void LoginBtn(ActionEvent click) throws Exception {
	    String visitorID = VisitorID.getText(); // get the id
	    Alerts alertID = InputValidation.ValidateVisitorID(visitorID); // get the right alert
	    Boolean isSuccessful = alertID.getAlertType().toString().equals("INFORMATION");
	    
	    if (isSuccessful) { // if entered right ID with 9 digits
	        try {
	        	// creating a traveler instance to send to server controller for validation 
		    	Traveler TryLoginVistor = new Traveler(Integer.parseInt(visitorID), null, null, null, null);
		    	
		        ClientServerMessage<?> TravelerLoginAttemptInformation = new ClientServerMessage<>(TryLoginVistor, Operation.GET_TRAVLER_INFO);
		        ClientUI.clientControllerInstance.sendMessageToServer(TravelerLoginAttemptInformation);
		        // get the data return from server 
		        Traveler TravelerFromServer = (Traveler) ClientController.data.getDataTransfered();
		        // update the current traveler in UserManage
		        Usermanager.setCurrentTraveler(TravelerFromServer);
		        // if traveler has an order in the system
		       
		        if(TravelerFromServer instanceof Traveler) {
	        		// open visitor screen 
		        	NavigationManager.openPage("VisitorFrame.fxml", click, "Traveler Screen", true);
		        }
		        else {
		        	// if traveler has not an order in the system
		        	if (TravelerFromServer == null) {
		        		// open order a visit screen 
		        		NavigationManager.openPage("OrdervisitFrame.fxml", click, "order A visit Screen", true);
		        	}	
		        }
	        } catch (Exception e) {
	            System.out.print("Something went wrong while clicking on visitor login button, check stack trace");
	            e.printStackTrace();
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
