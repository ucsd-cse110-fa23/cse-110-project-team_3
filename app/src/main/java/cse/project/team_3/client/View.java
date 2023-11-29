package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class View extends FlowPane {
    private AudioPrompt audioPrompt;
    private RecipeListView recipeListView;
    private RecipeListAppFrame recipeListAppFrame;
    private LoginView loginView;

    public View() throws Exception {
        loginView = new LoginView();
        loginView.start(new Stage());

        recipeListView = new RecipeListView();
        // recipeListView.start(new Stage());

        recipeListAppFrame = recipeListView.getAppFrame();

        audioPrompt = new AudioPrompt();

        // this.getChildren().addAll(recipe);
    }

    public AudioPrompt getAudioPrompt() {
        return audioPrompt;
    }

    public RecipeListAppFrame getRecipeListAppFrame() {
        return recipeListAppFrame;
    }
}
