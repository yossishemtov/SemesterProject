package gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.chart.LineChart;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import common.ClientServerMessage;

import common.Operation;

import javafx.scene.chart.CategoryAxis;

import javafx.scene.control.Button;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import common.worker.VisitReport;
import common.worker.VisitReport.TypeOfOrder;
import common.worker.VisitData;

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

	// This is a placeholder for your VisitReport object
	private VisitReport Visitreport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport(); // Mock method to load report data
		populateLineChart();

	} 

	private void loadUsageReport() {

		this.Visitreport = (VisitReport) ClientUI.clientControllerInstance.getData().getDataTransfered();

	}

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

	@FXML
	void ClosepageAction(ActionEvent event) {
		// Get the current stage using the event source
		Stage stage = (Stage) Closebth.getScene().getWindow();
		// Close the current stage
		stage.close();
	}
}
