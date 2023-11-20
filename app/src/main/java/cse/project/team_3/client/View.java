package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;

public class View extends FlowPane {
    private Recipe recipe;

    public View() {
        recipe = new Recipe();

        this.getChildren().addAll(recipe);
    }
 
    public Recipe getRecipe() {
        return recipe;
    }
}
