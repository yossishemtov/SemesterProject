package gui;

import client.NavigationManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomePageController {
	@FXML
    private Button ExitBtn;

    @FXML
    private JFXButton WorkerLoginBtn;

    @FXML
    private JFXButton TravelerLoginBtn;

	public void start(Stage primaryStage) throws Exception {

		// Starting the root scene of the HomePage
		try {
			NavigationManager.openPage("HomePageFrame.fxml", primaryStage, "Home Page", true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void TravelerLoginBtn(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Client Login button
		try {

			NavigationManager.openPage("TravelerLoginFrame.fxml", click, "Traveler Login", true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the Traveler login button, check stack trace");
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
