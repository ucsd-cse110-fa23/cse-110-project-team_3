package cse.project.team_3.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class RecipeViewRecipe extends HBox {
    private Label recipeNameLabel;
    private Label mealTypeLabel;
    private Button viewButton;
    private Button deleteButton;
    private Label dateCreatedLabel;
    private int recipeIndex; // Index for sorting
    private boolean toDelete;
    private String body;
    private Recipe recipe;

    RecipeViewRecipe() {
        this.toDelete = false;
        // Empty constructor
    }

    RecipeViewRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.toDelete = false;
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        recipeNameLabel = new Label(recipe.getTitle());
        recipeNameLabel.setFont(Font.font("Arial", 12));
        recipeNameLabel.setPrefSize(300, 20);
        recipeNameLabel.setPadding(new Insets(10, 0, 10, 0));

        mealTypeLabel = new Label(recipe.getMealType());
        mealTypeLabel.setFont(Font.font("Arial", 12));
        mealTypeLabel.setPrefSize(80, 20);
        mealTypeLabel.setPadding(new Insets(10, 0, 10, 0));

        viewButton = new Button("View/Edit");
        viewButton.setPrefSize(100, 20);
        viewButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(70, 20);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        dateCreatedLabel = new Label(recipe.getDateCreated().toLocalDate().toString());
        dateCreatedLabel.setFont(Font.font("Arial", 12));
        dateCreatedLabel.setPrefSize(80, 20);
        dateCreatedLabel.setPadding(new Insets(10, 0, 10, 0));

        this.getChildren().addAll(recipeNameLabel, mealTypeLabel, viewButton, deleteButton,
                dateCreatedLabel);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    // Getter methods
    public Button getViewButton() {
        return viewButton;
    }

    public Recipe getRecipe() {
        return recipe;
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

    public void toDelete() {
        this.toDelete = true;
    }

    public boolean isDelete() {
        return toDelete;
    }

    public String getBody() {
        return body;
    }
}

class RecipeListBox extends VBox {
    RecipeListBox() {
        this.setSpacing(5); // sets spacing between contacts
        this.setPrefSize(750, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof RecipeViewRecipe) {
                ((RecipeViewRecipe) this.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    public void sortRecipes() {
        ArrayList<RecipeViewRecipe> contactList = new ArrayList<RecipeViewRecipe>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof RecipeViewRecipe) {
                contactList.add((RecipeViewRecipe) this.getChildren().get(i));
            }
        }
        /*
         * code generated from chatGPT 3.5 using the prompt
         * sort tasks in a to-do-list lexicographically in java
         */
        Collections.sort(contactList, new Comparator<RecipeViewRecipe>() {
            public int compare(RecipeViewRecipe contact1, RecipeViewRecipe contact2) {
                String contactString1 = contact1.getRecipeName().getText();
                String contactString2 = contact2.getRecipeName().getText();
                return contactString1.compareToIgnoreCase(contactString2);
            }
        });
        this.getChildren().setAll(contactList);
        updateRecipeIndices();
    }

    public void removeRecipe() {
        this.getChildren()
                .removeIf(recipe -> recipe instanceof RecipeViewRecipe && ((RecipeViewRecipe) recipe).isDelete());
        this.updateRecipeIndices();
    }

    public void removeAllRecipes() {
        this.getChildren().removeIf(recipe -> recipe instanceof RecipeViewRecipe);
        this.updateRecipeIndices();
    }
}

class RecipeViewFooter extends HBox {

    private Button addButton;
    private ComboBox<String> sortDrop; // ComboBox for sorting options
    private Button saveButton;
    private ComboBox<String> filterDrop;

    RecipeViewFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // Set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Create New Recipe"); // Button for creating a new recipe
        addButton.setStyle(defaultButtonStyle); // Styling the button
        saveButton = new Button("Save and Close");
        saveButton.setStyle(defaultButtonStyle);

        // Initialize the ComboBox for sorting options
        sortDrop = new ComboBox<>();
        sortDrop.setPromptText("Sort By"); // Placeholder text for the ComboBox
        sortDrop.getItems().addAll("A-Z", "Z-A", "First Created", "Last Created"); // Options for sorting

        filterDrop = new ComboBox<>();
        filterDrop.setPromptText("Filter By");
        filterDrop.getItems().addAll("Breakfast", "Lunch", "Dinner", "All");

        this.getChildren().addAll(addButton, saveButton, sortDrop, filterDrop); // Adding the buttons and ComboBox to
                                                                                // the footer
        this.setAlignment(Pos.CENTER); // Aligning the buttons to the center
    }

    public Button getAddButton() {
        return addButton;
    }

    public ComboBox<String> getSortDrop() {
        return sortDrop;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public ComboBox<String> getFilterDrop() {
        return filterDrop;
    }
}

class RecipeViewHeader extends HBox {
    RecipeViewHeader() {
        this.setPrefSize(750, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("RECIPE LIST"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center Left

    }
}

class RecipeListAppFrame extends BorderPane {
    private RecipeViewHeader header;
    private RecipeViewFooter footer;
    private RecipeListBox recipeListBox;
    private Button addButton;
    private Button saveButton;
    private ComboBox<String> sortDrop;

    RecipeListAppFrame() {
        // Initialize the header Object
        header = new RecipeViewHeader();

        // Create a tasklist Object to hold the tasks
        recipeListBox = new RecipeListBox();

        // Initialise the Footer Object
        footer = new RecipeViewFooter();

        ScrollPane scroller = new ScrollPane(recipeListBox);
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
        saveButton = footer.getSaveButton();

    }

    public RecipeViewFooter getFooter() {
        return footer;
    }

    public RecipeListBox getRecipeList() {
        return recipeListBox;
    }

}

public class RecipeListView extends Application {
    RecipeListAppFrame root;

    public void setupRecipeList(Stage primaryStage, RecipeListAppFrame root) {
        // Set the title of the app
        primaryStage.setTitle("Recipe List");
        // Create a scene of the mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 750, 600));
        // Make the window non-resizable
        primaryStage.setResizable(true);
        // Show the app
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setting the Layout of the Window - Should contain a Header, Footer, and the
        // RecipeList
        root = new RecipeListAppFrame();
        // populateWithExistingRecipes(this.recipeList);
        setupRecipeList(primaryStage, root);
    }

    public RecipeListAppFrame getRoot() {
        return root;
    }

    public void clearRecipes() {
        root.getRecipeList().removeAllRecipes();
    }
}