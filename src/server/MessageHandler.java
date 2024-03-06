package server;

import common.*;


import java.io.IOException;
import java.util.ArrayList;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandler {

    public static void handleMessage(Object msg, ConnectionToClient client) throws IOException {
    	//A class that is intended to handle diffrent messages from the client and response accordingly
    	 BackEndServer backEndServerInstance = BackEndServer.getBackEndServer();
    	 DatabaseController dbcontroller = backEndServerInstance.DBController;
    	 
    	 
    	//Checking if message is of type of generic message intended for client and server communication
        if (!(msg instanceof ClientServerMessage)) {
            try {
                client.sendToClient(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        //Extracting data from the generic message object intended for further parsing
        ClientServerMessage message = (ClientServerMessage) msg;
        String command = message.getCommand();
        Object result;
        Object dataForClient;
        Object orderId;

        
        //Parsing the command
        switch (command) {
        
        	//User sends a disconnect command to the server
            case Operation.Disconnecting:
                backEndServerInstance.clientDisconnected(client);
                try {
                    client.sendToClient("ack_disconnect"); // Send acknowledgment to client
                } catch (IOException e) {
                    System.out.println("Error sending ack_disconnect to client: " + e.getMessage());
                }
                break;


                case Operation.GetAllOrders:
                    // Placeholder for getting all orders from the database
                	ClientServerMessage responseToclient = new ClientServerMessage(dbcontroller.getOrderDataFromDatabase(),Operation.Responseallorder);
                    client.sendToClient(responseToclient);
                	break;

                case Operation.GetTravlerInfo:
                    // Placeholder for getting traveler information
                    break;

                case Operation.GetTravlerLoginDetails:
                    // Placeholder for getting traveler login details
                    break;

                case Operation.GetAllReports:
                    // Placeholder for getting all reports
                    break;

                case Operation.GetSpecificReport:
                    // Placeholder for getting a specific report
                    break;

                case Operation.GetMessages:
                    // Placeholder for getting messages
                    break;

                case Operation.GetProfile:
                    // Placeholder for getting a profile
                    break;

                case Operation.GetAmountOfVisitors:
                    // Placeholder for getting the amount of visitors
                    break;

                case Operation.PostNewTravlerGuider:
                    // Placeholder for posting a new traveler guide request
                	ArrayList<GroupGuide> groupGuide=(ArrayList<GroupGuide>)message.getDataTransfered();
;                	try {
                	dbcontroller.addNewGroupGuide(groupGuide.get(0));
}
					catch(IndexOutOfBoundsException e)
					{
						Alerts errorAlert = new Alerts(Alerts.AlertType.ERROR, "Data base error", "Error",
								"Error to enter a new group guide to db.");
						errorAlert.showAndWait();
					}

                	
                    break;

                case Operation.PostTravlerOrder:
                    // Placeholder for posting a new traveler order
                	
                	try {
                		ArrayList travelerorder=(ArrayList) message.getDataTransfered();
                    	
                    	dbcontroller.insertTravelerOrder((Traveler)travelerorder.get(0),(Order)travelerorder.get(1));
    }
    					catch(IndexOutOfBoundsException e)
    					{
    						Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Data base error", "Error",
    								"Error to enter a new traveler order.");
    						erorAlert.showAndWait();
    					}

                	break;

                case Operation.PostNewReport:
                    // Placeholder for posting a new report
                    break;

                case Operation.PatchExistOrder:
                    // Placeholder for patching an existing order
                    break;

                case Operation.PatchParkParameters:
                    // Placeholder for patching park parameters
                    break;

                case Operation.PatchOrderStatus:
                    // Placeholder for patching order status
                	
                	try {
                		ArrayList<Order> travelerorder=(ArrayList<Order>) message.getDataTransfered();
                    	
                    	dbcontroller.updateOrderStatus((Order)travelerorder.get(0));
    }
    					catch(IndexOutOfBoundsException e)
    					{
    						Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Data base error", "Error",
    								"Error to order status order.");
    						erorAlert.showAndWait();
    					}
                	
                	
                	
                    break;

                case Operation.DeleteExistingOrder:
                    // Placeholder for deleting an existing order
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
