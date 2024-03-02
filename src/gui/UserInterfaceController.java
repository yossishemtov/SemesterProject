package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterfaceController {

	
	public void start(Stage primaryStage) throws Exception {
		
		//Starting the root scene of the userInterface
		try {			
			Parent root = FXMLLoader.load(getClass().getResource("UserInterfaceFrame.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("User Menu");
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void btnAllReservation(ActionEvent click) throws Exception{
		//Function for opening a new scene when clicking on the Update View all reserveation button
		try {
			
		Parent root = new FXMLLoader().load(getClass().getResource("UserOrdersFrame.fxml"));
		Scene scene = new Scene(root);	
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		
		stage.setTitle("User Orders Window");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the all reservation button, check stack trace");
			e.printStackTrace();
		}
		
	}
	
public void btnUpdateReservation(ActionEvent click) throws Exception{
		//Function for opening a new scene when clicking on the Update Reservation Button
	
	
	try {
		
		Parent root = new FXMLLoader().load(getClass().getResource("UserUpdateOrderFrame.fxml"));
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		Scene scene = new Scene(root);	
		
		stage.setTitle("Edit Reservation");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the Update Reservation button, check stack trace");
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
