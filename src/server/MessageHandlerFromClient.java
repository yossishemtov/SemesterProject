package server;

import common.*;
import common.worker.GeneralParkWorker;
import common.worker.ParkWorker;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

				try {
		        // Placeholder for checking full days in the park
				Integer parkToCheck = (Integer) messageFromClient.getDataTransfered();
		        
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.getParkDetails(parkToCheck));
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
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

		case Operation.GET_VISITORS_REPORT:
			GeneralParkWorker worker = (GeneralParkWorker) messageFromClient.getDataTransfered();
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
			//Get the status of isloggedin of generalparkworker
			GeneralParkWorker checkStatusOfParkWorker = (GeneralParkWorker) messageFromClient.getDataTransfered();
			
			if(dbControllerInstance.getSignedinStatusOfGeneralParkWorker(checkStatusOfParkWorker)) {
				messageFromClient.setflagTrue();
			}else {
				messageFromClient.setflagFalse();
			}
			
			client.sendToClient(messageFromClient);
			
			break;
			
		case Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT:
			//Signing out a GeneralParkWorker
			GeneralParkWorker parkWorkerToSignOut = (GeneralParkWorker) messageFromClient.getDataTransfered();
			
			try {
				
				if(dbControllerInstance.changeSignedOutOfGeneralParkWorker(parkWorkerToSignOut)) {
					messageFromClient.setflagTrue();
				}else {
					messageFromClient.setflagFalse();
				}
				
				client.sendToClient(messageFromClient);
				
			}catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			
			break;
			
		case Operation.PATCH_GENERALPARKWORKER_SIGNEDIN:
			//Signing in a GeneralParkWorker
				
			try {
				GeneralParkWorker parkWorkerToSignIn = (GeneralParkWorker) messageFromClient.getDataTransfered();
				
				if(dbControllerInstance.changeSingedInOfGeneralParkWorker(parkWorkerToSignIn)) {
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
			
		case Operation.POST_TRAVLER_ORDER: //Emanuel 
			// Placeholder for posting a new traveler order

			try {
				Order travelerOrder = (Order) messageFromClient.getDataTransfered();

				if (dbControllerInstance.insertTravelerOrder(travelerOrder)) {
					messageFromClient.setflagTrue();

				} else {
					messageFromClient.setflagFalse();
				}
				client.sendToClient(messageFromClient);
				
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
			
			
		case Operation.GET_LAST_ORDER_ID: //Emanuel
		    Integer lastOrder = dbControllerInstance.getLastOrderId();
		    
		    // Create a message to send to the client
		    ClientServerMessage<?> lastOrderMessage = new ClientServerMessage<>(lastOrder, Operation.GET_LAST_ORDER_ID);
		    
		    // Send the message to the client
		    client.sendToClient(lastOrderMessage);
		    break;

//		case Operation.CHECK_SPOT_AVAILABILITY: //Emanuel
//			try {
//			    Order orderToCheck = (Order) messageFromClient.getDataTransfered();
//			    if ( dbControllerInstance.isSpotAvailable(orderToCheck))
//			    	messageFromClient.setflagTrue();
//			    else {
//			    	messageFromClient.setflagFalse();
//
//			    }
//			    client.sendToClient(messageFromClient);
//			}catch (IndexOutOfBoundsException e) {
//
//			}
//		    break;
		    
		case Operation.CHECK_IF_ORDER_VALID: //Emanuel
		    Order orderToCheckValidity = (Order) messageFromClient.getDataTransfered();

		    try {
		        if (dbControllerInstance.checkIfOrderisValid(orderToCheckValidity)) {
		            messageFromClient.setflagTrue();  // Order is valid
		        } else {
		            messageFromClient.setflagFalse(); // Order is not valid
		        }
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.GET_PARK_BY_NAME: //Emanuel
			try {
		        // Placeholder for checking full days in the park
				String parkToCheck = (String) messageFromClient.getDataTransfered();
		        
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.findParkByName(parkToCheck));
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
//		case Operation.CHECK_PARK_FULL_DAYS: 
//		    try {
//		        // Placeholder for checking full days in the park
//		        Order orderToCheck = (Order) messageFromClient.getDataTransfered();
//		        
//		        // Create a message to send to the client
//		        messageFromClient.setDataTransfered(dbControllerInstance.checkParkFullDays(orderToCheck));
//		        
//		        // Send the message to the client
//		        client.sendToClient(messageFromClient);
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        // Handle the exception according to your needs
//		    }
//		    break;
		  
		case Operation.GET_RECENT_ID_TRAVELER: //emanuel
			try {
		        // Placeholder for checking full days in the park
				Integer recentId = (Integer) messageFromClient.getDataTransfered();
		        
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.getTravelerRecentOrder(recentId));
		        
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.FIND_ORDERS_WITHIN_DATES: //emanuel
			try {
		        List<Order> orderToCheck = (List<Order>) messageFromClient.getDataTransfered();
		        ArrayList<Order> orderList = new ArrayList<>(orderToCheck);
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.findOrdersWithinDates(orderList));
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.GET_PARKS_INFO: //emanuel
			try {
		        
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.getParksInfo());
		        
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.POST_NEW_WAITING_LIST:
			
		    WaitingList waitingToAdd = (WaitingList) messageFromClient.getDataTransfered();

		    try {
		        if (dbControllerInstance.insertWaitingList(waitingToAdd)) {
		            messageFromClient.setflagTrue();  
		        } else {
		            messageFromClient.setflagFalse(); 
		        }
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.POST_NEW_TRAVLER:
		
		    Traveler TravelerToAdd = (Traveler) messageFromClient.getDataTransfered();

		    try {
		        if (dbControllerInstance.insertNewTraveler(TravelerToAdd)) {
		            messageFromClient.setflagTrue();  
		        } else {
		            messageFromClient.setflagFalse(); 
		        }
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
		    break;
		    
		case Operation.GET_LAST_WAITINGLIST: //Emanuel
		    Integer lastWaiting = dbControllerInstance.getLastWaitingId();
		    
		    // Create a message to send to the client
		    ClientServerMessage<?> lastWaitingMsg = new ClientServerMessage<>(lastWaiting, Operation.GET_LAST_WAITINGLIST);
		    
		    // Send the message to the client
		    client.sendToClient(lastWaitingMsg);
		    break;

		case Operation.FIND_PLACE_IN_WAITING_LIST: //emanuel
			try {
		        List<Order> findPlace = (List<Order>) messageFromClient.getDataTransfered();
		        ArrayList<Order> placeArr = new ArrayList<>(findPlace);
		        // Create a message to send to the client
		        messageFromClient.setDataTransfered(dbControllerInstance.findPlaceWaiting(placeArr));
		        // Send the message to the client
		        client.sendToClient(messageFromClient);
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Handle the exception according to your needs
		    }
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
