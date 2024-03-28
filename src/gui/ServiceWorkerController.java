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
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.event.ActionEvent;


/**
 * Controller class for managing the service worker view.
 * This class handles the interaction logic for the service worker interface,
 * including actions such as viewing profile, registering new guides,
 * registering existing travelers as guides, and logging out.
 */
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
	 private JFXButton registerExistTraveler;

	 @FXML
	 private JFXButton logoutBtn;
	 
	 
	 /**
     * Initializes the controller.
     * 
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		 // get the current loggedin service worker
		 GeneralParkWorker serviceWorker = Usermanager.getCurrentWorker();
		 loadProfileImmediately();
	 }
	 
	 /**
     * Loads the profile immediately upon initialization.
     */
	 private void loadProfileImmediately() {
        try {
        	viewProfile(null); 
        } 
         catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 /**
     * Opens the profile view.
     * 
     * @param click The action event triggering the method.
     * @throws Exception If an error occurs while opening the profile view.
     */
	 public void viewProfile(ActionEvent click) throws Exception{ 
		 try {
			 NavigationManager.openPageInCenter(mainPane,"Profile.fxml");
		 } catch(Exception e) {
			 e.printStackTrace();
			 Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed to load service worker profile.");
			 errorAlert.showAndWait();
		 }
		 
	 }
	 
	 /**
     * Logs out the current user.
     * 
     * @param click The action event triggering the method.
     * @throws Exception If an error occurs while logging out.
     */
	 public void LogoutBtn(ActionEvent click) throws Exception{
		try {
			//Logging out a worker from the system
			if(Usermanager.getCurrentWorker() != null) {
    			ClientServerMessage<?> requestToLogout = new ClientServerMessage<>(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
    			ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				Usermanager.setCurrentWorker(null);

    		}
			// navigate back to home page
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true, true);
		} catch(Exception e) {
				e.printStackTrace();
				Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on the back button");
				errorAlert.showAndWait();
				
			}
		}
	 
	 /**
	  * Navigates to the page for registering a new guide.
	  * 
	  * @param click The action event triggering the method.
	  * @throws Exception If an error occurs while navigating to the register new guide page.
	  */
	 public void registerNewGuide(ActionEvent click) throws Exception {
		 // navigate to register form screen
		 try {
			 NavigationManager.openPageInCenter(mainPane, "RegisterNewGuide.fxml");
		 } catch(Exception e) {
			 e.printStackTrace();
			 Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on register new guide button");
			 errorAlert.showAndWait();
			 
			}
	 }
	 
	 /**
	  * Navigates to the page for registering an existing traveler as a guide.
	  * 
	  * @param click The action event triggering the method.
	  * @throws Exception If an error occurs while navigating to the register existing traveler guide page.
	  */
	 public void registerExistingTravelerGuide(ActionEvent click) throws Exception {
		 // navigate to register form screen
		 try {
			 NavigationManager.openPageInCenter(mainPane, "RegisterExistingTraveler.fxml");
		 } catch(Exception e) {
			 e.printStackTrace();
			 Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on register exist traveler as guide button");
			 errorAlert.showAndWait();
			 
		 	}
	 }
}

		 
	 

