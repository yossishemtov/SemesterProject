package gui;

import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.InputValidation;
import common.Traveler;
import common.TravelerChecker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import client.NavigationManager;
import common.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Controls the behavior of the traveler frame, allowing the traveler to navigate through different functionalities.
 * This includes viewing profile, ordering visits, viewing orders, managing waiting lists, checking messages, and logging out.
 */
public class TravelerFrameController implements Initializable {

	@FXML
	private BorderPane pane;

	@FXML
	private AnchorPane topBorder;

	@FXML
	private Label userLabel;

	@FXML

	private JFXButton viewOrdersbutton;

	@FXML

	private StackPane leftBorder;

	@FXML
	private JFXButton travelerProfile;

	@FXML
	private JFXButton orderBtn;

	@FXML
	private JFXButton waitingListBtn;

	@FXML
	private JFXButton logoutBtn;

	
	 /**
     * Initializes the traveler frame controller, setting up the initial state of the traveler frame 
     * and loading the traveler's profile immediately upon initialization.
     * This method also checks for any pending notifications or messages for the traveler.
     *
     * @param location  The URL location of the FXML file.
     * @param resources The ResourceBundle for the FXML file.
     */
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	TravelerChecker travelerChecker = new TravelerChecker();
		travelerChecker.viewPendingNotifications();
		travelerChecker.viewCanceledNotifications();
		travelerChecker.viewWaitingNotifications();
		Traveler currentTraveler = Usermanager.getCurrentTraveler();
		loadProfileImmediately();

	}

	/**
     * Loads the traveler's profile immediately upon initialization of the traveler frame.
     * This method ensures that the traveler's profile is displayed as soon as the frame is loaded.
     * It invokes the method to navigate to the traveler's profile page.
     */
	private void loadProfileImmediately() {
		try {
			travelerProfile(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 /**
     * Navigates to the traveler's profile page when the corresponding button is clicked.
     * This method updates the main content pane of the traveler frame with the traveler's profile information.
     *
     * @param event The action event triggered by clicking the traveler profile button.
     * @throws Exception If an error occurs while loading the profile page.
     */
	public void travelerProfile(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "Profile.fxml");
		} catch (Exception e) {
			e.printStackTrace();
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while trying view service traveler profile");
			errorAlert.showAndWait();
		}
	}

	/**
     * Navigates to the order visit page when the corresponding button is clicked.
     * This method updates the main content pane of the traveler frame with the order visit form.
     *
     * @param event The action event triggered by clicking the order visit button.
     * @throws Exception If an error occurs while loading the order visit page.
     */
	public void orderBtn(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "OrderVisit.fxml");
		} catch (Exception e) {
			e.printStackTrace();
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on order a visit button");
			errorAlert.showAndWait();
		}
	}
	
	/**
     * Navigates to the traveler's orders page when the corresponding button is clicked.
     * This method updates the main content pane of the traveler frame with the list of traveler's orders.
     *
     * @param event The action event triggered by clicking the view orders button.
     * @throws Exception If an error occurs while loading the traveler's orders page.
     */
	public void viewOrdersbutton(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "TravelerOrdersFrame.fxml");
		} catch (Exception e) {
			e.printStackTrace();
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on on view orders button");
			errorAlert.showAndWait();
		}

	}

	 /**
     * Navigates to the traveler's waiting lists page when the corresponding button is clicked.
     * This method updates the main content pane of the traveler frame with the list of traveler's waiting lists.
     *
     * @param event The action event triggered by clicking the waiting list button.
     * @throws Exception If an error occurs while loading the traveler's waiting lists page.
     */
	public void waitingListBtn(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "TravelerWaitingLists.fxml");
		} catch (Exception e) {
			e.printStackTrace();
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "", "Failed while clicking on view waiting list button");
			errorAlert.showAndWait();
		}
	}

	/**
     * Logs out the traveler from the application and navigates back to the main menu.
     * This method sends a logout request to the server, clears the current traveler's session, and redirects to the homepage.
     *
     * @param event The action event triggered by clicking the logout button.
     * @throws Exception If an error occurs while logging out the traveler.
     */
	public void logoutBtn(ActionEvent event) throws Exception {
		try {

			if (Usermanager.getCurrentTraveler() != null) {
				ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentTraveler(),
						Operation.PATCH_TRAVELER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				Usermanager.setCurrentTraveler(null);

			}

			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
					"Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
	}

	/**
	 * Handles the action of traveler logout.
	 * This method logs out the traveler with navigating to the home page.
	 * 
	 * @param click The ActionEvent triggered by clicking the exit button.
	 * @throws Exception If an error occurs during the exit process.
	 */
	public void exit(ActionEvent click) throws Exception {
		try {
			
			if (Usermanager.getCurrentTraveler() != null) {
				ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentTraveler(),
						Operation.PATCH_TRAVELER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				Usermanager.setCurrentTraveler(null);
			}
			NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true, true);
		} catch (Exception e) {
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
					"Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
	}
}

