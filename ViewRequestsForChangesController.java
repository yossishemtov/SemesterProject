package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Controllers.ParkControl;
import Controllers.RequestControl;
import alerts.CustomAlerts;
import client.ChatClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.Discount;
import logic.OrderStatusName;
import logic.Request;

/**
 * this class handles the control of the request from the Department Manager side,
 * displays necessary details, and enables confirming or canceling requests.
 */
@SuppressWarnings("unchecked")
public class ViewRequestsForChangesController implements Initializable {
	@FXML
	private TableView<Request> parametersTable;

	@FXML
	private TableView<Discount> discountTable;

	@FXML
	private TableColumn<Request, Integer> parametersIdCol;

	@FXML
	private TableColumn<Request, String> parkIDCol;

	@FXML
	private TableColumn<Request, String> typeCol;

	@FXML
	private TableColumn<Request, String> oldValueCol;

	@FXML
	private TableColumn<Request, String> newValueCol;

	@FXML
	private TableColumn<Request, String> parametersStatusCol;

	@FXML
	private TableColumn<Discount, Integer> discountIdCol;

	@FXML
	private TableColumn<Discount, String> discountCol;

	@FXML
	private TableColumn<Discount, String> startDateCol;

	@FXML
	private TableColumn<Discount, String> endDateCol;

	@FXML
	private TableColumn<Discount, String> discountStatusCol;

	@FXML
	private Label headerLabel;

	@FXML
	private Button confirmRequestBtn;

	@FXML
	private Button cancelRequestBtn;

	@FXML
	private Label selectedRequestLabel;

	private String tableChosen = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		selectedRequestLabel.setText("");
		loadChanges();
	}

	@FXML
	void discountTableClicked() {

		tableChosen = "discount";
		if (discountTable.getSelectionModel().getSelectedItem() != null) {
			TableViewSelectionModel<Discount> discount = discountTable.getSelectionModel();
			selectedRequestLabel.setText("Discount " + discount.getSelectedItem().getAmount());
		}

	}

	@FXML
	void requestTableClicked() {

		tableChosen = "request";
		if (parametersTable.getSelectionModel().getSelectedItem() != null) {
			TableViewSelectionModel<Request> request = parametersTable.getSelectionModel();
			selectedRequestLabel.setText(" Change Request " + request.getSelectedItem().getChangeName() + " to "
					+ request.getSelectedItem().getNewValue());
		}

	}

	@FXML
	void confirmRequestBtn() {

		ArrayList<String> changeParkParameterList = new ArrayList<>();

		TableViewSelectionModel<Request> request = null;
		TableViewSelectionModel<Discount> discount;

		if (tableChosen != null && tableChosen.equals("request")) {
			request = parametersTable.getSelectionModel();
			Request r = request.getSelectedItem();
			if (r != null && r.getRequestStatus().equals(OrderStatusName.PENDING.toString())) {
				RequestControl.changeRequestStatus(r.getRequestId(), true);
				changeParkParameterList.add(r.getChangeName());
				changeParkParameterList.add(r.getNewValue());
				System.out.println(r.getParkId());
				changeParkParameterList.add(String.valueOf(r.getParkId()));
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent",
						"Request " + r.getChangeName() + " was confirmed with value " + r.getNewValue()).showAndWait();

				ParkControl.changeParkParameters(changeParkParameterList);

			}

			else {
				new CustomAlerts(AlertType.ERROR, "Sent", "Sent", "cannot change status that is not 'pending'")
						.showAndWait();

			}

		}

		if (tableChosen != null && tableChosen.equals("discount")) {
			discount = discountTable.getSelectionModel();
			Discount d = discount.getSelectedItem();
			if (d != null && d.getStatus().equals(OrderStatusName.PENDING.toString())) {

				RequestControl.changeDiscountStatus(d.getDiscountId(), true);
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent",
						"Discount was confirmed with value " + d.getAmount()).showAndWait();
			} else {
				new CustomAlerts(AlertType.ERROR, "Sent", "Sent", "cannot change status that is not 'pending'")
						.showAndWait();

			}
		}
		selectedRequestLabel.setText("");
		loadChanges();

	}

	void loadChanges() {
		parametersTable.setTooltip(new Tooltip("click on a row to select it"));
		discountTable.setTooltip(new Tooltip("click on a row to select it"));

		RequestControl.viewcurrentRequests();

		parametersIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
		parkIDCol.setCellValueFactory(new PropertyValueFactory<>("parkId"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("changeName"));
		parametersStatusCol.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
		newValueCol.setCellValueFactory(new PropertyValueFactory<>("newValue"));

		oldValueCol.setCellValueFactory(new PropertyValueFactory<>("oldValue"));

		ObservableList<Request> requests = FXCollections.observableArrayList();
		requests.addAll(ChatClient.responseFromServer.getResultSet());

		parametersTable.setItems(requests);

		RequestControl.viewcurrentDiscounts();

		discountIdCol.setCellValueFactory(new PropertyValueFactory<>("discountId"));
		discountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		discountStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		ObservableList<Discount> discounts = FXCollections.observableArrayList();

		discounts.addAll(ChatClient.responseFromServer.getResultSet());
		discountTable.setItems(discounts);

	}

	@FXML
	void cancelRequestBtn() {

		TableViewSelectionModel<Request> request = null;
		TableViewSelectionModel<Discount> discount;

		if (tableChosen.equals("request")) {
			request = parametersTable.getSelectionModel();
			Request r = request.getSelectedItem();
			if (r.getRequestStatus().equals(OrderStatusName.PENDING.toString())) {

				RequestControl.changeRequestStatus(r.getRequestId(), false);
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent",
						"Request " + r.getChangeName() + " was declined with value " + r.getNewValue()).showAndWait();
			} else {
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent", "cannot change status that is not 'pending'")
						.showAndWait();

			}

		}

		if (tableChosen.equals("discount")) {
			discount = discountTable.getSelectionModel();
			Discount d = discount.getSelectedItem();
			if (d.getStatus().equals(OrderStatusName.PENDING.toString())) {

				RequestControl.changeDiscountStatus(d.getDiscountId(), false);
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent",
						"Discount was declined with value " + d.getAmount()).showAndWait();
			} else {
				new CustomAlerts(AlertType.INFORMATION, "Sent", "Sent", "cannot change status that is not 'pending'")
						.showAndWait();

			}

		}
		loadChanges();

	}

}