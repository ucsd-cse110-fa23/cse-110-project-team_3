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
        model.startRecording("audiofile.wav");
    }

    private void handleStopButton(ActionEvent event){
        model.stopRecording();
    }

    private void handleBreakfastButton(ActionEvent event) {
        String lastSelectedMealType = "Breakfast";
    }

    private void handleLunchButton(ActionEvent event) {
        String lastSelectedMealType = "Lunch";
    }

    private void handleDinnerButton(ActionEvent event) {
        String lastSelectedMealType = "Dinner";
    }
}