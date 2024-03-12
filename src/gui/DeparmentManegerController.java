
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	
	GeneralParkWorker departmentManager;

	@FXML
	void loadParkParameters(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane,"ParkParameters.fxml");

	

	}

	@FXML
	void loadProfile(MouseEvent event) throws IOException {
		NavigationManager.openPage("Profile.fxml", event, "Profile screen", true);

	}

	@FXML
	void loadReports(MouseEvent event) {

	}

	@FXML
	void loadViewRequests(MouseEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cast the current worker to DepartmentManager if possible
		departmentManager = Usermanager.getCurrentWorker();


	}

	@FXML
	void logOut(MouseEvent event) {
		try {
			ClientUI.clientControllerInstance.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@FXML
	void NumberVisitorsFunc(ActionEvent event) {

		ClientServerMessage<?> messege = new ClientServerMessage(departmentManager, Operation.GET_AMOUNT_OF_VISITORS);
		ClientUI.clientControllerInstance.sendMessageToServer(messege);
		Integer NumberOfVisitorInt = ((Park) ClientController.data.getDataTransfered()).getCurrentVisitors();
		String NumberOfVisitor = "The Number of visitors present in the park is: " + NumberOfVisitorInt;
		Alerts erorAlert = new Alerts(Alerts.AlertType.INFORMATION, "Information for department maneger",
				"Number of visitor", NumberOfVisitor);
		erorAlert.showAndWait();

	}

}
