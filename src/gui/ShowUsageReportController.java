package gui;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import client.ClientUI;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Controller class for displaying a usage report within a GridPane layout.
 * This class is responsible for loading and visualizing usage data, including
 * daily usage statistics, for a specific month and year.
 */
public class ShowUsageReportController implements Initializable {

	@FXML
	private GridPane gridPaneUsageReport;

	@FXML
	private JFXButton ClosePagebth;
	@FXML
	private GridPane GridPane;

	private UsageReport usageReport;
	@FXML
	private JFXTextArea CommentTextArea;
	@FXML
	private Label titlelLabal;

	private Map<Integer, Integer> dailyUsage;

    /**
     * Initializes the controller. This method is automatically called after the
     * FXML file has been loaded. It initializes the usage report view by loading
     * data and setting up the grid pane.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport();
		populateGridPane();
	}

    /**
     * Loads the usage report data from the server/client controller.
     * The data is expected to be of type UsageReport.
     */
	private void loadUsageReport() {
		usageReport = (UsageReport) ClientUI.clientControllerInstance.getData().getDataTransfered();
		

	}

    /**
     * Populates the grid pane with daily usage statistics for the month specified
     * in the usage report. It visualizes the data using rectangles with different
     * colors indicating the occupancy levels.
     */
	private void populateGridPane() {
		if (usageReport == null)
			return; // Ensure we have a report to work with
		titlelLabal.setText("Usage Report for month "+usageReport.getMonth());

		dailyUsage = usageReport.getDailyUsage();
		CommentTextArea.setText(usageReport.getComment());

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
			GridPane.setMargin(lblDayName, new Insets(0, 0, 0, 0)); // Adjust margins if needed to move closer to the
																	// top

			gridPaneUsageReport.add(lblDayName, i, 0); // Adding to the first row
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
	
	
    /**
     * Handles the action of closing the usage report window.
     * 
     * @param event The action event triggered by clicking the close button.
     */
	@FXML
		void ClosePageAction(ActionEvent event) {
			// Get the current stage using the event source
			Stage stage = (Stage) ClosePagebth.getScene().getWindow();
			// Close the current stage
			stage.close();
		}
}