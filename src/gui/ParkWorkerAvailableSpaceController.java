package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Park;
import common.Usermanager;
import common.worker.GeneralParkWorker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

public class ParkWorkerAvailableSpaceController implements Initializable {
	@FXML
    private Label parkspace;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
	    	
	    	GeneralParkWorker loggedInWorker = Usermanager.getCurrentWorker();
	    	
	    	//Send the worker with command to retrieve information about the park the worker works at.
	    	ClientServerMessage sendRequestForParkInformation = new ClientServerMessage(loggedInWorker, Operation.GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER);
	    	ClientUI.clientControllerInstance.sendMessageToServer(sendRequestForParkInformation);
	    	
	    	
	    	Park parkInformation = (Park)(ClientUI.clientControllerInstance.getData()).getDataTransfered();
	    	
	    	String showNumberOfVisitorsAndCapacity = Integer.toString(parkInformation.getCurrentVisitors()) + "/" + Integer.toString(parkInformation.getCapacity());
	    	
	    	parkspace.setText(showNumberOfVisitorsAndCapacity);
    	
    	}catch (Exception e){
    		Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR","", "Something went wrong when receiving park current amount of visitors");
			somethingWentWrong.showAndWait();
    	}
		
	}

}
