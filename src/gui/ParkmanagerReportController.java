package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Report;
import common.Usermanager;
import common.VisitorsReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ParkmanagerReportController implements Initializable {

	@FXML
	private JFXComboBox<String> monthCombobox; // Changed to String for display purposes
	@FXML
	private JFXComboBox<String> ReportTypeCombobox; // Changed to String for display purposes

	ObservableList<?> observable = FXCollections.observableArrayList();





	@FXML
	private TableView<Report> ReportsTableView;

	@FXML
	private TableColumn<Report, Integer> reportIDCol;

	@FXML
	private TableColumn<Report, String> reportTypeCol;

	@FXML
	private TableColumn<Report, Integer> parkIDCol;

	@FXML
	private TableColumn<Report, String> DateCol;

	@FXML
	private TableColumn<Report, String> commentCol;
	@FXML
	private JFXButton Createbth;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize months 1-12
		for (int month = 1; month <= 12; month++) {
			monthCombobox.getItems().add(String.valueOf(month));
			// Initialize report types
			ReportTypeCombobox.setItems(FXCollections.observableArrayList("Visit Report", "Occupancy Report"));
		

		}
		configureTableColumns();
		ShowReportparkIdAction();
	}
	private void configureTableColumns() {
		reportIDCol.setCellValueFactory(new PropertyValueFactory<>("reportID"));
		reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("reportType"));
		parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkID"));
		DateCol.setCellValueFactory(cellData -> {
			LocalDate date = cellData.getValue().getDate();
			return new javafx.beans.property.SimpleStringProperty(
					date != null ? date.format(DateTimeFormatter.ofPattern("MMMM yyyy")) : "");
		});
		commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
	}

	private void ShowReportparkIdAction() {
		Integer parkId = Usermanager.getCurrentWorker().getWorksAtPark();
	
			// Here, send request to server to get reports by parkId
			ObservableList<Report> reports = getReportsByParkId(parkId);
			if (reports.isEmpty()) {
				Alerts infoalert = new Alerts(Alert.AlertType.INFORMATION, "Information", "", "Not have report to show.");
				infoalert.showAndWait();
			}
			else {
				 ReportsTableView.getItems().clear(); // Clear existing content
		            ReportsTableView.setItems(reports); // Set new items
		            ReportsTableView.refresh(); // Explicitly refresh the table view
			}
		}
	

	@FXML
	void ShowReportTableClickAction(MouseEvent event) {
		if (event.getClickCount() == 2) { // Double click
			Report selectedReport = ReportsTableView.getSelectionModel().getSelectedItem();
			if (selectedReport != null) {
				ClientServerMessage<Report> messageForServer = new ClientServerMessage<>(selectedReport, Operation.GET_EXISTS_VISITORS_REPORT);
	             ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
	      
	             
	             if (ClientController.data.getFlag()) {
	            	 System.out.println(ClientController.data.getDataTransfered());
	            		Alerts infoalert = new Alerts(Alerts.AlertType.INFORMATION, "INFORMATION", "",
								"Report get from database");
	            		infoalert.showAndWait();
	            		try {
							NavigationManager.openPage("VisitorsReport.fxml", event, "", false);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		
	             }
	             else {
	            	 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
								"A report return empty request was not successfully executed on database");
						somethingWentWrong.showAndWait();
	             }
			
			}
			else {
				Alerts warningalert = new Alerts(Alert.AlertType.WARNING, "Warning", "", "Selction is empty.");
				warningalert.showAndWait();
			}
		}
	}

	private ObservableList<Report> getReportsByParkId(int parkId) {
		ClientServerMessage<?> messageForServer = new ClientServerMessage<>(parkId, Operation.GET_GENERAL_REPORT);
		System.out.println("1");
		ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);

		if (ClientController.data.getFlag()) {
			ArrayList<Report> serverResponse = (ArrayList<Report>) ClientController.data.getDataTransfered();
			return FXCollections.observableArrayList(serverResponse);
		} else {
			// Handle the case where the server response indicates failure or no data
			Alerts infoAlert = new Alerts(Alerts.AlertType.INFORMATION, "Information", "",
					"No reports were found or there was an error processing your request.");
			infoAlert.showAndWait();
			return FXCollections.observableArrayList(); // Return an empty list
		}
	}

	@FXML
	void CreateReportAction(ActionEvent event) {
		String selectedMonthString = monthCombobox.getSelectionModel().getSelectedItem();
	    String selectedReportType = ReportTypeCombobox.getSelectionModel().getSelectedItem();
	    
	    // Validate that both selections are not null (i.e., a selection has been made)
	    if (selectedMonthString == null || selectedReportType == null) {
	        Alert errorAlert =new Alerts(Alert.AlertType.ERROR, "Error", "", "You must select a month and report type.");
	        errorAlert.showAndWait(); 
	    }
	    else {
			int selectedMonth = Integer.parseInt(selectedMonthString);

	    
	    // React based on the selected report type
	    switch (selectedReportType) {
	        case "Visit Report":
	        	VisitorsReport visitorReportToServer = new VisitorsReport(
	        		    0, // Assuming 0 is a placeholder for the report ID
	        		    null, // Assuming this is for ReportType, which you might want to specify
	        		    Usermanager.getCurrentWorker().getWorksAtPark(), // Gets the park ID from the current worker
	        		    LocalDate.now(), // Sets the report's date to today
	        		    selectedMonth, // The month selected, make sure this is correctly parsed as an integer if needed
	        		    null, // Placeholder for comment
	        		    null, // Placeholder for total individual visitors
	        		    null, // Placeholder for total group visitors
	        		    null, // Placeholder for total family visitors
	        		    null  // Placeholder for total visitors
	        		);
	        	ClientServerMessage<?> messageForServer = new ClientServerMessage<>(visitorReportToServer, Operation.GET_NEW_VISITORS_REPORT);
	             ClientUI.clientControllerInstance.sendMessageToServer(messageForServer);
			try {
				NavigationManager.openPage("VisitorsReport.fxml", event, "", false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	             
	        	System.out.println("Creating Visit Report for Month: " + selectedMonthString);
	            break;
	        case "Occupancy Report":
	            // Code to handle occupancy report creation
	            System.out.println("Creating Occupancy Report for Month: " + selectedMonthString);
	            break;
	        default:
	            // Handle unexpected report type (if applicable)
	            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Unknown report type selected.");
	            infoAlert.setHeaderText("Information");
	            infoAlert.showAndWait();
	            break;
	    }
	    }
	}

	

}
