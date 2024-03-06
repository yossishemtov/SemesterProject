package client;

import java.io.*;
import java.util.ArrayList;

import common.ClientServerMessage;
import common.DisplayIF;
import server.BackEndServer;

public class ClientController<T> implements DisplayIF{
    
    SystemClient systemClient;
    final public static int DEFAULT_PORT = 5555;
    public ClientServerMessage<T> data;
    
    // Making a new instance of the SystemClient that implements OCSF Abstract client
    public ClientController(String host, int port) 
    {
        try 
        {
            systemClient = new SystemClient(host, port, this);
        } 
        catch(IOException exception) 
        {
            System.out.println("Error: Can't setup connection! Terminating client.");
            System.exit(1);
        }
    }

    @Override
    public void display(String message) {
        // Display message logic here
    }

    public void accept(String command) {
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

    // Method to close the connection
    public void closeConnection() throws IOException {
    	
        systemClient.quit();
    }


    public void setData(ClientServerMessage<T> data) {
        this.data = data;
    }

    public ClientServerMessage<T> getData() {
        return this.data;
    }
    
}
