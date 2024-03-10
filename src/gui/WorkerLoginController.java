package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.GeneralParkWorker; // Make sure this import statement correctly points to your GeneralParkWorker class

public class WorkerLoginController {

	@FXML
	private Button LoginBtn;
	@FXML
	private Button BackBtn;
	@FXML
	private TextField WorkerUsername;
	@FXML
	private TextField WorkerPwd;

	// Assuming you have a way to store or handle these workers after login

	@FXML
	public void WorkerLoginBtn(ActionEvent click) throws IOException {
		// Simulated login (Note: real authentication should verify credentials)
		// For demonstration, credentials are not verified and worker information is
		
		GeneralParkWorker workerForServer = new GeneralParkWorker(null, null, null, null, null,
				WorkerUsername.getText(), WorkerPwd.getText(), null);

		ClientServerMessage<?> messageForServer = new ClientServerMessage(workerForServer,
				Operation.GET_GENERAL_PARK_WORKER_DETAILS);
		System.out.println("0");
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
		// ClientController.systemClient.handleMessageFromClientController(message);
		System.out.println("1");
		GeneralParkWorker workerFromServer = (GeneralParkWorker) ClientController.data.getDataTransfered();
		System.out.println("2");

		Usermanager.setCurrentWorker(workerFromServer);
		System.out.println(workerFromServer.toString());
		String role = workerFromServer.getRole();
		switch (role) {

		case "Department Manager":
			System.out.println("in Department Manager");
			NavigationManager.openPage("DepartmentManagerScreen.fxml", click, "departmentmenigerScreen", true);
		}

		System.out.println("Worker added to list. Worker Username: " + workerFromServer.getUserName()); // For debugging
																										// purposes
	}

	public void BackBtn(ActionEvent click) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("HomePageFrame.fxml"));
			Stage stage = (Stage) ((Node) click.getSource()).getScene().getWindow(); // hiding primary window
			Scene scene = new Scene(root);

			stage.setTitle("Home Page");

			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}
}
