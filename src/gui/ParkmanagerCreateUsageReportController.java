
package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.worker.UsageReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import javafx.scene.control.Label;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ParkmanagerCreateUsageReportController implements Initializable {

	@FXML
	private GridPane gridPaneUsageReport;

	@FXML
	private JFXButton ClosePagebth;
	@FXML
	private GridPane GridPane;

	private UsageReport usageReport;
	@FXML
	private Label titlelLabal;
	@FXML
	private JFXTextArea CommentTextArea;

	@FXML
	private JFXButton SaveReportbth;

	private Map<Integer, Integer> dailyUsage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport();
		populateGridPane();
	}

	private void loadUsageReport() {
		usageReport = (UsageReport) ClientUI.clientControllerInstance.getData().getDataTransfered();

	}

	private void populateGridPane() {
		if (usageReport == null)
			return;
		titlelLabal.setText("Usage Report for month " + usageReport.getMonth());

		String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

		// Clearing existing content and column constraints from the grid
		gridPaneUsageReport.getChildren().clear();
		gridPaneUsageReport.getColumnConstraints().clear();
		gridPaneUsageReport.getRowConstraints().clear(); // Clear any existing row constraints

		// Setting up column and first row constraints for alignment
		for (int i = 0; i < 7; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setHalignment(HPos.CENTER); // Ensure horizontal alignment within the column
			gridPaneUsageReport.getColumnConstraints().add(column);
		}

		// Set constraints for the first row where day names will be
		RowConstraints rowConstraintsForDayNames = new RowConstraints();
		rowConstraintsForDayNames.setValignment(VPos.TOP); // Align to the top of the row
		gridPaneUsageReport.getRowConstraints().add(rowConstraintsForDayNames); // Apply to the first row

		// Adding day names in the first row, aligned to the top center of their cells
		for (int i = 0; i < dayNames.length; i++) {
			Label lblDayName = new Label(dayNames[i]);
			lblDayName.setStyle("-fx-text-alignment: center; -fx-alignment: top-center;"); // Center text and align
																							// label top-center within
																							// its cell
			lblDayName.setMaxWidth(Double.MAX_VALUE); // Allow the label to grow and fill the cell width
			GridPane.setMargin(lblDayName, new Insets(0, 0, 0, 0));

			gridPaneUsageReport.add(lblDayName, i, 0); // Adding to the first row
		}

		Map<Integer, Integer> dailyUsage = usageReport.getDailyUsage();
		int reportMonth = usageReport.getMonth();
		int reportYear = usageReport.getDate().getYear();

		YearMonth yearMonth = YearMonth.of(reportYear, reportMonth);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		int firstDayWeekValue = firstDayOfMonth.getDayOfWeek().getValue() % 7;
		for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
			int dayOfMonth = date.getDayOfMonth();
			// Calculating rowIndex considering the first day's offset, starting from the
			// second row for dates
			int rowIndex = ((firstDayWeekValue + dayOfMonth - 1) / 7) + 1;
			int columnIndex = (firstDayWeekValue + dayOfMonth - 1) % 7;

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
		Stage stage = (Stage) ClosePagebth.getScene().getWindow();
		stage.close();
	}

	@FXML
	void SaveReportAction(ActionEvent event) {
		if (usageReport != null) {
			if (CommentTextArea.getText() != null) {
				usageReport.setComment(CommentTextArea.getText());
			}
			ClientServerMessage<?> messageForServer = new ClientServerMessage<>(usageReport,
					Operation.POST_USAGE_REPORT);
			ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

			if (ClientController.data.getFlag()) {
				ParkmanagerReportController.refreshReportsTable();
				Alerts erorAlert = new Alerts(Alerts.AlertType.INFORMATION, "Confirmtion", "",
						"The report was successfully saved.");
				erorAlert.showAndWait();
				ClosePageAction(event);

			}
		} else {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to save visitor report.");
			erorAlert.showAndWait();
		}

	}

}