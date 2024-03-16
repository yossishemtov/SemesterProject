package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Report;
import common.VisitorsReport;
import common.worker.ChangeRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepartmentManagerReportsController implements Initializable {

	ObservableList<?> observable = FXCollections.observableArrayList();

	@FXML
	private Label headerLabel;

	@FXML
	private JFXButton visitReportBtn;

	@FXML
	private JFXButton CancelsReportBtn;


	@FXML
	private TableView<Report> ReportsTableView;

	@FXML
	private TableColumn<Report, Integer> reportIDCol;

	@FXML
	private TableColumn<Report, String> reportTypeCol;

	@FXML
	private TableColumn<Report, Integer> parkIDCol;

	@FXML
	private TableColumn<Report, String> DateCol;

	@FXML
	private TableColumn<Report, String> commentCol;
	@FXML
	private JFXTextField ParkIdField;

	@FXML
	private JFXButton ShowReportBth;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureTableColumns();
	}

	private void configureTableColumns() {
		reportIDCol.setCellValueFactory(new PropertyValueFactory<>("reportID"));
		reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
		parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkID"));
		DateCol.setCellValueFactory(cellData -> {
			LocalDate date = cellData.getValue().getDate();
			return new javafx.beans.property.SimpleStringProperty(
					date != null ? date.format(DateTimeFormatter.ofPattern("MMMM yyyy")) : "");
		});
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
	}

	@FXML
	void ShowReportparkIdAction(ActionEvent event) {
		String parkId = ParkIdField.getText().trim();
		if (parkId.isEmpty()) {
			Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "", "Park ID field cannot be empty.");
			warningalert.showAndWait();
		} else {
			// Here, send request to server to get reports by parkId
			ObservableList<Report> reports = getReportsByParkId(Integer.parseInt(parkId));
			if (reports.isEmpty()) {
				Alerts infoalert = new Alerts(Alert.AlertType.INFORMATION, "Information", "", "Not have report to show.");
				infoalert.showAndWait();
			}
			else {
				 ReportsTableView.getItems().clear(); // Clear existing content
		            ReportsTableView.setItems(reports); // Set new items
		            ReportsTableView.refresh(); // Explicitly refresh the table view
			}
		}
	}

	@FXML
	void ShowReportTableClickAction(MouseEvent event) {
		if (event.getClickCount() == 2) { // Double click
			Report selectedReport = ReportsTableView.getSelectionModel().getSelectedItem();
			if (selectedReport != null) {
				ClientServerMessage<Report> messageForServer = new ClientServerMessage<>(selectedReport, Operation.GET_EXISTS_VISITORS_REPORT);
	             ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
	      
	             
	             if (ClientController.data.getFlag()) {
	            	 System.out.println(ClientController.data.getDataTransfered().toString());
	            		Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report get from database");
	            		infoalert.showAndWait();
	            		try {
							NavigationManager.openPage("VisitorsReport.fxml", event, "", false);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		
	             }
	             else {
	            	 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
								"A report return empty request was not successfully executed on database");
						somethingWentWrong.showAndWait();
	             }
			
			}
			else {
				Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "", "Selction is empty.");
				warningalert.showAndWait();
			}
		}
	}

	private ObservableList<Report> getReportsByParkId(int parkId) {
		ClientServerMessage<Integer> messageForServer = new ClientServerMessage<>(parkId, Operation.GET_GENERAL_REPORT);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		if (ClientController.data.getFlag()) {
			ArrayList<Report> serverResponse = (ArrayList<Report>) ClientController.data.getDataTransfered();
			return FXCollections.observableArrayList(serverResponse);
		} else {
			// Handle the case where the server response indicates failure or no data
			Alerts infoAlert = new Alerts(Alerts.AlertType.INFORMATION, "Information", "",
					"No reports were found or there was an error processing your request.");
			infoAlert.showAndWait();
			return FXCollections.observableArrayList(); // Return an empty list
		}
	}



	@FXML
	void cancelReportBtn(MouseEvent event) {

	}

	@FXML
	void visitReportBtn(MouseEvent event) {

		// NavigationManager.openPage("DeparmentManegerVisitsReport.fxml",event , "Home
		// Page", true);
		try {
			System.out.println("Loading FXML from path: " + NavigationManager.class.getResource("/gui/" + "test.fxml"));

			NavigationManager.openPage("test.fxml", event, "", true);
		} catch (Exception ex) {
			System.out.println(ex.toString()); // Log the exception to understand what went wrong
		}

	}

}
