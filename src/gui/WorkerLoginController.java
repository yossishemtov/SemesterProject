package gui;

import client.ClientUI;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.worker.GeneralParkWorker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientController;
import client.NavigationManager;
import common.*;

/**
 * Controller class for managing the worker login functionality. This class
 * handles user input validation for the username and password fields, and
 * facilitates the login process by communicating with the server to authenticate
 * the worker. It also provides navigation to different screens based on the
 * worker's role and login status.
 */
public class WorkerLoginController implements Initializable{
	@FXML
    private Button LoginBtn;

    @FXML
    private Button BackBtn;

    @FXML
    private JFXTextField WorkerUsername;

    @FXML
    private JFXPasswordField WorkerPwd;
    
    @FXML
    private FontAwesomeIconView userIcon;

    @FXML
    private FontAwesomeIconView passIcon;
    
    
    /**
     * Initializes the worker login screen. It sets up listeners for the username
     * and password text fields to provide visual feedback based on input validation.
     * Specifically, it changes the color and icon of the text fields to indicate
     * whether the input is valid or not.
     * 
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        // Listener for the username text field
        WorkerUsername.textProperty().addListener((obs, oldValue, newValue) -> {
            String pattern = "^[a-zA-Z]{1}[a-zA-Z0-9]{3,20}$"; // Username pattern

            // Set text field color and icon based on input
            if (newValue.isEmpty()) {
                // Set to original color
                userIcon.setFill(Color.web("#5aed99"));
                WorkerUsername.setStyle("-jfx-unfocus-color: #5aed99; " + "-fx-text-fill: white; " + "-fx-prompt-text-fill: #5aed99;");
            } else {
                // Set to red if input is not empty
                userIcon.setFill(Color.RED);
                WorkerUsername.setStyle("-jfx-unfocus-color: red; " + "-fx-text-fill: red; " + "-fx-prompt-text-fill: red;");
            }

            // Set to green if input matches pattern
            if (!newValue.isEmpty() && newValue.matches(pattern)) {
                userIcon.setFill(Color.web("#2cdd43"));
                WorkerUsername.setStyle("-jfx-unfocus-color: #2cdd43; " + "-fx-text-fill: #2cdd43; " + "-fx-prompt-text-fill: #2cdd43;");
            }
        });

        // Listener for the password text field
        WorkerPwd.textProperty().addListener((obs, oldValue, newValue) -> {
            String pattern = "^.{4,30}$"; // Password pattern

            // Set text field color and icon based on input
            if (newValue.isEmpty()) {
                // Set to original color
                passIcon.setFill(Color.web("#5aed99"));
                WorkerPwd.setStyle("-jfx-unfocus-color: #5aed99; " + "-fx-text-fill: white; " + "-fx-prompt-text-fill: #5aed99;");
            } else {
                // Set to red if input is not empty
                passIcon.setFill(Color.RED);
                WorkerPwd.setStyle("-jfx-unfocus-color: red; " + "-fx-text-fill: red; " + "-fx-prompt-text-fill: red;");
            }

            // Set to green if input matches pattern
            if (!newValue.isEmpty() && newValue.matches(pattern)) {
                passIcon.setFill(Color.web("#2cdd43"));
                WorkerPwd.setStyle("-jfx-unfocus-color: #2cdd43; " + "-fx-text-fill: #2cdd43; " + "-fx-prompt-text-fill: #2cdd43;");
            }
        });
    }

  

	/**
	 * Handles the login button action for worker login. When triggered, this method
	 * retrieves the worker's username and password from the input fields, validates
	 * them, and communicates with the server to authenticate the worker. Depending on
	 * the authentication result and the worker's role, it navigates to the corresponding
	 * screen for the worker (e.g., Department Manager Screen, Park Manager Screen, Park
	 * Worker Screen, or Service Worker Screen).
	 * 
	 * @param click The action event triggering the method, typically generated when
	 *              the user clicks the login button.
	 * @throws IOException If an error occurs during I/O operations while communicating
	 *                     with the server or navigating to the next screen.
	 */
	public void WorkerLoginBtn(ActionEvent click) throws IOException {
		// Retrieve worker username and password from input fields
		String workerUsername = WorkerUsername.getText();
		String workerPassword = WorkerPwd.getText();

		// Validate username and password
		Alerts alertUsername = InputValidation.validateUsername(workerUsername);
		Alerts alertPwd = InputValidation.validatePassword(workerPassword);

		Boolean usernameValid = alertUsername.getAlertType().toString().equals("INFORMATION");
		Boolean passwordValid = alertPwd.getAlertType().toString().equals("INFORMATION");

		// Proceed only if both username and password validations passed
		if (usernameValid && passwordValid) { 
			// Create a worker object to send to the server
			GeneralParkWorker workerForServer = new GeneralParkWorker(null, null, null, null, null, workerUsername,
					workerPassword, null);
			
			System.out.println("Worker username:"+workerUsername + " "+workerPassword );
			// Send worker object to server and request worker details
			ClientServerMessage messageForServer = new ClientServerMessage(workerForServer,
					Operation.GET_GENERAL_PARK_WORKER_DETAILS);
			
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
			System.out.println("Worker username:");
			// Retrieve worker details from server
			ClientServerMessage<?> retrieveInformationIfLoggedIn=ClientController.data;
			// Check if the server response is not null
			if (retrieveInformationIfLoggedIn.getFlag() ) {
				GeneralParkWorker workerFromServer = (GeneralParkWorker) ClientController.data.getDataTransfered();
				// Update the current worker in UserManager
				Usermanager.setCurrentWorker(workerFromServer);
				System.out.println("Worker username: " + workerFromServer.getUserName()+ "worker password: " + workerFromServer.getPassword());
				retrieveInformationIfLoggedIn = new ClientServerMessage<>(workerFromServer, Operation.GET_GENERALPARKWORKER_SIGNED);
				ClientUI.clientControllerInstance.sendMessageToServer(retrieveInformationIfLoggedIn);
				//Checks if worker is not loggedin
				Boolean isLoggedIn = ClientController.data.getFlag();
				if(!isLoggedIn) {
					//Logging in the user
					ClientServerMessage<?> requestToLoginTheWorker = new ClientServerMessage<>(workerFromServer, Operation.PATCH_GENERALPARKWORKER_SIGNEDIN);
					ClientUI.clientControllerInstance.sendMessageToServer(requestToLoginTheWorker);
					// Navigate based on the worker's role
					String role = workerFromServer.getRole();
					switch (role) {
					case "Department Manager":
						System.out.println("in Department Manager");
						NavigationManager.openPage("DepartmentManagerScreen.fxml", click, "departmentManagerScreen", true, true);
						break;
					case "Park Manager":
						System.out.println("in Park Manager");
						NavigationManager.openPage("ParkManager.fxml", click, "parkManagerScreen", true, true);
						break;
					case "Park Worker":
						System.out.println("in Worker");
						NavigationManager.openPage("ParkWorkerFrame.fxml", click, "workerScreen", true, true);

						break;
					case "Service Worker":
						NavigationManager.openPage("ServiceWorkerFrame.fxml", click, "ServiceWorkerScreen", true, true);
						break;
					default:
						System.out.println("Role not recognized. Unable to proceed.");
						// Optionally, display an error message or alert to the user here
						break;
					}
					// if the worker is already logged in
				}else {
					Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Already Logged In",
							"Worker already logged in!", "Worker already logged in!");
					nullResponseAlert.showAndWait();
				}

				System.out.println("Worker added to list. Worker Username: " + workerFromServer.getUserName());
			} else {
				// Handle null response from server (worker details not found or error occurred)

				System.out.println("Getting null worker from server");
				Alerts nullResponseAlert = new Alerts(Alert.AlertType.ERROR, "Login Error",
						"Wrong Username or Password", "Please try again or contact the system administrator.");
				nullResponseAlert.showAndWait();
			}
		} else {
			// Display error alerts if the username or password validation failed
			if (!usernameValid) {
				alertUsername.showAndWait();
			}
			if (!passwordValid) {
				alertPwd.showAndWait();
			}
		}
	}

	/**
	 * Handles the back button action. When triggered, this method navigates the user
	 * back to the home page by loading the "HomePageFrame.fxml" view. 
	 * @param click The action event triggering the method, typically generated when
	 *              the user clicks the back button.
	 */
	public void BackBtn(ActionEvent click) {
		try {
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true, true);

		} catch (Exception e) {
			e.printStackTrace();
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on the back button");
			errorAlert.showAndWait();
		}
	}
}
