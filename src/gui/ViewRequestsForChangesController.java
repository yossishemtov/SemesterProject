package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.ChangeRequest;
import common.worker.GeneralParkWorker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;


public class ViewRequestsForChangesController implements Initializable {

    @FXML
    private TableView<ChangeRequest> parametersTable;
    @FXML
    private TableColumn<ChangeRequest, Integer> RequestIDCol, parkIDCol, NewCapacityCol, NewStayTimeCol, NewGapCol;
    @FXML
    private TableColumn<ChangeRequest, String> StatusCol;
    @FXML
    private Button confirmRequestBtn, cancelRequestBtn;
    @FXML
    private Label headerLabel;
    @FXML
    private Label SelectLabal;
    
    private  GeneralParkWorker CurWorker;

    // List to hold and display change requests in the table view
    private ObservableList<ChangeRequest> changeRequestsData = FXCollections.observableArrayList();

    /**
     * Initialize the controller and fetch initial data for the table.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        CurWorker = Usermanager.getCurrentWorker();
        adjustUIForRole(CurWorker);

        fetchChangeRequestsWaitingForApproval(CurWorker);
    }
    
    /**
     * Adjusts the UI elements based on the worker's role.
     */
    private void adjustUIForRole(GeneralParkWorker worker) {
        String role = worker.getRole();

        if ("Park Manager".equals(role)) {
            // Hide confirm and cancel buttons for park managers, including their icons
            confirmRequestBtn.setVisible(false);
            confirmRequestBtn.setManaged(false);
            cancelRequestBtn.setVisible(false);
            cancelRequestBtn.setManaged(false);
            SelectLabal.setVisible(false);
        } else {
            // Show buttons for department managers , including their icons
            confirmRequestBtn.setVisible(true);
            confirmRequestBtn.setManaged(true);
            cancelRequestBtn.setVisible(true);
            cancelRequestBtn.setManaged(true);
        }
    } 

 
    /**
     * Sets up the table columns with the corresponding properties from the ChangeRequest class.
     */
    private void setupTableColumns() {
        // Binding table columns to ChangeRequest properties
        RequestIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkNumber"));
        NewCapacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        NewStayTimeCol.setCellValueFactory(new PropertyValueFactory<>("staytime"));
        NewGapCol.setCellValueFactory(new PropertyValueFactory<>("gap"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("approved"));
    }

    /**
     * Sends a request to the server to fetch change requests awaiting approval.
     */
    private void fetchChangeRequestsWaitingForApproval(GeneralParkWorker CurWorker) {
        ClientServerMessage<?> messageForServer = new ClientServerMessage<>(CurWorker, Operation.GET_CHANGE_REQUESTS);
        ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
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
                    List<ChangeRequest> changeRequests = (List<ChangeRequest>) servermsg.getDataTransfered();
                    Platform.runLater(() -> {
                        changeRequestsData.setAll(changeRequests);
                        parametersTable.setItems(changeRequestsData);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Handles the confirmation of a change request.
     */
    @FXML
    private void confirmRequestBtn(ActionEvent event) {
    	ChangeRequest selectedRequest = parametersTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
        	 ClientServerMessage<ChangeRequest> messageForServer = new ClientServerMessage<>(selectedRequest, Operation.PATCH_PARK_PARAMETERS);
             ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
             if (ClientController.data.getFlag()) {
            		Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
							"A change request was successfully executed on database");
            		infoalert.showAndWait();
            		
             }
             else {
            	 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
							"A change request was not successfully executed on database");
					somethingWentWrong.showAndWait();
             }
        }
        
        handleRequestChange(ChangeRequest.ApprovalStatus.APPROVAL.toString());
    }

    /**
     * Handles the cancellation of a change request.
     */
    @FXML
    private void cancelRequestBtn(ActionEvent event) {
        handleRequestChange(ChangeRequest.ApprovalStatus.REJECTED.toString());
    }
    

    /**
     * Common method to handle both confirm and cancel operations for change requests.
     */
    private void handleRequestChange(String status) {
        ChangeRequest selectedRequest = parametersTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            selectedRequest.setApproved(status);
            ClientServerMessage<ChangeRequest> messageForServer = new ClientServerMessage<>(selectedRequest, Operation.PATCH_CHANGE_REQUEST_STATUS);
            ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
            changeRequestsData.remove(selectedRequest);
            parametersTable.refresh();
            refreshChangeRequests(); // Refresh table to reflect changes
        } else {
            System.out.println("No request selected.");
        }
    }

    /**
     * Refreshes the list of change requests by fetching updated data from the server.
     */
    private void refreshChangeRequests() {
        fetchChangeRequestsWaitingForApproval(CurWorker);
    }
}
