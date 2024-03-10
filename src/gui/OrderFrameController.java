package gui;

import client.ClientController;
import common.ClientServerMessage;
import common.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OrderFrameController {
	
	private Order order = new Order(null, null, orderId, null, null, null, null, null, null, null, null);
	
	ClientController clientController = new ClientController(null, 0);
	
	@FXML
    private Button cancelVisitBtn;
    
    @FXML
    private Button confirmVisitBtn;
	
    @FXML
    private Button backBtn;
    
	@FXML
    private Label parkNumberLabel;

    @FXML
    private Label amountOfVisitorsLabel;

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label orderTypeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label visitTimeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label visitorIdLabel;

    @FXML
    private Label telephoneNumberLabel;

    @FXML
    private Label orderStatusLabel;
    
	public void setOrderDetails(Order order) {
		// sets the order detailes
		
        parkNumberLabel.setText("Park Number: " + order.getParkNumber());
        amountOfVisitorsLabel.setText("Amount of Visitors: " + order.getAmountOfVisitors());
        orderIdLabel.setText("Order ID: " + order.getOrderId());
        orderTypeLabel.setText("Order Type: " + order.getTypeOfOrder());
        dateLabel.setText("Date: " + order.getDate());
        visitTimeLabel.setText("Visit Time: " + order.getVisitTime());
        priceLabel.setText("Price: " + order.getPrice());
        visitorIdLabel.setText("Visitor ID: " + order.getVisitorId());
        telephoneNumberLabel.setText("Telephone Number: " + order.getTelephoneNumber());
        orderStatusLabel.setText("Order Status: " + order.getOrderStatus());
    }
	
	public void cancelVisit(ActionEvent click) throws Exception {
		// sends a request to the server to delete the order from the DB
		
		ClientServerMessage messageToServer = new ClientServerMessage(order , "DELETE_EXISTING_ORDER" );
		clientController.sendMessageToServer(messageToServer);
		// Display a confirmation dialog
		showOrderCanceledDialog();
		
	}
	
	private void showOrderCanceledDialog() {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Order Canceled");
	    alert.setHeaderText(null);
	    alert.setContentText("Your order has been canceled.");

	    // Show the dialog
	    alert.showAndWait();
	}

    public void confirmVisit(ActionEvent click) throws Exception {
    	// sends a request to the server to change the order status in the DB
    	
    	//order.setStatus("CONFIRM");
    	ClientServerMessage messageToServer = new ClientServerMessage(order , "CHANGE_ORDER_STATUS_CONFIRM" );
		clientController.sendMessageToServer(messageToServer);
		// Display a confirmation dialog
	    showConfirmationDialog();
    	
    }
    
    private void showConfirmationDialog() {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Order Confirmed");
	    alert.setHeaderText(null);
	    alert.setContentText("Your order has been confirmed.");

	    // Show the dialog
	    alert.showAndWait();
	}
    
    public void btnBack(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Update Reservation
		// Button

		try {

			new FXMLLoader();
			Parent root = FXMLLoader.load(getClass().getResource("VisitorFrame.fxml"));
			Stage stage = (Stage) ((Node) click.getSource()).getScene().getWindow(); // hiding primary window
			Scene scene = new Scene(root);

			stage.setTitle("Visitor screen");

			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}

	}
    
}