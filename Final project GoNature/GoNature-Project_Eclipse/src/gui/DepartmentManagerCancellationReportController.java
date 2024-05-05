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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller class for the Department Manager's Cancellation and Unfulfilled
 * Orders Report. This class handles the UI and data visualization for the
 * cancellation and unfulfilled orders report, allowing the department manager
 * to view statistical data on cancellations and unfulfilled orders, including
 * totals, averages, and medians.
 */
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

	/**
	 * Handles the action of closing the report page.
	 * 
	 * @param event The event that triggered this action.
	 */
	@FXML
	void ClosePageAction(ActionEvent event) {
		Stage stage = (Stage) ClosePagebth.getScene().getWindow();
		stage.close();
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded. It initiates the process of loading and
	 * displaying the cancellation report data.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCancellationReport();
	}

	/**
	 * Loads the cancellation and unfulfilled orders report data and updates the UI
	 * components to display this data, including bar charts and statistical
	 * measures.
	 */

	private void loadCancellationReport() {
		CancellationReport report = (CancellationReport) ClientController.data.getDataTransfered();
		if (report != null) {
			titlelLabal.setText("Cancellation Report for Month " + report.getMonthNumber());
			updateBarChart(CancellationBarChart, report.getDailyCancellations(), "Cancellations");
			updateBarChart(UnfulfilledOrder, report.getDailyUnfulfilledOrders(), "Unfulfilled Orders");
			calculateAndDisplayStats(report);
		} else {
			Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error",
					"Not able to load cancellation report.");
			errorAlert.showAndWait();
		}
	}

	/**
	 * Updates a given bar chart with the provided data. Each entry in the data map
	 * represents a day and its corresponding count (e.g., cancellations or
	 * unfulfilled orders), which is then displayed in the bar chart.
	 * 
	 * @param barChart   The bar chart to update.
	 * @param data       The data to display in the bar chart, keyed by day with
	 *                   counts as values.
	 * @param seriesName The name of the series to display on the bar chart.
	 */
	private void updateBarChart(BarChart<String, Number> barChart, Map<Integer, Integer> data, String seriesName) {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(seriesName);

		// Calculate the total count of all entries to find each entry's percentage
		// contribution
		int totalCount = data.values().stream().mapToInt(Integer::intValue).sum();

		data.forEach((day, count) -> {
			// Calculate the percentage each day contributes to the total
			if (totalCount > 0) { // Prevent division by zero
				double percentage = (double) count / totalCount; // Adjust here to ensure sum equals to 100%
				series.getData().add(new XYChart.Data<>(String.format("%d", day), percentage));
			} else {
				series.getData().add(new XYChart.Data<>(String.format("%d", day), 0));
			}
		});

		barChart.getData().clear(); // Clear previous data
		barChart.getData().add(series);
	}

	/**
	 * Calculates and displays statistical measures (average and median) for both
	 * canceled orders and unfulfilled orders based on the report data. It also
	 * calculates and displays the total counts.
	 * 
	 * @param report The cancellation report containing the data for calculation.
	 */

	private void calculateAndDisplayStats(CancellationReport report) {
		AverageCanceledOrdersLabal.setText(String.format("%.2f", calculateAverage(report.getDailyCancellations())));
		MedianCanceledOdersLabal.setText(String.format("%.2f", calculateMedian(report.getDailyCancellations())));
		AverageUnfulfilledOrderLabal
				.setText(String.format("%.2f", calculateAverage(report.getDailyUnfulfilledOrders())));
		MedianUnfulfilledOrdersLabal
				.setText(String.format("%.2f", calculateMedian(report.getDailyUnfulfilledOrders())));
		// Calculate totals
		int totalCancellations = report.getDailyCancellations().values().stream().mapToInt(Integer::intValue).sum();
		int totalUnfulfilledOrders = report.getDailyUnfulfilledOrders().values().stream().mapToInt(Integer::intValue)
				.sum();

		// Update labels
		TotalCanceledLabal.setText(String.valueOf(totalCancellations));
		TotalUnfulfilledLabal.setText(String.valueOf(totalUnfulfilledOrders));
	}

	/**
	 * Calculates the average of a set of integer values.
	 * 
	 * @param data The data set from which to calculate the average.
	 * @return The average of the provided data set.
	 */
	private double calculateAverage(Map<Integer, Integer> data) {
		int totalDays = data.size();
		double total = data.values().stream().mapToDouble(Integer::doubleValue).sum();
		return (total / totalDays);
	}

	/**
	 * Calculates the median of a set of integer values.
	 * 
	 * @param data The data set from which to calculate the median.
	 * @return The median of the provided data set.
	 */
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