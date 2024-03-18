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
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.VisitorsReport;

public class ShowVisitorsReportController {

	private VisitorsReport visitorReport;

	@FXML
	private PieChart visitorsChart;
	@FXML
	private JFXTextArea CommentTextArea;
	@FXML
	private JFXButton CloseWindowBth;
	@FXML
	private Label commentLabal;

	@FXML
	void CloseWindowAction(ActionEvent event) {
		// Get the current stage using the event source
		Stage stage = (Stage) CloseWindowBth.getScene().getWindow();
		// Close the current stage
		stage.close();
	}

	@FXML
	private void initialize() {
		visitorReport = null;
		loadVisitorsReport();

	}

	private void loadVisitorsReport() {

		visitorReport = (VisitorsReport) ClientController.data.getDataTransfered();
		System.out.println(visitorReport.toString());
		if (visitorReport != null) {
			updateChart(visitorReport);
			if (visitorReport.getComment() != null) {
				CommentTextArea.setText(visitorReport.getComment());

			}

		} else {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to load visitor report.");
			erorAlert.showAndWait();
		}
	}

	private void updateChart(VisitorsReport report) {
		// Fixed ObservableList creation to properly store PieChart.Data objects only
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Individual " + report.getTotalIndividualVisitors(),
						report.getTotalIndividualVisitors()),
				new PieChart.Data("Group " + report.getTotalGroupVisitors(), report.getTotalGroupVisitors()),
				new PieChart.Data("Family " + report.getTotalFamilyVisitors(), report.getTotalFamilyVisitors()));

		// Clearing previous data and adding new data
		visitorsChart.getData().clear();
		visitorsChart.setData(pieChartData); // Correctly set the data for the pie chart

		// Disable the labels on the chart slices to avoid overlap
		visitorsChart.setLabelsVisible(false);
		visitorsChart.setLegendVisible(true); // Show legend, adjust as needed
	}

}
