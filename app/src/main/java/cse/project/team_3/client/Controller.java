package cse.project.team_3.client;

import javafx.event.ActionEvent;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        this.view.setStartButtonAction(this::handleStartButton);
        this.view.setStopButtonAction(this::handleStopButton);
        this.view.setBreakfastButton(this::handleBreakfastButton);
        this.view.setLunchButton(this::handleLunchButton);
        this.view.setDinnerButton(this::handleDinnerButton);
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