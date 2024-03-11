package gui;

import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import client.SystemClient;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import common.Order;
import common.Traveler;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import gui.VisitorLoginController;
import client.ClientUI;
import client.InputValidation;
import client.SystemClient;

public class VisitorFrameController {
	
	    ClientController clientController = new ClientController(null, 0);
	    
	   // private int visitorID = VisitorLoginController.getvisitorID;

	    private Traveler traveler = new Traveler(null, null, null, null, null);
	    //usermanager.getcurrenttraveler
	    
	    @FXML
	    private TextArea messagesTextArea;
	    
	    @FXML
	    private TextField OrderID;
	    
	    @FXML
	    private Button editOrderBtn;
	    
	    @FXML
	    private Button orderVisitBtn;
	    
	    @FXML
	    private Button viewOrdersBtn;
	    
	    @FXML
	    private Button viewWaitingListBtn;
	    
	    @FXML
	    private Button viewMessagesBtn;
	    
	    @FXML
	    private Button exitBtn;
	    
	    public void editOrderBtn(ActionEvent click) throws Exception {
	    	
	    	String orderID = OrderID.getText(); // get the order ID
		    Alerts alertID = InputValidation.ValidateVisitorID(orderID); // get the right alert
		    Boolean isSuccessful = alertID.getAlertType().toString().equals("INFORMATION");
		    
		    if (isSuccessful) { // if entered the right order ID
		        try {
		            // open the order
		            Parent root = new FXMLLoader().load(getClass().getResource("OrderFrame.fxml"));
		            Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		            Scene scene = new Scene(root);

		            stage.setTitle("");
		            stage.setScene(scene);
		            stage.show();

		        } catch (Exception e) {
		            System.out.print("Something went wrong while clicking on edit order button, check stack trace");
		            e.printStackTrace();
		        }
		    } else {
		        // Display the error alert to the user
		        alertID.showAndWait();
		    }
	    	
	    }
	    
	    public void orderVisit(ActionEvent click) throws Exception {
	    	
	    	    try {
				
	    		Parent root = new FXMLLoader().load(getClass().getResource("OrderAVisitFrame.fxml"));
	    		Scene scene = new Scene(root);	
	    		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
	    		
	    		stage.setTitle("Order a visit");
	    		
	    		stage.setScene(scene);		
	    		stage.show();
	    		
	    		}catch(Exception e) {
	    			System.out.print("Something went wrong while clicking on the Order a visit button, check stack trace");
	    			e.printStackTrace();
	    		}
	    	
		}
	    
        public void viewOrders(ActionEvent click) throws Exception{
		
                try {
				
	    		Parent root = new FXMLLoader().load(getClass().getResource("OrdersFrame.fxml"));
	    		Scene scene = new Scene(root);	
	    		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
	    		
	    		stage.setTitle("Orders");
	    		
	    		stage.setScene(scene);		
	    		stage.show();
	    		
	    		}catch(Exception e) {
	    			System.out.print("Something went wrong while clicking on the view orders button, check stack trace");
	    			e.printStackTrace();
	    		}
		
	    }
        
        public void viewWaitingList(ActionEvent click) throws Exception{
        	
                try {
				
	    		Parent root = new FXMLLoader().load(getClass().getResource("WaitingListFrame.fxml"));
	    		Scene scene = new Scene(root);	
	    		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
	    		
	    		stage.setTitle("Waiting list");
	    		
	    		stage.setScene(scene);		
	    		stage.show();
	    		
	    		}catch(Exception e) {
	    			System.out.print("Something went wrong while clicking on the view waiting list button, check stack trace");
	    			e.printStackTrace();
	    		}
			
		}
 
        public void viewMessages(ActionEvent click) throws Exception{
        	 
        	// Fetch messages from the server and set them in the TextArea
        	ClientServerMessage messageToServer = new ClientServerMessage(traveler , Operation.GET_MESSAGES );
			clientController.sendMessageToServer(messageToServer);
            
			List<String> messages = (List<String>) new ClientServerMessage(traveler , "GET_MESSAGES" );

            StringBuilder messagesText = new StringBuilder();

            for (String message : messages) {
                messagesText.append(message).append("\n");
            }

            messagesTextArea.setText(messagesText.toString());
		
	    }
        
        public void exit(ActionEvent click) throws Exception{
	    	System.exit(1);
	    }
	    
}