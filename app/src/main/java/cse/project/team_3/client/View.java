package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class View {
    private AudioPrompt audioPrompt;
    private RecipeListView recipeListView;
    private LoginView loginView;
    private RecipeView recipeView;

    public View() throws Exception {
        loginView = new LoginView();

        audioPrompt = new AudioPrompt();

        recipeListView = new RecipeListView();

        recipeView = new RecipeView();
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public AudioPrompt getAudioPrompt() {
        return audioPrompt;
    }
    public RecipeListView getRecipeListView() {
        return recipeListView;
    }
    public RecipeView getRecipeView() {
        return recipeView;
    }
    public void setRecipeView(RecipeView recipeView) {
        this.recipeView = recipeView;
    }

}
