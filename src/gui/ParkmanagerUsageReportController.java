
package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.ClientUI;
import common.worker.UsageReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import javafx.scene.control.Label;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ParkmanagerUsageReportController implements Initializable {

	@FXML
	private GridPane gridPaneUsageReport;

	@FXML
	private JFXButton ClosePagebth;
	@FXML
	private GridPane GridPane;
	
	private UsageReport usageReport;

	@FXML
	private JFXButton SaveReportbth;


	private Map<Integer, Integer> dailyUsage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport(); 
		populateGridPane();
	}

	private void loadUsageReport() {
		 usageReport=(UsageReport) ClientUI.clientControllerInstance.getData().getDataTransfered();
		 dailyUsage=usageReport.getDailyUsage();

	}

	   private void populateGridPane() {
	        if (usageReport == null) return; // Ensure we have a report to work with

	        // Setup for the names of the days
	        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	        // Clear any existing column constraints and set new ones for centering
	        gridPaneUsageReport.getColumnConstraints().clear();
	        for (int i = 0; i < 7; i++) {
	            ColumnConstraints column = new ColumnConstraints();
	            column.setHalignment(HPos.CENTER);
	            gridPaneUsageReport.getColumnConstraints().add(column);
	        }

	        // Adding the day names at the top of each column
	        for (int i = 0; i < dayNames.length; i++) {
	            Label lblDayName = new Label(dayNames[i]);
	            lblDayName.setAlignment(Pos.CENTER);
	            GridPane.setHalignment(lblDayName, HPos.CENTER); // Ensure label is centered in its cell
	            gridPaneUsageReport.add(lblDayName, i, 0);
	        }

	        int reportMonth = usageReport.getMonth();
	        int reportYear = usageReport.getDate().getYear();

	        YearMonth yearMonth = YearMonth.of(reportYear, reportMonth);
	        LocalDate firstDayOfMonth = yearMonth.atDay(1);
	        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

	        int dayOfWeekOffset = firstDayOfMonth.getDayOfWeek().getValue() % 7;
	        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
	            int dayOfMonth = date.getDayOfMonth();
	            int rowIndex = (dayOfWeekOffset + dayOfMonth - 1) / 7 + 1;
	            int columnIndex = (dayOfWeekOffset + dayOfMonth - 1) % 7;

	            Rectangle rect = new Rectangle(50, 50);
	            rect.setStroke(Color.BLACK);

	            Integer occupancy = dailyUsage.getOrDefault(dayOfMonth, 0);
	            int parkCapacity = usageReport.getParkCapacity();
	            rect.setFill(occupancy >= parkCapacity ? Color.GREEN : Color.RED);

	            Label dayLabel = new Label(String.valueOf(dayOfMonth));
	            dayLabel.setTextFill(Color.WHITE);
	            StackPane stackPane = new StackPane(rect, dayLabel);
	            stackPane.setAlignment(Pos.CENTER);

	            gridPaneUsageReport.add(stackPane, columnIndex, rowIndex);
	        }
	    }

	@FXML
	void ClosePageAction(ActionEvent event) {

		// Get the current stage using the event source
		Stage stage = (Stage) ClosePagebth.getScene().getWindow();
		// Close the current stage
		stage.close();
	}

	

	@FXML
	void SaveReportAction(ActionEvent event) {

	}

}