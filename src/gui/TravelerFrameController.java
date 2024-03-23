package gui;

import java.awt.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Usermanager;
import client.NavigationManager;
import common.Message;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;



public class TravelerFrameController implements Initializable {
	
	@FXML
    private BorderPane pane;

    @FXML
    private AnchorPane topBorder; 

    @FXML
    private Label userLabel;

    @FXML

    private JFXButton viewOrdersbutton;
    
    @FXML

    private StackPane leftBorder;

    @FXML
    private JFXButton travelerProfile;

    @FXML
    private JFXButton orderBtn;

    @FXML
    private JFXButton waitingListBtn;

    @FXML
    private JFXButton messages;

    @FXML
    private JFXButton logoutBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	Traveler currentTraveler = Usermanager.getCurrentTraveler();
    	
    	try {
    		
    	
    	ClientServerMessage<?> findPendingNotificationsOfTravelerOrders = new ClientServerMessage<>(currentTraveler.getId(), Operation.GET_STATUS_PENDING_EMAIL_BY_TRAVELERID);
		ClientUI.clientControllerInstance.sendMessageToServer(findPendingNotificationsOfTravelerOrders);
		
	    ClientServerMessage<?> pendingNotificationsOrders = ClientUI.clientControllerInstance.getData();
	    ArrayList<Order> pendingOrders = (ArrayList<Order>) pendingNotificationsOrders.getDataTransfered();
	    
	    for (Order order : pendingOrders) {
	    	if(currentTraveler.getId().equals(order.getVisitorId())) {
	    		String parkName = order.getParkName();
	    	    String orderDate = order.getDate().toString();
	    	    String orderTime = order.getVisitTime().toString();
	    	    int option = JOptionPane.showConfirmDialog(null, "Your order at " + parkName + " on " + orderDate + " at " + orderTime + " has been confirmed. Would you like to see more details?", "Order Confirmed", JOptionPane.YES_NO_OPTION);
	    	    
	    	    if (option == JOptionPane.YES_OPTION) {
	    	        //change the orderStatus to confirmed
	    	    	 ClientServerMessage<?> updateStatusConfirmed = new ClientServerMessage<>(new ArrayList<String>
	                    (Arrays.asList("CONFIRMED",order.getOrderId()+"")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
	                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatusConfirmed);
	    	    	//change notification status to passed
	    	    	
	    	        // For simplicity, let's display a message dialog with order details
	    	        String message = "Park: " + parkName + "\nDate: " + orderDate + "\nTime: " + orderTime;
	    	        JOptionPane.showMessageDialog(null, message, "Order Details", JOptionPane.INFORMATION_MESSAGE);
	    	        
	    	    }
	    	    
	    	    if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
                    String message = "Park: " + parkName + "\nDate: " + orderDate + "\nTime: " + orderTime;
                    JOptionPane.showMessageDialog(null, message, "Order Details", JOptionPane.INFORMATION_MESSAGE);

                    ClientServerMessage<?> updateStatusCanceled = new ClientServerMessage<>(new ArrayList<String>
                    (Arrays.asList("CANCELED",order.getOrderId()+"")), Operation.PATCH_ORDER_STATUS_ARRAYLIST);
                    ClientUI.clientControllerInstance.sendMessageToServer(updateStatusCanceled);
	                }
		    	}
	
		    }
	    
    		}catch (Exception e) {
    			new Alerts(AlertType.WARNING, "Something went wrong", "Cancellation",
    					"Something went wrong with receiving notifications for orders").showAndWait();
	    	}

    }
    
    @FXML
    public void travelerProfile(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane,"Profile.fxml");
    	}  catch(Exception e) {
			 System.out.print("Something went wrong while trying view service traveler profile, check stack trace");
			 e.printStackTrace();
		 }

    }
    


    // Method to display a cancellation message
    private void showCancellationMessage() {
    	new Alerts(AlertType.WARNING, "Cancellation", "Cancellation",
				"Your visit has been cancelled.").showAndWait();
    }
    
    @FXML
    public void orderBtn(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "OrderVisit.fxml");
    	} catch(Exception e) {
			 System.out.print("Something went wrong while clicking on order a visit button, check stack trace");
			 e.printStackTrace();
    	}

    }
    
    public void viewOrdersbutton(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "TravelerOrdersFrame.fxml");
    	} catch(Exception e) {
			 System.out.print("Something went wrong while clicking on view orders button, check stack trace");
			 e.printStackTrace();
    	}

    }
    
    @FXML
    public void waitingListBtn(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "TravelerWaitingLists.fxml");
    	} catch(Exception e) {
			 System.out.print("Something went wrong while clicking on view waiting list button, check stack trace");
			 e.printStackTrace();
    	}
    }
    
    @FXML
    public void messages(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "TravelerMessages.fxml");
    	} catch(Exception e) {
			 System.out.print("Something went wrong while clicking on view messages button, check stack trace");
			 e.printStackTrace();
    	}
    }
    
    @FXML
    public void logoutBtn(ActionEvent event) throws Exception {
    	try {

    		
    		if(Usermanager.getCurrentTraveler() != null) {
				ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentTraveler(), Operation.PATCH_TRAVELER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				
			}
    		

			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
    }
    
   

	    

	public void orderVisit(ActionEvent click) throws Exception {

		NavigationManager.openPage("OrderAVisitFrame.fxml", click, "Order a visit", true);

	}



	public void viewWaitingList(ActionEvent click) throws Exception {

		NavigationManager.openPage("WaitingListFrame.fxml", click, "Waiting list", true);

	}


	public void exit(ActionEvent click) throws Exception {
		//Exit button, logs out the traveler without going to main menu
		try {
			 
			if(Usermanager.getCurrentTraveler() != null) {
				ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentTraveler(), Operation.PATCH_TRAVELER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				
			}
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true);
			
		}catch(Exception e) {
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
		
	}
		

		
    
    
    public void viewOrders(ActionEvent click) throws Exception {

        try {
            NavigationManager.openPage("OrdersFrame.fxml", click, "Orders", true);
        } catch(Exception e) {
             System.out.print("Something went wrong while clicking on view messages button, check stack trace");
             e.printStackTrace();
        }
    }
   
	    
	    
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



//	public void viewMessages(ActionEvent click) throws Exception {

//		// Fetch messages from the server and set them in the TextArea
//		ClientServerMessage<?> messege = new ClientServerMessage(currentTraveler, Operation.GET_MESSAGES);
//		ClientUI.clientControllerInstance.sendMessageToServer(messege);
//		// get data from server
//		ArrayList<Message> messages = (ArrayList<Message>) ClientController.data.getDataTransfered();
//		
//		ArrayList<String> stringMessages = new ArrayList<>();
//		
//		for (Message message : messages) {
//			stringMessages.add(message.toString());
//		}
//
//		StringBuilder messagesText = new StringBuilder();
//
//		for (String message : stringMessages) {
//			messagesText.append(message).append("\n");
//		}
//
//		messagesTextArea.setText(messagesText.toString());


}