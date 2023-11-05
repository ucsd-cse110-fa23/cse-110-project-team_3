//App.java
package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser.ExtensionFilter;

class Recipe extends HBox {

}

class RecipeList extends VBox {

}

class Footer extends HBox {

}

class Header extends HBox {
    Header() {
        this.setPrefSize(750, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("RECIPE LIST"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER_LEFT); // Align the text to the Center Left
    }
}

class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button addButton;
    private ComboBox<String> sortDrop;
    
    AppFrame()
    {
        // Initialize the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        recipeList = new RecipeList();
        
        // Initialise the Footer Object
        footer = new Footer();

        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroller);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button/ComboBox Variables through the getters in Footer
        addButton = footer.getAddButton();
        sortDrop = footer.getSortDrop();

        // Call Event Listeners for the Buttons
        addListeners();
    }
    
    public void addListeners() {
        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new recipe
            Recipe recipe = new Recipe();
            // Add recipe to recipelist
            recipeList.getChildren().add(recipe);
            
            // Update task indices
            recipeList.updateRecipeIndices();
        });
    }
}

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    
    }

    public static void main(String[] args) {
        launch(args);
    }
}