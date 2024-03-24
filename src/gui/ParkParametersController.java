package gui;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import common.Park;

/**
 * This Class is the GUI controller of ParkParameters.fxml
 * It handles all the JavaFx nodes events.
 * 
 * This screen shows the parameters of a certain park
 *
 */
public class ParkParametersController implements Initializable {

    @FXML
    private Label headerLabel;

    @FXML
    private Label currentvistorLabel;
    
    @FXML
    private JFXButton getDatabth;

    @FXML
    private Label maxvisitorLabel;

    @FXML
    private Label allowedGapLabel;

    @FXML
    private Label CapacityLabel;

    @FXML
    private Label parkNameLabel;
   

    @FXML
    private JFXTextField parkNumberFiled;
    
    @FXML
    private Label locationLabel;

    @FXML
    private Label StayTimeLabel;
    

    @FXML
	private JFXComboBox<String> parkNameComboBox;



	private Map<String, Park> parkNamesToParkInfo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		parkNamesToParkInfo = loadParkNamesToNumbersMap();
		parkNameComboBox.setItems(FXCollections.observableArrayList(parkNamesToParkInfo.keySet()));


		}

	

	private Map<String, Park> loadParkNamesToNumbersMap() {
		ClientServerMessage<?> getParkInfoMsg = new ClientServerMessage(null, Operation.GET_PARKS_INFO);
		ClientUI.clientControllerInstance.sendMessageToServer(getParkInfoMsg);
		getParkInfoMsg = ClientUI.clientControllerInstance.getData();

		List<Park> parks = (List<Park>) getParkInfoMsg.getDataTransfered();
		Map<String, Park> parkNamesToNumbers = new HashMap<>();
		for (Park park : parks) {
			parkNamesToNumbers.put(park.getName(), park);
		}

		return parkNamesToNumbers;
	}
	
	@FXML
	void Getparkparameters(ActionEvent event) {
	    String selectedParkName = parkNameComboBox.getSelectionModel().getSelectedItem();

	    if (selectedParkName == null || !parkNamesToParkInfo.containsKey(selectedParkName)) {
	        Alerts errorAlert = new Alerts(Alert.AlertType.ERROR, "Error", "", "You must choose a park name.");
	        errorAlert.showAndWait();
	    } else {
	        Park selectedPark = parkNamesToParkInfo.get(selectedParkName);
	        updateLabels(selectedPark);
	    }
	}

	private void updateLabels(Park parkData) {
	    if (parkData != null) {
	        parkNameLabel.setText(parkData.getName());
	        locationLabel.setText(parkData.getLocation());
	        maxvisitorLabel.setText(String.valueOf(parkData.getMaxVisitors()));
	        CapacityLabel.setText(String.valueOf(parkData.getCapacity()));
	        currentvistorLabel.setText(String.valueOf(parkData.getCurrentVisitors()));
	        StayTimeLabel.setText(parkData.getStaytime() + " hours");
	        allowedGapLabel.setText(String.valueOf(parkData.getGap()));
	    } else {
	        Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error",
	                "Failed to load park parameters.", "There was an error processing the selected park.");
	        nullResponseAlert.showAndWait();
	    }
	}
}