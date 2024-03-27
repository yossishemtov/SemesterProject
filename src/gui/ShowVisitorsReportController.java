package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import client.ClientController;
import common.Alerts;
import common.worker.VisitorsReport;

/**
 * Controller class for displaying a visitors report.
 * It handles the visualization of visitor statistics through a pie chart and displays any associated comments.
 */
public class ShowVisitorsReportController {

	private VisitorsReport visitorReport;

	@FXML
	private PieChart visitorsChart;
	@FXML
	private JFXTextArea CommentTextArea;
	@FXML
	private JFXButton CloseWindowBth;
	@FXML
	private Label titlelLabal;
	@FXML
	private Label commentLabal;

	

	/**
	 * Initializes the controller class. It is automatically called after the FXML file has been loaded.
	 * Initiates the process of loading the visitors report.
	 */
	@FXML
	private void initialize() {
		visitorReport = null;
		loadVisitorsReport();

	}

	/**
	 * Loads the visitors report data from the client controller, updates the view with this data.
	 */
	private void loadVisitorsReport() {
		// Attempt to retrieve the visitors report data transferred from another part of the application
		visitorReport = (VisitorsReport) ClientController.data.getDataTransfered();

		if (visitorReport != null) {
			// Set report title
			titlelLabal.setText("Visitors Report for month " + visitorReport.getMonth());
			// Update the chart with the report data
			updateChart(visitorReport);
			// Set report comment if available
			if (visitorReport.getComment() != null) {
				CommentTextArea.setText(visitorReport.getComment());
			}
		} else {
			// Display error alert if report is null
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to load visitor report.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Updates the pie chart with data from the visitors report.
	 * 
	 * @param report The visitors report containing data to be visualized.
	 */
	private void updateChart(VisitorsReport report) {
		// Create and populate the observable list for the pie chart
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Individual " + report.getTotalIndividualVisitors(), report.getTotalIndividualVisitors()),
				new PieChart.Data("Group " + report.getTotalGroupVisitors(), report.getTotalGroupVisitors()),
				new PieChart.Data("Family " + report.getTotalFamilyVisitors(), report.getTotalFamilyVisitors()));

		visitorsChart.getData().clear(); // Clear any existing data
		visitorsChart.setData(pieChartData); // Set new data for the chart

		// Adjust pie chart settings
		visitorsChart.setLabelsVisible(false); // Hide labels on the chart itself for a cleaner look
		visitorsChart.setLegendVisible(true); // Show legend to explain the pie slices
	}

	/**
	 * Handles the close window button action to terminate the view.
	 * 
	 * @param event The action event triggered by clicking the close button.
	 */
	@FXML
	void CloseWindowAction(ActionEvent event) {
		// Get the current stage using the event source
		Stage stage = (Stage) CloseWindowBth.getScene().getWindow();
		// Close the current stage
		stage.close();
	}

}
