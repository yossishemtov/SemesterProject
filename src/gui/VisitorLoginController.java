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

import client.InputValidation;
import common.Alerts;


public class VisitorLoginController {
	@FXML
    private Button LoginBtn;
	@FXML
    private Button BackBtn;
	@FXML
    private TextField VisitorID;
	
	public void LoginBtn(ActionEvent click) throws Exception {
	    String visitorID = VisitorID.getText(); // get the id
	    Alerts alertID = InputValidation.ValidateVisitorID(visitorID); // get the right alert

	    if (alertID.getAlertType().toString().equals("INFORMATION")) { // if entered right ID
	        try {
	            // open visitor screen 
	            Parent root = new FXMLLoader().load(getClass().getResource("VisitorFrame.fxml"));
	            Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
	            Scene scene = new Scene(root);

	            stage.setTitle("Visitor Screen");
	            stage.setScene(scene);
	            stage.show();

	        } catch (Exception e) {
	            System.out.print("Something went wrong while clicking on visitor login button, check stack trace");
	            e.printStackTrace();
	        }
	    } else {
	        // Display the error alert to the user
	        alertID.showAndWait();
	    }
	}
	
	public void BackBtn(ActionEvent click) throws Exception{
		// Opening home page scene when clicking on the Back Button from visitor login scene
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
