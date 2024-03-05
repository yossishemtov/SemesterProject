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
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class UserConnectController {
	
	    @FXML
	    private TextField IpAddressField;

	    @FXML
	    private TextField portAddressField;

	    @FXML
	    private Button connectBtn;

	    @FXML
	    private Button exitBtn;
	    
	    
	    public void start(Stage primaryStage) throws Exception {
			
			//Starting the root scene of the userInterface
			try {			
				Parent root = FXMLLoader.load(getClass().getResource("userConnectingFrame.fxml"));
				Scene scene = new Scene(root);

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
