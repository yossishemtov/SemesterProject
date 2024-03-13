package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.GeneralParkWorker;
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
    
    
    
    public void availableSpaceBtnAction(ActionEvent click) throws Exception{
    	//Receiving information about the park, sending the worker information
    	
    	try {
    	
    	GeneralParkWorker loggedInWorker = Usermanager.getCurrentWorker();
    	
    	//Send the worker with command to retrieve information about the park the worker works at.
    	ClientServerMessage sendRequestForParkInformation = new ClientServerMessage(loggedInWorker, Operation.GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER);
    	ClientUI.clientControllerInstance.sendMessageToServer(sendRequestForParkInformation);
    	
    	
    	Park parkInformation = (Park)(ClientUI.clientControllerInstance.getData()).getDataTransfered();
    	
    	String showNumberOfVisitorsAndCapacity = Integer.toString(parkInformation.getCurrentVisitors()) + "/" + Integer.toString(parkInformation.getCapacity());
    	
    	availableSpaceLabel.setText(showNumberOfVisitorsAndCapacity);
    	
    	}catch (Exception e){
    		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when receiving park current amount of visitors");
			somethingWentWrong.showAndWait();
    	}
    	
    	
    	
    }
    
    
    public void presentBillBtnAction(ActionEvent click) throws Exception{
    	
    }

    
    public void backBtnAction(ActionEvent click) throws Exception{
    		//Loading main login screen when clicking on the back button
    	try {
	    		if(Usermanager.getCurrentWorker() != null) {
	    			ClientServerMessage requestToLogout = new ClientServerMessage(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
	    			ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
	    			
	    		}
	    		
	    		//Changing page back to main menu
	    		NavigationManager.openPage("HomePageFrame.fxml", click, "Home Page", true);
    		
    		}catch(Exception e) {
    			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when trying to return to main menu");
    			somethingWentWrong.showAndWait();
    		}
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Render user information when coming to the parkWorkerFrame screen
		
		try {
			//Parsing worker information to the screen
			GeneralParkWorker loggedInWorker = Usermanager.getCurrentWorker();
			
			String firstNameLoggedInUser = loggedInWorker.getFirstName();
			String lastNameLoggedInUser = loggedInWorker.getLastName();
			String roleLoggedInUser = loggedInWorker.getRole();
			
			nametextlabel.setText(firstNameLoggedInUser);
			lastnametextlabel.setText(lastNameLoggedInUser);
			roletextlabel.setText(roleLoggedInUser);
		
		}catch(Exception e) {
			
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading user information");
			somethingWentWrong.showAndWait();
		}
			
	}

}
