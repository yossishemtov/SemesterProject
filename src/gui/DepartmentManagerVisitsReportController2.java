package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.ClientUI;
import common.ClientServerMessage;
import common.HourlyVisitData;
import common.Operation;




// Assume HourlyVisitData is a class holding hour, guidedGroupCount, familyCount, and soloCount

public class DepartmentManagerVisitsReportController2 implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label headerReport;

    // The bar chart will be created dynamically
    private BarChart<String, Number> barChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAndAddChart();
        fetchDataAndPopulateChart(); // Simulate fetching data on initialization
    }

    private void createAndAddChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(true); // Allow auto-ranging for the yAxis

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(800, 400);
        barChart.setLayoutX(50);
        barChart.setLayoutY(100);

        rootPane.getChildren().add(barChart);
    }

    // This method simulates fetching data and then populating the chart
    private void fetchDataAndPopulateChart() {
        // Simulated data fetching, replace with actual data fetching logic
        ArrayList<HourlyVisitData> listHourlyVisit = getHourlyVisitDataForPark(1); // Example park ID
        populateChart(listHourlyVisit);
    }

    private void populateChart(ArrayList<HourlyVisitData> data) {
        barChart.getData().clear();

        XYChart.Series<String, Number> seriesGUIDEDGROUP = new XYChart.Series<>();
        seriesGUIDEDGROUP.setName("GUIDEDGROUP");
        XYChart.Series<String, Number> seriesFAMILY = new XYChart.Series<>();
        seriesFAMILY.setName("FAMILY");
        XYChart.Series<String, Number> seriesSOLO = new XYChart.Series<>();
        seriesSOLO.setName("SOLO");

        for (HourlyVisitData hourlyData : data) {
            seriesGUIDEDGROUP.getData().add(new XYChart.Data<>(hourlyData.getHour(), hourlyData.getGuidedGroupCount()));
            seriesFAMILY.getData().add(new XYChart.Data<>(hourlyData.getHour(), hourlyData.getFamilyCount()));
            seriesSOLO.getData().add(new XYChart.Data<>(hourlyData.getHour(), hourlyData.getSoloCount()));
        }

        barChart.getData().addAll(seriesGUIDEDGROUP, seriesFAMILY, seriesSOLO);
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
