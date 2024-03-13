package gui;


import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;

import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ServiceWorkerController implements Initializable {

    @FXML
    private JFXTextField ServiceWorkerName;

    @FXML
    private JFXTextField serviceWorkerEmail;

    @FXML
    private JFXTextField serviceRole;

    @FXML
    private JFXTextField serviceWorksAtPark;

    @FXML
    private Button logoutButton;

    @FXML
    private JFXButton registration;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		 // get the current loggedin service worker
		 GeneralParkWorker serviceWorker = Usermanager.getCurrentWorker();
		 // get the details of the service worker and set into labels
		 String fullName =  serviceWorker.getFirstName() + " " + serviceWorker.getLastName();
		 String email = serviceWorker.getEmail();
		 String role = serviceWorker.getRole();
		 Integer worksAt = serviceWorker.getWorksAtPark();
		 System.out.println(fullName);
		 // set service worker details
		 ServiceWorkerName.setText(fullName);
		 serviceWorkerEmail.setText(email);
		 serviceRole.setText(role);
		 serviceWorksAtPark.setText(Integer.toString(worksAt));
	 }
	 public void LogoutBtn(ActionEvent click) throws Exception{
			//Function for opening a new scene when clicking on the Back Button
		try {
			
			if(Usermanager.getCurrentWorker() != null) {
    			ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
    			ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
    			
    		}
			
			NavigationManager.openPage("HomePageFrame.fxml", click, "User Menu", true);
		} catch(Exception e) {
				e.printStackTrace();
    			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when trying to return to main menu");
    			somethingWentWrong.showAndWait();
			}
		}
	 
	 public void registerNewGuide(ActionEvent click) throws Exception {
		 // navigate to register form screen
		 try {
			 NavigationManager.openPage("RegisterNewGuide.fxml", click, "departmentManagerScreen", true);
		 } catch(Exception e) {
			 System.out.print("Something went wrong while clicking on register new guide button, check stack trace");
			 e.printStackTrace();
			}
	 }
	 
}

		 
	 

