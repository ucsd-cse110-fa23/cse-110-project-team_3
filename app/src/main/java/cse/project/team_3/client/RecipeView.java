package cse.project.team_3.client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

class RecipeBody extends VBox {
    TextArea text;
    ImageView imageView;

    RecipeBody(String recipe) {

        text = new TextArea(recipe);
        text.setPrefSize(700, 700);
        text.setWrapText(true);
        text.setMaxWidth(600);

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);
        imageView.setSmooth(true);

        this.getChildren().add(imageView);
        this.getChildren().add(text);
        VBox.setMargin(text, new Insets(20, 20, 20, 20));
        this.setStyle("-fx-font-size: 15;");
    }

    public void setText(String text) {
        this.text = new TextArea(text);
    }

    public String getText() {
        return text.getText();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public ImageView getImageView() {
        return imageView;
    }
    public void clearRecipeImage() {
        imageView.setImage(null);
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
    TextField titleText;

    RecipeHeader(String title) {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        titleText = new TextField(title); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        titleText.setPrefSize(400, 40);
        titleText.setAlignment(Pos.CENTER);
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }

    public String getTitleText() {
        return titleText.getText();
    }
}

class RecipeAppFrame extends BorderPane {

    private RecipeHeader header;
    private RecipeFooter footer;
    private RecipeBody recipeBody;
    private ScrollPane scroll;
    private Button saveRecipeButton;
    private Button discardRecipeButton;

    RecipeAppFrame(Recipe recipe) {

        // Initialise the header Object
        header = new RecipeHeader("Recipe");

        // Create a tasklist Object to hold the tasks
        recipeBody = new RecipeBody("Default Text");

        // Initialise the Footer Object
        footer = new RecipeFooter();

        scroll = new ScrollPane();
        scroll.setContent(recipeBody);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialize Button Variables
        saveRecipeButton = footer.getSaveRecipeButton();
        discardRecipeButton = footer.getDiscardRecipeButton();

    }

    public void setRecipeBody(String text, String fileName) {
        File file = new File(fileName);
        Image image = new Image(file.toURI().toString());
        recipeBody = new RecipeBody(text);
        recipeBody.getImageView().setImage(image);
        scroll.setContent(recipeBody);
    }

    public void setRecipeHeader(String text) {
        header = new RecipeHeader(text);
        this.setTop(header);
    }

    public void buildRecipe(Recipe recipe) {
        setRecipeBody(recipe.getBody(), recipe.getImageFileName());
        setRecipeHeader(recipe.getTitle());
    }
    public RecipeFooter getFooter() {
        return footer;
    }
    public RecipeHeader getHeader() {
        return header;
    }
    public RecipeBody getRecipeBody() {
        return recipeBody;
    }

}

public class RecipeView extends Application {
    Recipe recipe;
    RecipeAppFrame root;

    public RecipeView(Recipe recipe) {
        this.recipe = recipe;
    }

    public RecipeView() {
        this.recipe = new Recipe();
    }

    public void setUpRecipeView(Stage primaryStage, RecipeAppFrame root) {
        primaryStage.setTitle("Recipe");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(this.root, 700, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setting the Layout of the Window - Should contain a Header, Footer, and the
        // RecipeList
        root = new RecipeAppFrame(recipe);
        setUpRecipeView(primaryStage, root);
        root.buildRecipe(recipe);
    }

    public Recipe getRecipe() {
        String title = root.getHeader().getTitleText();
        String body = root.getRecipeBody().getText();
        Recipe toReturn = new Recipe(title, body, recipe.getMealType(), recipe.getIngredients(), recipe.getImageURL(), recipe.getImageFileName());
        return toReturn;
    }

    public RecipeAppFrame getRoot() {
        return root;
    }
}
