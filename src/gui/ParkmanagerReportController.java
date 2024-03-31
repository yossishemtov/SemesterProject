package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.Report;
import common.worker.Report.ReportType;
import common.worker.UsageReport;
import common.worker.VisitorsReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller class for managing park reports within the GUI. This class handles
 * the display and generation of reports such as visitors and usage reports for
 * a park manager.
 */
public class ParkmanagerReportController implements Initializable {

	@FXML
	private JFXComboBox<String> monthCombobox;
	@FXML
	private JFXComboBox<String> ReportTypeCombobox;

	ObservableList<?> observable = FXCollections.observableArrayList();

	@FXML
	private TableView<Report> ReportsTableView;

	@FXML
	private TableColumn<Report, Integer> reportIDCol;

	@FXML
	private TableColumn<Report, String> reportTypeCol;

	@FXML
	private TableColumn<Report, Integer> parkIDCol;

	@FXML
	private TableColumn<Report, String> MonthCol;

	@FXML
	private TableColumn<Report, String> commentCol;
	@FXML
	private JFXButton Createbth;
	private static ParkmanagerReportController instance;
	private Park park;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded. It initializes the comboboxes for months and
	 * report types, configures the table columns, and loads the park details.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize months 1-12
		for (int month = 1; month <= 12; month++) {
			monthCombobox.getItems().add(String.valueOf(month));
			// Initialize report types
			ReportTypeCombobox.setItems(FXCollections.observableArrayList("Visitors Report", "Usage Report"));

		}
		instance = this;
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(
				Usermanager.getCurrentWorker().getWorksAtPark(), Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		if (ClientController.data.getFlag()) {
			park = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
		} else {
			Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "", "Error to load park data .");
			warningalert.showAndWait();
		}

		configureTableColumns();
		ShowReportparkIdAction();

	}

	/**
	 * Static method to refresh the reports table. This can be called from other
	 * classes to update the table's contents.
	 */
	public static void refreshReportsTable() {
		if (instance != null) {
			instance.ShowReportparkIdAction();
		}
	}

	/**
	 * Configures the table columns with the correct property values to be
	 * displayed.
	 */
	private void configureTableColumns() {
		reportIDCol.setCellValueFactory(new PropertyValueFactory<>("reportID"));
		reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
		parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkID"));

		// Adjust this column to use the month field
		MonthCol.setCellValueFactory(cellData -> {
			int month = cellData.getValue().getMonth();
			return new javafx.beans.property.SimpleStringProperty(String.valueOf(month));
		});

		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
	}

	/**
	 * Loads and displays the reports for the current park manager's park ID.
	 */
	private void ShowReportparkIdAction() {
		Integer parkId = Usermanager.getCurrentWorker().getWorksAtPark();

		// Here, send request to server to get reports by parkId
		ObservableList<Report> reports = getReportsByParkId(parkId);
		if (!reports.isEmpty()) {
			ReportsTableView.getItems().clear(); // Clear existing content
			ReportsTableView.setItems(reports); // Set new items
			ReportsTableView.refresh(); // Explicitly refresh the table view
		}
	}

	/**
	 * Handles the action when a report in the table is double-clicked. It opens the
	 * selected report for viewing.
	 *
	 * @param event The mouse event that triggered this action.
	 */
	@FXML
	void ShowReportTableClickAction(MouseEvent event) {
		if (event.getClickCount() == 2) { // Double click
			Report selectedReport = ReportsTableView.getSelectionModel().getSelectedItem();
			if (selectedReport != null) {
				String operation = null;
				String fxmlFile = "";

				// Determine the type of report and set the operation and FXML file accordingly
				if (selectedReport.getReportType() == ReportType.USAGE.toString()) {
					operation = Operation.GET_EXISTS_USAGE_REPORT;
					fxmlFile = "ShowUsageReport.fxml";
				} else if (selectedReport.getReportType() == ReportType.VISITOR.toString()) {
					operation = Operation.GET_EXISTS_VISITORS_REPORT;
					fxmlFile = "ShowVisitorsReport.fxml";
				}

				if (operation != null && !fxmlFile.isEmpty()) {
					ClientServerMessage<Report> messageForServer = new ClientServerMessage<>(selectedReport, operation);
					ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

					if (ClientController.data.getFlag()) {
						System.out.println(ClientController.data.getDataTransfered());
						Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
						try {
							NavigationManager.openPage(fxmlFile, event, "", false, false);
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

	/**
	 * Retrieves a list of reports based on the park ID.
	 *
	 * @param parkId The park ID for which to retrieve the reports.
	 * @return An observable list of reports for the given park ID.
	 */
	private ObservableList<Report> getReportsByParkId(int parkId) {
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkId, Operation.GET_GENERAL_REPORT);
		System.out.println("1");
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		if (ClientController.data.getFlag()) {
			ArrayList<Report> serverResponse = (ArrayList<Report>) ClientController.data.getDataTransfered();
			return FXCollections.observableArrayList(serverResponse);
		} else {
			// Handle the case where the server response indicates failure or no data
			Alerts infoAlert = new Alerts(Alerts.AlertType.INFORMATION, "Information", "",
					"There are no existing reports to display.");
			infoAlert.showAndWait();
			return FXCollections.observableArrayList(); // Return an empty list
		}
	}

	/**
	 * Handles the action to create a new report based on the selected month and
	 * report type from the GUI.
	 *
	 * @param event The action event that triggered this action.
	 */
	@FXML
	void CreateReportAction(ActionEvent event) {
		Alerts infoalert;
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
			case "Visitors Report":
				VisitorsReport visitorReportToServer = new VisitorsReport(0, null,
						Usermanager.getCurrentWorker().getWorksAtPark(), // Gets the park ID from the current worker
						LocalDate.now(), // Sets the report's date to today
						selectedMonth, // The month selected
						null, null, null, null, null);
				ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitorReportToServer,
						Operation.GET_NEW_VISITORS_REPORT);
				ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

				if (ClientController.data.getFlag()) {
					try {
						infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
						NavigationManager.openPage("CreateVisitorsReport.fxml", event, "", false, false);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
							"Not have data for this month in database");
					infoalert.showAndWait();
				}

				System.out.println("Creating Visit Report for Month: " + selectedMonthString);
				break;
			case "Usage Report":
				UsageReport usageReport = new UsageReport(0, Report.ReportType.USAGE,
						Usermanager.getCurrentWorker().getWorksAtPark(), LocalDate.now(), selectedMonth, "", null,
						park.getCapacity());
				System.out.println(usageReport.toString());
				ClientServerMessage<?> messageForServerUsageReport = new ClientServerMessage<>(usageReport,
						Operation.GET_NEW_USAGE_REPORT);
				ClientUI.clientControllerInstance.sendMessageToServer(messageForServerUsageReport);

				try {
					if (ClientController.data.getFlag()) {
						infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
					} else {
						Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "",
								"Error to load park data .");
						warningalert.showAndWait();
					}

					NavigationManager.openPage("ParkManagerCreateUsageReport.fxml", event, "", false, false);
				} catch (IOException e) {
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
