package gui;


import java.time.LocalDate;
import java.time.LocalTime;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import client.ClientUI;
import client.InputValidation;
import client.NavigationManager;
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
        	
        	
        	
     
        	if(checkIfNoError) {
        		//Renderes the order to the screen
        		renderInformationAboutOrder(Integer.parseInt(orderIdText));
	        	
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
    
    public void entrenceControlAction(ActionEvent click) {
    	System.out.print(orderToEnterOrExit);
    	if(orderToEnterOrExit != null) {

    		
    		//Checks if the order wasn't already entered to the park
    		if(orderToEnterOrExit.getOrderStatus().toString().equals("CONFIRMED")) {
    			try {
    				
	    			ClientServerMessage changeStateOfOrder = new ClientServerMessage(orderToEnterOrExit, Operation.PATCH_ORDER_STATUS_TO_INPARK);
		        	ClientUI.clientControllerInstance.sendMessageToServer(changeStateOfOrder);
	    			
		        	//Get park details from server
	    			ClientServerMessage getParkDetails = new ClientServerMessage(Usermanager.getCurrentWorker().getWorksAtPark(), Operation.GET_PARK_DETAILS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(getParkDetails);
		        	
	    			//Change Park amount of visitors
		        	Park receivedParkInformationFromServer = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
		        	Integer visitorsWithAppendedWelcomedVisitors = receivedParkInformationFromServer.getCurrentVisitors() + orderToEnterOrExit.getAmountOfVisitors();
		        	receivedParkInformationFromServer.setCurrentVisitors(visitorsWithAppendedWelcomedVisitors);
		        	
	    			ClientServerMessage changeAmountOfVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_VISITORS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(changeAmountOfVisitors);
	    			orderToEnterOrExit.setOrderType("INPARK");
	    			
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
	    			
	    			//Show Traveler bill
	    			NavigationManager.openPage("ParkWorkerShowBill.fxml", click, "Traveler Bill", false);
	    			
    			}catch (Exception e) {
    				e.printStackTrace();
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
    
    
    public void exitControlAction(ActionEvent click) {
    	
    	
    	//Registering exit to traveler
    	if(orderToEnterOrExit != null) {
	    	
    		if(orderToEnterOrExit.getOrderStatus() == "INPARK" || orderToEnterOrExit.getOrderStatus() == "UNORDERED" ) {    			
    			//Get park details from server
    			ClientServerMessage getParkDetails = new ClientServerMessage(orderToEnterOrExit.getParkNumber(), Operation.GET_PARK_DETAILS);
    			ClientUI.clientControllerInstance.sendMessageToServer(getParkDetails);
    			
    			
    			//Deduct amount of visitors from park
    			//Change Park amount of visitors
	        	Park receivedParkInformationFromServer = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
	        	Integer visitorsWithAppendedWelcomedVisitors = receivedParkInformationFromServer.getCurrentVisitors() - orderToEnterOrExit.getAmountOfVisitors();
	        	receivedParkInformationFromServer.setCurrentVisitors(visitorsWithAppendedWelcomedVisitors);
	        	
	        	//handles when Unordered visit has occured (append the visits to the unorderedvisits of a park)
	        	if(orderToEnterOrExit.getOrderStatus() == "UNORDERED") {
	        		Integer newUnorderedVisits = receivedParkInformationFromServer.getUnorderedVisits() + orderToEnterOrExit.getAmountOfVisitors();
	        		receivedParkInformationFromServer.setUnorderedVisits(newUnorderedVisits);
	    			ClientServerMessage changeParkUnorderedVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_UNORDEREDVISITS);
	    			ClientUI.clientControllerInstance.sendMessageToServer(changeParkUnorderedVisitors);
	        	}
	        	
	        	//Change state of Order to COMPLETED
    			ClientServerMessage changeStateCompleted = new ClientServerMessage(orderToEnterOrExit, Operation.PATCH_ORDER_STATUS_TO_COMPLETED);
    			ClientUI.clientControllerInstance.sendMessageToServer(changeStateCompleted);
	        	
    			//Change amount of visitors and unordered visitors for unordered visit of park
	        	ClientServerMessage changeAmountOfVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_VISITORS);
    			ClientUI.clientControllerInstance.sendMessageToServer(changeAmountOfVisitors);
    			
    			//Parse indormation about updated order to the screen
    			renderInformationAboutOrder((orderToEnterOrExit.getOrderId()));
	        	Alerts successExiting = new Alerts(Alerts.AlertType.CONFIRMATION, "Success","", "Thank you for visiting our park :)");
	        	successExiting.showAndWait();
    		}else {
    			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Order is not in state INPARK or UNORDERED");
    			somethingWentWrong.showAndWait();
    		}
    }else {
		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Please insert order to submit to park entrence system");
		somethingWentWrong.showAndWait();
		}
	  }
    
    public void renderInformationAboutOrder(Integer orderNumberToParse) {
    	//Renders an order based on an ordernumber
    	
    	//Get the park where the worker works at
    	GeneralParkWorker currentWorkerAccount = Usermanager.getCurrentWorker();
    	Integer currentWorkerPark = currentWorkerAccount.getWorksAtPark();
    	
    	//Send the orderId within an order dummy object to the server
    	Order dummyOrder = new Order(orderNumberToParse, null, currentWorkerPark, null, null, null, LocalDate.now(), null, null, null, null,null);
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
			//If no order exists for today or for the park
			Alerts noOrderIdExists = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "No such order exists for your park for today!");
			noOrderIdExists.showAndWait();
		}
    }
    
    
}
