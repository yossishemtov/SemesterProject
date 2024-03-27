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



public class NavigationManager {
    private static SystemClient systemClient;

    public static void initialize(SystemClient client) {
        systemClient = client;
    }

    public static void openPage(String nameOfPage, Event event, String title, boolean hideCurrent, boolean quitOnClose) throws IOException {
        Stage currentStage = null;
        if (event != null && event.getSource() instanceof Node) {
            currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }
        openPage(nameOfPage, currentStage, title, hideCurrent, quitOnClose);
    }

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

    public static void openPageInCenter(BorderPane borderPane, String pageFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource("/gui/" + pageFXML));
        Node page = loader.load();
        borderPane.setCenter(page);
    }
}
