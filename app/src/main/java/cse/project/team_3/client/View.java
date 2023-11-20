package cse.project.team_3.client;

import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;

public class View extends FlowPane {
    private Recipe recipe;

    public View() {
        // Set properties for the flowpane
        this.setPrefSize(370, 400);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(370);
        this.setPrefHeight(100);

        recipe = new Recipe();


        this.getChildren().addAll(recipe);
        // this.getChildren().addAll(startButton, stopButton, recordingLabel);
    }


    public Recipe getRecipe() {
        return recipe;
    }
}
