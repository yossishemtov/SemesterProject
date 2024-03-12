package server;

import common.*;
import common.worker.GeneralParkWorker;
import common.worker.ParkWorker;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandlerFromClient {

	public static void handleMessage(ClientServerMessage messageFromClient, ConnectionToClient client) throws IOException {
		// A class that is intended to handle diffrent messages from the client and
		// response accordingly
        
    	//client.sendToClient("aaa");
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
		String command = (String)messageFromClient.getCommand();
		
        System.out.println("opertion "+command);

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

		// get all traveler orders from data base
		case Operation.GET_ALL_ORDERS:
			// Placeholder for getting all orders from the database
			Traveler dataTraveler = (Traveler) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getOrdersDataFromDatabase(dataTraveler));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_TRAVLER_INFO:
			// Placeholder for getting traveler information
			break;

		case Operation.GET_GENERAL_PARK_WORKER_DETAILS:
			GeneralParkWorker generalParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getGeneralParkWorkerDetails(generalParkWorker));
		    System.out.println(messageFromClient.toString());
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_ALL_REPORTS:
			// Placeholder for getting all reports
			break;

		case Operation.GET_VISITORS_REPORT:
			GeneralParkWorker worker=(GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getTotalNumberOfVisitorsReport(worker));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_MESSAGES:
			// Placeholder for getting messages
			break;
			
		case Operation.GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER:
			GeneralParkWorker loggedInParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getAmountOfVisitorsByParkWorker(loggedInParkWorker));
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
			ArrayList<GroupGuide> groupGuide = (ArrayList<GroupGuide>) messageFromClient.getDataTransfered();

			// send to data base group guide to insert
			dbControllerInstance.addNewGroupGuide(groupGuide.get(0));
			// if the insert success ,send to client true
			messageFromClient.setDataTransfered(true);
			client.sendToClient(messageFromClient);

			break;
		 
		case Operation.GET_GENERALPARKWORKER_SIGNED:
			//Get the status of isloggedin of generalparkworker
			GeneralParkWorker checkStatusOfParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			
			if(dbControllerInstance.getSignedinStatusOfGeneralParkWorker(checkStatusOfParkWorker)) {
				messageFromClient.setflagTrue();
			}else {
				messageFromClient.setflagFalse();
			}
			
			client.sendToClient(messageFromClient);
			
			break;
			
		case Operation.PATCH_GENERALPARKWORKER_SIGNEDIN:
			//Change a worker to signedin status
				
			try {
				GeneralParkWorker changeStatusOfParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
				
				if(dbControllerInstance.changeSingedInOfGeneralParkWorker(changeStatusOfParkWorker)) {
					//If changing the status of worker was successful sets the flag of the message back to the client to true
					messageFromClient.setflagTrue();
				}
				else {
					messageFromClient.setflagFalse();
				}
				
				client.sendToClient(messageFromClient);
				
			}catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
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

			break;

		case Operation.POST_NEW_REPORT:
			// Placeholder for posting a new report
			break;

		case Operation.PATCH_PARK_PARAMETERS:
			// Placeholder for patching park parameters
			ArrayList<Park> park = (ArrayList<Park>) messageFromClient.getDataTransfered();
			if (dbControllerInstance.patchParkParameters(park.get(0))) {
				messageFromClient.setflagTrue();
			} else {
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
