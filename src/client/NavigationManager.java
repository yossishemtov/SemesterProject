package client;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {
    private static SystemClient systemClient;

    // Initialize with SystemClient instance
    public static void initialize(SystemClient client) {
        systemClient = client;
    }

    /**
     * Method to open pages, adjusted for use with the 'gui' package.
     * @param nameOfPage The name of the FXML file to load (within the /gui directory).
     * @param event The event triggering this navigation (used to find the current stage).
     * @param title The title for the new stage.
     * @param hideCurrent Whether to hide the current stage.
     * @throws IOException If an I/O error occurs.
     */
    public static void openPage(String nameOfPage, Event event, String title, boolean hideCurrent) throws IOException {
  
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + nameOfPage));
        Parent pane = loader.load();
        Scene scene = new Scene(pane);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle(title);

        // Handle the current stage based on the event source
        if (event.getSource() instanceof Node) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            if (hideCurrent) {
                currentStage.hide(); // Hide the current stage
            }
            newStage.show(); // Show the new stage
        } else {
            // This block handles cases where the event source is not a Node, potentially logging or throwing an error
            newStage.show(); // Optionally handle this differently
        }

        // Handling the closing of the window
        newStage.setOnCloseRequest(e -> {
            if (systemClient != null) {
                systemClient.quit();
            }
        });
    }
}
