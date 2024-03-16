package gui;

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

import javafx.scene.chart.LineChart;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import common.ClientServerMessage;
import common.HourlyVisitData;
import common.Operation;

public class DeparmentManegerVisitsReportController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label headerReport;

	@FXML
	private LineChart<?, ?> stayTime_chart;

	@FXML
	private NumberAxis stayX2;

	@FXML
	private NumberAxis stayY;

	@FXML
	private NumberAxis enterX2; // Ensure this matches the fx:id in your FXML

	@FXML
	private NumberAxis enterY;

	@FXML
	private JFXTextField parkNumberFiled;

	@FXML

	private BarChart<String, Number> entranceTime_BarChart;
	@FXML
	private Label sendParkNumlabal;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configureChartAxes();
	}

	private void configureChartAxes() {
		enterX2.setAutoRanging(false);
		enterX2.setLowerBound(0);
		enterX2.setUpperBound(24);
		enterX2.setTickUnit(1);

		enterY.setAutoRanging(false);
		enterY.setLowerBound(0);
		enterY.setUpperBound(100);
		enterY.setTickUnit(10);
	}

	@FXML
	void fetchDataAndPopulateChart(MouseEvent event) {
		int parkID;
		try {
			parkID = Integer.parseInt(parkNumberFiled.getText().trim());
		} catch (NumberFormatException e) {
			showAlert(AlertType.ERROR, "Error", "Invalid Park Number", "Please enter a valid park number.");
			return;
		}

		ArrayList<HourlyVisitData> listHourlyVisit = getHourlyVisitDataForPark(parkID);
		if (listHourlyVisit == null || listHourlyVisit.isEmpty()) {
			showAlert(AlertType.ERROR, "Error", "Error loading park parameters", "Not have park for this park number.");
			return;
		}

		populateChart(listHourlyVisit);
	}

	private void populateChart(ArrayList<HourlyVisitData> data) {
		entranceTime_BarChart.getData().clear();

		XYChart.Series<String, Number> seriesGUIDEDGROUP = new XYChart.Series<>();
		seriesGUIDEDGROUP.setName("GUIDEDGROUP");
		XYChart.Series<String, Number> seriesFAMILY = new XYChart.Series<>();
		seriesFAMILY.setName("FAMILY");
		XYChart.Series<String, Number> seriesSOLO = new XYChart.Series<>();
		seriesSOLO.setName("SOLO");

		// Assuming each HourlyVisitData instance corresponds to one hour.
		// This is a simplified approach; you may need to adjust based on actual data
		// structure.
		for (HourlyVisitData hourlyData : data) {
			// Here, calculate the percentages for each hour and type.
			// Assuming 100 as a placeholder total count for demonstration.
			// Replace with actual logic to calculate percentages based on total counts.
			int total = 100; // Placeholder, replace with actual total calculation.

			float percentageGUIDEDGROUP = (float) hourlyData.getGuidedGroupCount() / total * 100;
			float percentageFAMILY = (float) hourlyData.getFamilyCount() / total * 100;
			float percentageSOLO = (float) hourlyData.getSoloCount() / total * 100;

			seriesGUIDEDGROUP.getData().add(new XYChart.Data<>(hourlyData.getHour(), percentageGUIDEDGROUP));
			seriesFAMILY.getData().add(new XYChart.Data<>(hourlyData.getHour(), percentageFAMILY));
			seriesSOLO.getData().add(new XYChart.Data<>(hourlyData.getHour(), percentageSOLO));
		}

		entranceTime_BarChart.getData().addAll(seriesGUIDEDGROUP, seriesFAMILY, seriesSOLO);
	}
 
	// Placeholder method for fetching data, replace with actual server call.
	private ArrayList<HourlyVisitData> getHourlyVisitDataForPark(int parkID) {
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkID,
				Operation.GET_HOURLY_VISIT_DATA_FOR_PARK);
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		ArrayList<HourlyVisitData> listHoyrVisit = (ArrayList<HourlyVisitData>) ClientController.data
				.getDataTransfered();

		return listHoyrVisit;
	}

	private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
