package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.ClientController;
import common.Alerts;
import common.worker.CancellationReport;
import common.worker.VisitorsReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DepartmentManagerCancellationReportController implements Initializable {

	@FXML
	private Label titlelLabal;

	@FXML
	private JFXButton ClosePagebth;

	@FXML
	private BarChart<String, Number> CancellationBarChart;

	@FXML
	private BarChart<String, Number> UnfulfilledOrder;

	@FXML
	private Label AverageCanceledOrdersLabal;

	@FXML
	private Label AverageUnfulfilledOrderLabal;

	@FXML
	private Label MedianUnfulfilledOrdersLabal;
    @FXML
    private Label TotalUnfulfilledLabal;

    @FXML
    private Label TotalCanceledLabal;


	@FXML
	private Label MedianCanceledOdersLabal;

	@FXML
	void ClosePageAction(ActionEvent event) {
		// Get the current stage using the event source
		Stage stage = (Stage) ClosePagebth.getScene().getWindow();
		// Close the current stage
		stage.close();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCancellationReport();
	}

	 private void loadCancellationReport() {
	        CancellationReport report = (CancellationReport) ClientController.data.getDataTransfered();
	        if (report != null) {
	            titlelLabal.setText("Cancellation and Unfulfilled Orders Report for Month " + report.getMonthNumber());
	            updateBarChart(CancellationBarChart, report.getDailyCancellations(), "Cancellations");
	            updateBarChart(UnfulfilledOrder, report.getDailyUnfulfilledOrders(), "Unfulfilled Orders");
	            calculateAndDisplayStats(report);
	        } else {
	            Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to load cancellation report.");
	            errorAlert.showAndWait();
	        }
	    }

	 private void updateBarChart(BarChart<String, Number> barChart, Map<Integer, Integer> data, String seriesName) {
		    XYChart.Series<String, Number> series = new XYChart.Series<>();
		    series.setName(seriesName);

		    // Calculate the total count of all entries to find each entry's percentage contribution
		    int totalCount = data.values().stream().mapToInt(Integer::intValue).sum();

		    data.forEach((day, count) -> {
		        // Calculate the percentage each day contributes to the total
		        if (totalCount > 0) { // Prevent division by zero
		            double percentage = (double) count / totalCount ; // Adjust here to ensure sum equals to 100%
		            series.getData().add(new XYChart.Data<>(String.format("%d", day), percentage));
		        } else {
		            series.getData().add(new XYChart.Data<>(String.format("%d", day), 0));
		        }
		    });

		    barChart.getData().clear(); // Clear previous data
		    barChart.getData().add(series);
		}


	    private void calculateAndDisplayStats(CancellationReport report) {
	        AverageCanceledOrdersLabal.setText(String.format("%.2f", calculateAverage(report.getDailyCancellations())));
	        MedianCanceledOdersLabal.setText(String.format("%.2f", calculateMedian(report.getDailyCancellations())));
	        AverageUnfulfilledOrderLabal.setText(String.format("%.2f", calculateAverage(report.getDailyUnfulfilledOrders())));
	        MedianUnfulfilledOrdersLabal.setText(String.format("%.2f", calculateMedian(report.getDailyUnfulfilledOrders())));
	        // Calculate totals
	        int totalCancellations = report.getDailyCancellations().values().stream().mapToInt(Integer::intValue).sum();
	        int totalUnfulfilledOrders = report.getDailyUnfulfilledOrders().values().stream().mapToInt(Integer::intValue).sum();
	        
	        // Update labels
	        TotalCanceledLabal.setText(String.valueOf(totalCancellations));
	        TotalUnfulfilledLabal.setText(String.valueOf(totalUnfulfilledOrders));
	    }

	    private double calculateAverage(Map<Integer, Integer> data) {
	        int totalDays = data.size();
	        double total = data.values().stream().mapToDouble(Integer::doubleValue).sum();
	        return (total / totalDays);
	    }

	    private double calculateMedian(Map<Integer, Integer> data) {
	        List<Integer> sortedValues = new ArrayList<>(data.values());
	        sortedValues.sort(Integer::compareTo);
	        int middle = sortedValues.size() / 2;
	        if (sortedValues.size() % 2 == 0) {
	            return (sortedValues.get(middle - 1) + sortedValues.get(middle)) / 2.0;
	        } else {
	            return sortedValues.get(middle);
	        }
	    }
}
