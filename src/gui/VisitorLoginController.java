package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VisitorLoginController {
	@FXML
    private Button LoginBtn;
	@FXML
    private Button BackBtn;
	@FXML
    private TextField VisitorID;
	
//	public void start(Stage primaryStage) throws Exception {
//		
//		//Starting the root scene of the VisitorLogin
//		try {			
//			Parent root = FXMLLoader.load(getClass().getResource("VisitorLoginFrame.fxml"));
//			Scene scene = new Scene(root);
//			primaryStage.setTitle("Visitor Login");
//			primaryStage.setScene(scene);
//			primaryStage.show();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
//	public void LoginBtn(ActionEvent click) throws Exception {
//		String visitorID = VisitorID.getText();
//		 if (isValidID(visitorID)) { // checking if ID is valid
//				try {
//					// open visitor screen
//					Parent root = new FXMLLoader().load(getClass().getResource("HomePageFrame.fxml"));
//					Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
//					Scene scene = new Scene(root);	
//					
//					stage.setTitle("Visitor Screen");
//					
//					stage.setScene(scene);		
//					stage.show();
//					
//					} catch(Exception e) {
//						System.out.print("Something went wrong while clicking on visitor login button, check stack trace");
//						e.printStackTrace();
//					}
//		 } else {
//			 
//			 System.out.print("visitor ID is not valid");
//		 }
//	}
//	
	// validate ID of visitor
	private boolean isValidID(String id) {
	    return id != null && id.matches("\\d{9}");
	}
	
	public void BackBtn(ActionEvent click) throws Exception{
		//Function for opening a new scene when clicking on the Back Button
	try {
		
		Parent root = new FXMLLoader().load(getClass().getResource("HomePageFrame.fxml"));
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		Scene scene = new Scene(root);	
		
		stage.setTitle("Home Page");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
	}
}
