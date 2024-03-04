package common;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * CustomAlerts is a customize Alerts
 *
 */
public class Alerts extends Alert {

    final String errorIcon = "/common/images/errorIcon.png";
    final String warningIcon = "/common/images/warningIcon.png";
    final String informationIcon = "/common/images/infoIcon.png";

    public Alerts(AlertType alertType, String title, String header, String content) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);
        setAlertStyle();
    }

    private void setAlertStyle() {
        String alertType = this.getAlertType().toString();
        if (alertType.equals("ERROR"))
            setupErrorIcon();
        else if (alertType.equals("WARNING"))
            setupWarningIcon();
        else if (alertType.equals("INFORMATION"))
            setupInfoIcon();
    }

    private void setupErrorIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(errorIcon)));
    }

    private void setupWarningIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(warningIcon)));
    }

    private void setupInfoIcon() {
        DialogPane pane = this.getDialogPane();
        ((Stage) pane.getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream(informationIcon)));
    }
}
