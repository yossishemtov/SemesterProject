package client;
import javafx.application.Application;
import gui.UserConnectController;
import javafx.stage.Stage;
import java.util.Vector;
import client.ClientController;
import gui.UserInterfaceController;

public class ClientUI extends Application {
    
    public static ClientController<?> clientControllerInstance; //only one instance

    public static void main( String args[] ) throws Exception
       { 
            launch(args);  
       } // end main
     
    @Override
    public void start(Stage primaryStage) throws Exception {
         
        // Initialize and show the primary stage (window) of your application
         UserConnectController aFrame = new UserConnectController();
         aFrame.start(primaryStage);
    }
    
    @Override
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
