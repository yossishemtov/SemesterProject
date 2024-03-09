package server;

import common.*;
import common.worker.GeneralParkWorker;
import common.worker.ParkWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandlerFromClient {

	public static void handleMessage(ClientServerMessage messageFromClient, ConnectionToClient client) throws IOException {
		// A class that is intended to handle diffrent messages from the client and
		// response accordingly
		BackEndServer backEndServerInstance = BackEndServer.getBackEndServer();
		DatabaseController dbControllerInstance = backEndServerInstance.DBController;

		// Checking if message is of type of generic message intended for client and
		// server communication
		if (!(messageFromClient instanceof ClientServerMessage)) {
			try {
				client.sendToClient(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// Extracting data from the generic message object intended for further parsing
		String command = messageFromClient.getCommand();
		Object result;
		Object dataForClient;
		Object orderId;

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
			Traveler dataTraveler = ((ArrayList<Traveler>) messageFromClient.getDataTransfered()).get(0);
			messageFromClient.setDataTransfered(dbControllerInstance.getOrdersDataFromDatabase(dataTraveler));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_TRAVLER_INFO:
			// Placeholder for getting traveler information
			break;

		case Operation.GET_GENERAL_PARK_WORKER_DETAILS:
			ArrayList<GeneralParkWorker> generalParkWorker = (ArrayList<GeneralParkWorker>) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getGeneralParkWorkerDetails(generalParkWorker.get(0)));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_ALL_REPORTS:
			// Placeholder for getting all reports
			break;

		case Operation.GET_VISITORS_REPORT:
			messageFromClient.setDataTransfered(dbControllerInstance.getTotalNumberOfVisitorsReport());
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_MESSAGES:
			// Placeholder for getting messages
			break;
			
		case Operation.GET_AMOUNT_OF_VISITORS_FOR_PARKWORKER:
			ParkWorker loggedInParkWorker = (ParkWorker) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getAmountOfVisitorsByParkWorker(loggedInParkWorker));
			client.sendToClient(messageFromClient);
			break;

		case Operation.GET_AMOUNT_OF_VISITORS:
			// Placeholder for getting the amount of visitors
			ArrayList<Park> park_Check_AmountVisitors = (ArrayList<Park>) messageFromClient.getDataTransfered();
			messageFromClient.setDataTransfered(dbControllerInstance.getAmountOfVisitors(park_Check_AmountVisitors.get(0)));
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
			System.out.println("Received an unknown request: " + command);
			try {
				client.sendToClient("Unknown request: " + command);
			} catch (IOException e) {
				System.out.println("Error sending unknown request response to client: " + e.getMessage());
			}
			break;
		}
	}
}
