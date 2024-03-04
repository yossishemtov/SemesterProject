package server;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Alerts;
import entities.ClientConnectionStatus;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ServerController {

	BackEndServer sv;
	private Map<String, ClientConnectionStatus> statusMap = new HashMap<>();

	@FXML
	private TextField PortTxt, DBUserNameTxt, DBNameTxt;

	// ip address textFields
	@FXML
	private TextField ip1, ip2, ip3, ip4;

	private List<TextField> txtFields = new ArrayList<>();
	@FXML
	private Label serverMessagesUi;

	@FXML
	private Label serverMsgTxt, PortErrorLabel, ipErrorLabel, passwordErrorLabel, usernameErrorLabel;

	@FXML
	private Label dbNameErrorLabel;

	@FXML
	private PasswordField PasswordTxt;

	@FXML
	private Button startserverBtn, stopServerBtn;

	@FXML
	private VBox TableViewContainer;

	@FXML
	private ImageView closeAppBtn;

	@FXML
	private TableView<ClientConnectionStatus> connStatusTable;

	@FXML
	private TableColumn<ClientConnectionStatus, String> IPCol;

	@FXML
	private TableColumn<ClientConnectionStatus, String> HostCol;

	@FXML
	private TableColumn<ClientConnectionStatus, String> StatusCol;

	@FXML
	private TableColumn<ClientConnectionStatus, String> StTimeCol;

	@FXML
	private Circle serverStartedCircle;

	@FXML
	void serveStopAction(ActionEvent event) {
		closeConnection();
	}

	public void updateConnectionStatusTable(Collection<ClientConnectionStatus> statuses) {
		connStatusTable.getItems().setAll(statuses);
		// Refresh the table to ensure UI is up-to-date
		connStatusTable.refresh();
	}

	public void start(Stage primaryStage) throws Exception {
		try {

			FXMLLoader serverGui = new FXMLLoader(getClass().getResource("ServerGUI.fxml"));
			Parent parent = serverGui.load();
			ServerController control = serverGui.getController();

			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(e -> control.closeConnection());
			primaryStage.setTitle("GoNature Server");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getPort() {
		return PortTxt.getText();
	}

	private String getuserNameOfDB() {
		return DBUserNameTxt.getText();
	}

	private String getPasswordOfDB() {
		return PasswordTxt.getText();
	}

	private boolean isVaiildLogin() {
		Integer port;
		String dbUserName;
		String dbPass;
		port = Integer.parseInt(getPort());

		dbUserName = getuserNameOfDB();
		dbPass = getPasswordOfDB();
		if (port == null || port < 0 || port > 65535 || dbPass.equals("") || dbUserName.equals("")) {
			Alerts warningAlert = new Alerts(Alerts.AlertType.WARNING, "Invalid Input", "Warning",
					"The value entered is invalid. Please try again.");
			warningAlert.showAndWait();
			return false;
		}
		return true;

	}

	// mouse click on start server button
	@FXML
	void serveStartAction(ActionEvent event) {
		Integer port;
		String dbUserName;
		String dbPass;

		port = Integer.parseInt(getPort()); // Set port to 555
		dbUserName = this.getuserNameOfDB();
		dbPass = this.getPasswordOfDB();

		if (isVaiildLogin()) {
			sv = new BackEndServer(port, this, dbUserName, dbPass);

			try {
				// Start server
				sv.listen();
				System.out.println("server start listen");

			} catch (IOException e) {
				System.out.println("Server fail.");
				e.printStackTrace();
			}
		}

	}

	@FXML
	protected void initialize() {
		HostCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHost()));
		IPCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIp()));
		StatusCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
		StTimeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStartTime()));

		ObservableList<ClientConnectionStatus> connectionStatuses = FXCollections.observableArrayList();
		connStatusTable.setItems(connectionStatuses);

	}

	public void closeConnection() {
		try {
			if (sv != null && sv.isListening())
				sv.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
