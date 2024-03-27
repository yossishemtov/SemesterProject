package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TravelerLoginController implements Initializable{

	 @FXML
	 private Button LoginBtn;

	 @FXML
	 private Button BackBtn;

	 @FXML
	 private JFXTextField TravelerID;
	 
	 @FXML
	 private FontAwesomeIconView userIcon;
	 
	 @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        // Listener for the username text field
		   TravelerID.textProperty().addListener((obs, oldValue, newValue) -> {
	            String pattern = "^.{7}$"; // Username pattern

	            // Set text field color and icon based on input
	            if (newValue.isEmpty()) {
	                // Set to original color
	                userIcon.setFill(Color.web("#b3ffb3"));
	                TravelerID.setStyle("-jfx-unfocus-color: #b3ffb3; " + "-fx-text-fill: #b3ffb3; " + "-fx-prompt-text-fill: #b3ffb3;");
	            } else {
	                // Set to red if input is not empty
	                userIcon.setFill(Color.RED);
	                TravelerID.setStyle("-jfx-unfocus-color: red; " + "-fx-text-fill: red; " + "-fx-prompt-text-fill: red;");
	            }

	            // Set to green if input matches pattern
	            if (!newValue.isEmpty() && newValue.matches(pattern)) {
	                userIcon.setFill(Color.GREEN);
	                TravelerID.setStyle("-jfx-unfocus-color: green; " + "-fx-text-fill: green; " + "-fx-prompt-text-fill: green;");
	            }
	        });

	    }
	 
	 
	
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
		        	Usermanager.setNewTraveler(false);
	        		//Open visitor screen if traveler is registered and not logged in
		        	ClientServerMessage checkIfNotLoggedIn = new ClientServerMessage(TravelerFromServer, Operation.GET_TRAVELER_SIGNED);
		        	ClientUI.clientControllerInstance.sendMessageToServer(checkIfNotLoggedIn);
		        	
		        	
		        	Boolean isloggedin = ClientUI.clientControllerInstance.getData().getFlag();
		        	if(isloggedin == false) {
		        		//Send a login request to server
		        		ClientServerMessage travelerLoginRequest = new ClientServerMessage(TravelerFromServer, Operation.PATCH_TRAVELER_SIGNEDIN);
		        		ClientUI.clientControllerInstance.sendMessageToServer(travelerLoginRequest);
		        		
		        		NavigationManager.openPage("TravelerFrame.fxml", click, "Traveler Screen", true, true);
		        	}else {
		        		
		        		Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Already Logged In",
								"Traveler already logged in!", "Traveler already logged in!");
						nullResponseAlert.showAndWait();
		        	}
		        }
		        else {
		        	// if traveler does not have an order in the system
		        	if (TravelerFromServer == null) {
				        TryLoginVistor = new Traveler(Integer.parseInt(visitorID), null, null, null, null, 0,1);
				        Usermanager.setCurrentTraveler(TryLoginVistor);
		        		// open order a visit screen 
		        		Usermanager.setNewTraveler(true);
		        		NavigationManager.openPage("OrderVisit.fxml", click, "order A visit Screen", true, true);
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
		NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true, true);
	} catch(Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}



}
