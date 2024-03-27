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
		if (Usermanager.isWorkerConnected()) {
			GeneralParkWorker worker = Usermanager.getCurrentWorker();
			profileNameLabel.setText(worker.getFirstName());
			profileLastNameLabel.setText(worker.getLastName());
			profileIDLabel.setText(String.valueOf(worker.getWorkerId()));
			ProfileEmailLabel.setText(worker.getEmail());
			profileAccountTypeLabel.setText(worker.getRole());
			profileParkLabel.setVisible(false);
			parkLabel.setVisible(false);
			line.setVisible(false);

		} else if (Usermanager.isTravelerConnected()) {
			Traveler traveler = Usermanager.getCurrentTraveler();
			profileNameLabel.setText(traveler.getFirstName());
			profileLastNameLabel.setText(traveler.getLastName());
			profileIDLabel.setText(String.valueOf(traveler.getId()));
			ProfileEmailLabel.setText(traveler.getEmail());
			profileParkLabel.setVisible(false);
			parkLabel.setVisible(false);
			line.setVisible(false);
			// Assuming "Guest" as default type for simplicity; adjust as necessary
			if (Usermanager.getCurrentTraveler().getIsGroupGuide()==1)
			{
				profileAccountTypeLabel.setText("Group Guide");
			}
			else {
				profileAccountTypeLabel.setText("Guest");

				
			}

		}
	}
}
