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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private TableView<ChangeRequest> parametersTable;

    @FXML
    private TableColumn<ChangeRequest, Integer> RequestIDCol;

    @FXML
    private TableColumn<ChangeRequest, Integer> parkIDCol;

    @FXML
    private TableColumn<ChangeRequest, Integer> NewMaxVisitorsCol;

    @FXML
    private TableColumn<ChangeRequest, Integer> NewStayTimeCol;

    @FXML
    private TableColumn<ChangeRequest, Double> NewGapCol;

    @FXML
    private TableColumn<ChangeRequest, String> StatusCol;

    @FXML
    private Button confirmRequestBtn, cancelRequestBtn;

    @FXML
    private Label headerLabel;

    private ObservableList<ChangeRequest> changeRequestsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        int parkId = Usermanager.getCurrentWorker().getWorksAtPark(); // Assumes you have this method to get the current worker's park ID
        fetchChangeRequestsWaitingForApproval(parkId);
    }

    private void setupTableColumns() {
        RequestIDCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkId"));
        NewMaxVisitorsCol.setCellValueFactory(new PropertyValueFactory<>("newMaxVisitors"));
        NewStayTimeCol.setCellValueFactory(new PropertyValueFactory<>("newStayTime"));
        NewGapCol.setCellValueFactory(new PropertyValueFactory<>("newGap"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void fetchChangeRequestsWaitingForApproval(int parkId) {
        // This method will send a request to the server to get change requests waiting for approval
        ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkId, Operation.GET_CHANGE_REQUESTS_WAITING_FOR_APPROVAL);
        ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

        // Wait for the response asynchronously
        waitForServerResponse();
    }

    private void waitForServerResponse() {
        // This is a simplistic way to wait for a response. In a real application, consider using more sophisticated concurrency handling mechanisms.
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Wait for 2 seconds for the server to respond. Adjust the timing as necessary.
                List<ChangeRequest> changeRequests = (List<ChangeRequest>) ClientUI.clientControllerInstance.getData().getDataTransfered();
                // Now update the UI with the received data. Must be run on the JavaFX application thread.
                javafx.application.Platform.runLater(() -> {
                    changeRequestsData.setAll(changeRequests);
                    parametersTable.setItems(changeRequestsData);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void onConfirmRequestAction() {
        ChangeRequest selectedRequest = parametersTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            // Send confirmation to the server
            System.out.println("Confirm Request: " + selectedRequest.getId());
            // Implement server communication here
        }
    }

    @FXML
    private void onCancelRequestAction() {
        ChangeRequest selectedRequest = parametersTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            // Send cancellation to the server
            System.out.println("Cancel Request: " + selectedRequest.getId());
            // Implement server communication here
        }
    }
}
