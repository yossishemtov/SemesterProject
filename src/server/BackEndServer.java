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
import common.ClientServerMessage;
import ocsf.server.*;


public class BackEndServer extends AbstractServer 
{

  final public static int DEFAULT_PORT = 5555;
  private static ServerController serverControllerInstance;
  public static  DatabaseController DBController;
  public static BackEndServer backEndServerInstance;
  private Map<Long, ClientConnectionStatus> connectedClients;
  
  public BackEndServer(int port, ServerController serverControllerInstance, String userName, String password) {
	    super(port);
	    
	    //Initiating a servercontroller instance
	    BackEndServer.serverControllerInstance = serverControllerInstance;
	    
	    //Initiate a connection to the database
	    BackEndServer.DBController = new DatabaseController(userName, password);
	    
	    //Initiate a hashmap for the client connecitons
	    this.connectedClients = new HashMap<>();
	    
	    //Saving the backend server instance
	    backEndServerInstance=this;
	}
  
  public static BackEndServer getBackEndServer() {
	 
	return backEndServerInstance;
  }
 
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
      super.clientConnected(client);
      
      //Getting the information of a client connection, address and hostname
      String ipAddress = client.getInetAddress().getHostAddress();
      String hostname = client.getInetAddress().getHostName();
      
      //Creating a clientConnectionStatusInstace to represent it in the table
      ClientConnectionStatus connectionStatus = new ClientConnectionStatus(ipAddress, hostname, "Connected");
      
      //Inserting to the hashmap that holding the client connections
      connectedClients.put((long) client.getId(), connectionStatus);
      
      //Updating the table UI by using the server controller method
      serverControllerInstance.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
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
          serverControllerInstance.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
          
          // Then remove if not required to show disconnected status
          connectedClients.remove(clientId);
      } else {
          // If it gets here, something went wrong. 
      }
  }




  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	  
	  try {
		MessageHandlerFromClient.handleMessage((ClientServerMessage)msg,client);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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

