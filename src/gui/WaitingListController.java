package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;

import client.ClientUI;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Usermanager;
import common.WaitingChecker;
import common.WaitingList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class WaitingListController implements Initializable { 

	@FXML
	private AnchorPane RePane;

	@FXML
	private Button btnSubmit;

	@FXML
	private Button btnWaiting;

    @FXML
    private Button Okay;
    
    @FXML
    private Label Timetxt;
    
	@FXML
	private JFXDatePicker txtDate;

	@FXML
	private JFXComboBox<String> TimeComboBox;
	
    @FXML
    private JFXListView<String> DatesToPick;
    
    @FXML
    private Pane WaitingSucc;

    @FXML
    private Label summaryPark;

    @FXML
    private Label summaryDate;

    @FXML
    private Label summaryTime;

    @FXML
    private Label summaryVisitors;

    @FXML
    private Label summaryPrice;

    @FXML
    private Label placeList;
    
    @FXML
    private AnchorPane loadingPane;
	
    private Order order;


	/**
     * Initializes the waiting list view.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Hide other elements initially
	    DatesToPick.setVisible(false);
    	WaitingSucc.setVisible(false);
	    // Show loading pane
	    loadingPane.setVisible(true);
	    Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				getAvailableDatesList();
				return true;
			}
		};

		new Thread(task).start();

		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				// Hide loading pane
	            loadingPane.setVisible(false);
	            // Show other elements
	            DatesToPick.setVisible(true);			}
		});

	}

	/**
     * Handles the action event for entering the waiting list.
     * @param event The action event.
     * @throws IOException If an I/O error occurs.
     */
	@FXML
	void EnterWaitingList(ActionEvent event) throws IOException {

	 
		String visitDate = order.getDate().toString();
		String visitTime = order.getVisitTime().toString();
		String parkName = order.getParkName();
	    Integer waitingId = 1;

		
		waitingId = WaitingChecker.GetLastWaiting();
		if (waitingId != null) {
			waitingId++;
		}
		
		Integer rightPlace = WaitingChecker.FindRightPlaceInWaiting(visitDate, visitTime, parkName);
    	
		WaitingList waiting = new WaitingList(order.getOrderId(), order.getVisitorId(),
				order.getParkNumber(), order.getAmountOfVisitors(), order.getPrice(),
				order.getVisitorEmail(), order.getDate(), order.getVisitTime(), order.getOrderStatus(),
				order.getTypeOfOrder(), order.getTelephoneNumber(), order.getParkName(), waitingId, rightPlace);

    	Boolean isNewWaiting = WaitingChecker.PostNewWaitingList(waiting);
		
    	if(isNewWaiting) {
    		this.summaryPark.setText(order.getParkName());
    		this.summaryDate.setText(order.getDate().toString());
    		this.summaryTime.setText(order.getVisitTime().toString());
    		this.summaryVisitors.setText(order.getAmountOfVisitors()+"");
    		this.summaryPrice.setText(order.getPrice()+"");
    		this.placeList.setText(waiting.getPlaceInList()+"");
    		WaitingSucc.setVisible(true);

    	}
	}
	

	@FXML
	void OkButton(ActionEvent event) throws IOException { 
		Stage stage = (Stage) Okay.getScene().getWindow();
		if(Usermanager.GetisNewTraveler())
			NavigationManager.openPage("HomePageFrame.fxml", event, null, false, true);
	    stage.close(); // Close the current stage

	}


	/**
     * Gets the list of available alternative dates.
     * @return The list of available alternative dates.
     */
	@SuppressWarnings("unchecked")
	private void getAvailableDatesList() {
		ClientServerMessage<?> findDates = new ClientServerMessage<>(order, Operation.FIND_AVAILABLE_DATES);
		ClientUI.clientControllerInstance.sendMessageToServer(findDates);
	    ClientServerMessage<?> findDatesMsg = ClientUI.clientControllerInstance.getData();
	    ArrayList<String> orders = (ArrayList<String>) findDatesMsg.getDataTransfered();
		DatesToPick.getItems().addAll(orders);

	}
	
	/**
     * Submits the selected date and time from the waiting list.
     */
	@FXML
	private void SubmitOrderbtn() {
		String newDateAndTime = DatesToPick.getSelectionModel().getSelectedItem();
		String newDate = newDateAndTime.split(",")[0];
		String newTime = newDateAndTime.split(",")[1].trim();
		OrderAVisitController.setDateTime(newDate, newTime);
		Stage stage2 = (Stage) btnSubmit.getScene().getWindow();
		stage2.close();

	}

	/**
     * Sets the time for the reschedule order in OrderVisit screen.
     */
	public void setTimeLabel(String DateAndTime) {
		this.Timetxt.setText(DateAndTime);
	}

	/**
     * Sets the order for the waiting list.
     * @param order The order to set.
     */
	public void setOrder(Order order) {
		this.order = order;
	}




}