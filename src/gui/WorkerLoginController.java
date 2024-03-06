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

public class WorkerLoginController {
	
	@FXML
    private Button LoginBtn;
	@FXML
    private Button BackBtn;
	@FXML
    private TextField WorkerUsername;
	@FXML
    private TextField WorkerPwd;
	
	
//	public void start(Stage primaryStage) throws Exception {
//		
//		//Starting the root scene of the VisitorLogin
//		try {	
//			
//			Parent root = FXMLLoader.load(getClass().getResource("WorkerrLoginFrame.fxml"));
//			Scene scene = new Scene(root);
//			primaryStage.setTitle("Worker Login");
//			primaryStage.setScene(scene);
//			primaryStage.show();
//			
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
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

