package server;

import common.*;
import common.worker.GeneralParkWorker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandlerFromClient {

	public static void handleMessage(Object msg, ConnectionToClient client) throws IOException {
		// A class that is intended to handle diffrent messages from the client and
		// response accordingly
		BackEndServer backEndServerInstance = BackEndServer.getBackEndServer();
		DatabaseController dbcontroller = backEndServerInstance.DBController;

		// Checking if message is of type of generic message intended for client and
		// server communication
		if (!(msg instanceof ClientServerMessage)) {
			try {
				client.sendToClient(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// Extracting data from the generic message object intended for further parsing
		ClientServerMessage message = (ClientServerMessage) msg;
		String command = message.getCommand();
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
			Traveler dataTraveler = ((ArrayList<Traveler>) message.getDataTransfered()).get(0);
			message.setDataTransfered(dbcontroller.getOrdersDataFromDatabase(dataTraveler));
			client.sendToClient(message);
			break;

		case Operation.GET_TRAVLER_INFO:
			// Placeholder for getting traveler information
			break;

		case Operation.GET_GENERAL_PARK_WORKER_DETAILS:
			ArrayList<GeneralParkWorker> generalParkWorker = (ArrayList<GeneralParkWorker>) message.getDataTransfered();
			message.setDataTransfered(dbcontroller.getGeneralParkWorkerDetails(generalParkWorker.get(0)));
			client.sendToClient(message);
			break;

		case Operation.GET_ALL_REPORTS:
			// Placeholder for getting all reports
			break;

		case Operation.GET_VISITORS_REPORT:
			message.setDataTransfered(dbcontroller.getTotalNumberOfVisitorsReport());
			client.sendToClient(message);
			break;

		case Operation.GET_MESSAGES:
			// Placeholder for getting messages
			break;

		case Operation.GET_AMOUNT_OF_VISITORS:
			// Placeholder for getting the amount of visitors
			ArrayList<Park> park_Check_AmountVisitors = (ArrayList<Park>) message.getDataTransfered();
			message.setDataTransfered(dbcontroller.getAmountOfVisitors(park_Check_AmountVisitors.get(0)));
			client.sendToClient(message);
			break;

		case Operation.POST_NEW_TRAVLER_GUIDER:
			// Placeholder for posting a new traveler guide request
			ArrayList<GroupGuide> groupGuide = (ArrayList<GroupGuide>) message.getDataTransfered();

			// send to data base group guide to insert
			dbcontroller.addNewGroupGuide(groupGuide.get(0));
			// if the insert success ,send to client true
			message.setDataTransfered(true);
			client.sendToClient(message);

			break;

		case Operation.POST_TRAVLER_ORDER:
			// Placeholder for posting a new traveler order

			try {
				ArrayList<Order> travelerOrder = (ArrayList<Order>) message.getDataTransfered();

				if (dbcontroller.insertTravelerOrder((Order) travelerOrder.get(0))) {
					message.setflagTrue();

				} else {
					message.setflagFalse();

				}
			} catch (IndexOutOfBoundsException e) {

			}

			break;

		case Operation.POST_NEW_REPORT:
			// Placeholder for posting a new report
			break;

		case Operation.PATCH_PARK_PARAMETERS:
			// Placeholder for patching park parameters
			ArrayList<Park> park = (ArrayList<Park>) message.getDataTransfered();
			if (dbcontroller.patchParkParameters(park.get(0))) {
				message.setflagTrue();
			} else {
				message.setflagFalse();

			}

			client.sendToClient(message);

			break;

		case Operation.PATCH_ORDER_STATUS:
			// Placeholder for patching order status

			ArrayList<Order> travelerorder = (ArrayList<Order>) message.getDataTransfered();

			if (dbcontroller.updateOrderStatus((Order) travelerorder.get(0))) {
				message.setflagTrue();
			} else {
				message.setflagFalse();

			}

			client.sendToClient(message);

			break;

		case Operation.DELETE_EXISTING_ORDER:
			Order orderToDelete = ((ArrayList<Order>) message.getDataTransfered()).get(0);

			if (dbcontroller.deleteOrder(orderToDelete.getOrderId())) {
				message.setflagTrue();
			} else {
				message.setflagFalse();

			}

			client.sendToClient(message);

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
