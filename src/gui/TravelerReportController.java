package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXButton;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.VisitorsReport;

public class TravelerReportController {

	private VisitorsReport visitorReport;

	@FXML
	private JFXButton savereportbth;

	@FXML
	private PieChart visitorsChart;
	   @FXML
	    private JFXButton CloseWindowBth;

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
		adjustSaveButtonVisibility();
		loadVisitorsReport();
		
	}

	private void adjustSaveButtonVisibility() {
	    // Assuming Usermanager.getCurrentWorker().getRole() returns the role of the user
	    String role = Usermanager.getCurrentWorker().getRole();
	    if ("Department Manager".equals(role)) {
	        // Hide the save button for Department Manager
	        savereportbth.setVisible(false);
	    } else {
	        // Show the save button for other roles
	        savereportbth.setVisible(true);
	        CloseWindowBth.setVisible(false);

	    }
	}

	@FXML
	void saveVisitorReportAction(ActionEvent event) {
		if (visitorReport != null) {
			ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitorReport,
					Operation.POST_VISITORS_REPORT);
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

			visitorReport = (VisitorsReport) ClientController.data.getDataTransfered(); // Assuming this correctly
																						// fetches the data
		} else {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to save visitor report.");
			erorAlert.showAndWait();
		}
	}

	private void loadVisitorsReport() {
		String role = Usermanager.getCurrentWorker().getRole();
		switch (role)

		{
		case "Department Manager":
			System.out.println("in Department Manager");
			break;
		case "Park Manager":
			ClientServerMessage<?> messageForServer = new ClientServerMessage<>(Usermanager.getCurrentWorker(),
					Operation.GET_NEW_VISITORS_REPORT);
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
			System.out.println("in Park Manager");
			break;
		}

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
