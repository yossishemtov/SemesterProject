package common;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Customized alert dialog class for displaying various types of alerts.
 */
public class Alerts extends Alert {

    final String errorIcon = "/common/images/errorIcon.png";
    final String warningIcon = "/common/images/warningIcon.png";
    final String informationIcon = "/common/images/informationIcon.png";
    final String confirmationIcon = "/common/images/confirmationIcon.png";

    /**
     * Constructs a new alert with the specified alert type, title, header, and content.
     *
     * @param alertType the type of alert
     * @param title the title text of the alert
     * @param header the header text of the alert
     * @param content the content text of the alert
     */
    public Alerts(AlertType alertType, String title, String header, String content) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);
        setAlertStyle();
    }

    /**
     * Sets up the style of the alert based on its type.
     */
    private void setAlertStyle() {
        String alertType = this.getAlertType().toString();
        if (alertType.equals("ERROR"))
            setupErrorIcon();
        else if (alertType.equals("WARNING"))
            setupWarningIcon();
        else if (alertType.equals("INFORMATION"))
            setupInfoIcon();
        else if (alertType.equals("CONFIRMATION"))
        	setupConfirmationIcon();
    }

    /**
     * Sets up the error icon for the alert.
     */
    private void setupErrorIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(errorIcon)));
    }

    /**
     * Sets up the warning icon for the alert.
     */
    private void setupWarningIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(warningIcon)));
    }

    /**
     * Sets up the information icon for the alert.
     */
    private void setupInfoIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(informationIcon)));
    }
    
    /**
     * Sets up the confirmation icon for the alert.
     */
    private void setupConfirmationIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(confirmationIcon)));
    }


}

