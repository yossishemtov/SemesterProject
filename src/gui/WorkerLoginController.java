package gui;

import client.ClientUI;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.worker.GeneralParkWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import client.ClientController;
import client.NavigationManager;
import common.*;

public class WorkerLoginController {
	@FXML
    private Button LoginBtn;

    @FXML
    private Button BackBtn;

    @FXML
    private JFXTextField WorkerUsername;

    @FXML
    private JFXPasswordField WorkerPwd;

 
	@FXML
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
//			System.out.println("worker username: " + workerForServer.getUserName() + "worker password: " + workerForServer.getPassword());
			System.out.println("Worker username:");
			// Send worker object to server and request worker details
			ClientServerMessage messageForServer = new ClientServerMessage(workerForServer,
					Operation.GET_GENERAL_PARK_WORKER_DETAILS);
			
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
			System.out.println("Worker username:");

			ClientServerMessage<?> retrieveInformationIfLoggedIn=ClientController.data;
			// Retrieve worker details from server
			

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
						NavigationManager.openPage("DepartmentManagerScreen.fxml", click, "departmentManagerScreen", true);
						break;
					case "Park Manager":
						System.out.println("in Park Manager");
						NavigationManager.openPage("ParkManager.fxml", click, "parkManagerScreen", true);
						break;
					case "Park Worker":
						System.out.println("in Worker");
						NavigationManager.openPage("ParkWorkerFrame.fxml", click, "workerScreen", true);

						break;
					case "Service Worker":
						NavigationManager.openPage("ServiceWorkerFrame.fxml", click, "ServiceWorkerScreen", true);
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

	public void BackBtn(ActionEvent click) {
		try {
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}

}
