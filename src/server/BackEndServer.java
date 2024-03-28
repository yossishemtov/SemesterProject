package server;
// "Object Oriented Software Engineering" and is issued under the open-source

// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import server.UpdateStatusThread;
import entities.ClientConnectionStatus;

import DB.*;
import common.ClientServerMessage;
import ocsf.server.*;

public class BackEndServer extends AbstractServer {

	final public static int DEFAULT_PORT = 5555;
	private static ServerController serverControllerInstance;
	public static DatabaseController DBController;
	public static BackEndServer backEndServerInstance;
	private Connection DbConnection;
	 private UpdateStatusThread updateStatusThread;
	private NotifyThread notifyThread;
	private Map<Long, ClientConnectionStatus> connectedClients;

	public BackEndServer(int port, ServerController serverControllerInstance, String userName, String password) {
		super(port);

		BackEndServer.serverControllerInstance = serverControllerInstance;
		Connection UserManagementSystemConnection = new MySqlConnector(userName, password,"User_management_system").getDbConnection();
		UserManagementSystemDB UserManagementSystemDB=new UserManagementSystemDB(UserManagementSystemConnection);
		this.DbConnection = new MySqlConnector(userName, password,"project").getDbConnection();
		// Initiate a connection to the database	
		DBController = new DatabaseController(DbConnection,UserManagementSystemDB);
		MessageHandlerFromClient.setDbController(DBController);
		notifyThread = new NotifyThread(DBController); // You may need to pass any required parameters to the
														// constructor
		Thread thread = new Thread(notifyThread);
		thread.setDaemon(true); // Set the thread as daemon so that it stops when the application exits
		thread.start();
		
        // Initialize and start the UpdateStatusThread
        updateStatusThread = new UpdateStatusThread(DBController); 
        Thread updateStatusThreadObj = new Thread(updateStatusThread);
        updateStatusThreadObj.setDaemon(true); 
        updateStatusThreadObj.start();

		// Initiate a hashmap for the client connecitons
		this.connectedClients = new HashMap<>();

		// Saving the backend server instance
		backEndServerInstance = this;
	}

	public static BackEndServer getBackEndServer() {

		return backEndServerInstance;
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);

		// Getting the information of a client connection, address and hostname
		String ipAddress = client.getInetAddress().getHostAddress();
		String hostname = client.getInetAddress().getHostName();

		// Creating a clientConnectionStatusInstace to represent it in the table
		ClientConnectionStatus connectionStatus = new ClientConnectionStatus(ipAddress, hostname, "Connected");

		// Inserting to the hashmap that holding the client connections
		connectedClients.put((long) client.getId(), connectionStatus);

		// Updating the table UI by using the server controller method
		serverControllerInstance.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
	}

	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		Long clientId = client.getId(); // Use Long without casting
		ClientConnectionStatus status = connectedClients.get(clientId);
		if (status != null) {
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
			MessageHandlerFromClient.handleMessage((ClientServerMessage) msg, client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
}
