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
	private JFXButton messages;

	@FXML
	private JFXButton logoutBtn;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	TravelerChecker travelerChecker = new TravelerChecker();
		travelerChecker.viewPendingNotifications();
		travelerChecker.viewCanceledNotifications();
		travelerChecker.viewWaitingNotifications();
		Traveler currentTraveler = Usermanager.getCurrentTraveler();
		loadProfileImmediately();

	}

	private void loadProfileImmediately() {
		try {
			travelerProfile(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void travelerProfile(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "Profile.fxml");
		} catch (Exception e) {
			System.out.print("Something went wrong while trying view service traveler profile, check stack trace");
			e.printStackTrace();
		}

	}

	@FXML
	public void orderBtn(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "OrderVisit.fxml");
		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on order a visit button, check stack trace");
			e.printStackTrace();
		}

	}

	public void viewOrdersbutton(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "TravelerOrdersFrame.fxml");
		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on view orders button, check stack trace");
			e.printStackTrace();
		}

	}

	@FXML
	public void waitingListBtn(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "TravelerWaitingLists.fxml");
		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on view waiting list button, check stack trace");
			e.printStackTrace();
		}
	}

	@FXML
	public void messages(ActionEvent event) throws Exception {
		try {
			NavigationManager.openPageInCenter(pane, "TravelerMessages.fxml");
		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on view messages button, check stack trace");
			e.printStackTrace();
		}
	}

	@FXML
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

	public void orderVisit(ActionEvent click) throws Exception {

		NavigationManager.openPage("OrderAVisitFrame.fxml", click, "Order a visit", true,false);

	}

	public void viewWaitingList(ActionEvent click) throws Exception {

		NavigationManager.openPage("WaitingListFrame.fxml", click, "Waiting list", true,false);

	}

	public void exit(ActionEvent click) throws Exception {
		// Exit button, logs out the traveler without going to main menu
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

