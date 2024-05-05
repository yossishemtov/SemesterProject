package gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import common.Park;

/**
 * Controls the Park Parameters GUI, allowing users to view and understand
 * park-specific parameters. This class handles events on the
 * ParkParameters.fxml screen, facilitating the display of park details such as
 * name, location, capacity, and visitor statistics.
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

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded. It initializes the park name combo box with
	 * park names.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		parkNamesToParkInfo = loadParkNamesToNumbersMap();
		parkNameComboBox.setItems(FXCollections.observableArrayList(parkNamesToParkInfo.keySet()));

	}

	/**
	 * Loads a mapping of park names to Park objects, which is used to populate the
	 * park name combo box and retrieve park details.
	 *
	 * @return A map of park names to their corresponding Park objects.
	 */
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

	/**
	 * Handles the event triggered by pressing the "Get Data" button. This method
	 * retrieves the selected park's parameters and updates the UI accordingly.
	 *
	 * @param event The ActionEvent triggered by clicking the button.
	 */
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

	/**
	 * Updates the UI labels with the details of the selected park.
	 *
	 * @param parkData The Park object containing the details to be displayed.
	 */
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
			Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error", "Failed to load park parameters.",
					"There was an error processing the selected park.");
			nullResponseAlert.showAndWait();
		}
	}
}