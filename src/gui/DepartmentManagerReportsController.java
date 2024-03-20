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
import common.Usermanager;
import common.worker.ChangeRequest;
import common.worker.*;
import common.worker.UsageReport;
import common.worker.Report.ReportType;

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
	private JFXButton Createbth;
	@FXML
	private JFXComboBox<String> monthCombobox; // Changed to String for display purposes
	@FXML
	private JFXComboBox<String> ReportTypeCombobox; // Changed to String for display purposes

	@FXML
	private JFXButton ShowReportBth;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (int month = 1; month <= 12; month++) {
			monthCombobox.getItems().add(String.valueOf(month));
			// Initialize report types
			ReportTypeCombobox.setItems(FXCollections.observableArrayList("Visit Report", "Cancellation Report"));

		}
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
				Alerts infoalert = new Alerts(Alert.AlertType.INFORMATION, "Information", "",
						"Not have report to show.");
				infoalert.showAndWait();
			} else {
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
				String operation = "";
				String fxmlFile = "";

				// Determine the type of report and set the operation and FXML file accordingly
				if (selectedReport.getReportType().equals(ReportType.USAGE.toString())) {
					operation = Operation.GET_EXISTS_USAGE_REPORT;
					fxmlFile = "ShowUsageReport.fxml";
				} else if (selectedReport.getReportType().equals(ReportType.VISITOR.toString())) {
					operation = Operation.GET_EXISTS_VISITORS_REPORT;
					fxmlFile = "ShowVisitorsReport.fxml";
				}

				if (!operation.isEmpty() && !fxmlFile.isEmpty()) {
					ClientServerMessage<Report> messageForServer = new ClientServerMessage<>(selectedReport, operation);
					ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

					if (ClientController.data.getFlag()) {
						System.out.println(ClientController.data.getDataTransfered());
						Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
						try {
							NavigationManager.openPage(fxmlFile, event, "", false);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
								"A report return empty request was not successfully executed on database");
						somethingWentWrong.showAndWait();
					}
				} else {
					Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "",
							"Selection is empty or report type is unknown.");
					warningalert.showAndWait();
				}
			} else {
				Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "", "Selection is empty.");
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
	void CreateReportAction(ActionEvent event) {
		System.out.println("in CreateReportAction ");
		String selectedMonthString = monthCombobox.getSelectionModel().getSelectedItem();
		String selectedReportType = ReportTypeCombobox.getSelectionModel().getSelectedItem();

		// Validate that both selections are not null (i.e., a selection has been made)
		if (selectedMonthString == null || selectedReportType == null) {
			Alert errorAlert = new Alerts(Alert.AlertType.ERROR, "Error", "",
					"You must select a month and report type.");
			errorAlert.showAndWait();
		} else {
			int selectedMonth = Integer.parseInt(selectedMonthString);

			// React based on the selected report type
			switch (selectedReportType) {
			case "Visit Report":
				VisitReport visitReportToServer = new VisitReport(Usermanager.getCurrentWorker().getWorksAtPark(),
						selectedMonth);
				System.out.println("in CVisit report ");

				ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitReportToServer,
						Operation.GET_NEW_VISIT_REPORT);
				System.out.println("out CVisit report ");

				ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
				if (ClientController.data.getFlag()) {
					try {
						   Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
		                            "Report retrieved from database");
		                    infoalert.showAndWait();
						NavigationManager.openPage("VisitReport.fxml", event, "", false);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "",
							"Error to load Visit report .");
					warningalert.showAndWait();
				}

				break;
			case "Cancellation Report":
				UsageReport usageReport = new UsageReport(0, Report.ReportType.USAGE,
						Usermanager.getCurrentWorker().getWorksAtPark(), LocalDate.now(), selectedMonth, "", null, 0);
				System.out.println(usageReport.toString());
				ClientServerMessage<?> messageForServerUsageReport = new ClientServerMessage<>(usageReport,
						Operation.GET_NEW_USAGE_REPORT);
				ClientUI.clientControllerInstance.sendMessageToServer(messageForServerUsageReport);

				try {
					if (ClientController.data.getFlag()) {
						   Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
		                            "Report retrieved from database");
		                    infoalert.showAndWait();
					} else {
						Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "",
								"Error to load park data .");
						warningalert.showAndWait();
					}

					NavigationManager.openPage("ParkManagerCreateUsageReport.fxml", event, "", false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("error Usage Report for Month: " + selectedMonthString);

					e.printStackTrace();
				}

				System.out.println("Creating Usage Report for Month: " + selectedMonthString);
				break;
			default:
				Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Unknown report type selected.");
				infoAlert.setHeaderText("Information");
				infoAlert.showAndWait();
				break;
			}
		}
	}

}
