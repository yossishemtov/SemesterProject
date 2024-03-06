package client;

import java.io.IOException;
import server.BackEndServer;
import java.util.ArrayList;

import clientEntities.Reservation;
import common.Alerts;
import common.ClientServerMessage;
import common.DisplayIF;
import ocsf.client.AbstractClient;

public class SystemClient extends AbstractClient{
	
	//A boolean to indicate waiting for a server response
	public static boolean awaitResponse = false;
	private ClientController clientControllerInstance;

	public SystemClient(String host, int port, ClientController clientController) throws IOException{
		super(host, port);
		this.clientControllerInstance = clientController;
		// TODO Auto-generated constructor stub
		openConnection();
	}


	//Handle message from the server
	  public void handleMessageFromServer(Object messageFromServer) 
	  {
		    // Check for disconnection acknowledgment
		    if ("ack_disconnect".equals(messageFromServer.toString())) {
		        awaitResponse = false; // Acknowledgment received; stop waiting
		        return; // Early return to skip further processing
		    }
		  
		  awaitResponse = false;
		    // Check if the message is of type of the ClientServerMessage
		    if (messageFromServer instanceof ClientServerMessage) {
		    	clientControllerInstance.setData((ClientServerMessage)messageFromServer);
		        
		    }else{
		    	Alerts alertOfUnknownTypeOfMessage = new Alerts(Alerts.AlertType.ERROR, "Received Data Error", "", "Something went wrong while receiving the data from the server");
		   }
	  }
	  
	  
	  public void handleMessageFromClientController(ClientServerMessage messageToServer)  
	  {
		  
		  awaitResponse = true;
		  
		//Send the message to the server and waiting for a response
	    try
		    {
		    	sendToServer(messageToServer);
		    	
		    	while (awaitResponse) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		    }
	    catch(IOException e)
		    {
		    	clientControllerInstance.display
		        ("Could not send message to server.  Terminating client.");
		      quit();
		    }
	  }
	  
	  public void quit() {
		    try {
		        // Notify server of disconnection
		        sendToServer("disconnecting");
		        awaitResponse = true; // Wait for the server to acknowledge disconnection

		        // Wait for acknowledgment
		        while (awaitResponse) {
		            try {
		                Thread.sleep(100); // Check for the acknowledgment every 100 milliseconds
		            } catch (InterruptedException e) {
		                Thread.currentThread().interrupt(); // Restore interrupted status
		                System.err.println("Interrupted while waiting for disconnection acknowledgment.");
		                break;
		            }
		        }

		    } catch(IOException e) {
		        // Handle exception
		        e.printStackTrace();
		    } finally {
		        // Consider a more graceful way to terminate, especially for GUI applications
		        System.exit(0); // Exit the application
		    }
		}

	
}
