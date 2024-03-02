package gui;

import java.util.ArrayList;

import client.ClientUI;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserUpdateController {
	
	@FXML
    private Button backBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private TextField parkNameText;

    @FXML
    private TextField telephoneNumText;
    
    @FXML
    private TextField orderId;

    @FXML
    private Button getOrderData;
	
	
	public void btnBack(ActionEvent click) throws Exception{
		//Function for opening a new scene when clicking on the Update Reservation Button
	
	
	try {
		
		Parent root = new FXMLLoader().load(getClass().getResource("UserInterfaceFrame.fxml"));
		Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
		Scene scene = new Scene(root);	
		
		stage.setTitle("User Menu");
		
		stage.setScene(scene);		
		stage.show();
		
		}catch(Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}
		
	}
	
	
	public void getReservesion(ActionEvent click) throws Exception{
		try {
			
		String orderIdValue = orderId.getText();
		
		String command = "GET ORDER| " + orderIdValue;
		
		ClientUI.clientControllerInstance.accept(command);
		
		
		loadOrderData(ClientUI.clientControllerInstance.getData());
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadOrderData(ArrayList<String> userDate) {
		String parkname = userDate.get(0);
		String phoneNum = userDate.get(1);
		
		parkNameText.setText(parkname);
		telephoneNumText.setText(phoneNum);
	}
	
	public void updateReservationPhoneAndParkName(ActionEvent click) throws Exception{
		String orderIdValue = orderId.getText();
		String parkname = parkNameText.getText();
		String phoneNum = telephoneNumText.getText();
		
		String command = "SET ORDER|" + orderIdValue +" "+ parkname +" "+ phoneNum;
		
		ClientUI.clientControllerInstance.accept(command);
		
	}
}
