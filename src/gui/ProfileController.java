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

public class ProfileController implements Initializable {

    @FXML
    private Label headerLabel;

    @FXML
    private Label profileNameLabel;

    @FXML
    private Label profileLastNameLabel;

    @FXML
    private Label profileIDLabel;

    @FXML
    private Label ProfileEmailLabel;

    @FXML
    private Label profileAccountTypeLabel;

    @FXML
    private Label profileParkLabel;

    @FXML
    private Label parkLabel;
    
    @FXML
    private Line line;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        init();
    }

    private void init() {
        loadInfo();
    }

    private void loadInfo() {
        if(Usermanager.isWorkerConnected()) { 
            GeneralParkWorker worker = Usermanager.getCurrentWorker();
            profileNameLabel.setText(worker.getFirstName());
            profileLastNameLabel.setText(worker.getLastName());
            profileIDLabel.setText(String.valueOf(worker.getWorkerId()));
            ProfileEmailLabel.setText(worker.getEmail());
            // Assuming ParkControl.getParkName(id) is a static method you have to fetch park names
            profileParkLabel.setText("Park Name Placeholder"); // Replace with actual method call
            profileAccountTypeLabel.setText(worker.getRole());
            // Assuming role determination logic for visibility
            if("Department Manager".equals(worker.getRole()) || "SERVICE".equals(worker.getRole())) {
                profileParkLabel.setVisible(false);
                parkLabel.setVisible(false);
                line.setVisible(false);
            }
        } else if(Usermanager.isTravelerConnected()) {
            Traveler traveler = Usermanager.getCurrentTraveler();
            profileNameLabel.setText(traveler.getFirstName());
            profileLastNameLabel.setText(traveler.getLastName());
            profileIDLabel.setText(String.valueOf(traveler.getId()));
            ProfileEmailLabel.setText(traveler.getEmail());
            profileParkLabel.setVisible(false);
            parkLabel.setVisible(false);
            line.setVisible(false);
            // Assuming "Guest" as default type for simplicity; adjust as necessary
            profileAccountTypeLabel.setText("Guest");
            // If you have a way to determine if the traveler is a subscriber, adjust logic here
        }
    }
}
