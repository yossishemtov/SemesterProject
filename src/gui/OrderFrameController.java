package gui;

import client.ClientController;
import client.ClientUI;
import client.SystemClient;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
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
import gui.UserOrdersController;

public class OrderFrameController {
	
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
    
    private Order orderToOpen;

    public OrderFrameController(Order order) {
        this.orderToOpen = order;
    }
    
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
		
		ClientServerMessage<?> messageToServer = new ClientServerMessage(orderToOpen , Operation.DELETE_EXISTING_ORDER );
		ClientUI.clientControllerInstance.sendMessageToServer(messageToServer);
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
    	ClientServerMessage<?> messageToServer = new ClientServerMessage(orderToOpen , Operation.PATCH_ORDER_STATUS );
    	ClientUI.clientControllerInstance.sendMessageToServer(messageToServer);
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
		
    	NavigationManager.openPage("VisitorFrame.fxml", click, "Visitor frame", true);

	}
    
}