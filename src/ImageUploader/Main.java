package ImageUploader;

// Add necessary imports
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// JavaFX Application main entry point
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("New Recipe");

        VBox titleVbox = new VBox();
        titleVbox.setPadding(new Insets(10, 100, 30, 100));
        Label labelTitle = new Label("Recipe title");
        labelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        titleVbox.getChildren().add(labelTitle);

        VBox middleVbox = new VBox();
        Label labelIngredients = new Label("Recipe Ingredients");
        middleVbox.getChildren().add(labelIngredients);

        VBox bottomVbox = new VBox();
        Label labelDirections = new Label("Recipe Directions");
        bottomVbox.getChildren().add(labelDirections);

        VBox mainVBox = new VBox();
        mainVBox.getChildren().addAll(titleVbox, middleVbox, bottomVbox);

        titleVbox.setAlignment(Pos.CENTER);
        middleVbox.setAlignment(Pos.CENTER);
        bottomVbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainVBox, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}