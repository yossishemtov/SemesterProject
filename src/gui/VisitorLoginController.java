package gui;

import client.ClientUI;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Traveler;
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
		        ClientServerMessage VisitorLoginAttemptInformation = new ClientServerMessage(TryLoginVistor, Operation.GetTravlerInfo);
		        ClientUI.clientControllerInstance.sendMessageToServer(VisitorLoginAttemptInformation);
		        // get the data return from server 
		        ClientServerMessage dataFromServer = ClientUI.clientControllerInstance.getData();
		        // if traveler has an order in the system
		        if (dataFromServer.getDataTransfered() instanceof Traveler) {
		        	// open visitor screen 
		            Parent root = new FXMLLoader().load(getClass().getResource("VisitorFrame.fxml"));
		            Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		            Scene scene = new Scene(root);

		            stage.setTitle("Visitor Screen");
		            stage.setScene(scene);
		            stage.show();
		        }
		        else {
		        	// if traveler has not an order in the system
		        	if (dataFromServer.getDataTransfered() == null) {
		        		// open order a visit screen 
			            Parent root = new FXMLLoader().load(getClass().getResource("OrderAvisitFrame.fxml"));
			            Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			            Scene scene = new Scene(root);

			            stage.setTitle("order A visit Screen");
			            stage.setScene(scene);
			            stage.show();
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
		
		Parent root = new FXMLLoader().load(getClass().getResource("HomePageFrame.fxml"));
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		Scene scene = new Scene(root);	
		
		stage.setTitle("Home Page");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}
}
