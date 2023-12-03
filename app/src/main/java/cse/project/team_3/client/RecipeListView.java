//App.java
package cse.project.team_3.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
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
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser.ExtensionFilter;

// New imports
import javafx.scene.control.ComboBox;

class RecipeViewRecipe extends HBox {
    private Label recipeNameLabel;
    private Label mealTypeLabel;
    private Button viewButton; // private Button editButton;
    private Button deleteButton;
    private Label dateCreatedLabel;
    private int recipeIndex; // Index for sorting
    private boolean toDelete;
    private String body;

    RecipeViewRecipe() {
        this.toDelete = false;
        // Empty constructor
    }

    RecipeViewRecipe(String name, String mealType, String dateCreated, String body) {
        this.toDelete = false;
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        recipeNameLabel = new Label(name);
        recipeNameLabel.setFont(Font.font("Arial", 12));
        recipeNameLabel.setPrefSize(300, 20);
        recipeNameLabel.setPadding(new Insets(10, 0, 10, 0));

        mealTypeLabel = new Label(mealType);
        mealTypeLabel.setFont(Font.font("Arial", 12));
        mealTypeLabel.setPrefSize(80, 20);
        mealTypeLabel.setPadding(new Insets(10, 0, 10, 0));

        viewButton = new Button("View/Edit");
        viewButton.setPrefSize(100, 20);
        viewButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        // editButton = new Button("Edit");
        // editButton.setPrefSize(60, 20);
        // editButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(70, 20);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");

        dateCreatedLabel = new Label(dateCreated);
        dateCreatedLabel.setFont(Font.font("Arial", 12));
        dateCreatedLabel.setPrefSize(80, 20);
        dateCreatedLabel.setPadding(new Insets(10, 0, 10, 0));

        this.getChildren().addAll(recipeNameLabel, mealTypeLabel, viewButton, deleteButton,
                dateCreatedLabel);
        this.setAlignment(Pos.CENTER_LEFT);

        this.body = body;
    }

    // Getter methods
    public Button getViewButton() {
        return viewButton;
    }

    /*
     * public Button getEditButton() {
     * return editButton;
     * }
     */

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

    public String[] getCSVFields() {
        String[] toReturn = { recipeNameLabel.getText(), mealTypeLabel.getText(), dateCreatedLabel.getText(), body };
        return toReturn;
    }

    public String getBody() {
        return body;
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

    public void writeToCSV() {
        try {
            String fName = "src/main/java/cse/project/team_3/Recipes.csv";
            FileWriter fWriter = new FileWriter(fName);

            for (int i = 0; i < this.getChildren().size(); i++) {
                if (this.getChildren().get(i) instanceof RecipeViewRecipe) {
                    String[] recipeCSV = ((RecipeViewRecipe) this.getChildren().get(i)).getCSVFields();
                    fWriter.write(recipeCSV[0] + ";" + recipeCSV[1] + ";" + recipeCSV[2] + ";\"" + recipeCSV[3] + "\"");
                    fWriter.write('\n');
                }
            }
            fWriter.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

class RecipeViewFooter extends HBox {

    private Button addButton;
    private ComboBox<String> sortDrop; // ComboBox for sorting options
    private Button saveButton;

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
        sortDrop.getItems().addAll("Meal Type", "Date Created"); // Options for sorting

        this.getChildren().addAll(addButton, saveButton); // Adding the buttons and ComboBox to the footer
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
    private RecipeList recipeList;
    private Button addButton;
    private Button saveButton;
    private ComboBox<String> sortDrop;

    RecipeListAppFrame() {
        // Initialize the header Object
        header = new RecipeViewHeader();

        // Create a tasklist Object to hold the tasks
        recipeList = new RecipeList();

        // Initialise the Footer Object
        footer = new RecipeViewFooter();

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
        saveButton = footer.getSaveButton();

        // Call Event Listeners for the Buttons
        // addListeners();
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public void setupRecipeList() {

    }

    // public void addListeners() {
    // // Add button functionality
    // addButton.setOnAction(e -> {
    // Recipe recipePrompt = new Recipe();
    // });
    // saveButton.setOnAction(e -> {
    // recipeList.writeToCSV();
    // ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    // });
    // }

    public void deleteRecipe() {
        recipeList.removeRecipe();
    }

    public Button getAddButton() {
        return addButton;
    }
}

public class RecipeListView extends Application {
    private RecipeListAppFrame root;

    public RecipeListView() {
        root = new RecipeListAppFrame();
    }

    public static void setupRecipeList(Stage primaryStage, RecipeListAppFrame root) {
        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create a scene of the mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 750, 600));
        // Make the window non-resizable
        primaryStage.setResizable(true);
        // Show the app
        primaryStage.show();
    }

    public void createRecipe(String title, String type, String date, String body) {
        // Create a new recipe
        RecipeViewRecipe recipe = new RecipeViewRecipe(title, type, date, body);
        // Add recipe to recipelist
        root.getRecipeList().getChildren().add(recipe);
        Button deleteButton = recipe.getDeleteButton();
        deleteButton.setOnAction(e1 -> {
            recipe.toDelete();
            root.deleteRecipe();
        });
        Button viewButton = recipe.getViewButton();
        viewButton.setOnAction(e1 -> {
            try {
                RecipeView recipeView = new RecipeView();
                recipeView.setRecipeTitle((recipe.getRecipeName().getText()));
                recipeView.setRecipeText(recipe.getBody());
                recipeView.setType(type);
                recipeView.setDate(date);
                recipeView.showDefault();
                recipe.toDelete();
                root.deleteRecipe();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        // Update task indices
        root.getRecipeList().updateRecipeIndices();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // // Setting the Layout of the Window - Should contain a Header, Footer, and
        // the
        // // RecipeList
        // root = new RecipeListAppFrame();
        // populateWithExistingRecipes();
        setupRecipeList(primaryStage, root);
    }

    public void populateWithExistingRecipes() throws FileNotFoundException, IOException {

        /*
         * I modeled this method after an algorithm found at this URL
         * URL to Source:
         * https://stackoverflow.com/questions/6786708/reading-from-a-file-until-
         * specific-character-in-java
         * Date Accessed: 11/12/2023
         */
        List<List<String>> recipes = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("src/main/java/cse/project/team_3/Recipes.csv"));
        int ch;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while ((ch = br.read()) >= 0) {
            if (ch == '\"') {
                if (count == 1) {
                    String[] values = sb.toString().split(";");
                    recipes.add(Arrays.asList(values));
                    System.out.println();
                    sb.setLength(0);
                    count = 0;
                } else {
                    count++;
                }

            } else {
                sb.append((char) ch);
            }

        }
        for (List<String> recipe : recipes) {
            String title = recipe.get(0);
            String type = recipe.get(1);
            String date = recipe.get(2);
            String body = recipe.get(3);
            createRecipe(title.trim(), type.trim(), date.trim(), body.trim());
        }
        br.close();
    }

    public RecipeListAppFrame getAppFrame() {
        return root;
    }
}