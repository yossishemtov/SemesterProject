package server;
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entities.ClientConnectionStatus;

import DB.*;
import ocsf.server.*;


public class BackEndServer extends AbstractServer 
{

  final public static int DEFAULT_PORT = 5555;
  private static ServerController sc;
  private static  DatabaseController DBController;
  private Map<Long, ClientConnectionStatus> connectedClients;
  
  public BackEndServer(int port, ServerController sc, String userName, String password) {
	    super(port);
	    BackEndServer.sc = sc;
	    BackEndServer.DBController = new DatabaseController(userName, password);
	    this.connectedClients = new HashMap<>(); // Initialize the map here
	}
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
      super.clientConnected(client);
      String ipAddress = client.getInetAddress().getHostAddress();
      String hostname = client.getInetAddress().getHostName();
      ClientConnectionStatus connectionStatus = new ClientConnectionStatus(ipAddress, hostname, "Connected");
      connectedClients.put((long) client.getId(), connectionStatus);
      sc.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
  }
  @Override
  protected void clientDisconnected(ConnectionToClient client) {
      System.out.println("Client disconnected: " + client);
      Long clientId = client.getId(); // Use Long without casting
      ClientConnectionStatus status = connectedClients.get(clientId);
      if (status != null) {
          // Notify the client that it is being disconnected
          try {
              client.sendToClient("disconnecting");
          } catch (IOException e) {
              System.out.println("Error sending disconnect message to client: " + e.getMessage());
          }
          
          status.updateStatus("Disconnected");
          // Update the GUI after any change
          sc.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
          
          // Then remove if not required to show disconnected status
          connectedClients.remove(clientId);
      } else {
          // If it gets here, something went wrong. 
      }
  }




  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	  
	  if (msg instanceof String && "disconnecting".equals(msg)) {
		    try {
		    	clientDisconnected(client);
		    	client.sendToClient("ack_disconnect"); // Send acknowledgment to client
		        
		    } catch (IOException e) {
		        System.err.println("Failed to send acknowledgment to client.");
		        e.printStackTrace();
		    }
		    
	  }
	    if (msg instanceof String) {
	        String message = (String) msg;
	        int index_end_command = message.indexOf("|");
	        String command = message.substring(0, index_end_command);
	        System.out.println("Message received: " + msg + " from " + client);

	        String data = message.substring(index_end_command + 1);
	        String[] result;
	        String orderId;
	        ArrayList<String> dataForClient;
	       
	        // Use switch-case to handle different messages
	        switch (command) {
	            // Case for retrieving specific order data based on the order number
	            case "GET ORDER":
	                result = data.split(" ");
	                orderId = result[1];
	                dataForClient = DBController.getSpecificDataFromDB(orderId);
	                dataForClient.add(0, command);
	                // Send the order data back to the client
	                try {
	                    client.sendToClient(dataForClient);
	                } catch (IOException e) {
	                    System.out.println("Error sending data to client: " + e.getMessage());
	                }
	                break;

	            // Case for updating specific order data (telephone and park name)in the database:
	            case "SET ORDER":
	                result = data.split(" ");
	                System.out.print(data);
	                orderId = result[0];
	                String newParkName = result[1];
	                String newTelephone = result[2];
	                DBController.setOrderDataOnDatabase_TelphoneParkNameChange(orderId, newParkName, newTelephone);

	                try {
	                    client.sendToClient("Order data changed");
	                } catch (IOException e) {
	                    System.out.println("Error sending data to client: " + e.getMessage());
	                }
	                break;

	            // Case for retrieving all orders data
	            case "GET ALL":
	                dataForClient = DBController.getOrderDataFromDatabase();
	                dataForClient.add(0, command);

	                try {
	                    client.sendToClient(dataForClient);
	                } catch (IOException e) {
	                    System.out.println("Error sending data to client: " + e.getMessage());
	                }
	                break;

	            // Default case for handling unknown requests
	            default:
	                System.out.println("Received an unknown request: " + message);
	                try {
	                    client.sendToClient("Unknown request: " + message);
	                } catch (IOException e) {
	                    System.out.println("Error sending unknown request response to client: " + e.getMessage());
	                }
	                break;
	        }
	    } else {
	        System.out.println("Message received is not of type String");
	        try {
	            client.sendToClient("Error: Message format not recognized.");
	        } catch (IOException e) {
	            System.out.println("Error sending format error response to client: " + e.getMessage());
	        }
	    }
	}


    

  
  

  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
}

