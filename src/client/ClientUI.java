package client;
import javafx.application.Application;
import gui.UserConnectController;
import javafx.stage.Stage;
import java.util.Vector;
import client.ClientController;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import gui.UserInterfaceController;

/**
 * The main class representing the client-side user interface of the application.
 * Extends the JavaFX Application class.
 */
public class ClientUI extends Application {
    
    public static ClientController<?> clientControllerInstance; //only one instance

    /**
     * The main method of the client application. Launches the JavaFX application.
     *
     * @param args command-line arguments
     * @throws Exception if an error occurs during application launch
     */
    public static void main( String args[] ) throws Exception
       { 
            launch(args);  
       } // end main
     
    @Override
    /**
     * Initializes the primary stage (window) of the application.
     *
     * @param primaryStage the primary stage of the application
     * @throws Exception if an error occurs during initialization
     */
    public void start(Stage primaryStage) throws Exception {
         
        // Initialize and show the primary stage (window) of your application
         UserConnectController aFrame = new UserConnectController();
         aFrame.start(primaryStage);
    }
    
    @Override
    /**
     * Called when the application is closing. Performs cleanup tasks such as closing the client connection.
     */
    public void stop(){
        // This method is called when the application is closing.
        System.out.println("Application is closing");
        try {
            if (clientControllerInstance != null) {
                clientControllerInstance.closeConnection(); // Assuming this method exists in your ClientController
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
