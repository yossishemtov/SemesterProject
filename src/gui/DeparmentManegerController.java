
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import client.SystemClient;
import common.*;
import common.worker.*;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DeparmentManegerController implements Initializable {
	@FXML
	private BorderPane borderPane;

	@FXML
	private AnchorPane topPane;

	@FXML
	private Label userLabel;

	@FXML
	private VBox vbox;

	@FXML
	private JFXButton profileButton;

	@FXML
	private JFXButton ParkParametersBth;

	@FXML
	private JFXButton createReportsButton;

	@FXML
	private JFXButton updateParametersButton;

	@FXML
	private JFXButton logoutBtn;





	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadProfileImmediately();

	}
	   private void loadProfileImmediately() {
	        try {
	            loadProfile(null); 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	

	@FXML
	void loadParkParameters(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane, "ParkParameters.fxml");

	}

	@FXML
	void loadProfile(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane, "Profile.fxml");

	}

	@FXML
	void loadReports(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane, "DepartmentManagerReports.fxml");

	}

	@FXML
	void loadViewRequests(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane, "ViewRequestsForChanges.fxml");

	}

	@FXML
	void logOut(MouseEvent event) {
		try {

			if (Usermanager.getCurrentWorker() != null) {
				ClientServerMessage<?> requestToLogout = new ClientServerMessage(Usermanager.getCurrentWorker(),
						Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
				ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
				Usermanager.setCurrentWorker(null);

			}

			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
					"Something went wrong when trying to return to main menu");
			somethingWentWrong.showAndWait();
		}
	}

}
