package gui;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.Alert.AlertType;
import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Traveler;
import common.Usermanager;
import common.WaitingList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TravelerWaitingListsController implements Initializable {


	@FXML
	private TableColumn<WaitingList, Integer> waitingListIdCol;

	@FXML
	private TableColumn<WaitingList, Integer> placeInListCol;

	@FXML
	private TableColumn<WaitingList, String> parkNameCol;

	@FXML
	private TableColumn<WaitingList, String> amountOfVisitorsCol;

	@FXML
	private TableColumn<WaitingList, String> priceCol;

	@FXML
	private TableColumn<WaitingList, Integer> dateCol;

	@FXML
	private TableColumn<WaitingList, String> visitTimeCol;

	@FXML
	private TableColumn<WaitingList, String> orderTypeCol;

	@FXML
	private TableView<WaitingList> waitingListsTable;

	@FXML
	private JFXButton exitWaitingListBtn;

	private ObservableList<WaitingList> waitingListsData = FXCollections.observableArrayList();

	private Traveler currentTraveler;

	// order class needs to implement serializable

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load orders data from the database and initialize the table
		 currentTraveler = Usermanager.getCurrentTraveler();
//		currentTraveler = new Traveler(1214214, null, null, null, null, null, null);
		System.out.println(currentTraveler.getId());
		setupTableColumns();
		loadWaitingListsFromDatabase(currentTraveler);
	}

	private void loadWaitingListsFromDatabase(Traveler currentTraveler) {
		ClientServerMessage<?> clientServerMessage = new ClientServerMessage<>(currentTraveler, Operation.GET_ALL_WAITING_LISTS);
		ClientUI.clientControllerInstance.sendMessageToServer(clientServerMessage);
		waitForServerResponse();

		/*
		 * try { // The client controller receives the command to pass it further // (It
		 * stops the execution flow for the client until the answer is received)
		 * loadOrdersData(ClientUI.clientControllerInstance.getData()); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

	/**
	 * Sets up the table columns with the corresponding properties from the
	 * ChangeRequest class.
	 */
	private void setupTableColumns() {
		// Binding table columns to ChangeRequest properties
		waitingListIdCol.setCellValueFactory(new PropertyValueFactory<>("waitingListId"));
		placeInListCol.setCellValueFactory(new PropertyValueFactory<>("placeInList"));
		parkNameCol.setCellValueFactory(new PropertyValueFactory<>("parkName"));
		amountOfVisitorsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfVisitors"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		visitTimeCol.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
		orderTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeOfOrder"));
	}

	/**
	 * Waits for a server response asynchronously and updates the UI with the
	 * fetched data.
	 */
	private void waitForServerResponse() {
		new Thread(() -> {
			try {
				Thread.sleep(10); // Simulated wait for server response
				// Check server response and update UI accordingly
				ClientServerMessage<?> servermsg = ClientUI.clientControllerInstance.getData();
				if(servermsg.getDataTransfered() != null) {
					List<WaitingList> waitingListsList = (List<WaitingList>) servermsg.getDataTransfered();
					System.out.println("1");
					Platform.runLater(() -> {
						waitingListsData.setAll(waitingListsList);
						waitingListsTable.setItems(waitingListsData);
					});
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}


	/**
	 * Handles exiting from a waiting list.
	 */
	@FXML
//	private void exitWaitingListBtn(ActionEvent click) {
//		WaitingList selectedWaitingList = waitingListsTable.getSelectionModel().getSelectedItem();
//		if (selectedWaitingList != null) {
//			ClientServerMessage<WaitingList> messageForServer = new ClientServerMessage<>(selectedWaitingList,
//					Operation.DELETE_EXISTING_WAITING_LIST);
//			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
//			if (ClientController.data.getFlag()) {
//				Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
//						"Deleting waiting list was successfully executed on database");
//				infoalert.showAndWait();
//
//			} else {
//				Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
//						"Deleting waiting list was not successfully executed on database");
//				somethingWentWrong.showAndWait();
//			}
//			waitingListsTable.refresh();
//			loadWaitingListsFromDatabase(currentTraveler); // Refresh table to reflect changes
//		}
//	    else {
//	    	// If no waiting list is selected, display a warning
//	        Alerts noOrderSelectedAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
//	                "Please select an waiting list to cancel.");
//	        noOrderSelectedAlert.showAndWait();
//        }
//	}
	
	
	public void exitWaitingList(ActionEvent click) throws Exception {
	    // Get the selected order from the TableView
	    Order selectedWaitingList = waitingListsTable.getSelectionModel().getSelectedItem();
	    
	    // Check if an waiting list is selected
	    if (selectedWaitingList != null) {
	        try {
	        	// Create a message to send to the server to delete from waiting list
	        	ClientServerMessage<?> messageForServer = new ClientServerMessage<>(selectedWaitingList, Operation.DELETE_EXISTING_WAITING_LIST);
	            ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

	            // Handle the response from the server
	            if (ClientUI.clientControllerInstance.getData().getDataTransfered() != null) {
	            	// If the waiting list is successfully deleted in the database
	                // Show a warning message
	                Alerts infoalert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "", "You just exit waiting list");
	                infoalert.showAndWait(); 
                    // Remove the updated order from the TableView
                    waitingListsData.remove(selectedWaitingList);
                    // Refresh the TableView to reflect the changes
                    waitingListsTable.refresh();
                    // Reload orders from the database to update the TableView with the latest data
                    loadWaitingListsFromDatabase(currentTraveler); 
	            } 
	            else {
                    // If there was an error updating the order status in the database
                    Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
                            "Exiting from waiting list was not successfully executed on database");
                    somethingWentWrong.showAndWait();
                }
            } catch (Exception e) {
		            // If an error occurs during the process, display an error message
		            e.printStackTrace();
		            Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
		                    "An error occurred while exiting waiting list. Please try again later.");
		            errorAlert.showAndWait();
            }
	    } else {
	        // If no order is selected, display a warning
	        Alerts noWaitingListSelectedAlert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "",
	                "Please select an waiting list to exit from.");
	        noWaitingListSelectedAlert.showAndWait();
	    }    
	}
}
