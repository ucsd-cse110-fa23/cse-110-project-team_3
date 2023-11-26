package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class View extends FlowPane {
    private AudioPrompt audioPrompt;
    private RecipeListView recipeListView;
    private RecipeListAppFrame recipeListAppFrame;

    public View() throws Exception {
        recipeListView = new RecipeListView();
        recipeListView.start(new Stage());
        
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
