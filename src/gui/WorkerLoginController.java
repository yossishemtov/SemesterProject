package gui;

import client.ClientUI;
import client.InputValidation;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.worker.DepartmentManager;
import common.worker.GeneralParkWorker;
import common.worker.ParkManager;
import common.worker.ParkWorker;
import common.worker.ServiceWorker;
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
	
	public void LoginBtn(ActionEvent click) throws Exception {
		 String workerUsername = WorkerUsername.getText(); // get worker username
		 String workerPassword = WorkerPwd.getText(); // get worker password
		 
		 Alerts alertUsername = InputValidation.validateUsername(workerUsername); 
		 Alerts alertPwd = InputValidation.validatePassword(workerPassword); 
		 Boolean usernameValide = alertUsername.getAlertType().toString().equals("INFORMATION");
		 Boolean PasswordValid = alertPwd.getAlertType().toString().equals("INFORMATION");
		 if( usernameValide && PasswordValid) {
			 
			 try {
				// creating an instance of general park worker to send to server
				 GeneralParkWorker workerforcheck = new GeneralParkWorker(null,null,null,null,null,workerUsername,workerPassword);
				 ClientServerMessage WorkerLoginAttemptInformation = new ClientServerMessage(workerforcheck, Operation.GetWorkerInfo);
			     ClientUI.clientControllerInstance.sendMessageToServer(WorkerLoginAttemptInformation);
			     // get the data return from server 
			     ClientServerMessage dataFromServer = (ClientUI.clientControllerInstance.getData());
			     // check witch worker we get from DB
			     if (dataFromServer.getDataTransfered() instanceof ParkManager) {
			    	 // render to park manager screen 
			    	 Parent root = new FXMLLoader().load(getClass().getResource("ParkManagerFrame.fxml"));
			    	 Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			    	 Scene scene = new Scene(root);

			    	 stage.setTitle("Park Manager Screen");
			    	 stage.setScene(scene);
			    	 stage.show();
			    	 
			     }
			     else if (dataFromServer.getDataTransfered() instanceof ParkWorker) {
			    	// render to park worker screen 
			    	 Parent root = new FXMLLoader().load(getClass().getResource("ParkWorkerFrame.fxml"));
			    	 Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			    	 Scene scene = new Scene(root);

			    	 stage.setTitle("Park Worker Screen");
			    	 stage.setScene(scene);
			    	 stage.show();
			     }
			     else if (dataFromServer.getDataTransfered() instanceof ServiceWorker) {
			    	// render to service worker screen 
			    	 Parent root = new FXMLLoader().load(getClass().getResource("ServiceWorkerFrame.fxml"));
			    	 Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			    	 Scene scene = new Scene(root);

			    	 stage.setTitle("Service Worker Screen");
			    	 stage.setScene(scene);
			    	 stage.show();
			     }
			     else if (dataFromServer.getDataTransfered() instanceof DepartmentManager) {
			    	 // render to department manager screen 
			    	 Parent root = new FXMLLoader().load(getClass().getResource("DepartmentManagerFrame.fxml"));
			    	 Stage stage = (Stage)((Node)click.getSource()).getScene().getWindow(); //hiding primary window
			    	 Scene scene = new Scene(root);

			    	 stage.setTitle("Department Manager Screen");
			    	 stage.setScene(scene);
			    	 stage.show();
			     }
			     else {
			    	 if (dataFromServer.getDataTransfered() == null ) {
			    		 new Alerts(Alerts.AlertType.ERROR, "Invalid username or password", "","Invalid username or password" );
			    	 }
			     }
			 } catch (Exception e) {
		            System.out.print("Something went wrong while clicking on visitor login button, check stack trace");
		            e.printStackTrace();
		        }
		} 
		 else {
				// Display the error alert to the user
				 alertUsername.showAndWait();
				 alertPwd.showAndWait();
			 }
	}
			 
	}


