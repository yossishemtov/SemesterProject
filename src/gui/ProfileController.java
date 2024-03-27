package gui;

import java.net.URL;
import java.util.ResourceBundle;
import common.Usermanager;
import common.Traveler;
import common.worker.GeneralParkWorker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

/**
 * The ProfileController class is responsible for controlling the Profile view.
 * It initializes and displays the user profile information based on the current
 * user type (e.g., Worker or Traveler) logged into the system.
 */
public class ProfileController implements Initializable {

    @FXML
    private Label headerLabel, profileNameLabel, profileLastNameLabel, profileIDLabel,
                  ProfileEmailLabel, profileAccountTypeLabel, profileParkLabel, parkLabel;

    @FXML
    private Line line;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the profile information
     * to be displayed on the screen.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    /**
     * Initializes the user profile view by loading the profile information.
     */
    private void init() {
        loadInfo();
    }

    /**
     * Loads the profile information of the current user into the view. It checks
     * whether a worker or a traveler is connected and displays their information accordingly.
     * Group guides and guests are differentiated among travelers.
     */
    private void loadInfo() {
        if (Usermanager.isWorkerConnected()) {
            // If the current user is a worker, fetch and display worker's information
            GeneralParkWorker worker = Usermanager.getCurrentWorker();
            profileNameLabel.setText(worker.getFirstName());
            profileLastNameLabel.setText(worker.getLastName());
            profileIDLabel.setText(String.valueOf(worker.getWorkerId()));
            ProfileEmailLabel.setText(worker.getEmail());
            profileAccountTypeLabel.setText(worker.getRole());
            // Hide park related information for workers
            profileParkLabel.setVisible(false);
            parkLabel.setVisible(false);
            line.setVisible(false);

        } else if (Usermanager.isTravelerConnected()) {
            // If the current user is a traveler, fetch and display traveler's information
            Traveler traveler = Usermanager.getCurrentTraveler();
            profileNameLabel.setText(traveler.getFirstName());
            profileLastNameLabel.setText(traveler.getLastName());
            profileIDLabel.setText(String.valueOf(traveler.getId()));
            ProfileEmailLabel.setText(traveler.getEmail());
            // Hide park related information for travelers as well
            profileParkLabel.setVisible(false);
            parkLabel.setVisible(false);
            line.setVisible(false);
            // Differentiate between group guides and regular guests
            if (traveler.getIsGroupGuide() == 1) {
                profileAccountTypeLabel.setText("Group Guide");
            } else {
                profileAccountTypeLabel.setText("Guest");
            }
        }
    }
}
