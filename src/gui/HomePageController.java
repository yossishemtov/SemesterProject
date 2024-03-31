package gui;

import client.NavigationManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for managing the home page view. Handles the interaction
 * logic for the home page interface, including actions such as logging in as a
 * worker or traveler, playing YouTube videos, and exiting the application.
 */
public class HomePageController {
	@FXML
	private Button ExitBtn;

	@FXML
	private JFXButton WorkerLoginBtn;

	@FXML
	private JFXButton TravelerLoginBtn;

	@FXML
	private ImageView MountRainier;

	@FXML
	private ImageView YellowStone;

	@FXML
	private ImageView Yosemite;

	/**
	 * Initializes the controller. Sets up event handlers for the images to play
	 * YouTube videos when clicked.
	 */
	@FXML
	void initialize() {
		// Add event handlers to images
		MountRainier.setOnMouseClicked(
				event -> playYouTubeVideo("https://www.youtube.com/embed/kQY3fgSkVnI?si=qlrX2_P-K84fgej9"));
		YellowStone.setOnMouseClicked(
				event -> playYouTubeVideo("https://www.youtube.com/embed/bme0rs75Z3E?si=hlM9hYCsrLXw5aE0"));
		Yosemite.setOnMouseClicked(
				event -> playYouTubeVideo("https://www.youtube.com/embed/9fJEFi3ccwI?si=TaKcZo4Rov7VNs96"));

	}

	/**
	 * Plays a YouTube video in a new stage.
	 * 
	 * @param videoId The YouTube video ID.
	 */

	private void playYouTubeVideo(String videoId) {
		// Create a new stage to display the YouTube video
		Stage videoStage = new Stage();
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		webEngine.load(videoId);

		// Set up the layout and scene
		videoStage.initModality(Modality.APPLICATION_MODAL);
		videoStage.setScene(new Scene(webView, 640, 480));
		videoStage.setTitle("YouTube Video");

		// Add a listener to handle video stage close event
		videoStage.setOnCloseRequest(event -> {
			// Stop the video playback when the stage is closed
			webEngine.load(null); // This stops the video and clears the WebView content
		});

		videoStage.show();
	}

	/**
	 * Starts the home page.
	 * 
	 * @param primaryStage The primary stage of the application.
	 * @throws Exception If an error occurs while starting the home page.
	 */
	public void start(Stage primaryStage) throws Exception {

		// Starting the root scene of the HomePage
		try {
			NavigationManager.openPage("HomePageFrame.fxml", primaryStage, "Home Page", true, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the action when the traveler login button is clicked. Opens a new
	 * scene for traveler login.
	 * 
	 * @param click The action event triggering the method.
	 * @throws Exception If an error occurs while opening the traveler login scene.
	 */
	public void TravelerLoginBtn(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Client Login button
		try {

			NavigationManager.openPage("TravelerLoginFrame.fxml", click, "Traveler Login", true, true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the Traveler login button, check stack trace");
			e.printStackTrace();
		}
	}

	/**
	 * Handles the action when the worker login button is clicked. Opens a new scene
	 * for worker login.
	 * 
	 * @param click The action event triggering the method.
	 * @throws Exception If an error occurs while opening the worker login scene.
	 */
	public void WorkerLoginBtn(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Worker Login button
		try {

			NavigationManager.openPage("WorkerLoginFrame.fxml", click, "worker Login", true, true);

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the Worker login button, check stack trace");
			e.printStackTrace();
		}

	}

	/**
	 * Handles the action when the exit button is clicked. Exits the application.
	 * 
	 * @param event The action event triggering the method.
	 */
	public void ExitBtnAction(ActionEvent event) {
		// Get a handle to the stage
		Stage stage = (Stage) ExitBtn.getScene().getWindow();
		// Do what you need to do before closing
		stage.close();
	}

}