package client;

import java.io.IOException;
import server.BackEndServer;
import java.util.ArrayList;


import common.Alerts;
import common.ClientServerMessage;

import common.Operation;
import common.Usermanager;
import javafx.application.Platform;
import ocsf.client.AbstractClient;

/**
 * A client class representing the system client communicating with the backend server.
 */
public class SystemClient extends AbstractClient{
	
	//A boolean to indicate waiting for a server response
	public static boolean awaitResponse = false;
	private ClientController<?> clientControllerInstance;

	/**
	 * Constructs a new SystemClient with the specified host, port, and client controller instance.
	 *
	 * @param host the hostname of the server
	 * @param port the port number to connect to on the server
	 * @param clientController the client controller instance associated with this client
	 * @throws IOException if an I/O error occurs when opening the connection
	 */
	public SystemClient(String host, int port, ClientController clientController) throws IOException{
		super(host, port);
		this.clientControllerInstance = clientController;
		// TODO Auto-generated constructor stub
		openConnection();
	}



	
	//Handle message from the server
	@Override
	/**
	 * Handles a message received from the server.
	 *
	 * @param messageFromServer the message received from the server
	 */
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

	  
	/**
	 * Sends a message from the client controller to the server and waits for a response.
	 *
	 * @param messageToServer the message to send to the server
	 */
	  public void handleMessageFromClientController(ClientServerMessage<?> messageToServer)  
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
	  
	  /**
	   * Terminates the client, closing the connection to the server and performing necessary cleanup.
	   * If a worker or traveler is logged in, this method sends logout requests to the server before terminating.
	   */
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
		  
		//Check if Traveler is logged in (if the user exited from Traveler signin page) and sign him off
		  if(Usermanager.getCurrentTraveler() != null) {
				ClientServerMessage requestToLogoutTraveler = new ClientServerMessage(Usermanager.getCurrentTraveler(), Operation.PATCH_TRAVELER_SIGNEDOUT);
				
				try {
			        sendToServer(requestToLogoutTraveler);
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
