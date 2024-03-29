package client;

import java.io.*;
import java.util.ArrayList;

import common.ClientServerMessage;
import common.DisplayIF;
import common.Operation;
import common.Usermanager;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.BackEndServer;

/**
 * The controller class responsible for handling client-side operations and communication with the server.
 *
 * @param <T> the type parameter for the client controller
 */
public class ClientController<T> implements DisplayIF{
    
	/**
	 * The SystemClient instance used for communication with the server.
	 */
    public static SystemClient systemClient;
    
    /**
     * The default port number for client-server communication.
     */
    final public static int DEFAULT_PORT = 5555;
    
    /**
     * The data received from the server.
     */
    public static  ClientServerMessage<?> data;
    
    /**
     * Constructs a new ClientController with the specified host and port, initializing the SystemClient.
     *
     * @param host the hostname of the server
     * @param port the port number to connect to on the server
     */
    public ClientController(String host, int port) 
    {
    	// Making a new instance of the SystemClient that implements OCSF Abstract client
        try  
        {
            systemClient = new SystemClient(host, port, this);
            NavigationManager.initialize(systemClient);
        } 
        catch(IOException exception) 
        {
        	   System.out.println(exception.getMessage());
            System.out.println("Error: Can't setup connection! Terminating client.");
            System.exit(1);
        }
    }

    @Override
    /**
     * Displays a message.
     *
     * @param message the message to display
     */
    public void display(String message) {
        // Display message logic here
    }

    /**
     * Sends a message to the server.
     *
     * @param command the command message to send
     */
    public void sendMessageToServer(ClientServerMessage<?> command) {
        // Handle message from any controller that is related to the client
    	
        try
        {
            systemClient.handleMessageFromClientController(command);
        } 
        catch (Exception ex) 
        {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * Closes the connection to the server.
     *
     * @throws IOException if an I/O error occurs while closing the connection
     */
    public void closeConnection() throws IOException {
    	// Method to close the connection
    	    	
    	//Check if user was connected as worker before quitting and signing out its account
    	if(Usermanager.getCurrentWorker() != null) {
			ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
			this.sendMessageToServer(requestToLogout);
			
		}		
    	
        systemClient.quit();
    }


    /**
     * Sets the data received from the server.
     *
     * @param clientServerMessage the data received from the server
     */
    public void setData(ClientServerMessage<?> clientServerMessage) {
        this.data = (ClientServerMessage<?>) clientServerMessage;
    }

    /**
     * Retrieves the data received from the server.
     *
     * @return the data received from the server
     */
    public ClientServerMessage<?> getData() {
        return this.data;
    }
    
    

    
}
