package server;

import common.*;
import java.io.IOException;

import DB.DatabaseController;
import ocsf.server.ConnectionToClient;

public class MessageHandler {

    public static void handleMessage(Object msg, ConnectionToClient client) throws IOException {
    	 BackEndServer bs = BackEndServer.getBackEndServer();
    	 DatabaseController dbcontroller=bs.DBController;
    	 
    	 

        if (!(msg instanceof ClientServerMessage)) {
            try {
                client.sendToClient(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        ClientServerMessage message = (ClientServerMessage) msg;
        String command = message.getCommand();
        Object result;
        Object dataForClient;
        Object orderId;

        switch (command) {
            case Operation.Disconnecting:
                bs.clientDisconnected(client);
                try {
                    client.sendToClient("ack_disconnect"); // Send acknowledgment to client
                } catch (IOException e) {
                    System.out.println("Error sending ack_disconnect to client: " + e.getMessage());
                }
                break;


                case Operation.GetAllOrders:
                    // Placeholder for getting all orders from the database
                	ClientServerMessage responseToclient=new ClientServerMessage(dbcontroller.getOrderDataFromDatabase(),Operation.Responseallorder);
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
                    break;

                case Operation.PostTravlerOrder:
                    // Placeholder for posting a new traveler order
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
