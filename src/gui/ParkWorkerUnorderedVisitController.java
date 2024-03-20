package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;


public class ParkWorkerUnorderedVisitController implements Initializable{
	
	@FXML
    private Label allowedVisitorsLabel;

    @FXML
    private JFXTextField travelerIdField;

    @FXML
    private JFXTextField amountOfVisitorsField;

    @FXML
    private JFXTextField visitorEmailField;

    @FXML
    private JFXTextField phoneNumberField;

    @FXML
    private SplitMenuButton menuField;

    @FXML
    private MenuItem familyMenuFeild;

    @FXML
    private MenuItem soloMenuField;

    @FXML
    private Label priceLabel;
    
    private static Park currentParkDetails;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize the display of amount of allowed unordered visits 
		
		//Receive amount of allowed unordered visits
		Integer currentPark = Usermanager.getCurrentWorker().getWorksAtPark();
		ClientServerMessage getParkAllowedUnorderedVists = new ClientServerMessage(currentPark, Operation.GET_PARK_UNORDEREDVISITS);
		ClientUI.clientControllerInstance.sendMessageToServer(getParkAllowedUnorderedVists);
		
		//Received allowedUnorderedVists from the server
		Integer allowedUnorderedVists = (Integer) ClientUI.clientControllerInstance.getData().getDataTransfered();
		allowedVisitorsLabel.setText(Integer.toString(allowedUnorderedVists));
		
		//Get park details (needed to calculate cost of entrance)
		ClientServerMessage parkVisitingDetails = new ClientServerMessage(currentPark, Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(parkVisitingDetails);
		
		//Received Park Information
		currentParkDetails = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
	}
	
	public void chooseEntrencePlan(ActionEvent click) throws Exception{
		//Calculate entrance price when choosing Type Of Order
		
		Integer amountOfVisitors = Integer.parseInt(amountOfVisitorsField.getText());
		Alerts visitorsValidate = InputValidation.validateFamilyVisitors(amountOfVisitors.toString());

		
	}
	
	
	public void orderButtonAction (ActionEvent click) throws Exception{
		
		
		
		
		//Parse Information about the order
		Integer travelerId = Integer.parseInt(travelerIdField.getText());
		Integer amountOfVisitors = Integer.parseInt(amountOfVisitorsField.getText());
		String visitorEmail = (visitorEmailField.getText());
		String phoneNumber = (phoneNumberField.getText());
		String typeOfOrder = (menuField.getText());
		Float price = Float.parseFloat(priceLabel.getText());
		Integer parkNumber = currentParkDetails.getParkNumber();
		LocalDate today = LocalDate.now();
		LocalTime timeNow = LocalTime.now();
		String orderStatus = "UNORDERED";
		
		
		//Need to Implement checks
		Alerts alertID = InputValidation.ValidateVisitorID(travelerId.toString()); 
		Alerts visitorsValidate = InputValidation.validateFamilyVisitors(amountOfVisitors.toString());
		Alerts emailValidation = InputValidation.validateEmail(visitorEmail);
		Alerts validPhoneNuber = InputValidation.validatePhoneNumber(phoneNumber);
		
		//Calculate price according to number of visitors
		
		//validates id of traveler
		if(alertID.getAlertType().toString().equals("INFORMATION")) {
			
			//Validates amount of visitors
			if(visitorsValidate.getAlertType().toString().equals("INFORMATION")) {
				
				//Validate email address
				if(emailValidation.getAlertType().toString().equals("INFORMATION")) {
					
					//Validate phone number
					if(validPhoneNuber.getAlertType().toString().equals("INFORMATION")) {
						
						
						//Creating new order
						Float calculatedPrice = (float) (100*amountOfVisitors);
						Order newUnorderedOrder = new Order(null, travelerId, currentParkDetails.getParkNumber(), amountOfVisitors, calculatedPrice,
								visitorEmail,LocalDate.now(), LocalTime.now(), orderStatus, typeOfOrder, phoneNumber,null);
						
						ArrayList<Order> unorderedArrayList = new ArrayList<Order>();
						unorderedArrayList.add(newUnorderedOrder);
						
						//Posting new order in the server
						ClientServerMessage postUnorderedorder = new ClientServerMessage(unorderedArrayList, Operation.POST_TRAVLER_ORDER);
						ClientUI.clientControllerInstance.sendMessageToServer(postUnorderedorder);
						
						//Post new visit in visits table
						createAndPostToServerVisitForUnordered(newUnorderedOrder);
						
						if(ClientUI.clientControllerInstance.getData().getFlag() == true) {
							//If process was successful
							Alerts succeedRegistration = new Alerts(Alert.AlertType.CONFIRMATION, "Succeed to registrate","","Succeed to registrate");
					    	 succeedRegistration.showAndWait();
						}else {
							//If posting visit wasn't successful
							Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Could not post a visit for the unordered visit");
							somethingWentWrong.showAndWait();
						}
						
						
						if(ClientUI.clientControllerInstance.getData().getFlag() == false) {
							//Error while posting order in database
							Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Order is not in state INPARK");
			    			somethingWentWrong.showAndWait();
						}else {
							//Success message to screen
							Alerts somethingWentWrong = new Alerts(Alerts.AlertType.CONFIRMATION, "Success","", "Thank you for visiting our park :)");
			    			somethingWentWrong.showAndWait();
						}
						
						
					}else {
						validPhoneNuber.showAndWait();
					}
					
				}else {
					emailValidation.showAndWait();
				}
				
			}else {
				visitorsValidate.showAndWait();
			}
		}else {
			alertID.showAndWait();
		}
	}
	
	public void createAndPostToServerVisitForUnordered(Order unorderedOrder) {
		//Create a new visit in the visit table
		LocalDate currentDate = unorderedOrder.getDate();
		LocalTime timeOfEnter = unorderedOrder.getVisitTime();
		LocalTime timeOfExit = unorderedOrder.getVisitTime();
		Integer parkNumberOfVisit = unorderedOrder.getParkNumber();
		Integer orderIdOfVisit = unorderedOrder.getOrderId();
		
		//Post the visit to the db
		Visit createVisitForEntrence = new Visit(currentDate, timeOfEnter, timeOfExit, parkNumberOfVisit, orderIdOfVisit);
		ClientServerMessage addNewVisitMessage = new ClientServerMessage(createVisitForEntrence ,Operation.POST_NEW_VISIT);
		ClientUI.clientControllerInstance.sendMessageToServer(addNewVisitMessage);
	}
    
    
}
