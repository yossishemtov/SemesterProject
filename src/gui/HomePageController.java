package gui;

import client.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomePageController {
	@FXML
	private Button ClientLoginBtn;
	@FXML
	private Button WorkerLoginBtn;
	@FXML
	private Button ExitBtn;

	public void start(Stage primaryStage) throws Exception {

		// Starting the root scene of the HomePage
		try {
			NavigationManager.openPage("HomePageFrame.fxml", primaryStage, "Home Page", true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ClientLoginBtn(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Client Login button
		try {

			NavigationManager.openPage("VisitorLoginFrame.fxml", click, "Client Login", true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the Client login button, check stack trace");
			e.printStackTrace();
		}
	}

	public void WorkerLoginBtn(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Worker Login button
		try {

			NavigationManager.openPage("WorkerLoginFrame.fxml", click, "worker Login", true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the Worker login button, check stack trace");
			e.printStackTrace();
		}

	}

	public void ExitBtnAction(ActionEvent event) {
		// Get a handle to the stage
		Stage stage = (Stage) ExitBtn.getScene().getWindow();
		// Do what you need to do before closing
		stage.close();
	}

}
