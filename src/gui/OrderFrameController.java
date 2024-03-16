package gui;


import client.ClientUI;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class OrderFrameController {
	
	//private Order order = new Order(null, null, null, null, null, null, null, null, null, null, null);
	
	//ClientController clientController = new ClientController(null, 0);
	@FXML
    private Button cancelVisitBtn;
    
    @FXML
    private Button confirmVisitBtn;
	
    @FXML
    private Button backBtn;
    
    private Order orderToOpen;

    public OrderFrameController(Order order) {
        this.orderToOpen = order;
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
		
    	NavigationManager.openPage("OrdersFrame.fxml", click, "Visitor frame", true);

	}
    
}