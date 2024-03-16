package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

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
import common.worker.ChangeRequest;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class TravelerOrdersController implements Initializable {

	
    @FXML
    private TableView<?> TravelerOrders;

    @FXML
    private TableColumn<Order, String> orderIDCol;

    @FXML
    private TableColumn<Order, String> ParkIDCol;

    @FXML
    private TableColumn<Order, String> AmtOfVisitorsCol;

    @FXML
    private TableColumn<Order, String> PriceCol;

    @FXML
    private TableColumn<Order, String> DateCol;

    @FXML
    private TableColumn<Order, String> VisitTimeCol;

    @FXML
    private TableColumn<Order, String> StatusCol;

    @FXML
    private TableColumn<Order, String> TypeOfVisitCol;

    @FXML
    private JFXButton camcelOrder;

    @FXML
    private JFXButton ConfirmOrder;
//	@FXML
//	private TextField OrderID;
//
//	@FXML
//	private Button editOrderBtn;
//
//	@FXML
//	private Button backbtn;
//
//	@FXML
//	private TableColumn<Order, String> orderIdCol;
//	
//	@FXML
//	private TableColumn<Order, String> travelerIdCol;
//	
//	@FXML
//	private TableColumn<Order, String> travelerEmailCol;
//	
//	@FXML
//	private TableColumn<Order, String> telephoneNumberCol;
//
//	@FXML
//	private TableColumn<Order, String> parkNumberCol;
//
//	@FXML
//	private TableColumn<Order, String> amountOfVisitorsCol;
//
//	@FXML
//	private TableColumn<Order, String> priceCol;
//
//	@FXML
//	private TableColumn<Order, String> dateCol;
//
//	@FXML
//	private TableColumn<Order, String> visitTimeCol;
//
//	@FXML
//	private TableColumn<Order, String> orderStatusCol;
//
//	@FXML
//	private TableColumn<Order, String> orderTypeCol;
//	
//	@FXML
//	private TableView<Order> OrdersTable;

	static HashMap<Integer, Order> orderHashMap = new HashMap<>();

	private Order orderToOpen;
	
	private ObservableList<Order> ordersData = FXCollections.observableArrayList();
	
	private Traveler currentTraveler;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load orders data from the database and initialize the table
		//currentTraveler = Usermanager.getCurrentTraveler();
		System.out.println("1");
		currentTraveler = new Traveler(1214214, null, null, null, null, null, null);
		System.out.println(currentTraveler.getId());
		System.out.println("1");
		setupTableColumns();
		System.out.println("1");
		loadOrdersFromDatabase();
	}

	private void loadOrdersFromDatabase() {
		ClientServerMessage<?> clientServerMessage = new ClientServerMessage<>(currentTraveler,Operation.GET_ALL_ORDERS);
		System.out.println("1");
		ClientUI.clientControllerInstance.sendMessageToServer(clientServerMessage);
		System.out.println("1");
		waitForServerResponse();

		/*try {
			// The client controller receives the command to pass it further
			// (It stops the execution flow for the client until the answer is received)
			loadOrdersData(ClientUI.clientControllerInstance.getData());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	/**
     * Sets up the table columns with the corresponding properties from the ChangeRequest class.
     */
    private void setupTableColumns() {
        // Binding table columns to ChangeRequest properties
    	orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    	travelerIdCol.setCellValueFactory(new PropertyValueFactory<>("travelerId"));
		parkNumberCol.setCellValueFactory(new PropertyValueFactory<>("parkNumber"));
		amountOfVisitorsCol.setCellValueFactory(new PropertyValueFactory<>("amountOfVisitors"));
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		travelerEmailCol.setCellValueFactory(new PropertyValueFactory<>("travelerEmail"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		telephoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
		visitTimeCol.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
		orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		orderTypeCol.setCellValueFactory(new PropertyValueFactory<>("orderType"));
    }
	
	/**
     * Waits for a server response asynchronously and updates the UI with the fetched data.
     */
	private void waitForServerResponse() {
        new Thread(() -> {
            try {
                Thread.sleep(10); // Simulated wait for server response
                // Check server response and update UI accordingly
                ClientServerMessage<?> servermsg = ClientUI.clientControllerInstance.getData();
                System.out.println("1");
                if (servermsg.getFlag()) {
                    List<Order> ordersList = (List<Order>) servermsg.getDataTransfered();
                    System.out.println("1");
                    // Iterating through the list of orders and adding them to the HashMap
                    // Clear the existing HashMap
            		orderHashMap.clear();
        			for (Order order : ordersList) {
        				orderHashMap.put(order.getOrderId(), order);
        			}
                    Platform.runLater(() -> {
                        ordersData.setAll(ordersList);
                        OrdersTable.setItems(ordersData);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

	public Order getOrderById(Integer orderId) {
		return orderHashMap.get(orderId);
	}

	public void btnBack(ActionEvent click) throws Exception {

		NavigationManager.openPage("VisitorFrame.fxml", click, "Visitor frame", true);

	}

	
	/*public <T> void loadOrdersData(ClientServerMessage<T> allOrders) throws Exception {

	//ArrayList<Order> ordersList = (ArrayList<Order>) allOrders.convertDataToArrayList();

	// Clear the existing HashMap
	//orderHashMap.clear();

	try {
		// Iterating through the list of orders and adding them to the HashMap
		//for (Order order : ordersList) {
		//	orderHashMap.put(order.getOrderId(), order);
		//}

		orderIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderId())));
		parkNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getParkNumber())));
		amountOfVisitorsCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAmountOfVisitors())));
		priceCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));
		dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDate())));
		visitTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVisitTime())));
		orderStatusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus()));
		orderTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeOfOrder()));

		// Populate the TableView with data
		//ObservableList<Order> ordersObservableList = FXCollections.observableArrayList(ordersList);
		//OrdersTable.setItems(ordersObservableList);
	} catch (Exception e) {
		e.printStackTrace();
	}

}*/
	
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

}