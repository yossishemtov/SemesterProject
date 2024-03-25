package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;

import client.ClientUI;
import common.ClientServerMessage;
import common.GetInstance;
import common.Operation;
import common.Order;
import common.OrderChecker;
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
	private static ArrayList<Object> anotherDates = new ArrayList<>();
	private static int setDateFromWaitList = 0;

	/**
     * Getter for the flag indicating whether to set the date from the wait list.
     * @return 1 if the date should be set from the wait list, 0 otherwise.
     */
	public static int getSetDateFromWaitList() {
		return setDateFromWaitList;
	}

	public static void setSetDateFromWaitList(int setDateFromWaitList) {
		WaitingListController.setDateFromWaitList = setDateFromWaitList;
	}

	/**
     * Getter for the list of alternative dates.
     * @return The list of alternative dates.
     */
	public static ArrayList<Object> getAnotherDates() {
		return anotherDates;
	}

	public static void setAnotherDates(ArrayList<Object> anotherDates) {
		WaitingListController.anotherDates = anotherDates;
	}
	
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
	    stage.close(); // Close the current stage

	    // Enable the OrderAVisit pane
	    OrderAVisitController orderAVisitController = GetInstance.getInstance().getOrderC();
	    orderAVisitController.getPaneOrder().setDisable(false);
	}


	/**
     * Gets the list of available alternative dates.
     * @return The list of available alternative dates.
     */
	private void getAvailableDatesList() {
		Order tempOrder = new Order(order.getOrderId(), order.getVisitorId(), order.getParkNumber(), order.getAmountOfVisitors(),
				order.getPrice(),order.getVisitorEmail(), order.getDate(), order.getVisitTime(), order.getOrderStatus(),
				order.getTypeOfOrder(), order.getTelephoneNumber(), order.getParkName());
		LocalDate originalDate = order.getDate(); 


		LocalTime currentTime = LocalTime.parse("08:00");
		LocalDate currentDate = originalDate;
		int counter = 0;
		ArrayList<String> availableDatesList = new ArrayList<String>();
		while (counter != 10) {
			tempOrder.setDate(currentDate);
			tempOrder.setVisitTime(currentTime);;
			
			if (OrderChecker.isDateAvailable(tempOrder)) {
				availableDatesList.add(tempOrder.getDate().toString() + ", " + tempOrder.getVisitTime().toString());
				counter++;
			}

			LocalTime hour = currentTime.plusHours(1);

			if (hour.isAfter(LocalTime.of(17, 0))) {
				currentTime = LocalTime.parse("08:00");

				// Date after adding the days to the given date
				currentDate = currentDate.plusDays(1);
			} else {
				currentTime = currentTime.plusHours(1);
			}

		}
		DatesToPick.getItems().addAll(availableDatesList);

	}
	
	/**
     * Submits the selected date and time from the waiting list.
     */
	@FXML
	private void SubmitOrderbtn() {
		WaitingListController.setSetDateFromWaitList(1);
		String newDateAndTime = DatesToPick.getSelectionModel().getSelectedItem();
		String newDate = newDateAndTime.split(",")[0];
		String newTime = newDateAndTime.split(",")[1].trim();
		anotherDates.add(newDate);
		anotherDates.add(newTime);
		OrderAVisitController Ordercon = GetInstance.getInstance().getOrderC();
		Ordercon.getPaneOrder().setDisable(false);
		Ordercon.initialize(Ordercon.getLocation(), Ordercon.getResources());
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








