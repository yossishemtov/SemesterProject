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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Controller class for creating and displaying a usage report in the park manager's interface.
 * This class handles the visual representation and interaction of generating a usage report,
 * including displaying usage statistics on a calendar-like grid and allowing the manager to
 * add comments and save the report.
 */
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


	

	   /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It initializes the usage report
     * view by loading the usage report data and setting up the grid pane.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadUsageReport();
		populateGridPane();
	}

    /**
     * Loads the usage report data from the server response stored in the client's current session.
     */
	private void loadUsageReport() {
		usageReport = (UsageReport) ClientUI.clientControllerInstance.getData().getDataTransfered();

	}
	

	   /**
     * Populates the grid pane with data from the usage report. It sets up the
     * grid to display days of the week and fills in each day with color-coded
     * rectangles to represent park occupancy.
     */
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
			lblDayName.setStyle("-fx-text-alignment: center; -fx-alignment: top-center; -fx-text-fill: white; -fx-padding: 10px;"); // Center text and align
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
			rect.setStroke(Color.web("#282829"));

			Integer occupancy = dailyUsage.getOrDefault(dayOfMonth, 0);
			int parkCapacity = usageReport.getParkCapacity();
			rect.setFill(occupancy >= parkCapacity ?  Color.RED  : Color.web("#3dee5a"));
			
			

			Label dayLabel = new Label(String.valueOf(dayOfMonth));
			dayLabel.setTextFill(Color.web("#282829"));
			StackPane stackPane = new StackPane(rect, dayLabel);
			stackPane.setAlignment(Pos.CENTER);

			gridPaneUsageReport.add(stackPane, columnIndex, rowIndex);
		}

	}

    /**
     * Handles the action of closing the usage report page.
     * @param event The event that triggered the action.
     */
	@FXML
	void ClosePageAction(ActionEvent event) {
		Stage stage = (Stage) ClosePagebth.getScene().getWindow();
		stage.close();
	}

	 /**
     * Handles saving the usage report, including any comments made by the park manager.
     * @param event The event that triggered the action.
     */
    @FXML
    void SaveReportAction(ActionEvent event) {
        if (usageReport != null) {
            // Adding comments to the report if present
            if (CommentTextArea.getText() != null) {
                usageReport.setComment(CommentTextArea.getText());
            }

            // Sending the report to the server for saving
            ClientServerMessage<?> messageForServer = new ClientServerMessage<>(usageReport, Operation.POST_USAGE_REPORT);
            ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

            // Confirmation message upon successful save
            if (ClientController.data.getFlag()) {
                ParkmanagerReportController.refreshReportsTable();
                Alerts confirmationAlert = new Alerts(Alerts.AlertType.INFORMATION, "Confirmation", "", "The report was successfully saved.");
                confirmationAlert.showAndWait();
                ClosePageAction(event); // Close the page after saving
            }
        } else {
            // Error message if the report data is not available
            Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to save visitor report.");
            errorAlert.showAndWait();
        }
    }

}