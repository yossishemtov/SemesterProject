package gui;

import client.ClientController;
import client.ClientUI;
import client.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;

public class UserConnectController implements Initializable {
	
	    @FXML
	    private JFXTextField IpAddressField;
	
	    @FXML
	    private JFXTextField portAddressField;

	    @FXML
	    private Button connectBtn;

	    @FXML
	    private Button exitBtn;
	    
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	        try {
	            InetAddress inetAddress = InetAddress.getLocalHost();
	            String ipAddress = inetAddress.getHostAddress();
	            IpAddressField.setText(ipAddress);
	            portAddressField.setText("5555");
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Could not determine IP address.");
	        }
	    }
	    
	    public void start(Stage primaryStage) throws Exception {
			
			//Starting the root scene of the userInterface
	    	
			try {			
				Parent root = FXMLLoader.load(getClass().getResource("userConnectingFrame.fxml"));
				Scene scene = new Scene(root);
//				NavigationManager.setStyleScene(scene, "/styles/styles.css");
				primaryStage.setTitle("User Connection");
				primaryStage.setScene(scene);
				primaryStage.show();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	    
	    public void connectBtn(ActionEvent click) throws Exception{
	    	String IpAddressFieldValue = IpAddressField.getText();
			Integer portAddressFieldValue = Integer.parseInt(portAddressField.getText());
			
			
			try {				
			ClientUI.clientControllerInstance = new ClientController(IpAddressFieldValue, portAddressFieldValue);
			Parent root = new FXMLLoader().load(getClass().getResource("HomePageFrame.fxml"));
			Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			Scene scene = new Scene(root);	
			
			stage.setTitle("User Menu");
			
			stage.setScene(scene);		
			stage.show();
			
			
			}catch(Exception e) {
				System.out.println("Something went wrong with connecting to the server");
				e.printStackTrace();
			}
	    }
	    
	    public void exitBtn(ActionEvent click) throws Exception{
	    	System.exit(1);
	    }
	}
