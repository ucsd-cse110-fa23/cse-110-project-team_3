package RecipeView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;

class Recipe extends VBox {
    Recipe(String recipe) throws IOException {
        Text text = new Text(recipe);
        text.setWrappingWidth(600);
        this.getChildren().add(text);
        VBox.setMargin(text, new Insets(20, 20, 20, 20));
        this.setStyle("-fx-font-size: 15;");
    }
}

class Footer extends HBox {

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);
    }
}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private Header header;
    private Footer footer;
    private Recipe recipe;

    AppFrame() throws IOException
    {

        String recipeBody = RecipeGeneration.generateFakeRecipe();

        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        recipe = new Recipe(recipeBody);
        
        // Initialise the Footer Object
        footer = new Footer();

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(recipe);


        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners() {

    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Recipe");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 700, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
