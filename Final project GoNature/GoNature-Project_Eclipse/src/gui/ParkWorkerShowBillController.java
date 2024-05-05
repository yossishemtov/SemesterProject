package gui;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.NavigationManager;
import common.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ParkWorkerShowBillController implements Initializable{

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label travelerIdLabel;

    @FXML
    private Label amountOfVisitorsLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label visitorEmailLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label visitTimeLabel;

    @FXML
    private Label orderTypeLabel;
    
    @FXML
    private JFXButton confirmOrderButton;

	@Override
	/**
	 * Initializes the controller class. This method is automatically called after
	 * the FXML file has been loaded. It initializes the information to display in the order and prints the invoice to the screen
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Parsing order information to the bill screen
		if(ParkWorkerEntrenceControlController.orderToEnterOrExit != null) {
			Integer orderId = ParkWorkerEntrenceControlController.orderToEnterOrExit.getOrderId();
			Integer travelerId = ParkWorkerEntrenceControlController.orderToEnterOrExit.getVisitorId();
			Integer amountOfVisitors = ParkWorkerEntrenceControlController.orderToEnterOrExit.getAmountOfVisitors();
			Float priceAmount = ParkWorkerEntrenceControlController.orderToEnterOrExit.getPrice();
			String travelerEmail = ParkWorkerEntrenceControlController.orderToEnterOrExit.getVisitorEmail();
			String travelerPhone = ParkWorkerEntrenceControlController.orderToEnterOrExit.getTelephoneNumber();
			LocalDate currentDate = ParkWorkerEntrenceControlController.orderToEnterOrExit.getDate();
			LocalTime timeOfEnter = ParkWorkerEntrenceControlController.orderToEnterOrExit.getVisitTime();
			String orderType = ParkWorkerEntrenceControlController.orderToEnterOrExit.getTypeOfOrder();
			
			
			orderIdLabel.setText(Integer.toString(orderId));
			travelerIdLabel.setText(Integer.toString(travelerId));
			amountOfVisitorsLabel.setText(Integer.toString(amountOfVisitors));
			priceLabel.setText(Float.toString(priceAmount));
			visitorEmailLabel.setText(travelerEmail);
			phoneNumberLabel.setText(travelerPhone);
			dateLabel.setText(currentDate.toString());
			visitTimeLabel.setText(timeOfEnter.toString());
			orderTypeLabel.setText(orderType);
			
			
		}else {
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading order information");
			somethingWentWrong.showAndWait();
		}
		
		ParkWorkerEntrenceControlController.orderToEnterOrExit = null;
	}
	
	/**
	 * Closing the window of the bill
	 * @param event (Listening for an event)
	 */
	public void confirmButtonAction(ActionEvent event) throws Exception {
    	//Function for closing the bill screen
    	try {
    		
    		Stage stage = (Stage) confirmOrderButton.getScene().getWindow();
    		// Close the current stage
    		stage.close();
    		
    	} catch(Exception e) {
    			System.out.print("Something went wrong while clicking on the back button, check stack trace");
    			e.printStackTrace();
    		}
    	
    	}


}
