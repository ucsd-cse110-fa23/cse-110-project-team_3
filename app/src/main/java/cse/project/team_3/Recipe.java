package cse.project.team_3;

import cse.project.team_3.client.Controller;
import cse.project.team_3.client.Model;
import cse.project.team_3.client.View;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Recipe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        // AppFrame root = new AppFrame();

        Model audioRecorderModel = new Model();
        View view = new View();
        Controller controller = new Controller(audioRecorderModel, view);

        // Set the title of the app
        primaryStage.setTitle("Audio Recorder");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(view, 370, 120));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}