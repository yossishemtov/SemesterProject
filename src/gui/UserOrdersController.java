package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import client.ClientUI;
import client.NavigationManager;
import common.Alerts;
import common.ClientServerMessage;
import common.Operation;
import common.Order;
import common.Traveler;
import common.Usermanager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class UserOrdersController implements Initializable {

	@FXML
	private TableView<Order> OrdersTable;

	@FXML
	private TextField OrderID;

	@FXML
	private Button editOrderBtn;

	@FXML
	private Button backbtn;

	@FXML
	private TableColumn<Order, String> orderIdCol;

	@FXML
	private TableColumn<Order, String> parkNumberCol;

	@FXML
	private TableColumn<Order, String> amountOfVisitorsCol;

	@FXML
	private TableColumn<Order, String> priceCol;

	@FXML
	private TableColumn<Order, String> dateCol;

	@FXML
	private TableColumn<Order, String> visitTimeCol;

	@FXML
	private TableColumn<Order, String> orderStatusCol;

	@FXML
	private TableColumn<Order, String> orderTypeCol;

	static HashMap<Integer, Order> orderHashMap = new HashMap<>();

	private Order orderToOpen;
	
	Traveler currentTraveler = Usermanager.getCurrentTraveler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load orders data from the database and initialize the table
		loadOrdersFromDatabase();
	}

	private void loadOrdersFromDatabase() {
		ClientServerMessage<?> clientServerMessage = new ClientServerMessage<>(currentTraveler,
				Operation.GET_ALL_ORDERS);
		ClientUI.clientControllerInstance.sendMessageToServer(clientServerMessage);

		try {
			// The client controller receives the command to pass it further
			// (It stops the execution flow for the client until the answer is received)
			loadOrdersData(ClientUI.clientControllerInstance.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> void loadOrdersData(ClientServerMessage<T> allOrders) throws Exception {

		ArrayList<Order> ordersList = (ArrayList<Order>) allOrders.convertDataToArrayList();

		// Clear the existing HashMap
		orderHashMap.clear();

		try {
			// Iterating through the list of orders and adding them to the HashMap
			for (Order order : ordersList) {
				orderHashMap.put(order.getOrderId(), order);
			}

			orderIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderId())));
			parkNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getParkName())));
			amountOfVisitorsCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAmountOfVisitors())));
			priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
			dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate())));
			visitTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVisitTime())));
			orderStatusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus()));
			orderTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeOfOrder()));

			// Populate the TableView with data
			ObservableList<Order> ordersObservableList = FXCollections.observableArrayList(ordersList);
			OrdersTable.setItems(ordersObservableList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Order getOrderById(Integer orderId) {
		return orderHashMap.get(orderId);
	}

	public void editOrderBtn(ActionEvent click) throws Exception {

		String orderID = OrderID.getText(); // get the order ID

		if (orderID != null) {
			// find the right order to open
			orderToOpen = getOrderById(Integer.parseInt(orderID));

		}

		else {
			// Display the error alert for missing order ID
	        Alerts alertID = new Alerts(AlertType.ERROR, "Error", "Missing Order ID", "Please enter the order ID.");
	        alertID.showAndWait();
	        return;
		}

		if (orderToOpen != null) {
			// if entered the right order ID
			OrderFrameController orderFrameController = new OrderFrameController(orderToOpen);
			NavigationManager.openPage("OrderFrame.fxml", click, "Order", true);

		}

		else {
			// Display the error alert for order not found
	        Alerts alertNotFound = new Alerts(AlertType.ERROR, "Error", "Order Not Found", "The entered order ID was not found.");
	        alertNotFound.showAndWait();
		}

	}

	public void btnBack(ActionEvent click) throws Exception {

		NavigationManager.openPage("VisitorFrame.fxml", click, "Visitor frame", true);

	}

	
	
	


}