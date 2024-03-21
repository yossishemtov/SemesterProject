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

public class CreateVisitorsReportController {


	private VisitorsReport visitorReport;

	@FXML
	private JFXButton savereportbth;

	@FXML
	private PieChart visitorsChart;
	@FXML
	private JFXTextArea CommentTextArea;
	@FXML
	private JFXButton CloseWindowBth;
	@FXML
	private Label commentLabal;
	@FXML
	private Label titlelLabal;

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

	@FXML
	void saveVisitorReportAction(ActionEvent event) {
		if (visitorReport != null) {
			if (CommentTextArea.getText() !=null)
			{
			visitorReport.setComment(CommentTextArea.getText());
			}
			ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitorReport,
					Operation.POST_VISITORS_REPORT);
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

			if (ClientController.data.getFlag()) {
				Alerts erorAlert = new Alerts(Alerts.AlertType.CONFIRMATION, "Confirmtion", "",
						"The report was successfully saved.");
				erorAlert.showAndWait();

			}
			// fetches the data
		} else {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to save visitor report.");
			erorAlert.showAndWait();
		}
	}

	private void loadVisitorsReport() {

		visitorReport = (VisitorsReport) ClientController.data.getDataTransfered();
		System.out.println(visitorReport.toString());
		if (visitorReport != null) {
			updateChart(visitorReport);
		} else {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to load visitor report.");
			erorAlert.showAndWait();
		}
	}

	private void updateChart(VisitorsReport report) {
		titlelLabal.setText("Visitors Report for month " + visitorReport.getMonth());

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
