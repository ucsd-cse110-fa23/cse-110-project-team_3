package cse.project.team_3.client;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

class RecipeBody extends VBox {
    TextArea text;
    RecipeBody(String recipe) throws IOException {
        text = new TextArea(recipe);
        text.setPrefSize(700, 700);
        text.setWrapText(true);
        text.setMaxWidth(600);
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
    private RecipeBody recipe;
    private ScrollPane scroll;
    private String type;
    private String date;

    private Button saveRecipeButton;
    private Button discardRecipeButton;

    private RecipeEventListener saveRecipeListener;
    private RecipeEventListener discardRecipeListener;

    RecipeAppFrame() throws IOException {

        type = "No Type";
        date = "No Date";

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

    public void setSaveRecipeListener(RecipeEventListener listener) {
        this.saveRecipeListener = listener;
    }

    public void setDiscardRecipeListener(RecipeEventListener listener) {
        this.discardRecipeListener = listener;
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
    public void setType(String type) {
        this.type = type;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void addListeners() {
        saveRecipeButton.setOnAction(e -> {
            try {
                String fName = "TempRecipes.txt";
                FileWriter fWriter = new FileWriter(fName);
                fWriter.write(getRecipeHeader() + "\n" + type + "\n" + java.time.LocalDate.now() + "\n" + getRecipeBody());
                fWriter.write('\n');
                fWriter.close();
                // App.sendRecipeFields(fName);
                
                if (saveRecipeListener != null) {
                    saveRecipeListener.handle(fName);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }   
            ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        });

        discardRecipeButton.setOnAction(e -> {
            ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        });
    }
}

@FunctionalInterface
interface RecipeEventListener {
    void handle(String fileName);
}

public class RecipeView extends Stage {
    Stage recipeStage;
    RecipeAppFrame root;
    public RecipeView() throws IOException {
        root = new RecipeAppFrame();
    }

    public void setRecipeText(String text) throws IOException {
        root.setRecipeBody(text);
    }
    public void setRecipeTitle(String title) {
        root.setRecipeHeader(title);
    }
    public String[] setUpRecipe(String recipe, String type) throws IOException {
        root.setType(type);
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
        return new String[]{recipe,type};
    }
    public String getRecipeTitle() {
        return this.root.getRecipeHeader();
    }

    public String getRecipeText() {
        return this.root.getRecipeBody();
    }
    public void setType(String type) {
        root.setType(type);
    }
    public void setDate(String date) {
        root.setDate(date);
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
