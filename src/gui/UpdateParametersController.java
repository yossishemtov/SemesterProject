package gui;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.ChangeRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controls the UI and logic for sending requests to update park parameters by a
 * Park Manager.
 */
public class UpdateParametersController implements Initializable {

	@FXML
	private Label CapacityLabel;

	@FXML
	private Label allowedGapLabel;

	@FXML
	private Label StayTimeLabel;

	@FXML
	private TextField NewStayTime;

	@FXML
	private TextField CapacityField;

	@FXML
	private TextField NewAllowedGap;

	@FXML
	private Label headerLabel;

	@FXML
	private JFXButton SendRequestbth;

	@FXML
	private Label parkNameLabel;
	private Park parkData = null;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		init();
	}

	/**
	 * Initializes and loads park information.
	 */
	private void init() {
		loadInfo();
	}

	/**
	 * Loads park information from the server and updates UI elements accordingly.
	 */
	private void loadInfo() {
		Integer parkNumber = Usermanager.getCurrentWorker().getWorksAtPark();
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkNumber, Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
		if (ClientController.data.getFlag()) {
			parkData = (Park) ClientController.data.getDataTransfered();
			updateLabels(parkData);
		} else {
			Alerts infoalert;

			infoalert = new Alerts(Alerts.AlertType.ERROR, "ERROR", "", "Error to load park parameters.");
			infoalert.showAndWait();

		}
	}

	/**
	 * Updates the UI labels with the data of the specified park.
	 * 
	 * @param parkData The park data to be displayed.
	 */
	private void updateLabels(Park parkData) {
		if (parkData != null) {
			parkNameLabel.setText(parkData.getName());
			CapacityLabel.setText(String.valueOf(parkData.getCapacity()));
			allowedGapLabel.setText(String.valueOf(parkData.getGap()));
			StayTimeLabel.setText(String.valueOf(parkData.getStaytime()) + " hours");
		}
	}

	/**
	 * Handles the action of sending a change request for park parameters.
	 * 
	 * @param event The event that triggered the action.
	 */
	@FXML
	public void SendRequestAction(ActionEvent event) {
		if (validateInput()) {
			Alerts infoalert;

			try {
				Integer newStayTime = Integer.valueOf(NewStayTime.getText());
				Integer newCapacity = Integer.valueOf(CapacityField.getText());
				Integer newAllowedGap = Integer.valueOf(NewAllowedGap.getText());
				Integer parkNumber = Usermanager.getCurrentWorker().getWorksAtPark();

				ChangeRequest request = new ChangeRequest(0, parkData.getName(), parkNumber, newCapacity, newAllowedGap,
						newStayTime, "WAITING_FOR_APPROVAL");
				ClientServerMessage<?> requestForServer = new ClientServerMessage<>(request,
						Operation.POST_NEW_CHANGE_REQUEST);
				ClientUI.clientControllerInstance.sendMessageToServer(requestForServer);

				if (ClientController.data.getFlag()) {
					infoalert = new Alerts(Alerts.AlertType.CONFIRMATION, "CONFIRMATION", "",
							"A change request was successfully received.");
					infoalert.showAndWait();
				} else {
					infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
							"A change request was not successful.");
					infoalert.showAndWait();
				}
			} catch (NumberFormatException e) {
				infoalert = new Alerts(Alerts.AlertType.WARNING, "WARNING", "", "Input values must be numeric.");
				infoalert.showAndWait();
			}
		}
	}

	/**
	 * Validates that all input fields for new parameters are filled out.
	 * 
	 * @return true if all inputs are valid, false otherwise.
	 */
	private boolean validateInput() {
		if (NewStayTime.getText().isEmpty() || CapacityField.getText().isEmpty() || NewAllowedGap.getText().isEmpty()) {
			Alerts infoalert;
			infoalert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "All fields are required.");
			infoalert.showAndWait();
			return false;
		}
		return true;
	}

}
