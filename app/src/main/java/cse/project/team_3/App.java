package src.main.java.cse.project.team_3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pantry Pal");
        Button btn = new Button();
        btn.setText("Make a Recipe");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                try {
                    RecipeView recipe = new RecipeView();
                    recipe.setUpRecipe(RecipeGeneration.generateFakeRecipe());
                    recipe.showDefault();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}


/*
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            RecipeView recipe;
            try {
                recipe = new RecipeView();
                recipe.setRecipeText(response);
                recipe.showDefault();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
 */