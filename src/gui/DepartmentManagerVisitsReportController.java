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
import common.HourlyVisitData;
import common.Operation;

import javafx.scene.chart.CategoryAxis;

import javafx.scene.control.Button;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import common.worker.VisitReport;
import common.worker.VisitData;

public class DepartmentManagerVisitsReportController implements Initializable {

	@FXML
	private LineChart<String, Number> VisitLineChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;
	

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
		 VisitLineChart.getData().clear();
	        xAxis.setLabel("Hour of Day");
	        yAxis.setLabel("Number of Visits");

	        // Group visits by TypeOfOrder and hour
	        Visitreport.getVisits().stream().collect(Collectors.groupingBy(VisitData::getTypeOfOrder)).forEach((type, visits) -> {
	            XYChart.Series<String, Number> series = new XYChart.Series<>();
	            series.setName(type.toString());

	            // Assuming the list of visits is not too large to make this inefficient
	            IntStream.rangeClosed(1, 24).forEach(hour -> {
	                long count = visits.stream().filter(visit -> visit.getEnteringTime().getHour() == hour).count();
	                series.getData().add(new XYChart.Data<>(String.valueOf(hour), count));
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
