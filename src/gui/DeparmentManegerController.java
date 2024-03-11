
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.SystemClient;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.DepartmentManager;
import common.worker.GeneralParkWorker;
import common.worker.ParkManager;
import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Department manager controller
 *
 */
public class DeparmentManegerController implements Initializable {

	@FXML
	private Label idtxt;

	@FXML
	private ComboBox<String> ReporeCombo;

	@FXML
	private ImageView ContactUsBtn;

	@FXML
	private Label LogoutBtn;

	@FXML
	private Button CreateReportBtn;

	@FXML
	private TextArea Reminder;

	@FXML
	private Button NumberVisitors;

	@FXML
	private Label DepDiscount;

	@FXML
	private Button DiscountCombo;

	@FXML
	private Label ConfirmationOParameters;

	@FXML
	private ComboBox<String> ListOfReports;
	private GeneralParkWorker departmentManager;

	/**
	 * For choosing a type of report
	 * 
	 * @param event on action
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void ListOfReportfunc(ActionEvent event) throws IOException {

		if (ListOfReports.getValue().equals("Total number of visitors report")) {

			NavigationManager.openPage("VisitorsReport.fxml", event, "Choosing page", true);

		}

		else {
			if (ListOfReports.getValue().equals("Usage report")) {

				NavigationManager.openPage("BforeDepartment.fxml", event, "Choosing page", true);

			} else {

				NavigationManager.openPage("BforeDepartment.fxml", event, "Choosing page", true);

			}
		}

	}

	/**
	 * send message from client to server about the current number of the visitors
	 * in the park
	 * 
	 * @param event on action
	 */
	@FXML
	void NumberVisitorsFunc(ActionEvent event) {
		

		ClientServerMessage<?> messege = new ClientServerMessage(departmentManager,
				Operation.GET_AMOUNT_OF_VISITORS);
		ClientUI.clientControllerInstance.sendMessageToServer(messege); //send to server
		Integer NumberOfVisitorInt = ((Park)ClientController.data.getDataTransfered()).getCurrentVisitors(); //getting the info
		String NumberOfVisitor = "The Number of visitors present in the park is: " + NumberOfVisitorInt;
		Alerts erorAlert = new Alerts(Alerts.AlertType.INFORMATION, "Information for department maneger",
				"Number of visitor", NumberOfVisitor);
		erorAlert.showAndWait();

	}

	/**
	 * creating a report with a suitable name of the type of the report, after
	 * checking the input existing.
	 * 
	 * @param event on action
	 * @throws IOException CLIENT EXCEPTION
	 */
	@FXML
	void CreateReport(ActionEvent event) throws IOException {

		if (ReporeCombo.getValue() == null) {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Invalid Input", "Warning",
					"The value entered is invalid. Please try again.");
			erorAlert.showAndWait();

		} else {

			switch (ReporeCombo.getValue()) {
			case "Visit report - by length of stay":
				NavigationManager.openPage("VisitingTimeStayTime.fxml", event, "Choosing report", false);

				break;

			case "Visit report - by time of entry":

				NavigationManager.openPage("VisitingReportEnterTime.fxml", event, "Choosing report", false);

				break;

			case "Cancellation report":

				NavigationManager.openPage("BeforeReport.fxml", event, "Choosing report", false);

				break;
			}
		}
	}

	/**
	 * This function logs out
	 * 
	 * @param event on mouse click
	 * @throws Exception CLIENT EXCEPTION
	 */
	@FXML
	void LogoutFunc(MouseEvent event) {
		try {
			ClientUI.clientControllerInstance.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize the the parameters and the reminder text
	 * 
	 * @param location  location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Cast the current worker to DepartmentManager if possible
		departmentManager = Usermanager.getCurrentWorker();

		idtxt.setText(departmentManager.getUserName());
		// Initialize other UI components based on departmentManager details

		// Populate your ComboBoxes or any other UI elements here
		ReporeCombo.getItems().addAll("Cancellation report", "Visit report - by length of stay",
				"Visit report - by time of entry");
		ListOfReports.getItems().addAll("Total number of visitors report", "Usage report",
				"Income report for a specific month");
	}

}
