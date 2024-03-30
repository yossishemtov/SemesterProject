package gui;

import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

/**
 * Controller for user connection UI.
 * This controller manages the UI logic for connecting to the server using a specified IP address and port.
 */
public class UserConnectController implements Initializable {

    @FXML
    private JFXTextField IpAddressField; // Field for IP address input

    @FXML
    private JFXTextField portAddressField; // Field for port number input

    @FXML
    private Button connectBtn; // Button to initiate connection

    @FXML
    private Button exitBtn; // Button to exit application

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It pre-populates the IP address field
     * with the user's current IP address and sets the default port number.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            IpAddressField.setText(ipAddress); // Set user's current IP in IP address field
            portAddressField.setText("5555"); // Set default port number
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not determine IP address.");
        }
    }

    /**
     * Starts the user interface for the connection.
     * 
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception if the FXML file cannot be loaded.
     */
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("userConnectingFrame.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("User Connection");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("/common/images/1.png"));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Connect button action. Attempts to connect to the server using the provided IP address and port number.
     * 
     * @param click The action event that triggered this method.
     * @throws Exception if there is an issue connecting to the server or loading the next scene.
     */
    public void connectBtn(ActionEvent click) throws Exception {
        String IpAddressFieldValue = IpAddressField.getText();
        Integer portAddressFieldValue = Integer.parseInt(portAddressField.getText());
        
        try {
            ClientUI.clientControllerInstance = new ClientController(IpAddressFieldValue, portAddressFieldValue);
            Parent root = new FXMLLoader().load(getClass().getResource("HomePageFrame.fxml"));
            Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); // Get the current window
            Scene scene = new Scene(root);    
            stage.setTitle("Home Page");
            stage.setScene(scene);        
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong with connecting to the server");
            e.printStackTrace();
        }
    }

    /**
     * Handles the Exit button action. Exits the application.
     * 
     * @param click The action event that triggered this method.
     * @throws Exception if an error occurs during application exit.
     */
    public void exitBtn(ActionEvent click) throws Exception {
        System.exit(1);
    }
}
