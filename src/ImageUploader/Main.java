package ImageUploader;

// Add necessary imports
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.*;

import java.io.File;

// JavaFX Application main entry point
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AppFrame root = new AppFrame();
        primaryStage.setTitle("New Recipe");
        VBox vbox = new VBox();
        Label label = new Label("text recipe goes here");
        vbox.getChildren().add(label);

        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}