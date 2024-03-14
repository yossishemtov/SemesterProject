package gui;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import com.jfoenix.controls.JFXButton;

import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.ChangeRequest;
import common.worker.GeneralParkWorker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientUI;
import common.ClientServerMessage;
import common.Operation;

import common.Usermanager;
import common.worker.ChangeRequest;


public class ViewRequestsForChangesController implements Initializable {

    // FXML components bound to the view
    @FXML
    private TableView<ChangeRequest> parametersTable;
    @FXML
    private TableColumn<ChangeRequest, Integer> RequestIDCol, parkIDCol, NewMaxVisitorsCol, NewStayTimeCol, NewGapCol;
    @FXML
    private TableColumn<ChangeRequest, String> StatusCol;
    @FXML
    private Button confirmRequestBtn, cancelRequestBtn;
    @FXML
    private Label headerLabel;
    
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
        // Assuming getRole() returns a String indicating the worker's role
        String role = worker.getRole();

        if ("Park Manager".equals(role)) {
            // Hide confirm and cancel buttons for park managers
            confirmRequestBtn.setVisible(false);
            cancelRequestBtn.setVisible(false);
        } else {
            // Show buttons for department managers or any other roles if needed
            confirmRequestBtn.setVisible(true);
            cancelRequestBtn.setVisible(true);
        }
    }
 
    /**
     * Sets up the table columns with the corresponding properties from the ChangeRequest class.
     */
    private void setupTableColumns() {
        // Binding table columns to ChangeRequest properties
        RequestIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkNumber"));
        NewMaxVisitorsCol.setCellValueFactory(new PropertyValueFactory<>("maxVisitors"));
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
