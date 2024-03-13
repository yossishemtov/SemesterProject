package client;

import java.io.IOException;
import server.BackEndServer;
import java.util.ArrayList;

import clientEntities.Reservation;
import common.Alerts;
import common.ClientServerMessage;
import common.DisplayIF;
import common.Operation;
import common.Usermanager;
import javafx.application.Platform;
import ocsf.client.AbstractClient;

public class SystemClient extends AbstractClient{
	
	//A boolean to indicate waiting for a server response
	public static boolean awaitResponse = false;
	private ClientController<?> clientControllerInstance;

	public SystemClient(String host, int port, ClientController clientController) throws IOException{
		super(host, port);
		this.clientControllerInstance = clientController;
		// TODO Auto-generated constructor stub
		openConnection();
	}



	
	//Handle message from the server
	@Override
	public void handleMessageFromServer(Object messageFromServer) {
	  
	    // Log the class type and content of the message for debugging
	    System.out.println("Message Class: " + messageFromServer.getClass().getName());
	    System.out.println("Message Content: " + messageFromServer.toString());

	    // Check for disconnection acknowledgment
	    if (Operation.DISCONNECTING.equals(messageFromServer.toString())) {
	        
	        awaitResponse = false; // Acknowledgment received; stop waiting
	        return; // Early return to skip further processing
	    }

	    // Check if the message is of type ClientServerMessage
	    if (messageFromServer instanceof ClientServerMessage) {
	        System.out.println("in instanceof");
	        ClientServerMessage<?> clientServerMessage = (ClientServerMessage<?>) messageFromServer;

	        // Assuming you have some specific handling or logging based on the message content
	        // For example:
	        System.out.println("Received ClientServerMessage with command in system client: " + clientServerMessage.toString());
	        clientControllerInstance.setData(clientServerMessage);
	        awaitResponse = false;
	        
	    } else {
	        System.out.println("Received message of unknown type");
	        Platform.runLater(() -> {
	            Alerts alertOfUnknownTypeOfMessage = new Alerts(Alerts.AlertType.ERROR, "Received Data Error", "", "Something went wrong while receiving the data from the server");
	            alertOfUnknownTypeOfMessage.showAndWait();
	        });
	    }
	    awaitResponse = false;
	}

	  
	  
	  public void handleMessageFromClientController(ClientServerMessage<?> messageToServer)  
	  {
		   	System.out.println("handleMessageFromClientController");
		  
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
		  
		  
		  //Check if worker is logged in (if the user exited from worker signin page) and sign him off
		  if(Usermanager.getCurrentWorker() != null) {
				ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
				
				try {
			        sendToServer(requestToLogout);
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
				
					}catch(IOException e) {
				        // Handle exception
				        e.printStackTrace();
					}
			  }
		    
		    	
		    	
		    	try {
		    	
		        // Notify server of disconnection
		    	ClientServerMessage<?> message=new ClientServerMessage(null,Operation.DISCONNECTING);
		        sendToServer(message);
		        awaitResponse = true; // Wsait for the server to acknowledge disconnection

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
