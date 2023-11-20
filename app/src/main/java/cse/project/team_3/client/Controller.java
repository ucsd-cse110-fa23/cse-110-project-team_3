package cse.project.team_3.client;

import javafx.event.ActionEvent;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        this.view.getRecipe().getBreakfastButton().setOnAction(this::handleBreakfastButton);
        this.view.getRecipe().getLunchButton().setOnAction(this::handleLunchButton);
        this.view.getRecipe().getDinnerButton().setOnAction(this::handleDinnerButton);
        this.view.getRecipe().getStartButton().setOnAction(this::handleStartButton);
        this.view.getRecipe().getStopButton().setOnAction(this::handleStopButton);

        this.model.setView(this.view);
    }
    
    private void handleStartButton(ActionEvent event){
        String response = model.performRequest("POST", null, "audiofile.wav");
    }

    private void handleStopButton(ActionEvent event){
        String response = model.performRequest("PUT", null, null);
    }

    private void handleBreakfastButton(ActionEvent event) {
        String response = model.performRequest("PUT", "Breakfast", null);
    }

    private void handleLunchButton(ActionEvent event) {
        String response = model.performRequest("PUT", "Lunch", null);
    }

    private void handleDinnerButton(ActionEvent event) {
        String response = model.performRequest("PUT", "Dinner", null);
    }
}