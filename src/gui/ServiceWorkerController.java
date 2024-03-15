package gui;


import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import client.ClientUI;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.event.ActionEvent;

public class ServiceWorkerController implements Initializable {

	 @FXML
	 private BorderPane mainPane;

	 @FXML
	 private AnchorPane topPane;

	 @FXML
	 private Label UserLabel;

	 @FXML
	 private StackPane leftPane;

	 @FXML
	 private VBox vbox;

	 @FXML
	 private JFXButton viewProfile;

	 @FXML
	 private JFXButton registerNewGuide;

	 @FXML
	 private JFXButton logoutBtn;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		 // get the current loggedin service worker
		 GeneralParkWorker serviceWorker = Usermanager.getCurrentWorker();
	 }
	 

	 public void viewProfile(ActionEvent click) throws Exception{ 
		 try {
			 NavigationManager.openPageInCenter(mainPane,"Profile.fxml");
		 } catch(Exception e) {
			 System.out.print("Something went wrong while trying view service worker profile, check stack trace");
			 e.printStackTrace();
		 }
		 
	 }
	 
	 public void LogoutBtn(ActionEvent click) throws Exception{
		try {
			//Logging out a worker from the system
			if(Usermanager.getCurrentWorker() != null) {
    			ClientServerMessage<?> requestToLogout = new ClientServerMessage<>(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
    			ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
    		}
			// navigate back to home page
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true);
		} catch(Exception e) {
				System.out.print("Something went wrong while clicking on the back button, check stack trace");
				e.printStackTrace();
				
			}
		}
	 
	 public void registerNewGuide(ActionEvent click) throws Exception {
		 // navigate to register form screen
		 try {
			 NavigationManager.openPageInCenter(mainPane, "RegisterNewGuide.fxml");
		 } catch(Exception e) {
			 System.out.print("Something went wrong while clicking on register new guide button, check stack trace");
			 e.printStackTrace();
			}
	 }
	 
}

		 
	 

