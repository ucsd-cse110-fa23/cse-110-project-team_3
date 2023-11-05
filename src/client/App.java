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

// New imports
import javafx.scene.control.ComboBox;

class Recipe extends HBox {
    private Label recipeNameLabel;
    private Label mealTypeLabel;
    private Button viewButton;
    private Button editButton;
    private Button deleteButton;
    private Label dateCreatedLabel;
    private int recipeIndex; // Index for sorting

    Recipe() {
        // Empty constructor
    }

    Recipe(String name, String mealType, String dateCreated) {
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        recipeNameLabel = new Label(name);
        recipeNameLabel.setFont(Font.font("Arial", 12));
        recipeNameLabel.setPrefSize(120, 20);
        recipeNameLabel.setPadding(new Insets(10, 0, 10, 0));

        mealTypeLabel = new Label(mealType);
        mealTypeLabel.setFont(Font.font("Arial", 12));
        mealTypeLabel.setPrefSize(80, 20);
        mealTypeLabel.setPadding(new Insets(10, 0, 10, 0));

        viewButton = new Button("View");
        viewButton.setPrefSize(60, 20);
        viewButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        editButton = new Button("Edit");
        editButton.setPrefSize(60, 20);
        editButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(70, 20);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        dateCreatedLabel = new Label(dateCreated);
        dateCreatedLabel.setFont(Font.font("Arial", 12));
        dateCreatedLabel.setPrefSize(80, 20);
        dateCreatedLabel.setPadding(new Insets(10, 0, 10, 0));

        this.getChildren().addAll(recipeNameLabel, mealTypeLabel, viewButton, editButton, deleteButton,
                dateCreatedLabel);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    // Getter methods
    public Button getViewButton() {
        return viewButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Label getRecipeName() {
        return recipeNameLabel;
    }

    public Label getMealType() {
        return mealTypeLabel;
    }

    public int getRecipeIndex() {
        return recipeIndex;
    }

    // Set recipe index
    public void setRecipeIndex(int index) {
        this.recipeIndex = index;
    }
}


class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5); // sets spacing between contacts
        this.setPrefSize(750, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Recipe) {
                ((Recipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    public void sortRecipes() {
        ArrayList<Recipe> contactList = new ArrayList<Recipe>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Recipe) {
                contactList.add((Recipe) this.getChildren().get(i));
            }
        }
        /*
         * code generated from chatGPT 3.5 using the prompt
         * sort tasks in a to-do-list lexicographically in java
         */
        Collections.sort(contactList, new Comparator<Recipe>() {
            public int compare(Recipe contact1, Recipe contact2) {
                String contactString1 = contact1.getRecipeName().getText();
                String contactString2 = contact2.getRecipeName().getText();
                return contactString1.compareToIgnoreCase(contactString2);
            }
        });
        this.getChildren().setAll(contactList);
        updateRecipeIndices();
    }
}

class Footer extends HBox {

    private Button addButton;
    private ComboBox<String> sortDrop; // ComboBox for sorting options

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // Set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Create New Recipe"); // Button for creating a new recipe
        addButton.setStyle(defaultButtonStyle); // Styling the button

        // Initialize the ComboBox for sorting options
        sortDrop = new ComboBox<>();
        sortDrop.setPromptText("Sort By"); // Placeholder text for the ComboBox
        sortDrop.getItems().addAll("Meal Type", "Date Created"); // Options for sorting

        this.getChildren().addAll(addButton, sortDrop); // Adding the buttons and ComboBox to the footer
        this.setAlignment(Pos.CENTER); // Aligning the buttons to the center
    }

    public Button getAddButton() {
        return addButton;
    }

    public ComboBox<String> getSortDrop() {
        return sortDrop;
    }
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

    AppFrame() {
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
        // Setting the Layout of the Window - Should contain a Header, Footer, and the RecipeList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Recipe List");
        // Create a scene of the mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 750, 600));
        // Make the window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
