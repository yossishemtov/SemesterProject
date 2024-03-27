package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import client.ClientUI;
import client.NavigationManager;
import common.ClientServerMessage;
import common.Operation;
import common.Usermanager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main screen of the Park Manager in the GUI. Handles interaction
 * and navigation within the Park Manager's dashboard, allowing the manager to navigate
 * to different functionalities such as viewing current visitors, managing park parameters,
 * and creating reports.
 */
public class ParkManagerController implements Initializable {

    @FXML
    protected BorderPane borderPane;

    @FXML
    private AnchorPane topPane;

    @FXML
    private Label userLabel;

    @FXML
    private VBox vbox;

    @FXML
    private JFXButton profileButton;

    @FXML
    private JFXButton currentVisitorsButton;

    @FXML
    private JFXButton enterVisitorIDButton;

    @FXML
    private JFXButton createReportsButton;

    @FXML
    private JFXButton updateParametersButton;

    @FXML
    private JFXButton RequeststatusBth;

    /**
     * Initializes the controller class. This method is called after the FXML file has
     * been loaded. It sets up the user interface initially by loading the profile of
     * the user.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
			loadProfileImmediately();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Loads the profile view into the center of the main screen immediately after
     * initialization.
     */
    private void loadProfileImmediately() throws IOException {
        loadProfile(null);
    }

    /**
     * Handles request to view the status of park parameter change requests.
     * 
     * @param event The event that triggered the method call.
     * @throws IOException If there is an error loading the requested page.
     */
    @FXML
    void loadRequeststatus(MouseEvent event) {
        try {
            NavigationManager.openPageInCenter(borderPane, "ViewRequestsForChanges.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load request status view: " + e.getMessage());
        }
    }

    /**
     * Loads the interface for creating reports into the main screen.
     */
    @FXML
    private void loadCreateReports() {
        try {
            NavigationManager.openPageInCenter(borderPane, "ParkManagerReportScreen.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load report creation view: " + e.getMessage());
        }
    }

    /**
     * Loads the park parameters view.
     * 
     * @param event The event that triggered the method call.
     */
    @FXML
    void loadParkParameters(MouseEvent event) {
        try {
            NavigationManager.openPageInCenter(borderPane, "ParkParameters.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load park parameters view: " + e.getMessage());
        }
    }

    /**
     * Opens the interface for updating park parameters.
     * 
     * @param event The event that triggered the method call.
     */
    @FXML
    void loadUpdateParameters(MouseEvent event) {
        try {
            NavigationManager.openPageInCenter(borderPane, "UpdateParkParameters.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load update parameters view: " + e.getMessage());
        }
    }

    /**
     * Loads the profile view into the main screen.
     * 
     * @param event The event that triggered the method call.
     */
    @FXML
    void loadProfile(MouseEvent event) {
        try {
            NavigationManager.openPageInCenter(borderPane, "Profile.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load profile view: " + e.getMessage());
        }
    }

    /**
     * Logs out the current user and returns to the home page.
     * 
     * @param event The event that triggered the method call.
     */
    @FXML
    void logOut(MouseEvent event) {
        try {
            if (Usermanager.getCurrentWorker() != null) {
                ClientServerMessage<?> requestToLogout = new ClientServerMessage<>(Usermanager.getCurrentWorker(), Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
                ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
                Usermanager.setCurrentWorker(null);
            }

            NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true, true);
        } catch (IOException e) {
            System.out.println("Failed to log out: " + e.getMessage());
        }
    }
}
