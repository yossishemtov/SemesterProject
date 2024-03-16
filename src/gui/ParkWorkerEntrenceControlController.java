package gui;


import java.time.LocalDate;
import java.time.LocalTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import client.ClientUI;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ParkWorkerEntrenceControlController {
	@FXML
    private JFXButton getInfromationBtn;

    @FXML
    private JFXTextField orderTextBox;

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label TravelerIdLabel;

    @FXML
    private Label amountOfVisitorsLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label visitTimeLabel;
    
    static private Order orderToEnterOrExit = null;
    
    @FXML
    public void receiveOrderInformationAction(ActionEvent event) {
    	//Send a request to the server to receive order Information
    	
    	try {
    		
    		String orderIdText = orderTextBox.getText();
    		
    		//Checks if order id entered by user only contains numbers
    		Alerts alertIndication = InputValidation.validateOrderNumber(orderIdText);
        	Boolean checkIfNoError = alertIndication.getAlertType().toString().equals("INFORMATION");
        	
        	//Get the park where the worker works at
        	GeneralParkWorker currentWorkerAccount = Usermanager.getCurrentWorker();
        	Integer currentWorkerPark = currentWorkerAccount.getWorksAtPark();
        	
        	//Get current date of today
        	LocalDate today = LocalDate.now();
        	
        	System.out.print(currentWorkerPark);
        	if(checkIfNoError) {
        		
	        	Integer orderIdInteger = Integer.parseInt(orderIdText);
	        	
	        	//Send the orderId within an order dummy object to the server
	        	Order dummyOrder = new Order(orderIdInteger, null, currentWorkerPark, null, null, null, today, null, null, null, null);
	        	ClientServerMessage requestOrderInformation = new ClientServerMessage(dummyOrder, Operation.GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE);
	        	ClientUI.clientControllerInstance.sendMessageToServer(requestOrderInformation);
	        	
	        	
	        	Order receivedOrderFromServer = (Order) ClientUI.clientControllerInstance.getData().getDataTransfered();
        	if(receivedOrderFromServer != null) {
        		//Receiving information to parse to the screen
        		Integer orderIdReceived = receivedOrderFromServer.getOrderId();
        		Integer travelerIdReceived = receivedOrderFromServer.getVisitorId();
        		Integer amountOfVisitorsReceived = receivedOrderFromServer.getAmountOfVisitors();
        		LocalDate dateReceived = receivedOrderFromServer.getDate();
        		LocalTime timeReceived = receivedOrderFromServer.getVisitTime();

        		//Sets the labels placeholders to the recived information
        		orderIdLabel.setText(Integer.toString(orderIdReceived));
        		TravelerIdLabel.setText(Integer.toString(travelerIdReceived));
        		amountOfVisitorsLabel.setText(Integer.toString(amountOfVisitorsReceived));
        		dateLabel.setText(dateReceived.toString());
        		visitTimeLabel.setText(timeReceived.toString());
        		
        	}else {
        		Alerts noOrderIdExists = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "No such order exists for your park for today!");
        		noOrderIdExists.showAndWait();
        	}
        }else {
        	//If validation fails (user didnt enter only numbers)
        	alertIndication.showAndWait();
        }
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong");
			somethingWentWrong.showAndWait();
    	}

    }
    
    public void entrenceControlAction(ActionEvent event) {
    	if(orderToEnterOrExit == null) {
    		
    		if(orderToEnterOrExit.getDate() != LocalDate.now());
    		
    		
    		
    		
    	}else {
    		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Please insert order to submit to park entrence system");
			somethingWentWrong.showAndWait();
    	}
    }
    
    
}
