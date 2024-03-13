package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import client.ClientUI;
import common.ClientServerMessage;
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
public class UpdateParametersController  {

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

    void SendRequestAction(ActionEvent event) {
        if (validateInput()) {
        	  try {
                  Integer newStayTime = Integer.valueOf(NewStayTime.getText());
                  Integer newMaxVisitors = Integer.valueOf(NewMaxVisitorsField.getText());
                  Double newAllowedGap = Double.valueOf(NewAllowedGap.getText());
                  Integer parkNumber = Usermanager.getCurrentWorker().getWorksAtPark(); // Assuming this method exists and returns the current user's park number

                  // Instantiating Park object with specified values and nulls for unspecified fields
                  Park updatedPark = new Park(
                      null, // Assuming 'name' is not specified here
                      parkNumber,
                      newMaxVisitors,
                      null, // 'capacity' not specified
                      null, // 'currentVisitors' not specified
                      null, // 'location' not specified
                      newStayTime,
                      null, // 'workersAmount' not specified
                      newAllowedGap,
                      null, // 'managerID' not specified
                      null  // 'workingTime' not specified
                  );

                  // Send updated park object to server
                 // ClientServerMessage<Park> message = new ClientServerMessage<>(updatedPark, Operation.UPDATE_PARK_PARAMETERS);
                 // ClientUI.clientControllerInstance.sendMessageToServer(message);

                  // Assuming response handling is done elsewhere or add it here
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