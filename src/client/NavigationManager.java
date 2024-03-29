package client;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Manages navigation within the application, facilitating opening new pages and controlling their behavior.
 */
public class NavigationManager {
    private static SystemClient systemClient;

    /**
     * Initializes the NavigationManager with the specified SystemClient instance.
     *
     * @param client the SystemClient instance to associate with the NavigationManager
     */
    public static void initialize(SystemClient client) {
        systemClient = client;
    }

    /**
     * Opens a new page in the application.
     *
     * @param nameOfPage the name of the FXML file representing the page
     * @param event the event that triggered the page opening (can be null)
     * @param title the title of the new page
     * @param hideCurrent true to hide the current stage, false otherwise
     * @param quitOnClose true to quit the application when the new stage is closed, false otherwise
     * @throws IOException if an I/O error occurs when loading the FXML file
     */
    public static void openPage(String nameOfPage, Event event, String title, boolean hideCurrent, boolean quitOnClose) throws IOException {
        Stage currentStage = null;
        if (event != null && event.getSource() instanceof Node) {
            currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }
        openPage(nameOfPage, currentStage, title, hideCurrent, quitOnClose);
    }

    /**
     * Opens a new page in the application.
     *
     * @param nameOfPage the name of the FXML file representing the page
     * @param currentStage the current stage (can be null)
     * @param title the title of the new page
     * @param hideCurrent true to hide the current stage, false otherwise
     * @param quitOnClose true to quit the application when the new stage is closed, false otherwise
     * @throws IOException if an I/O error occurs when loading the FXML file
     */
    public static void openPage(String nameOfPage, Stage currentStage, String title, boolean hideCurrent, boolean quitOnClose) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + nameOfPage));
        Parent pane = loader.load();
        Scene scene = new Scene(pane);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.getIcons().add(new Image("/common/images/1.png"));
        newStage.setTitle(title);

        if (currentStage != null && hideCurrent) {
            currentStage.hide();
        }
        newStage.show();

        // Set the onCloseRequest behavior based on the quitOnClose flag
        if (quitOnClose) {
            newStage.setOnCloseRequest(e -> {
                if (systemClient != null) {
                    systemClient.quit();
                }
            });
        } else {
            newStage.setOnCloseRequest(e -> {
                System.out.println("Window closed without quitting the application.");
            });
        }
    }

    /**
     * Opens a new page within a BorderPane, replacing its center content.
     *
     * @param borderPane the BorderPane to replace the center content of
     * @param pageFXML the name of the FXML file representing the page
     * @throws IOException if an I/O error occurs when loading the FXML file
     */
    public static void openPageInCenter(BorderPane borderPane, String pageFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + pageFXML));
        Node page = loader.load();
        borderPane.setCenter(page);
    }
}
