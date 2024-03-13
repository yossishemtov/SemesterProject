package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
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

/**
 * This class control the request sending, that is used by Park Manager.
 */
public class UpdateParametersController implements Initializable {

	@FXML
	private Label maxvisitorLabel;

	@FXML
	private Label allowedGapLabel;

	@FXML
	private Label StayTimeLabel;

	@FXML
	private TextField NewStayTime;

	@FXML
	private TextField NewMaxVisitorsField;

	@FXML
	private TextField NewAllowedGap;

	@FXML
	private Label headerLabel;

	@FXML
	private JFXButton SendRequestbth;

	@FXML
	private Label parkNameLabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
	}

	private void init() {
		loadInfo();
	}

	private void loadInfo() {

		Integer parkNumber = Usermanager.getCurrentWorker().getWorksAtPark();

		// Construct and send a message to the server to fetch park details for the
		// given park number
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkNumber, Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
		if (ClientController.data.getFlag()) {

			Park parkData = (Park) ClientController.data.getDataTransfered(); // This is assumed to be synchronous but
																				// typically isn't.
			System.out.println(parkData.toString());
			// Update UI labels with park data
			updateLabels(parkData);
		} else {
			Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Error", "Error to load park parameters.",
					"Not have park for this park number.");
			nullResponseAlert.showAndWait();
		}

	}

	private void updateLabels(Park parkData) {
		if (parkData != null) {
			parkNameLabel.setText(parkData.getName());
			maxvisitorLabel.setText(String.valueOf(parkData.getMaxVisitors()));
			NewAllowedGap.setText(String.valueOf(parkData.getGap()));
			StayTimeLabel.setText(String.valueOf(parkData.getStaytime()) + " hours");
		}
	}

	void SendRequestAction(ActionEvent event) {
		if (validateInput()) {
			try {
				Integer newStayTime = Integer.valueOf(NewStayTime.getText());
				Integer newMaxVisitors = Integer.valueOf(NewMaxVisitorsField.getText());
				Double newAllowedGap = Double.valueOf(NewAllowedGap.getText());
				Integer parkNumber = Usermanager.getCurrentWorker().getWorksAtPark(); // Assuming this method exists and
																						// returns the current user's
																						// park number

				Park updatedPark = new Park(null, // Assuming 'name' is not specified here
						parkNumber, newMaxVisitors, null, // 'capacity' not specified
						null, // 'currentVisitors' not specified
						null, // 'location' not specified
						newStayTime, null, // 'workersAmount' not specified
						newAllowedGap, null, // 'managerID' not specified
						null // 'workingTime' not specified
				);

			} catch (NumberFormatException e) {
				showAlert("Input values must be numeric.");
			}
		}
	}

	/**
	 * Validates that all new parameter fields are filled.
	 * 
	 * @return true if all inputs are valid, false otherwise.
	 */
	private boolean validateInput() {
		if (NewStayTime.getText().isEmpty()) {
			showAlert("New Stay Time is required.");
			return false;
		}
		if (NewMaxVisitorsField.getText().isEmpty()) {
			showAlert("New Max Visitors field is required.");
			return false;
		}
		if (NewAllowedGap.getText().isEmpty()) {
			showAlert("New Allowed Gap is required.");
			return false;
		}
		// Additional validation can be added here (e.g., numeric values, ranges, etc.)
		return true;
	}

	/**
	 * Shows an alert dialog with the specified message.
	 * 
	 * @param message The message to display in the alert dialog.
	 */
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Validation Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}