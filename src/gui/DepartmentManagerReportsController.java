package gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import common.worker.*;
import common.worker.Report.ReportType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * Controls the Department Manager Reports GUI. This controller handles the UI
 * logic for generating and displaying various reports for a department manager.
 */
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
	private TableColumn<Report, String> MonthCol;

	@FXML
	private TableColumn<Report, String> commentCol;

	@FXML
	private JFXButton Createbth;
	@FXML
	private JFXComboBox<String> monthCombobox;
	@FXML
	private JFXComboBox<String> ReportTypeCombobox;
	@FXML
	private JFXComboBox<String> parkNameComboBox;
	@FXML
	private JFXComboBox<String> parkNameComboBoxShowReport;

	@FXML
	private JFXButton ShowReportBth;

	private Map<String, Integer> parkNamesToNumbers;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded. It sets up the UI components and prepares them
	 * for user interaction.
	 *
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureTableColumns();
		parkNamesToNumbers = loadParkNamesToNumbersMap();
		parkNameComboBoxShowReport.setItems(FXCollections.observableArrayList(parkNamesToNumbers.keySet()));
		parkNameComboBox.setItems(FXCollections.observableArrayList(parkNamesToNumbers.keySet()));

		for (int month = 1; month <= 12; month++) {
			monthCombobox.getItems().add(String.valueOf(month));
			// Initialize report types
			ReportTypeCombobox.setItems(FXCollections.observableArrayList("Visit Report", "Cancellation Report"));

		}

	}

	/**
	 * Loads the mapping from park names to their numeric identifiers.
	 *
	 * @return A map where each key is a park name and its value is the
	 *         corresponding park number.
	 */
	private Map<String, Integer> loadParkNamesToNumbersMap() {
		ClientServerMessage<?> getParkInfoMsg = new ClientServerMessage(null, Operation.GET_PARKS_INFO);
		ClientUI.clientControllerInstance.sendMessageToServer(getParkInfoMsg);
		getParkInfoMsg = ClientUI.clientControllerInstance.getData();

		List<Park> parks = (List<Park>) getParkInfoMsg.getDataTransfered();
		Map<String, Integer> parkNamesToNumbers = new HashMap<>();
		for (Park park : parks) {
			parkNamesToNumbers.put(park.getName(), park.getParkNumber());
		}

		return parkNamesToNumbers;
	}

	/**
	 * Configures the table columns for the reports table view. This method sets up
	 * the cell value factories for each column in the reports table.
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
	 * Handles the action to show reports based on selected park ID. This method is
	 * invoked when the "Show Report" button is clicked.
	 *
	 * @param event The event that occurred.
	 */
	@FXML
	void ShowReportparkIdAction(ActionEvent event) {
		String selectedparkString = parkNameComboBoxShowReport.getSelectionModel().getSelectedItem();

		// Validate that both selections are not null (i.e., a selection has been made)
		if (!parkNamesToNumbers.containsKey(selectedparkString)) {
			Alert errorAlert = new Alerts(Alert.AlertType.ERROR, "Error", "", "You have choose park name.");
			errorAlert.showAndWait();
		} else {
			int selectedParkNumber = parkNamesToNumbers.get(selectedparkString);

			// Here, send request to server to get reports by parkId
			ObservableList<Report> reports = getReportsByParkId(selectedParkNumber);
			if (reports.isEmpty()) {
				Alerts infoalert = new Alerts(Alert.AlertType.INFORMATION, "Information", "",
						"Not have report to show.");
				infoalert.showAndWait();
				ReportsTableView.getItems().clear(); // Clear existing content
				ReportsTableView.refresh(); // Explicitly refresh the table view
			} else {
				ReportsTableView.getItems().clear(); // Clear existing content
				ReportsTableView.setItems(reports); // Set new items
				ReportsTableView.refresh(); // Explicitly refresh the table view
			}
		}
	}

	/**
	 * Handles the action when a report is double-clicked in the reports table. This
	 * method is intended to open a detailed view of the selected report when it is
	 * double-clicked.
	 *
	 * @param event The mouse event that occurred.
	 */
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
	 * Retrieves a list of reports for a given park ID from the server. This method
	 * sends a request to the server to fetch reports associated with a specific
	 * park. If the server returns a positive response, it converts the received
	 * list to an ObservableList and returns it. In case of failure or no data
	 * found, it alerts the user and returns an empty ObservableList.
	 *
	 * @param parkId The ID of the park for which reports are being requested.
	 * @return An ObservableList of {@link Report} objects for the specified park.
	 *         If no reports are found or in case of an error, returns an empty
	 *         list.
	 */
	private ObservableList<Report> getReportsByParkId(int parkId) {
		ClientServerMessage<Integer> messageForServer = new ClientServerMessage<>(parkId, Operation.GET_GENERAL_REPORT);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		if (ClientController.data.getFlag()) {
			ArrayList<Report> serverResponse = (ArrayList<Report>) ClientController.data.getDataTransfered();
			return FXCollections.observableArrayList(serverResponse);
		} else {
				return FXCollections.observableArrayList(); // Return an empty list
		}
	}

	/**
	 * Handles the action to create a report based on the user's selections in the
	 * GUI. This method is called when the user triggers the report creation
	 * process, typically by clicking a button. It validates the user's selections
	 * from the GUI, prepares the appropriate request based on the selected report
	 * type, and sends it to the server. Depending on the server's response, it
	 * either navigates to the relevant report page or informs the user of any
	 * issues.
	 *
	 * @param event The {@link ActionEvent} triggered by the user's interaction.
	 */
	@FXML
	void CreateReportAction(ActionEvent event) {
		Alerts infoalert; 
		String selectedMonthString = monthCombobox.getSelectionModel().getSelectedItem();
		String selectedReportType = ReportTypeCombobox.getSelectionModel().getSelectedItem();
		String selectedparkString = parkNameComboBox.getSelectionModel().getSelectedItem();

		// Validate that both selections are not null (i.e., a selection has been made)
		if (selectedMonthString == null || selectedReportType == null || selectedparkString == null
				|| !parkNamesToNumbers.containsKey(selectedparkString)) {
			Alert errorAlert = new Alerts(Alert.AlertType.ERROR, "Error", "",
					"You must select a month ,report and park name.");
			errorAlert.showAndWait();
		} else {
			int selectedMonth = Integer.parseInt(selectedMonthString);
			int selectedParkNumber = parkNamesToNumbers.get(selectedparkString);
			// React based on the selected report type
			switch (selectedReportType) {
			case "Visit Report":
				VisitReport visitReportToServer = new VisitReport(selectedParkNumber, selectedMonth);

				ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitReportToServer,
						Operation.GET_NEW_VISIT_REPORT);

				ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
				if (ClientController.data.getFlag()) {
					try {
						infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
						NavigationManager.openPage("VisitReport.fxml", event, "", false, false);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
							"Not have data for this month in database");
					infoalert.showAndWait();
				}

				break;
			case "Cancellation Report":
				CancellationReport usageReport = new CancellationReport(selectedParkNumber, selectedMonth);
				ClientServerMessage<?> messageForServerUsageReport = new ClientServerMessage<>(usageReport,
						Operation.GET_CANCELLATION_REPORT);
				ClientUI.clientControllerInstance.sendMessageToServer(messageForServerUsageReport);

				try {
					if (ClientController.data.getFlag()) {

						infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report retrieved from database");
						infoalert.showAndWait();
						NavigationManager.openPage("CancellationReportScreen.fxml", event, "", false, false);
					} else {
						infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Not have data for this month in database");
						infoalert.showAndWait();
					}

				} catch (IOException e) {
					System.out.println("error Usage Report for Month: " + selectedMonthString);

					e.printStackTrace();
				}

				System.out.println("Creating Usage Report for Month: " + selectedMonthString);
				break;

			}
		}
	}

}
