package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomePageController {
	@FXML
    private Button ClientLoginBtn;
	@FXML
    private Button WorkerLoginBtn;
	@FXML
    private Button ExitBtn;
	
	
	public void start(Stage primaryStage) throws Exception {
		
		//Starting the root scene of the HomePage
		try {			
			Parent root = FXMLLoader.load(getClass().getResource("HomePageFrame.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Home Page");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClientLoginBtn(ActionEvent click) throws Exception {
		//Function for opening a new scene when clicking on the Client Login button
		try {
			
		Parent root = new FXMLLoader().load(getClass().getResource("VisitorLoginFrame.fxml"));
		Scene scene = new Scene(root);	
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		
		stage.setTitle("Client Login");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the Client login button, check stack trace");
			e.printStackTrace();
		}
	}
	
	public void WorkerLoginBtn(ActionEvent click) throws Exception {
		//Function for opening a new scene when clicking on the Worker Login button
		try {
			
		Parent root = new FXMLLoader().load(getClass().getResource("WorkerLoginFrame.fxml"));
		Scene scene = new Scene(root);	
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		
		stage.setTitle("Client Login");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the Worker login button, check stack trace");
			e.printStackTrace();
		}
		
	}
}