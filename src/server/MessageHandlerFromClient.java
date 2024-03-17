package server;

import common.*;
import common.worker.ChangeRequest;
import common.worker.GeneralParkWorker;
import common.worker.ParkWorker;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.xdevapi.Client;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandlerFromClient {

	public static void handleMessage(ClientServerMessage messageFromClient, ConnectionToClient client)
			throws IOException {
		// A class that is intended to handle diffrent messages from the client and
		// response accordingly

		// client.sendToClient("aaa");
		BackEndServer backEndServerInstance = BackEndServer.getBackEndServer();
		DatabaseController dbControllerInstance = BackEndServer.DBController;

		// Checking if message is of type of generic message intended for client and
		// server communication
		if (!(messageFromClient instanceof ClientServerMessage)) {
			try {
				System.out.println("return null");

				client.sendToClient(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// Extracting data from the generic message object intended for further parsing
		String command = (String) messageFromClient.getCommand();

		System.out.println("opertion " + command);

		// Parsing the command
		switch (command) {

		// User sends a disconnect command to the server
		case Operation.DISCONNECTING:
			backEndServerInstance.clientDisconnected(client);

			try {
				client.sendToClient(Operation.DISCONNECTING); // Send acknowledgment to client
			} catch (IOException e) {
				System.out.println("Error sending ack_disconnect to client: " + e.getMessage());
			}
			break;

		case Operation.GET_PARK_DETAILS:
			// Placeholder for getting all orders from the database
			Integer parkId = (Integer) messageFromClient.getDataTransfered();
			Park parkDetails = dbControllerInstance.getParkDetails(parkId);

			if (parkDetails == null) {
				// Handle the case where no park was found with the provided ID
				// You could set the data transferred to a specific value or an error message
				// indicating the park was not found
				messageFromClient.setflagFalse(); // Assuming you have a way to indicate success/failure
			} else {
				// Park was found, proceed as normal
				messageFromClient.setDataTransfered(parkDetails);
				messageFromClient.setflagTrue(); // Again, assuming success/failure indication
			}
			client.sendToClient(messageFromClient);
			break;

		// get all traveler orders from data base
		case Operation.GET_ALL_ORDERS:
			// Placeholder for getting all orders from the database
			Traveler dataTraveler = (Traveler) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getOrdersDataFromDatabase(dataTraveler));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_TRAVLER_INFO:
			// Placeholder for getting traveler information
			Traveler traveler = (Traveler) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getTravelerDetails(traveler));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_GENERAL_PARK_WORKER_DETAILS:
			GeneralParkWorker generalParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getGeneralParkWorkerDetails(generalParkWorker));

			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_ALL_REPORTS:
			// Placeholder for getting all reports
			break;

		case Operation.GET_NEW_VISITORS_REPORT:
			VisitorsReport NewVisitorReportFromClient = (VisitorsReport) messageFromClient.getDataTransfered();
		
			NewVisitorReportFromClient=dbControllerInstance.getNewVisitorsReport(NewVisitorReportFromClient);
			System.out.println("return from");
			if (NewVisitorReportFromClient != null)
			{
				messageFromClient.setDataTransfered(NewVisitorReportFromClient);
				messageFromClient.setflagTrue();
				
			}
			else
			{
				messageFromClient.setflagFalse();
			}
			
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_MESSAGES:
			// Placeholder for getting messages
			break;
		
		case Operation.GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE:
			//Get order information by orderId
			Order orderInformation = (Order) messageFromClient.getDataTransfered();
			Order receivedOrderInformationFromDb = dbControllerInstance.getOrderInformationByOrderIdAndParkNumber(orderInformation);
			
			messageFromClient.setDataTransfered(receivedOrderInformationFromDb);
			
			client.sendToClient(messageFromClient);
			
			break;
		
		case Operation.GET_TRAVELER_SIGNED:
			//Get status if traveler is already signedin
			Traveler checkStatusOfTraveler = (Traveler) messageFromClient.getDataTransfered();
			
			if(dbControllerInstance.getSignedinStatusOfTraveler(checkStatusOfTraveler)) {
				messageFromClient.setflagTrue();
			}else {
				messageFromClient.setflagFalse();
			}
			
			client.sendToClient(messageFromClient);
			
			
			break;
			
		case Operation.POST_VISITORS_REPORT:
	        System.out.println("in opertion insert...");

			
	        VisitorsReport visitorReportFromClient = (VisitorsReport) messageFromClient.getDataTransfered();
	        System.out.println("in opertion insert 11...");

	        if (dbControllerInstance.insertVisitorReport(visitorReportFromClient)) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}

			client.sendToClient(messageFromClient);
			break;
			
		case Operation.GET_GENERAL_REPORT:
			Integer park = (Integer) messageFromClient.getDataTransfered();
			ArrayList<Report> listGeneralReport = (ArrayList<Report>) dbControllerInstance
					.getGeneralReportsByParkId(park);
			if (listGeneralReport != null) {
				messageFromClient.setDataTransfered(listGeneralReport);
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}

			client.sendToClient(messageFromClient);
			break;
		case Operation.GET_EXISTS_VISITORS_REPORT:
			Report reportFromClient = (Report) messageFromClient.getDataTransfered();
			VisitorsReport VisitReport = (VisitorsReport) dbControllerInstance
					.getVisitorsReportByReportId(reportFromClient);
			if (VisitReport != null) {
				messageFromClient.setDataTransfered(VisitReport);
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}

			client.sendToClient(messageFromClient);
			break;
			

		case Operation.GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER:
			GeneralParkWorker loggedInParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient
					.setDataTransfered(dbControllerInstance.getAmountOfVisitorsByParkWorker(loggedInParkWorker));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_AMOUNT_OF_VISITORS:
			// Placeholder for getting the amount of visitors
			GeneralParkWorker park_Check_AmountVisitors = (GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getAmountOfVisitors(park_Check_AmountVisitors));
			client.sendToClient(messageFromClient);
			break;

		case Operation.POST_NEW_TRAVLER_GUIDER:
			// Placeholder for posting a new traveler guide request
			Traveler groupGuide = (Traveler) messageFromClient.getDataTransfered();

			// send to data base group guide to insert
			if (dbControllerInstance.insertNewGroupGuide(groupGuide)) {
				messageFromClient.setflagTrue();

			} else {
				messageFromClient.setflagFalse();

			}

			client.sendToClient(messageFromClient);

			break;

		case Operation.GET_GENERALPARKWORKER_SIGNED:
			// Get the status of isloggedin of generalparkworker
			GeneralParkWorker checkStatusOfParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();

			if (dbControllerInstance.getSignedinStatusOfGeneralParkWorker(checkStatusOfParkWorker)) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}

			client.sendToClient(messageFromClient);

			break;

		case Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT:
			// Signing out a GeneralParkWorker
			GeneralParkWorker parkWorkerToSignOut = (GeneralParkWorker) messageFromClient.getDataTransfered();

			try {

				if (dbControllerInstance.changeSignedOutOfGeneralParkWorker(parkWorkerToSignOut)) {
					messageFromClient.setflagTrue();
				} else {
					messageFromClient.setflagFalse();
				}

				client.sendToClient(messageFromClient);

			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			break;

		case Operation.PATCH_GENERALPARKWORKER_SIGNEDIN:
			// Signing in a GeneralParkWorker

			try {
				GeneralParkWorker parkWorkerToSignIn = (GeneralParkWorker) messageFromClient.getDataTransfered();

				if (dbControllerInstance.changeSingedInOfGeneralParkWorker(parkWorkerToSignIn)) {
					// If changing the status of worker was successful sets the flag of the message
					// back to the client to true
					messageFromClient.setflagTrue();
				} else {
					messageFromClient.setflagFalse();
				}

				client.sendToClient(messageFromClient);

			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			break;
			

		case Operation.POST_EXISTS_TRAVLER_GUIDER:
			// Placeholder for posting a new traveler guide request
			Traveler ExistsgroupGuide = (Traveler) messageFromClient.getDataTransfered();

			// send to data base group guide to insert
			if (dbControllerInstance.ChangeTravelerToGuide(ExistsgroupGuide)) {
				messageFromClient.setflagTrue();

			} else {
				messageFromClient.setflagFalse();

			}

			client.sendToClient(messageFromClient);

			break;
		
		case Operation.PATCH_TRAVELER_SIGNEDIN:
			//Changing state of traveler to signedin
			
			try {
				Traveler travelerToSignIn = (Traveler) messageFromClient.getDataTransfered();
				
				if(dbControllerInstance.changedSignedInOfTraveler(travelerToSignIn)) {
					//If changing the status of traveler was successful sets the flag of the message back to the client to true
					messageFromClient.setflagTrue();
				}
				else {
					messageFromClient.setflagFalse();
				}

			}catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			
			client.sendToClient(messageFromClient);
			break;
			
		case Operation.PATCH_TRAVELER_SIGNEDOUT:
			//Signing out a GeneralParkWorker
			Traveler travelerToSignOut = (Traveler) messageFromClient.getDataTransfered();
			
			try {
				
				if(dbControllerInstance.changedSignedOutOfTraveler(travelerToSignOut)) {
					messageFromClient.setflagTrue();
				}else {
					messageFromClient.setflagFalse();
				}
				
				
			}catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			client.sendToClient(messageFromClient);
			
			break;
			
		case Operation.POST_NEW_VISIT:
			//Post a new visit to the database
			Visit visitOfPark = (Visit) messageFromClient.getDataTransfered();
			
			try {				
				Boolean isSuccessful = dbControllerInstance.addNewVisit(visitOfPark);
				
				if(isSuccessful) {
					messageFromClient.setflagTrue();
				} else {
					messageFromClient.setflagFalse();

				}
				
			}catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			client.sendToClient(messageFromClient);
			break;
			
			
		case Operation.POST_TRAVLER_ORDER:
			// Placeholder for posting a new traveler order

			try {
				ArrayList<Order> travelerOrder = (ArrayList<Order>) messageFromClient.getDataTransfered();

				if (dbControllerInstance.insertTravelerOrder((Order) travelerOrder.get(0))) {
					messageFromClient.setflagTrue();

				} else {
					messageFromClient.setflagFalse();

				}
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			client.sendToClient(messageFromClient);

			break;

		case Operation.POST_NEW_REPORT:
			// Placeholder for posting a new report
			break;

		case Operation.PATCH_PARK_PARAMETERS:
			// Placeholder for patching park parameters
			ChangeRequest request = (ChangeRequest) messageFromClient.getDataTransfered();
			if (dbControllerInstance.patchParkParameters(request)) {
				System.out.println("set true");

				messageFromClient.setflagTrue();
			} else {
				System.out.println("set flase");

				messageFromClient.setflagFalse();

			}

			client.sendToClient(messageFromClient);

			break;

		case Operation.PATCH_ORDER_STATUS:
			// Placeholder for patching order status

			ArrayList<Order> travelerorder = (ArrayList<Order>) messageFromClient.getDataTransfered();

			if (dbControllerInstance.updateOrderStatus((Order) travelerorder.get(0))) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();

			}

			client.sendToClient(messageFromClient);

			break;

		case Operation.DELETE_EXISTING_ORDER:
			Order orderToDelete = ((ArrayList<Order>) messageFromClient.getDataTransfered()).get(0);

			if (dbControllerInstance.deleteOrder(orderToDelete.getOrderId())) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();

			}

			client.sendToClient(messageFromClient);

			break;

		case Operation.POST_NEW_CHANGE_REQUEST:
			// Placeholder for posting a new change request
			System.out.println("in POST_NEW_CHANGE_REQUEST");

			ChangeRequest newChangeRequest = (ChangeRequest) messageFromClient.getDataTransfered();
			if (dbControllerInstance.insertChangeRequest(newChangeRequest)) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_CHANGE_REQUESTS:
			// Placeholder for getting change requests waiting for approval
			System.out.println("in " + command);
			GeneralParkWorker parkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			ArrayList<ChangeRequest> waitingForApprovalRequests = dbControllerInstance
					.getChangeRequestsWaitingForApproval(parkWorker);
			// System.out.println(waitingForApprovalRequests);
			System.out.println("out " + command);
			if (waitingForApprovalRequests != null) {
				System.out.println(waitingForApprovalRequests.get(0));

				messageFromClient.setflagTrue();
				messageFromClient.setDataTransfered(waitingForApprovalRequests);
			} else {
				messageFromClient.setflagFalse(); // Indicate no requests found or an error occurred
			}
			client.sendToClient(messageFromClient);
			break;

		case Operation.PATCH_CHANGE_REQUEST_STATUS:
			// Placeholder for updating the status of a change request
			ChangeRequest changeRequestToUpdate = (ChangeRequest) messageFromClient.getDataTransfered();
			if (dbControllerInstance.updateChangeRequestStatus(changeRequestToUpdate)) {
				messageFromClient.setflagTrue();
			} else {
				messageFromClient.setflagFalse();
			}
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_HOURLY_VISIT_DATA_FOR_PARK:
			// Placeholder for updating the status of a change request
			Integer parkID = (Integer) messageFromClient.getDataTransfered();
			ArrayList<HourlyVisitData> listHoyrVisit = (ArrayList<HourlyVisitData>) dbControllerInstance
					.getHourlyVisitDataForPark(parkID);
			if (listHoyrVisit != null) {
				messageFromClient.setflagTrue();
				messageFromClient.setDataTransfered(listHoyrVisit);

			} else {
				messageFromClient.setflagFalse();
			}
			client.sendToClient(messageFromClient);
			break;

		case Operation.PATCH_ORDER_STATUS_TO_INPARK:
			//Changes the state of an order to INPARK
				Order orderInformationToChangeStatus = (Order) messageFromClient.getDataTransfered();
				Boolean receivedOrderInformationToChangeStatusFromDb = dbControllerInstance.patchOrderStatusToInpark(orderInformationToChangeStatus);

				if(receivedOrderInformationToChangeStatusFromDb) {
					messageFromClient.setflagTrue();
				}else {
					messageFromClient.setflagFalse();
				}
				
				client.sendToClient(messageFromClient);
			break;
			
		case Operation.PATCH_PARK_VISITORS_APPEND:
			//Append the number of visitors to the park
			Park parkToAppendVisitors = (Park) messageFromClient.getDataTransfered();
			
			Boolean isChangesAmountSucessful = dbControllerInstance.patchParkVisitorsNumberAppend(parkToAppendVisitors);
			
			if(isChangesAmountSucessful) {
				messageFromClient.setflagTrue();
			}else {
				messageFromClient.setflagFalse();
			}
			
			client.sendToClient(messageFromClient);
			break;
		    
		default:
			System.out.println("default");
			try {
				client.sendToClient("end");
			} catch (IOException e) {
				System.out.println("Error sending unknown request response to client: " + e.getMessage());
			}
			break;
		}
	}
}
