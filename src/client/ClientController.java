package client;

import java.io.*;
import java.util.ArrayList;

import common.ClientServerMessage;
import common.DisplayIF;
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

public class ClientController<T> implements DisplayIF{
    
    public static SystemClient systemClient;
    
    final public static int DEFAULT_PORT = 5555;
    public static  ClientServerMessage<?> data;
    
    // Making a new instance of the SystemClient that implements OCSF Abstract client
    public ClientController(String host, int port) 
    {
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
    public void display(String message) {
        // Display message logic here
    }

    public void sendMessageToServer(ClientServerMessage<?> command) {
        // Handle message from any controller that is related to the client
    	System.out.println("send to servrvr");
        try
        {
            systemClient.handleMessageFromClientController(command);
        } 
        catch (Exception ex) 
        {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    // Method to close the connection
    public void closeConnection() throws IOException {
    	
        systemClient.quit();
    }


    public void setData(ClientServerMessage<?> clientServerMessage) {
        this.data = (ClientServerMessage<?>) clientServerMessage;
    }

    public ClientServerMessage<?> getData() {
        return this.data;
    }
    
    

    
}
