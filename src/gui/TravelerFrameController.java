package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import common.Traveler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import common.Usermanager;
import client.NavigationManager;


public class TravelerFrameController implements Initializable {
	
	@FXML
    private BorderPane pane;

    @FXML
    private AnchorPane topBorder;

    @FXML
    private Label userLabel;

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
//    	System.out.println(currentTraveler.getEmail());
    	
    	
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
    
    @FXML
    public void orderBtn(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "OrderVisit.fxml");
    	} catch(Exception e) {
			 System.out.print("Something went wrong while clicking on order a visit button, check stack trace");
			 e.printStackTrace();
    	}
    	
    }
    
    @FXML
    public void waitingListBtn(ActionEvent event) throws Exception {
    	try {
    		NavigationManager.openPageInCenter(pane, "WaintingList.fxml");
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
			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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