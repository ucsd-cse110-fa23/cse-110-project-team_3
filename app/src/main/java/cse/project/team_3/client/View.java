package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class View extends FlowPane {
    private Recipe recipe;
    private RecipeListView recipeListView;
    private RecipeListAppFrame recipeListAppFrame;

    public View() throws Exception {
        recipeListView = new RecipeListView();
        recipeListView.start(new Stage());
        
        recipeListAppFrame = recipeListView.getAppFrame();

        recipe = new Recipe();

        // this.getChildren().addAll(recipe);
    }
 
    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeListAppFrame getRecipeListAppFrame() {
        return recipeListAppFrame;
    }
}
