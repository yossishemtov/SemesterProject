package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import common.*;
import client.ClientUI;
import client.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the Department Manager's UI in the application. This class
 * handles the interaction of the Department Manager with various UI components
 * like viewing and editing park parameters, creating reports, updating
 * parameters, and logging out.
 */
public class DeparmentManegerController implements Initializable {
	@FXML
	private BorderPane borderPane;
	@FXML
	private AnchorPane topPane;
	@FXML
	private Label userLabel;
	@FXML
	private VBox vbox;
	@FXML
	private JFXButton profileButton;
	@FXML
	private JFXButton ParkParametersBth;
	@FXML
	private JFXButton createReportsButton;
	@FXML
	private JFXButton updateParametersButton;
	@FXML
	private JFXButton logoutBtn;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded. It sets up the initial state of the UI
	 * components.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			loadProfileImmediately();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Loads the profile view immediately upon initialization of the controller.
	 */
	private void loadProfileImmediately() throws IOException {
		loadProfile(null);
	}

	/**
	 * Loads the park parameters view into the BorderPane center. Displays an alert
	 * if the view cannot be loaded.
	 *
	 * @param event The mouse event that triggered this method.
	 */
	@FXML
	void loadParkParameters(MouseEvent event) {
		try {
			NavigationManager.openPageInCenter(borderPane, "ParkParameters.fxml");
		} catch (IOException e) {
			e.printStackTrace(); // Consider replacing with logging
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed to load Park Parameters.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Loads the profile view into the BorderPane center. Displays an alert if the
	 * view cannot be loaded.
	 *
	 * @param event The mouse event that triggered this method, or null if called
	 *              directly.
	 */
	@FXML
	void loadProfile(MouseEvent event) {
		try {
			NavigationManager.openPageInCenter(borderPane, "Profile.fxml");
		} catch (IOException e) {
			e.printStackTrace(); // Consider replacing with logging
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed to load the Profile view.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Loads the reports view into the BorderPane center. Displays an alert if the
	 * view cannot be loaded.
	 *
	 * @param event The mouse event that triggered this method.
	 */
	@FXML
	void loadReports(MouseEvent event) {
		try {
			NavigationManager.openPageInCenter(borderPane, "DepartmentManagerReports.fxml");
		} catch (IOException e) {
			e.printStackTrace(); // Consider replacing with logging
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed to load the Reports view.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Loads the view requests for changes view into the BorderPane center. Displays
	 * an alert if the view cannot be loaded.
	 *
	 * @param event The mouse event that triggered this method.
	 */
	@FXML
	void loadViewRequests(MouseEvent event) {
		try {
			NavigationManager.openPageInCenter(borderPane, "ViewRequestsForChanges.fxml");
		} catch (IOException e) {
			e.printStackTrace(); // Consider replacing with logging
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "",
					"Failed to load the View Requests for Changes.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Handles the logout process for the Department Manager, including clearing
	 * current worker data and navigating back to the home page. Displays an alert
	 * on failure.
	 *
	 * @param event The mouse event that triggered this method.
	 */
	@FXML
	void logOut(MouseEvent event) {
		try {
			if (Usermanager.getCurrentWorker() != null) {
				ClientServerMessage<?> requestToLogout = new ClientServerMessage<>(Usermanager.getCurrentWorker(),
						Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				Usermanager.setCurrentWorker(null);
			}
			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true, true);
		} catch (IOException e) {
			e.printStackTrace();
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
					"Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
	}
}
