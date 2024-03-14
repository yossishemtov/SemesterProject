package gui;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

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
public class ParkParametersController {

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
    private TextField parkNumberFiled;
    
    @FXML
    private Label locationLabel;

    @FXML
    private Label StayTimeLabel;
    
    private Park parkData;
    private GeneralParkWorker departmentManager;
    @FXML
    void Getparkparameters(ActionEvent event) {
        String parkNumberStr = parkNumberFiled.getText();
        if (parkNumberStr != null && !parkNumberStr.isEmpty()) {
            try {
                int parkNumber = Integer.parseInt(parkNumberStr);
                fetchParkDetails(parkNumber);
            } catch (NumberFormatException e) {
            	Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error",
    					"Error to load park parameters.", "Park number must be a valid number.");
    			nullResponseAlert.showAndWait();
                // Show an error message to the user
            }
        } else {
            Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error",
					"Error to load park parameters.", "Park number cannot be empty.");
			nullResponseAlert.showAndWait();
            // Show an error message to the user
        }

    }


    private void fetchParkDetails(Integer parkNumber) {
        // Construct and send a message to the server to fetch park details for the given park number
        ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkNumber, Operation.GET_PARK_DETAILS);
        ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
        if (ClientController.data.getFlag()) {
        
        Park parkData = (Park) ClientController.data.getDataTransfered(); // This is assumed to be synchronous but typically isn't.
        System.out.println(parkData.toString());
        // Update UI labels with park data
        updateLabels(parkData);
        }
        else {
        	Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error",
					"Error to load park parameters.", "Not have park for this park number.");
			nullResponseAlert.showAndWait();
        }
    }

    private void updateLabels(Park parkData) {
        if (parkData != null) {
            parkNameLabel.setText(parkData.getName());
            locationLabel.setText(parkData.getLocation());
            maxvisitorLabel.setText(String.valueOf(parkData.getMaxVisitors()));
            CapacityLabel.setText(String.valueOf(parkData.getCapacity()));
            currentvistorLabel.setText(String.valueOf(parkData.getCurrentVisitors()));
            StayTimeLabel.setText(String.valueOf(parkData.getStaytime()) + " hours");
            allowedGapLabel.setText(String.valueOf(parkData.getGap()));
            
        }
	


}}
