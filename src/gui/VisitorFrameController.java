package gui;

import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import client.NavigationManager;
import common.Message;

public class VisitorFrameController {
	
	    ClientController clientController = new ClientController(null, 0);
	    
	   // private int visitorID = VisitorLoginController.getvisitorID;

	    private Traveler traveler = new Traveler(null, null, null, null, null, null);
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
	    
	    Traveler currentTraveler = Usermanager.getCurrentTraveler();
	    
	    
//	    public void editOrderBtn(ActionEvent click) throws Exception {
//	    	
//	    	
//	    	String orderID = OrderID.getText(); // get the order ID
//		    Alerts alertID = InputValidation.ValidateVisitorID(orderID); // get the right alert
//		    Boolean isSuccessful = alertID.getAlertType().toString().equals("INFORMATION");
//		    
//		    if (isSuccessful) { // if entered the right order ID
//		        try {
//		            NavigationManager.openPage("OrderFrame.fxml", click, "test", true);
//		        }
//		    }
//	    }



	public void orderVisit(ActionEvent click) throws Exception {

		NavigationManager.openPage("OrderAVisitFrame.fxml", click, "Order a visit", true);

	}

	public void viewOrders(ActionEvent click) throws Exception {

		NavigationManager.openPage("OrdersFrame.fxml", click, "Orders", true);

	}

	public void viewWaitingList(ActionEvent click) throws Exception {

		NavigationManager.openPage("WaitingListFrame.fxml", click, "Waiting list", true);

	}

	public void viewMessages(ActionEvent click) throws Exception {

		// Fetch messages from the server and set them in the TextArea
		ClientServerMessage<?> messege = new ClientServerMessage(currentTraveler, Operation.GET_MESSAGES);
		ClientUI.clientControllerInstance.sendMessageToServer(messege);
		ArrayList<Message> messages = (ArrayList<Message>) ClientController.data.getDataTransfered();
		
		ArrayList<String> stringMessages = new ArrayList<>();
		
		for (Message message : messages) {
			stringMessages.add(message.toString());
		}

		StringBuilder messagesText = new StringBuilder();

		for (String message : stringMessages) {
			messagesText.append(message).append("\n");
		}

		messagesTextArea.setText(messagesText.toString());

	}

	public void exit(ActionEvent click) throws Exception {
		System.exit(1);
	}

}