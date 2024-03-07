package gui;

import java.util.ArrayList;
import javafx.scene.control.TableView;

import client.ClientUI;
import clientEntities.Reservation;
import common.ClientServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class UserOrdersController {
	private String Command;

	@FXML
	private TableView<Reservation> reservationsTable;

	@FXML
	private Button backbtn;

	@FXML
	private TableColumn<Reservation, String> ParkNameCol;

	@FXML
	private TableColumn<Reservation, String> OrderNumberCol;

	@FXML
	private TableColumn<Reservation, String> TimeOfVisitCol;

	@FXML
	private TableColumn<Reservation, String> NumberOfVistiorsCol;

	@FXML
	private TableColumn<Reservation, String> TelephoneNumberCol;

	@FXML
	private TableColumn<Reservation, String> EmailAddressCol;

	@FXML
	private Button showOrdersBtn;

	public void btnBack(ActionEvent click) throws Exception {
		// Function for opening a new scene when clicking on the Update Reservation
		// Button

		try {

			new FXMLLoader();
			Parent root = FXMLLoader.load(getClass().getResource("UserInterfaceFrame.fxml"));
			Stage stage = (Stage) ((Node) click.getSource()).getScene().getWindow(); // hiding primary window
			Scene scene = new Scene(root);

			stage.setTitle("User Menu");

			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			System.out.print("Something went wrong while clicking on the back button, check stack trace");
			e.printStackTrace();
		}

	}

	public void showOrdersBtn(ActionEvent click) throws Exception {
		ClientServerMessage clientServerMessageInstanch = new ClientServerMessage("", "ALLORDERSOFVISITOR");


		try {
			// The client controller receives the command to pass it further
			// (It stops the execution flow for the client until answer received)

			//ClientUI.clientControllerInstance.accept(Command);

			loadOrdersData(ClientUI.clientControllerInstance.getData());
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public <T> void loadOrdersData(ClientServerMessage<T> allReservations) {
		// Login for handling the specific reservations of a user
		String concatenatedString = "ParkName1 OrderNumber1 TimeOfVisit1 NumberOfVisitors1 TelephoneNumber1 Email1";
		ArrayList<Reservation> reservationsObjs = new ArrayList<Reservation>();

		// Setting up javafx columns
		ParkNameCol.setCellValueFactory(cellData -> cellData.getValue().parkNameProperty());
		OrderNumberCol.setCellValueFactory(cellData -> cellData.getValue().orderNumberProperty());
		TimeOfVisitCol.setCellValueFactory(cellData -> cellData.getValue().timeOfVisitProperty());
		NumberOfVistiorsCol.setCellValueFactory(cellData -> cellData.getValue().numberOfVisitorsProperty());
		TelephoneNumberCol.setCellValueFactory(cellData -> cellData.getValue().telphoneNumberProperty());
		EmailAddressCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

		try {

			// Iterating through the string template to extract it to a Reservation object
			for (T reservation : allReservations.convertDataToArrayList()) {

				System.out.println(reservation);
				String[] fields = ((String) reservation).split(" ");
				String parkName;
				String orderNumber;
				String timeOfVisit;
				String numberOfVisitors;
				String telphoneNumber;
				String email;

				if (isNumeric(fields[1])) {
					parkName = fields[0];
					orderNumber = fields[1];
					timeOfVisit = fields[2] + " " + fields[3];
					numberOfVisitors = fields[4];
					telphoneNumber = fields[5];
					email = fields[6];
					orderNumber = fields[1];
				} else {
					parkName = fields[0] + fields[1];
					orderNumber = fields[2];
					timeOfVisit = fields[3] + " " + fields[4];
					numberOfVisitors = fields[5];
					telphoneNumber = fields[6];
					email = fields[7];
				}

				Reservation receivedRez = new Reservation(parkName, orderNumber, timeOfVisit, numberOfVisitors,
						telphoneNumber, email);
				reservationsObjs.add(receivedRez);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		ObservableList<Reservation> reservationsObservableList = FXCollections.observableArrayList(reservationsObjs);
		reservationsTable.setItems(reservationsObservableList);

	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
