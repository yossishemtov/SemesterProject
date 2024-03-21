package client;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationManager {
    private static SystemClient systemClient;

    // Initialize with SystemClient instance
    public static void initialize(SystemClient client) {
        systemClient = client;
    }

    /**
     * Opens a new page and optionally hides the current window.
     * @param nameOfPage The FXML file name of the page to be opened.
     * @param event The event that triggered the page opening (can be null if not applicable).
     * @param title The title for the new stage.
     * @param hideCurrent Determines whether the current stage should be hidden.
     * @throws IOException If an error occurs during loading the FXML.
     */
    public static void openPage(String nameOfPage, Event event, String title, boolean hideCurrent) throws IOException {
        Stage currentStage = null;
        if (event != null && event.getSource() instanceof Node) {
            currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }
        openPage(nameOfPage, currentStage, title, hideCurrent);
    }

    /**
     * Opens a new page and optionally hides the current window.
     * @param nameOfPage The FXML file name of the page to be opened.
     * @param currentStage The current Stage that might be hidden (can be null if not applicable).
     * @param title The title for the new stage.
     * @param hideCurrent Determines whether the current stage should be hidden.
     * @throws IOException If an error occurs during loading the FXML.
     */
    public static void openPage(String nameOfPage, Stage currentStage, String title, boolean hideCurrent) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + nameOfPage));
        Parent pane = loader.load();
        Scene scene = new Scene(pane);
        
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle(title);

        if (currentStage != null && hideCurrent) {
            currentStage.hide(); // Hide the current stage
        }
        newStage.show(); // Show the new stage

        // Handling the closing of the window
        newStage.setOnCloseRequest(e -> {
            // Perform any cleanup, including closing connections
            if (systemClient != null) {
                systemClient.quit();
            }
        });
    }

    /**
     * Opens a new page in the center of the given BorderPane.
     * @param borderPane The BorderPane where the page should be opened in the center.
     * @param pageFXML The FXML file name of the page to be opened.
     * @throws IOException If an error occurs during loading the FXML.
     */
    public static void openPageInCenter(BorderPane borderPane, String pageFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + pageFXML));
        Node page = loader.load(); // Load the page
        borderPane.setCenter(page); // Set the loaded page in the center of the BorderPane
    }
    
}
//    /**
//     * Sets the CSS stylesheet for the specified root node.
//     * 
//     * @param root The root node of the scene graph to apply the CSS stylesheet to.
//     * @param path The path to the CSS file.
//     */
//    public static void setStyleScene(Scene scene, String path) {
//        // Check if the root node is not null
//        if (scene != null) {
//            // Add the CSS stylesheet to the root node's stylesheets
//        	scene.getStylesheets().add(NavigationManager.class.getResource(path).toExternalForm());
//        } else {
//            // Log an error message if the root node is null
//            System.err.println("Root node is null. Cannot set style.");
//        }
//    }

