package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import client.ClientController;
import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.worker.VisitorsReport;

/**
 * Controller class for creating and managing a visitors report UI.
 * This class is responsible for handling the user interactions with the 
 * visitors report page, such as closing the window, saving the visitors report, 
 * and initializing and updating the visitors chart based on the report data.
 */
public class CreateVisitorsReportController {

    private VisitorsReport visitorReport;

    @FXML
    private JFXButton savereportbth;
    @FXML
    private PieChart visitorsChart;
    @FXML
    private JFXTextArea CommentTextArea;
    @FXML
    private JFXButton CloseWindowBth;
    @FXML
    private Label commentLabal;
    @FXML
    private Label titlelLabal;

    /**
     * Handles the action of closing the current window.
     * 
     * @param event The action event that triggered this method.
     */
    @FXML
    void CloseWindowAction(ActionEvent event) {
        Stage stage = (Stage) CloseWindowBth.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It initializes the visitor report data
     * and sets up the UI accordingly.
     */
    @FXML
    private void initialize() {
        visitorReport = null;
        loadVisitorsReport();
    }

    /**
     * Saves the visitors report to the server.
     * This method collects the data from the UI, updates the visitorReport object, 
     * and sends it to the server for persistence. It also provides feedback to the user
     * about the operation's success or failure.
     * 
     * @param event The action event that triggered this method.
     */
    @FXML
    void saveVisitorReportAction(ActionEvent event) {
        if (visitorReport != null) {
            if (CommentTextArea.getText() != null) {
                visitorReport.setComment(CommentTextArea.getText());
            }
            ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitorReport,
                    Operation.POST_VISITORS_REPORT);
            ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

            if (ClientController.data.getFlag()) {
                ParkmanagerReportController.refreshReportsTable();

                Alerts erorAlert = new Alerts(Alerts.AlertType.CONFIRMATION, "Confirmation", "",
                        "The report was successfully saved.");
                erorAlert.showAndWait();
                CloseWindowAction(event);

            }
        } else {
            Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to save visitor report.");
            erorAlert.showAndWait();
        }
    }

    /**
     * Loads the visitor report data from the server and updates the UI accordingly.
     * This method requests the latest visitors report data, if available, and updates
     * the chart to reflect the new data.
     */
    private void loadVisitorsReport() {
        visitorReport = (VisitorsReport) ClientController.data.getDataTransfered();
        System.out.println(visitorReport.toString());
        if (visitorReport != null) {
            updateChart(visitorReport);
        } else {
            Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Error", "Error", "Not able to load visitor report.");
            erorAlert.showAndWait();
        }
    }

    /**
     * Updates the visitors chart with the provided report data.
     * This method takes a VisitorsReport object and uses its data to populate
     * the visitors chart in the UI.
     * 
     * @param report The visitors report data used to update the chart.
     */
    private void updateChart(VisitorsReport report) {
        titlelLabal.setText("Visitors Report for month " + visitorReport.getMonth());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Individual " + report.getTotalIndividualVisitors(),
                        report.getTotalIndividualVisitors()),
                new PieChart.Data("Group " + report.getTotalGroupVisitors(), report.getTotalGroupVisitors()),
                new PieChart.Data("Family " + report.getTotalFamilyVisitors(), report.getTotalFamilyVisitors()));

        visitorsChart.getData().clear();
        visitorsChart.setData(pieChartData);

        visitorsChart.setLabelsVisible(false);
        visitorsChart.setLegendVisible(true);
    }
}
