package gui;

import java.io.IOException;
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
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ParkWorkerController implements Initializable {

	@FXML
	private JFXButton profileBtn;

	@FXML
	private JFXButton availableSpaceBtn;

	@FXML
	private JFXButton entrenceControlBtn;

	@FXML
	private JFXButton LogoutBtn;

    @FXML
    private Label nametextlabel;

    @FXML
    private Label lastnametextlabel;

    @FXML
    private Label roletextlabel;

    @FXML
    private Label parktextlabel;

    @FXML
    private Label availableSpaceLabel;
    
    @FXML
    private BorderPane mainPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private VBox vbox;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Render user information when coming to the parkWorkerFrame screen
		
		try {
			loadProfileImmediately();
			//Parsing worker information to the screen
			GeneralParkWorker loggedInWorker = Usermanager.getCurrentWorker();
		
		}catch(Exception e) {
			
			Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading user information");
			somethingWentWrong.showAndWait();
		
		}
		}

	   private void loadProfileImmediately() {
	        try {
	        	loadProfileOfWorker(null); 
	        } 
	         catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }


    
public void loadProfileOfWorker(ActionEvent click) throws Exception{
	//Loading profile of the current park worker
	try {
		 NavigationManager.openPageInCenter(mainPane,"Profile.fxml");
	 } catch(Exception e) {
		 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading profile");
			somethingWentWrong.showAndWait();
		 e.printStackTrace();
	 }
    } 

    
    
    public void availableSpaceBtnAction(ActionEvent click) throws Exception{
    	//Displaying the available space in the park
    	try {
   		 NavigationManager.openPageInCenter(mainPane,"ParkWorkerAvailableSpace.fxml");
   	 } catch(Exception e) {
   		 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading checking available space");
   			somethingWentWrong.showAndWait();
   		 e.printStackTrace();
   	 }

  }
    
    
    public void entrenceControlAction(ActionEvent click) throws Exception{
    	//Loading the entrenceControl page
    	try {
      		 NavigationManager.openPageInCenter(mainPane,"ParkWorkerEntrenceControl.fxml");
      	 } catch(Exception e) {
      		 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading parkworker entrence control");
      			somethingWentWrong.showAndWait();
      		 e.printStackTrace();
      	 }
    }
    
    public void unorderedVisitAction(ActionEvent click) throws Exception{
    	//Loading unordered visit system page
    	try {
     		 NavigationManager.openPageInCenter(mainPane,"ParkWorkerUnorderedVisit.fxml");
     	 } catch(Exception e) {
     		 Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when loading unordered visit system");
     			somethingWentWrong.showAndWait();
     		 e.printStackTrace();
     	 }
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


			
	}


