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

    @FXML
    void initialize() {
        // Add event handlers to images
    	MountRainier.setOnMouseClicked(event -> playYouTubeVideo("https://www.youtube.com/embed/kQY3fgSkVnI?si=qlrX2_P-K84fgej9"));
    	YellowStone.setOnMouseClicked(event -> playYouTubeVideo("https://www.youtube.com/embed/bme0rs75Z3E?si=hlM9hYCsrLXw5aE0"));
    	Yosemite.setOnMouseClicked(event -> playYouTubeVideo("https://www.youtube.com/embed/9fJEFi3ccwI?si=TaKcZo4Rov7VNs96"));

    }

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
        videoStage.show();
    }
    
    
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