package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.ClientUI;
import common.worker.UsageReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

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
		if (usageReport == null)
			return; // Ensure we have a report to work with

		int reportMonth = usageReport.getMonth();
		int reportYear = usageReport.getDate().getYear(); // Assuming you also have year info

		YearMonth yearMonth = YearMonth.of(reportYear, reportMonth);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		int dayOfWeekOffset = firstDayOfMonth.getDayOfWeek().getValue() % 7;
		for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
			int dayOfMonth = date.getDayOfMonth();
			int rowIndex = (dayOfWeekOffset + dayOfMonth - 1) / 7;
			int columnIndex = (dayOfWeekOffset + dayOfMonth - 1) % 7;

			Rectangle rect = new Rectangle(50, 50);
			rect.setStroke(Color.BLACK);

			Integer occupancy = dailyUsage.getOrDefault(dayOfMonth, 0);
			int parkCapacity = usageReport.getParkCapacity(); // Assuming park capacity is part of your report
			if (occupancy >= parkCapacity) {
				rect.setFill(Color.GREEN);
			} else {
				rect.setFill(Color.RED);
			}

			Label dayLabel = new Label(String.valueOf(dayOfMonth));
			dayLabel.setTextFill(Color.WHITE);

			gridPaneUsageReport.add(rect, columnIndex, rowIndex);
			gridPaneUsageReport.add(dayLabel, columnIndex, rowIndex);
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
