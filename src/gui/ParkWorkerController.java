package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.worker.ParkWorker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ParkWorkerController implements Initializable {
    @FXML
    private Button availableSpaceBtn;

    @FXML
    private Button PresentABillBtn;

    @FXML
    private Label nametextlabel;

    @FXML
    private Label lastnametextlabel;

    @FXML
    private Label roletextlabel;

    @FXML
    private Label parktextlabel;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Label availableSpaceLabel;
    
    public static ParkWorker currentSignedInParkWorker;
    
    
    public void availableSpaceBtnAction(ActionEvent click) throws Exception{
    	//Receiving information about the park, sending the worker information
    	ClientServerMessage sendRequestForParkInformation = new ClientServerMessage(ParkWorkerController.currentSignedInParkWorker, Operation.GET_GENERAL_PARK_WORKER_DETAILS);
    	ClientUI.clientControllerInstance.sendMessageToServer(sendRequestForParkInformation);
    	
    	//Park parkInformation = (Park)ClientUI.clientControllerInstance.getData();
    	
    	
    	
    	
    }
    
    
    public void presentBillBtnAction(ActionEvent click) throws Exception{
    	
    }

    
    public void backBtnAction(ActionEvent click) throws Exception{
    		//Loading main login screen when clicking on the back button
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


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Render user information when coming to the parkWorkerFrame screen
		
		try {
			
			String firstNameLoggedInUser = ParkWorkerController.currentSignedInParkWorker.getFirstName();
			String lastNameLoggedInUser = ParkWorkerController.currentSignedInParkWorker.getLastName();
			String roleLoggedInUser = ParkWorkerController.currentSignedInParkWorker.getRole();
			
			nametextlabel.setText(firstNameLoggedInUser);
			lastnametextlabel.setText(lastNameLoggedInUser);
			roletextlabel.setText(roleLoggedInUser);
		
		}catch(Exception e) {
			
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading user information");
			somethingWentWrong.showAndWait();
		}
			
	}

}
