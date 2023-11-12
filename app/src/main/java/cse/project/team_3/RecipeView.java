package src.main.java.cse.project.team_3;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

class RecipeBody extends VBox {
    Text text;
    RecipeBody(String recipe) throws IOException {
        text = new Text(recipe);
        text.setWrappingWidth(600);
        this.getChildren().add(text);
        VBox.setMargin(text, new Insets(20, 20, 20, 20));
        this.setStyle("-fx-font-size: 15;");
    }
    public void setText(String text) {
        this.text = new Text(text);
    }
    public String getText() {
        return text.toString();
    }
}

class RecipeFooter extends HBox {
    private Button saveRecipeButton;
    private Button discardRecipeButton;

    RecipeFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        saveRecipeButton = new Button("Save Recipe");
        saveRecipeButton.setPrefSize(100, 20);

        discardRecipeButton = new Button("Discard Recipe");
        discardRecipeButton.setPrefSize(100, 20);

        this.getChildren().add(saveRecipeButton);
        this.getChildren().add(discardRecipeButton);
        this.setAlignment(Pos.CENTER);

    }

    public Button getSaveRecipeButton() {
        return saveRecipeButton;
    }

    public Button getDiscardRecipeButton() {
        return discardRecipeButton;
    }
}

class RecipeHeader extends HBox {
    Text titleText;

    RecipeHeader(String title) {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        titleText = new Text(title); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
    public String getTitleText() {
        return titleText.toString();
    }
}

class RecipeAppFrame extends BorderPane {

    private RecipeHeader header;
    private RecipeFooter footer;
    private RecipeBody recipe;
    private ScrollPane scroll;

    private Button saveRecipeButton;
    private Button discardRecipeButton;

    RecipeAppFrame() throws IOException {

        // Initialise the header Object
        header = new RecipeHeader("Recipe");

        // Create a tasklist Object to hold the tasks
        recipe = new RecipeBody("Default Text");

        // Initialise the Footer Object
        footer = new RecipeFooter();

        scroll = new ScrollPane();
        scroll.setContent(recipe);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialize Button Variables
        saveRecipeButton = footer.getSaveRecipeButton();
        discardRecipeButton = footer.getDiscardRecipeButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void setRecipeBody(String text) throws IOException {
        recipe = new RecipeBody(text);
        scroll.setContent(recipe);
    }

    public void setRecipeHeader(String text) {
        header = new RecipeHeader(text);
        this.setTop(header);
    }

    public String getRecipeBody() {
        return this.recipe.getText();
    }
    public String getRecipeHeader() {
        return this.header.getTitleText();
    }

    public void addListeners() {
        saveRecipeButton.setOnAction(e -> {
            System.out.println("Save Recipe Button Clicked");
        });

        discardRecipeButton.setOnAction(e -> {
            System.out.println("Discard Recipe Button Clicked");
        });
    }
}

public class RecipeView extends Stage {
    Stage recipeStage;
    RecipeAppFrame root;
    RecipeView() throws IOException {
        root = new RecipeAppFrame();
    }

    public void setRecipeText(String text) throws IOException {
        root.setRecipeBody(text);
    }
    public void setRecipeTitle(String title) {
        root.setRecipeHeader(title);
    }
    public void setUpRecipe(String recipe) throws IOException {
        String adjustedRecipe = recipe.trim();
        String[] split = adjustedRecipe.split("\n", 3);
        if (split.length >= 3) {
            setRecipeTitle(split[0]);
            setRecipeText(split[2]);
        }
        else {
            setRecipeTitle("error handling title");
            setRecipeText("error handling body");
        }
    }
    public String getRecipeTitle() {
        return this.root.getRecipeHeader();
    }

    public String getRecipeText() {
        return this.root.getRecipeBody();
    }
    
    public void showDefault() {
        this.setTitle("Recipe");
        // Create scene of mentioned size with the border pane
        this.setScene(new Scene(this.root, 700, 600));
        // Make window non-resizable
        this.setResizable(false);
        // Show the app
        this.show();
    }
}
