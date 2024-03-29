package server;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import DB.*;
import common.ClientServerMessage;
import ocsf.server.*;
import entities.ClientConnectionStatus;

/**
 * The BackEndServer class extends the AbstractServer class from the OCSF framework,
 * providing a backend server implementation for handling client connections, messages,
 * and interactions with a database for a specific application.
 * This server initializes various threads for background tasks such as notifications
 * and client status updates, and manages client connections.
 */
public class BackEndServer extends AbstractServer {

    final public static int DEFAULT_PORT = 5555;
    private static ServerController serverControllerInstance;
    public static DatabaseController DBController;
    public static BackEndServer backEndServerInstance;
    private Connection DbConnection;
    private UpdateStatusThread updateStatusThread;
    private NotifyThread notifyThread;
    private Map<Long, ClientConnectionStatus> connectedClients;

    /**
     * Constructs a new BackEndServer with the specified port, server controller instance,
     * and database credentials. It also initializes background threads for handling
     * notifications, waiting list cancellations, and processing client requests.
     *
     * @param port                     The port number on which the server will listen for connections.
     * @param serverControllerInstance An instance of ServerController associated with this server.
     * @param userName                 The username for the database connection.
     * @param password                 The password for the database connection.
     */
    public BackEndServer(int port, ServerController serverControllerInstance, String userName, String password) {
        super(port);
        BackEndServer.serverControllerInstance = serverControllerInstance;
        
        // Connect to the User Management System database
        Connection UserManagementSystemConnection = new MySqlConnector(userName, password, "User_management_system").getDbConnection();
        UserManagementSystemDB UserManagementSystemDB = new UserManagementSystemDB(UserManagementSystemConnection);
        
        // Connect to the project database
        this.DbConnection = new MySqlConnector(userName, password, "project").getDbConnection();
        DBController = new DatabaseController(DbConnection, UserManagementSystemDB);
        MessageHandlerFromClient.setDbController(DBController);

        // Initialize and start the notification thread
        notifyThread = new NotifyThread(DBController);
        Thread thread = new Thread(notifyThread);
        thread.setDaemon(true); // Ensures the thread stops when the application exits
        thread.start();

        // Initialize and start the UpdateStatusThread for updating client connection statuses
        updateStatusThread = new UpdateStatusThread(DBController);
        Thread updateStatusThreadObj = new Thread(updateStatusThread);
        updateStatusThreadObj.setDaemon(true);
        updateStatusThreadObj.start();

        // Initialize a HashMap to track connected clients
        this.connectedClients = new HashMap<>();

        // Save the instance of this server for later retrieval
        backEndServerInstance = this;
    }

    /**
     * Provides access to the singleton instance of BackEndServer.
     *
     * @return The singleton instance of BackEndServer.
     */
    public static BackEndServer getBackEndServer() {
        return backEndServerInstance;
    }

    /**
     * This method is called whenever a client connects to the server.
     * It updates the server's record of connected clients and updates the UI accordingly.
     *
     * @param client The client that has connected to the server.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);

        String ipAddress = client.getInetAddress().getHostAddress();
        String hostname = client.getInetAddress().getHostName();
        ClientConnectionStatus connectionStatus = new ClientConnectionStatus(ipAddress, hostname, "Connected");
        connectedClients.put(client.getId(), connectionStatus);
        serverControllerInstance.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
    }

    /**
     * This method is called whenever a client disconnects from the server.
     * It updates the client's status to "Disconnected" and removes them from the server's record.
     *
     * @param client The client that has disconnected.
     */
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        Long clientId = client.getId();
        ClientConnectionStatus status = connectedClients.get(clientId);
        if (status != null) {
            try {
                client.sendToClient("disconnecting");
            } catch (IOException e) {
                System.out.println("Error sending disconnect message to client: " + e.getMessage());
            }

            status.updateStatus("Disconnected");
            serverControllerInstance.updateConnectionStatusTable(new ArrayList<>(connectedClients.values()));
            connectedClients.remove(clientId);
        }
    }

    /**
     * Handles messages sent from clients to the server.
     * Delegates the processing of messages to the MessageHandlerFromClient class.
     *
     * @param msg    The message sent from the client.
     * @param client The client that sent the message.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try {
            MessageHandlerFromClient.handleMessage((ClientServerMessage) msg, client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the server stops listening for connections.
     * Logs the event of the server stopping.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }
}
