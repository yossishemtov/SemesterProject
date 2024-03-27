package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import client.ClientUI;
import javafx.scene.chart.CategoryAxis;
import java.util.HashMap;
import java.util.Map;
import common.worker.VisitReport;
import common.worker.VisitReport.TypeOfOrder;
import common.worker.VisitData;

/**
 * Controller for the Department Manager Visits Report GUI.
 * This class is responsible for initializing and populating the visits line chart with data
 * obtained from a {@link VisitReport} object.
 */
public class DepartmentManagerVisitsReportController implements Initializable {

	@FXML
	private LineChart<String, Number> VisitLineChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private Label titlelLabal;

	@FXML
	private JFXButton Closebth;


    // Placeholder for your VisitReport object
    private VisitReport Visitreport;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It initializes the chart with data.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport(); // Mock method to load report data
		populateLineChart();

	} 

	/**
     * Loads the usage report data from the server and stores it in the {@link VisitReport} object.
     */
    private void loadUsageReport() {
        // Retrieve and store the VisitReport object sent from the server
        this.Visitreport = (VisitReport) ClientUI.clientControllerInstance.getData().getDataTransfered();
    }

    /**
     * Populates the line chart with visit report data.
     * This method organizes visit data by type of order and hour of day,
     * then displays it in the line chart.
     */
	private void populateLineChart() {
	    titlelLabal.setText("Visit Report for month " + Visitreport.getMonthNumber());
	    VisitLineChart.getData().clear();
	    xAxis.setLabel("Hour of Day");
	    yAxis.setLabel("Stay Time (Minutes)");

	    // Group visits by TypeOfOrder and hour
	    Map<TypeOfOrder, Map<Integer, Long>> groupedByTypeAndHour = new HashMap<>();

	    // Iterate over each visit to group by TypeOfOrder and then by hour with total duration
	    for (VisitData visit : Visitreport.getVisits()) {
	        TypeOfOrder type = visit.getTypeOfOrder();
	        int hour = visit.getEnteringTime().getHour();
	        long duration = visit.getDurationMinutes();

	        groupedByTypeAndHour.computeIfAbsent(type, k -> new HashMap<>()).merge(hour, duration, Long::sum);
	    }

	    // For each TypeOfOrder, create a series and add data points for each hour
	    groupedByTypeAndHour.forEach((type, hourMap) -> {
	        XYChart.Series<String, Number> series = new XYChart.Series<>();
	        series.setName(type.toString());

	        // Add data points for each hour, using 0 if no data exists for that hour
	        IntStream.rangeClosed(8, 18).forEach(hour -> {
	            long totalDuration = hourMap.getOrDefault(hour, 0L);
	            series.getData().add(new XYChart.Data<>(String.valueOf(hour), totalDuration));
	        });

	        VisitLineChart.getData().add(series);
	    });
	}

    /**
     * Handles the action of closing the visit report page.
     * This method is called when the close button is clicked.
     *
     * @param event The event that triggered the method call.
     */
	@FXML
	void ClosepageAction(ActionEvent event) {
		Stage stage = (Stage) Closebth.getScene().getWindow();
		stage.close();
	}
}
