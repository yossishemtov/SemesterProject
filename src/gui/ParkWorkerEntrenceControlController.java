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
import common.Park;
import common.Usermanager;
import common.Visit;
import common.worker.GeneralParkWorker;
import common.worker.ParkWorker;
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
    
    @FXML
    private Label orderStatusLabel;
   
	public static Order orderToEnterOrExit;
    
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
        	
        	if(checkIfNoError) {
        		
	        	Integer orderIdInteger = Integer.parseInt(orderIdText);
	        	
	        	//Send the orderId within an order dummy object to the server
	        	Order dummyOrder = new Order(orderIdInteger, null, currentWorkerPark, null, null, null, today, null, null, null, null);
	        	ClientServerMessage requestOrderInformation = new ClientServerMessage(dummyOrder, Operation.GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE);
	        	ClientUI.clientControllerInstance.sendMessageToServer(requestOrderInformation);
	        	
	        	
	        	Order receivedOrderFromServer = (Order) ClientUI.clientControllerInstance.getData().getDataTransfered();
        	if(receivedOrderFromServer != null) {
        		orderToEnterOrExit = receivedOrderFromServer;
        		
        		//Receiving information to parse to the screen
        		Integer orderIdReceived = receivedOrderFromServer.getOrderId();
        		Integer travelerIdReceived = receivedOrderFromServer.getVisitorId();
        		Integer amountOfVisitorsReceived = receivedOrderFromServer.getAmountOfVisitors();
        		LocalDate dateReceived = receivedOrderFromServer.getDate();
        		LocalTime timeReceived = receivedOrderFromServer.getVisitTime();
        		String orderStatus = receivedOrderFromServer.getOrderStatus();

        		//Sets the labels placeholders to the recived information
        		orderIdLabel.setText(Integer.toString(orderIdReceived));
        		TravelerIdLabel.setText(Integer.toString(travelerIdReceived));
        		amountOfVisitorsLabel.setText(Integer.toString(amountOfVisitorsReceived));
        		dateLabel.setText(dateReceived.toString());
        		visitTimeLabel.setText(timeReceived.toString());
        		orderStatusLabel.setText(orderStatus);
        		
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
    	System.out.print(orderToEnterOrExit);
    	if(orderToEnterOrExit != null) {

    		
    		//Checks if the order wasn't already entered to the park
    		if(orderToEnterOrExit.getOrderStatus() == "NOTARRIVED") {
    			try {
    				
	    			ClientServerMessage changeStateOfOrder = new ClientServerMessage(orderToEnterOrExit, Operation.PATCH_ORDER_STATUS_TO_INPARK);
		        	ClientUI.clientControllerInstance.sendMessageToServer(changeStateOfOrder);
	    			
		        	//Get park details from server
	    			ClientServerMessage getParkDetails = new ClientServerMessage(orderToEnterOrExit.getParkNumber(), Operation.GET_PARK_DETAILS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(getParkDetails);
		        	
	    			//Change Park amount of visitors
		        	Park receivedParkInformationFromServer = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
		        	Integer visitorsWithAppendedWelcomedVisitors = receivedParkInformationFromServer.getCurrentVisitors() + orderToEnterOrExit.getAmountOfVisitors();
		        	receivedParkInformationFromServer.setCurrentVisitors(visitorsWithAppendedWelcomedVisitors);
		        	
	    			ClientServerMessage changeAmountOfVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_VISITORS_APPEND);
	    			ClientUI.clientControllerInstance.sendMessageToServer(changeAmountOfVisitors);
	    			
	    			//Create a new visit in the visit table
	    			LocalDate currentDate = orderToEnterOrExit.getDate();
	    			LocalTime timeOfEnter = orderToEnterOrExit.getVisitTime();
	    			LocalTime timeOfExit = orderToEnterOrExit.getVisitTime();
	    			Integer parkNumberOfVisit = orderToEnterOrExit.getParkNumber();
	    			Integer orderIdOfVisit = orderToEnterOrExit.getOrderId();
	    			
	    			//Post the visit to the db
	    			Visit createVisitForEntrence = new Visit(currentDate, timeOfEnter, timeOfExit, parkNumberOfVisit, orderIdOfVisit);
	    			ClientServerMessage addNewVisitMessage = new ClientServerMessage(createVisitForEntrence ,Operation.POST_NEW_VISIT);
	    			ClientUI.clientControllerInstance.sendMessageToServer(addNewVisitMessage);
	    			
    			}catch (Exception e) {
    				Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong with the entrence system");
        			somethingWentWrong.showAndWait();
    			}

    		}else {
        		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Order was already fulfilled");
    			somethingWentWrong.showAndWait();
    		}
    		
    		
    		
    		
    	}else {
    		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Please insert order to submit to park entrence system");
			somethingWentWrong.showAndWait();
    	}
    }
    
    
}
