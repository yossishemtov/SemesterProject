package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import DB.MySqlConnector;
import client.NavigationManager;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Controller class for the server GUI. Manages server operations including
 * starting and stopping the server, and displaying server logs.
 */
public class ServerController {

	BackEndServer sv;
//	private Map<String, ClientConnectionStatus> statusMap = new HashMap<>();

	@FXML
	private JFXTextField PortTxt;

	@FXML
	private JFXTextField DBUserNameTxt;

	@FXML
	private JFXPasswordField PasswordTxt;

	@FXML
	private VBox TableViewContainer;

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
	private TextArea logTextArea;

	@FXML
	private JFXButton startserverBtn;

	@FXML
	private JFXButton StopserverBtn;

	@FXML
	private ImageView logoImage;

	@FXML
	private Circle circleStatus;

	/**
	 * Stops the server and closes connections when the stop button is clicked.
	 * 
	 * @param event The action event triggered by clicking the stop button.
	 */
	@FXML
	void serveStopAction(ActionEvent event) {
		closeConnection();
	}

	/**
	 * Updates the connection status table with the current status of all client
	 * connections.
	 * 
	 * @param statuses The collection of client connection statuses.
	 */
	public void updateConnectionStatusTable(Collection<ClientConnectionStatus> statuses) {
		connStatusTable.getItems().setAll(statuses);
		// Refresh the table to ensure UI is up-to-date
		connStatusTable.refresh();
	}

	/**
	 * Initializes the server GUI.
	 * 
	 * @param primaryStage The primary stage of the application.
	 * @throws Exception if an error occurs during the initialization.
	 */
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader serverGui = new FXMLLoader(getClass().getResource("ServerGUI.fxml"));
			Parent parent = serverGui.load();
			ServerController control = serverGui.getController();

			Scene scene = new Scene(parent);

//			Image image = new Image("../common/images/1.png");
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/common/images/1.png"));
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

	/**
	 * Validates the server login details input by the user, including port number,
	 * database username, and password. Displays an error alert if any validation
	 * checks fail.
	 * 
	 * @return true if all inputs are valid; false otherwise.
	 */
	private boolean isVaiildLogin() {
		Integer port;
		String dbUserName;
		String dbPass;
		port = Integer.parseInt(getPort());

		dbUserName = getuserNameOfDB();
		dbPass = getPasswordOfDB();
		if (port == null || port < 0 || port > 65535 || dbPass.equals("") || dbUserName.equals("")) {
			Alerts erorAlert = new Alerts(Alerts.AlertType.ERROR, "Invalid Input", "Warning",
					"The value entered is invalid. Please try again.");
			erorAlert.showAndWait();
			return false;
		}
		return true;

	}

	/**
	 * Initiates the server startup process when the start button is clicked in the
	 * UI. This method attempts to parse and validate the user-input port number,
	 * database username, and password. It then attempts to start the server with
	 * these parameters. If successful, the server begins listening for connections.
	 * The UI is updated to reflect the server's status. Error handling is included
	 * to alert the user of issues such as invalid port numbers, port already in
	 * use, IO exceptions, or any other unexpected errors.
	 *
	 * @param event The ActionEvent triggered by clicking the start button, not used
	 *              directly in the method but required by the @FXML annotation.
	 * @throws NumberFormatException  If the port number is not a valid integer.
	 * @throws java.net.BindException If the specified port is already in use.
	 * @throws IOException            If an I/O error occurs when attempting to
	 *                                start the server.
	 * @throws Exception              Covers any other exceptions that may occur
	 *                                during the server startup process, ensuring
	 *                                that no error condition is left unhandled.
	 */
	@FXML
	void serveStartAction(ActionEvent event) {
		Integer port = 0;
		String dbUserName;
		String dbPass;

		try {
			port = Integer.parseInt(getPort()); // Assume getPort() is a method that retrieves the port from the UI
			dbUserName = getuserNameOfDB(); // Assume this method retrieves the DB username from the UI
			dbPass = getPasswordOfDB(); // Assume this method retrieves the DB password from the UI

			if (isVaiildLogin()) { // Check for valid login details
				sv = new BackEndServer(port, this, dbUserName, dbPass); // Attempt to start the server

				// Start listening for connections
				sv.listen();
				circleStatus.setFill(Color.GREEN); // Update server status indicator
				logTextArea.setText("Server started listening."); // Log success
				System.out.println("Server started listening.");
			}
		} catch (NumberFormatException e) {
			Alerts invalidPortAlert = new Alerts(Alerts.AlertType.ERROR, "Invalid Port", "",
					"Port number is invalid. Please enter a valid port number.");
			invalidPortAlert.showAndWait();
			logTextArea.setText("Invalid port number. Please enter a valid port number.");
		} catch (java.net.BindException b) {
			Alerts bindExceptionAlert = new Alerts(Alerts.AlertType.ERROR, "Port In Use", "",
					"Error: Port " + port + " is already in use. Please choose a different port.");
			bindExceptionAlert.showAndWait();
			logTextArea.setText("Error: Port is already in use. Please choose a different port.");
		} catch (IOException ioException) {
			Alerts ioAlert = new Alerts(Alerts.AlertType.ERROR, "Server Error", "",
					"Server failed to start due to an I/O error.");
			ioAlert.showAndWait();
			logTextArea.setText("Server failed to start due to an I/O error.");
		} catch (Exception generalException) {
			Alerts unexpectedErrorAlert = new Alerts(Alerts.AlertType.ERROR, "Unexpected Error", "",
					"An unexpected error occurred: " + generalException.getMessage());
			unexpectedErrorAlert.showAndWait();
			logTextArea.setText("An unexpected error occurred. Please check the logs.");
		}
	}

	@FXML
	protected void initialize() {
		PortTxt.setText("5555");
		DBUserNameTxt.setText("root"); // Assuming you might want to set the username to 'root' as well

		// redirectSystemStreams(); // Redirect System.out and System.err

		HostCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHost()));
		IPCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIp()));
		StatusCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
		StTimeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStartTime()));
		PortTxt.setText("5555");
		DBUserNameTxt.setText("root");
		PasswordTxt.setText("root");
		ObservableList<ClientConnectionStatus> connectionStatuses = FXCollections.observableArrayList();
		connStatusTable.setItems(connectionStatuses);

	}

	/**
	 * Redirects standard output and error streams to the text area in the GUI.
	 */
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				appendText(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				appendText(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

	/**
	 * Appends text to the TextArea in the GUI. Ensures thread safety when updating
	 * the GUI from different threads.
	 * 
	 * @param str The string to append to the TextArea.
	 */
	private void appendText(String str) {
		Platform.runLater(() -> logTextArea.appendText(str));
	}

	/**
	 * Closes the server connection and updates the GUI to reflect the server's
	 * stopped status.
	 */
	public void closeConnection() {
		try {
			if (sv != null && sv.isListening()) {
				circleStatus.setFill(Color.RED);
				sv.close();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
