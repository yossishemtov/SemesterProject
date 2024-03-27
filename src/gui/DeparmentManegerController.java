package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import common.*;
import client.ClientUI;
import client.NavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the Department Manager's UI in the application.
 * This class handles the interaction of the Department Manager with
 * various UI components like viewing and editing park parameters, creating reports,
 * updating parameters, and logging out.
 */
public class DeparmentManegerController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane topPane;
    @FXML
    private Label userLabel;
    @FXML
    private VBox vbox;
    @FXML
    private JFXButton profileButton;
    @FXML
    private JFXButton ParkParametersBth;
    @FXML
    private JFXButton createReportsButton;
    @FXML
    private JFXButton updateParametersButton;
    @FXML
    private JFXButton logoutBtn;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the initial state of
     * the UI components.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the
     *                  root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProfileImmediately();
    }

    /**
     * Loads the profile view immediately upon initialization of the controller.
     */
    private void loadProfileImmediately() {
        try {
            loadProfile(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the park parameters view into the center of the BorderPane.
     *
     * @param event The mouse event that triggered this method.
     * @throws IOException if the FXML file for the park parameters view cannot be loaded.
     */
    @FXML
    void loadParkParameters(MouseEvent event) throws IOException {
        NavigationManager.openPageInCenter(borderPane, "ParkParameters.fxml");
    }

    /**
     * Loads the profile view into the center of the BorderPane.
     *
     * @param event The mouse event that triggered this method, or null if called directly.
     * @throws IOException if the FXML file for the profile view cannot be loaded.
     */
    @FXML
    void loadProfile(MouseEvent event) throws IOException {
        NavigationManager.openPageInCenter(borderPane, "Profile.fxml");
    }

    /**
     * Loads the reports view into the center of the BorderPane.
     *
     * @param event The mouse event that triggered this method.
     * @throws IOException if the FXML file for the reports view cannot be loaded.
     */
    @FXML
    void loadReports(MouseEvent event) throws IOException {
        NavigationManager.openPageInCenter(borderPane, "DepartmentManagerReports.fxml");
    }

    /**
     * Loads the view requests for changes view into the center of the BorderPane.
     *
     * @param event The mouse event that triggered this method.
     * @throws IOException if the FXML file for the view requests for changes cannot be loaded.
     */
    @FXML
    void loadViewRequests(MouseEvent event) throws IOException {
        NavigationManager.openPageInCenter(borderPane, "ViewRequestsForChanges.fxml");
    }

    /**
     * Handles the logout process for the Department Manager.
     * It sends a logout request to the server, clears the current worker data,
     * and navigates back to the home page.
     *
     * @param event The mouse event that triggered this method.
     */
    @FXML
    void logOut(MouseEvent event) {
        try {
            if (Usermanager.getCurrentWorker() != null) {
                ClientServerMessage<?> requestToLogout = new ClientServerMessage<>(Usermanager.getCurrentWorker(),
                        Operation.PATCH_GENERALPARKWORKER_SIGNEDOUT);
                ClientUI.clientControllerInstance.sendMessageToServer(requestToLogout);
                Usermanager.setCurrentWorker(null);
            }
            NavigationManager.openPage("HomePageFrame.fxml", event, "User Menu", true, true);
        } catch (IOException e) {
            e.printStackTrace();
            Alerts somethingWentWrong = new Alerts(Alerts.AlertType.ERROR, "ERROR", "",
                    "Something went wrong when trying to return to main menu");
            somethingWentWrong.showAndWait();
        }
    }
}
