package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import client.ClientController;
import client.ClientUI;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.VisitorsReport;

public class VisitorsReportController {

    @FXML
    private PieChart visitorsChart;

    @FXML
    private void initialize() {
        loadVisitorsReport();
    }

    private void loadVisitorsReport() {
        ClientServerMessage<?> messageForServer = new ClientServerMessage<>(Usermanager.getCurrentWorker(), Operation.GET_VISITORS_REPORT);
        ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

        VisitorsReport report = (VisitorsReport) ClientController.data.getDataTransfered(); // Assuming this correctly fetches the data
        System.out.println(report.toString());
        if (report != null) {
            updateChart(report);
        }
    }

    private void updateChart(VisitorsReport report) {
        // Fixed ObservableList creation to properly store PieChart.Data objects only
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Individual " + report.getTotalIndividualVisitors(), report.getTotalIndividualVisitors()),
                        new PieChart.Data("Group " + report.getTotalGroupVisitors(), report.getTotalGroupVisitors()),
                        new PieChart.Data("Family " + report.getTotalFamilyVisitors(), report.getTotalFamilyVisitors())
                );

        // Clearing previous data and adding new data
        visitorsChart.getData().clear();
        visitorsChart.setData(pieChartData); // Correctly set the data for the pie chart

        // Disable the labels on the chart slices to avoid overlap
        visitorsChart.setLabelsVisible(false);
        visitorsChart.setLegendVisible(true); // Show legend, adjust as needed
    }
}
