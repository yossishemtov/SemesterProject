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
  public static  DatabaseController DBController;
  public static BackEndServer bs;
  private Map<Long, ClientConnectionStatus> connectedClients;
  
  public BackEndServer(int port, ServerController sc, String userName, String password) {
	    super(port);
	    BackEndServer.sc = sc;
	    BackEndServer.DBController = new DatabaseController(userName, password);
	    this.connectedClients = new HashMap<>(); // Initialize the map here
	    bs=this;
	}
  
  public static BackEndServer getBackEndServer() {
	 
	return bs;
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
	  
	  try {
		MessageHandler.handleMessage(msg,client);
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

