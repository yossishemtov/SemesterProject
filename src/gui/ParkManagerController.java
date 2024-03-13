package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;


import client.ClientUI;
import client.NavigationManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * This Class is the GUI controller of DepartmentManager.fxml
 * It handles all the JavaFx nodes events.
 * 
 * This is the main screen of the park manager
 *
 */
public class ParkManagerController  {

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
	private JFXButton currentVisitorsButton;

	@FXML
	private JFXButton enterVisitorIDButton;

	@FXML
	private JFXButton createReportsButton;

	@FXML
	private JFXButton updateParametersButton;






//
//	@FXML
//	private void loadCreateReports() {
//		Pane view = loader.loadPaneToBorderPaneWithController("/gui/CreateReports.fxml", "createReport");
//		borderPane.setCenter(view);
//	}
	@FXML
	void loadUpdateParameters(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane,"UpdateParameters.fxml");

	

	}

	@FXML
	void loadProfile(MouseEvent event) throws IOException {
		NavigationManager.openPageInCenter(borderPane,"Profile.fxml");

	}


	@FXML
	void logOut(MouseEvent event) {
		try {
			NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
