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
import common.Traveler;
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
    
    @FXML
    private JFXTextField firstNameField;

    @FXML
    private JFXTextField lastNameField;

    
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
	
	public boolean chooseEntrencePlan(ActionEvent click) throws Exception{
		
		try {
				
			//Calculate entrance price when choosing Type Of Order
			Integer amountOfVisitors = Integer.parseInt(amountOfVisitorsField.getText());
			Alerts visitorsValidate = InputValidation.validateFamilyVisitors(amountOfVisitors.toString());
			
			//If amount of visitors is not between 1-6
			if(visitorsValidate.getAlertType().toString().equals("INFORMATION")) {
				//Renders the calculated price into screen
				Float calculatedPrice = (float) (100*amountOfVisitors);
	
				priceLabel.setText(calculatedPrice.toString());
				return true;
			}else {
				visitorsValidate.showAndWait();
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Please enter valid amount of visitors");
			somethingWentWrong.showAndWait();
		}
		
		return false;
		
	}
	
	
	public void orderButtonAction (ActionEvent click) throws Exception{
				
		try {
			//Parse Information about the order
			String firstNameVisitor = firstNameField.getText();
			String lastNameVisitor = lastNameField.getText();
			Integer travelerId = Integer.parseInt(travelerIdField.getText());
			Integer amountOfVisitors = Integer.parseInt(amountOfVisitorsField.getText());
			String visitorEmail = (visitorEmailField.getText());
			String phoneNumber = (phoneNumberField.getText());
			String typeOfOrder = (menuField.getText());
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
							
							//Post unordered visit as a traveler in the system
							
							//Check if Traveler already exists in the system
							Traveler checkTravelerInformation = new Traveler(travelerId, firstNameVisitor, lastNameVisitor, visitorEmail, phoneNumber, 0, 0);
							ClientServerMessage checkIfTravelerExists = new ClientServerMessage(checkTravelerInformation ,Operation.GET_TRAVLER_INFO);
							ClientUI.clientControllerInstance.sendMessageToServer(checkIfTravelerExists);

							
							//If traveler does not exist, register him
							if(ClientUI.clientControllerInstance.getData().getDataTransfered() == null) {
								ClientServerMessage sendRegistrationRequestToServer = new ClientServerMessage(checkTravelerInformation ,Operation.POST_NEW_TRAVLER);
								ClientUI.clientControllerInstance.sendMessageToServer(sendRegistrationRequestToServer);
								
							}

							
							//Receive last order number from db
							ClientServerMessage lastOrderFromServMessage = new ClientServerMessage(null ,Operation.GET_LAST_ORDER_ID);
							ClientUI.clientControllerInstance.sendMessageToServer(lastOrderFromServMessage);
							Integer lastOrderNumber = (Integer) ClientUI.clientControllerInstance.getData().getDataTransfered() + 1;
							
							//Creating new order
							Float calculatedPrice = (float) (100*amountOfVisitors);
							Order newUnorderedOrder = new Order(lastOrderNumber, travelerId, currentParkDetails.getParkNumber(), amountOfVisitors, calculatedPrice,
									visitorEmail,LocalDate.now(), LocalTime.now(), orderStatus, typeOfOrder, phoneNumber,null);
							
							//Posting new order in the server
							ClientServerMessage postUnorderedorder = new ClientServerMessage(newUnorderedOrder, Operation.POST_TRAVLER_ORDER);
							ClientUI.clientControllerInstance.sendMessageToServer(postUnorderedorder);
							
							//Post new visit in visits table
							createAndPostToServerVisitForUnordered(newUnorderedOrder);
							
							if(ClientUI.clientControllerInstance.getData().getFlag() == true) {
								
								//Append visits to park and deduct from unordered visits park column
								AppendUnorderedVisitsAndVisitorsToPark(newUnorderedOrder);
								
								//If process was successful
								Alerts succeedRegistration = new Alerts(Alert.AlertType.CONFIRMATION, "Succeed to registrate","","Succeed to registrate");
						    	 succeedRegistration.showAndWait();
							}else {
								//If posting visit wasn't successful
								Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Could not post a visit for the unordered visit");
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
			
		}catch (Exception e) {
			e.printStackTrace();
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong with the Unordered Vists System");
			somethingWentWrong.showAndWait();
		}
		
	}
	
	//Create a new visit in the visit table
	public void createAndPostToServerVisitForUnordered(Order unorderedOrder) {
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
	
	//deduct number of visitors to the unordered visits column and append to overall park visitors
	public void AppendUnorderedVisitsAndVisitorsToPark(Order orderOfUnorderedVisit) {
		//Receive park information
		ClientServerMessage getParkDetails = new ClientServerMessage(orderOfUnorderedVisit.getParkNumber(), Operation.GET_PARK_DETAILS);
		ClientUI.clientControllerInstance.sendMessageToServer(getParkDetails);
		Park receivedParkInformationFromServer = (Park) ClientUI.clientControllerInstance.getData().getDataTransfered();
		
		
		//Append amount of visitors from park
		//Change Park amount of visitors
    	Integer visitorsWithAppendedWelcomedVisitors = receivedParkInformationFromServer.getCurrentVisitors() + orderOfUnorderedVisit.getAmountOfVisitors();
    	receivedParkInformationFromServer.setCurrentVisitors(visitorsWithAppendedWelcomedVisitors);
    	
    	
    	//Change the allowed unordered visits in the park
    	Integer newUnorderedVisits = receivedParkInformationFromServer.getUnorderedVisits() - orderOfUnorderedVisit.getAmountOfVisitors();
		receivedParkInformationFromServer.setUnorderedVisits(newUnorderedVisits);
		
		//Send the change of allowed unordered visits in the park to the db
		ClientServerMessage changeParkUnorderedVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_UNORDEREDVISITS);
		ClientUI.clientControllerInstance.sendMessageToServer(changeParkUnorderedVisitors);
		
		//Change the amount of visitors in the park
		ClientServerMessage changeAmountOfVisitors = new ClientServerMessage(receivedParkInformationFromServer, Operation.PATCH_PARK_VISITORS);
		ClientUI.clientControllerInstance.sendMessageToServer(changeAmountOfVisitors);
	}
    
    
}
