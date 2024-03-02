package server;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class serves as the entry point for a server application using JavaFX.
 * It initializes and starts the server interface.
 */
public class ServerApp extends Application {
    private ServerController sv;

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command line arguments passed to the application. Not used in this application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application, setting up the primary stage and initializing the server controller.
     *
     * @param primaryStage The primary window for this application, onto which the application scene can be set.
     * @throws Exception if an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.sv = new ServerController();
        sv.start(primaryStage);
    }
}
