package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TableView;
import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Traveler;
import common.Usermanager;
import common.worker.ChangeRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class TravelerOrdersController implements Initializable {

	
    @FXML
    private TableView<Order> TravelerOrders;

    @FXML
    private TableColumn<Order, Integer> orderIDCol;
    
	@FXML
	private TableColumn<Order, String> parkNameCol;

    @FXML
    private TableColumn<Order, Integer> AmtOfVisitorsCol;

    @FXML
    private TableColumn<Order, Float> PriceCol;

    @FXML
    private TableColumn<Order, LocalDate> DateCol;

    @FXML
    private TableColumn<Order, LocalTime> VisitTimeCol;

    @FXML
    private TableColumn<Order, String> StatusCol;

    @FXML
    private TableColumn<Order, String> TypeOfVisitCol;

    @FXML
    private JFXButton cancelOrder;

    @FXML
    private JFXButton ConfirmOrder;
    
    

//	static HashMap<Integer, Order> orderHashMap = new HashMap<>();

//	private Order orderToOpen;
	
    private Traveler currentTraveler;
	private ObservableList<Order> ordersData = FXCollections.observableArrayList();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load orders data from the database and initialize the table
		currentTraveler = Usermanager.getCurrentTraveler() ;
		System.out.println(currentTraveler.getId());
		setupTableColumns();
		// load Traveler orders and present them in a table
		loadOrdersFromDatabase(currentTraveler);
	}
	
	/**
     * Sets up the table columns with the corresponding properties from the ChangeRequest class.
     */
    private void setupTableColumns() {
        // Binding table columns to ChangeRequest properties
    	orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    	parkNameCol.setCellValueFactory(new PropertyValueFactory<>("parkName"));
    	AmtOfVisitorsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfVisitors"));
    	PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		VisitTimeCol.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
		TypeOfVisitCol.setCellValueFactory(new PropertyValueFactory<>("typeOfOrder"));
		StatusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		
    }

	private void loadOrdersFromDatabase(Traveler currentTraveler) {
		ClientServerMessage<?> clientServerMessage = new ClientServerMessage<>(currentTraveler,Operation.GET_ALL_ORDERS);
		ClientUI.clientControllerInstance.sendMessageToServer(clientServerMessage);
		waitForServerResponse();
	}
	
	/**
     * Waits for a server response asynchronously and updates the UI with the fetched data.
     */
	private void waitForServerResponse() {
        new Thread(() -> {
            try {
                Thread.sleep(10); // Simulated wait for server response
                // Check server response and update UI accordingly
                ClientServerMessage<?> servermsg = ClientUI.clientControllerInstance.getData();

                if (servermsg.getFlag()) {
                    List<Order> ordersList = (List<Order>) servermsg.getDataTransfered();
                    System.out.println(ordersList.get(0).getPrice());
                    Platform.runLater(() -> {
                    	ordersData.setAll(ordersList);
                    	TravelerOrders.setItems(ordersData);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

	// function for confirm order
	public void ConfirmTravelerOrder(ActionEvent click) throws Exception {
	    // Get the selected order from the TableView
	    Order selectedRequest = TravelerOrders.getSelectionModel().getSelectedItem();
	    
	    // Check if an order is selected
	    if (selectedRequest != null) {
	        try {
	            // Check if the selected order's status is PENDING
	            if (selectedRequest.getOrderStatus().equals("PENDING") || selectedRequest.getOrderStatus().equals("PENDING_EMAIL_SENT") ) {
	                // Update the order status to CONFIRMED
	                selectedRequest.setStatus(Order.status.CONFIRMED.toString());
	                System.out.println("New order status: " + selectedRequest.getOrderStatus());

	                // Create a message to send to the server to update the order status
	                ClientServerMessage<?> messageForServer = new ClientServerMessage<>(selectedRequest, Operation.PATCH_ORDER_STATUS);
	                ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

	                // Handle the response from the server
	                if (ClientController.data.getFlag()) {
	                    // If the order status is successfully updated in the database
	                    // Show a confirmation message
	                    Alerts infoalert = new Alerts(Alerts.AlertType.CONFIRMATION, "CONFIRMATION", "",
	                            "You just confirmed your visit");
	                    infoalert.showAndWait();
	                    
	                    // Remove the updated order from the TableView
	                    ordersData.remove(selectedRequest);
	                    // Refresh the TableView to reflect the changes
	                    TravelerOrders.refresh();
	                    // Reload orders from the database to update the TableView with the latest data
	                    loadOrdersFromDatabase(currentTraveler); 
	                } else {
	                    // If there was an error updating the order status in the database
	                    Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
	                            "Approved your visit was not successfully executed on database");
	                    somethingWentWrong.showAndWait();
	                }
	            } else {
	                // If the selected order's status is not PENDING, display a warning
	                Alerts notPendingAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
	                        "Only orders with status PENDING can be confirmed.");
	                notPendingAlert.showAndWait();
	            }
	        } catch (Exception e) {
	            // If an error occurs during the process, display an error message
	            System.err.println("Error occurred while confirming order:");
	            e.printStackTrace();
	            Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
	                    "An error occurred while confirming your visit. Please try again later.");
	            errorAlert.showAndWait();
	        }
	    } else {
	        // If no order is selected, display a warning
	        Alerts noOrderSelectedAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
	                "Please select an order to confirm.");
	        noOrderSelectedAlert.showAndWait();
	    }    
	}
	
	// Function for canceling an order
	public void CancelTravelerOrder(ActionEvent click) throws Exception {
	    // Get the selected order from the TableView
	    Order selectedRequest = TravelerOrders.getSelectionModel().getSelectedItem();
	    
	    // Check if an order is selected
	    if (selectedRequest != null) {
	        try {
	            // Check if the selected order's status is PENDING or CONFIRMED
	        	if (selectedRequest.getOrderStatus().equals("PENDING") || selectedRequest.getOrderStatus().equals("CONFIRMED")) {
	                // Update the order status to CANCELED
	                selectedRequest.setStatus("CANCELED");
	                System.out.println("New order status: " + selectedRequest.getOrderStatus());

	                // Create a message to send to the server to update the order status
	                ClientServerMessage<?> messageForServer = new ClientServerMessage<>(selectedRequest, Operation.PATCH_ORDER_STATUS);
	                ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

	                // Handle the response from the server
	                if (ClientController.data.getFlag()) {
	                    // If the order status is successfully updated in the database
	                    // Show a confirmation message
	                    Alerts infoalert = new Alerts(Alerts.AlertType.CONFIRMATION, "CONFIRMATION", "",
	                            "You just canceled your visit");
	                    infoalert.showAndWait();
	                    
	                    // Remove the updated order from the TableView
	                    ordersData.remove(selectedRequest);
	                    // Refresh the TableView to reflect the changes
	                    TravelerOrders.refresh();
	                    // Reload orders from the database to update the TableView with the latest data
	                    loadOrdersFromDatabase(currentTraveler); 
	                } else {
	                    // If there was an error updating the order status in the database
	                    Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
	                            "Canceling your visit was not successfully executed on database");
	                    somethingWentWrong.showAndWait();
	                }
	            } else {
	                // If the selected order's status is not PENDING or CONFIRMED, display a warning
	                Alerts notPendingOrConfirmedAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
	                        "Only orders with status PENDING or CONFIRMED can be canceled.");
	                notPendingOrConfirmedAlert.showAndWait();
	            }
	        } catch (Exception e) {
	            // If an error occurs during the process, display an error message
	            System.err.println("Error occurred while canceling order:");
	            e.printStackTrace();
	            Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
	                    "An error occurred while canceling your visit. Please try again later.");
	            errorAlert.showAndWait();
	        }
	    } else {
	        // If no order is selected, display a warning
	        Alerts noOrderSelectedAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
	                "Please select an order to cancel.");
	        noOrderSelectedAlert.showAndWait();
	    }    
	}
}
