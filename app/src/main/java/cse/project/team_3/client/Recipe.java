package cse.project.team_3.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class Recipe extends FlowPane {
    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;
    private Button startButton;
    private Button stopButton;
    private Label recordingLabel;


    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultVoiceInputButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultFilterButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 113px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public Recipe() {
        // Set properties for the flowpane
        this.setPrefSize(370, 400);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(370);
        this.setPrefHeight(100);

        breakfastButton = new Button("Breakfast");
        breakfastButton.setStyle(defaultFilterButtonStyle);

        lunchButton = new Button("Lunch");
        lunchButton.setStyle(defaultFilterButtonStyle);

        dinnerButton = new Button("Dinner");
        dinnerButton.setStyle(defaultFilterButtonStyle);

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultVoiceInputButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultVoiceInputButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(breakfastButton, lunchButton, dinnerButton, startButton, stopButton, recordingLabel);
    }

    public Button getBreakfastButton() {
        return breakfastButton;
    }

    public Button getLunchButton() {
        return lunchButton;
    }

    public Button getDinnerButton() {
        return dinnerButton;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public void setRecordingState(boolean isRecording) {
        if (isRecording) {
            recordingLabel.setVisible(true);
        } else {
            recordingLabel.setVisible(false);
        }
    }

    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }
}