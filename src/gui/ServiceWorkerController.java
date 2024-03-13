package gui;


import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.net.URL;
import client.NavigationManager;
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
			NavigationManager.openPage("WorkerLoginFrame.fxml", click, "Worker Login", true);
		} catch(Exception e) {
				System.out.print("Something went wrong while clicking on the back button, check stack trace");
				e.printStackTrace();
				
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

		 
	 

