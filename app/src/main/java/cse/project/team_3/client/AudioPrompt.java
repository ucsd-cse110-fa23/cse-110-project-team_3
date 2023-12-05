package cse.project.team_3.client;

import cse.project.team_3.client.AudioPrompt.AudioPromptState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AudioPrompt extends FlowPane {
    private Label stateLabel;
    // private Button lunchButton;
    // private Button dinnerButton;
    private Button startButton;
    private Button stopButton;
    private Label recordingLabel;
    private AudioPromptState currState;
    private int stopCtr;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultVoiceInputButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultFilterLabelStyle = "-fx-border-color: #00015B; -fx-background-insets: 1,1,1,1; -fx-background-color: #33A8FF; -fx-font: 14 arial; -fx-text-alignment: center; -fx-font-weight: bold; -fx-text-fill: white; -fx-pref-width: 350px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    // Enum to represent different states
    public enum AudioPromptState {
        FILTER,
        INGREDIENTS
    }

    public AudioPrompt() {
        // Set properties for the flowpane
        this.setPrefSize(370, 400);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(370);
        this.setPrefHeight(100);

        currState = AudioPromptState.FILTER;
        stopCtr = 0;

        stateLabel = new Label("\"Voice: Meal type: Breakfast, Lunch, Dinner\"");
        stateLabel.setStyle(defaultFilterLabelStyle);

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultVoiceInputButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultVoiceInputButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(stateLabel, startButton, stopButton, recordingLabel);
    }

    public static void setupAudioPrompt(Stage primaryStage, AudioPrompt audioPrompt) {
        // Set the title of the app
        primaryStage.setTitle("Audio Recorder");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(audioPrompt, 370, 200));
        // Make window non-resizable
        primaryStage.setResizable(true);
        // Show the app
        primaryStage.show();
    }

    public AudioPromptState getCurrState() {
        return currState;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public int getStopCtr() {
        return stopCtr;
    }

    public void setStopCtr(int ctr) {
        stopCtr = ctr;
    }

    public void setRecordingState(boolean isRecording) {
        if (isRecording) {
            recordingLabel.setVisible(true);
        } else {
            recordingLabel.setVisible(false);
        }
    }

    public void setCurrentStateBasedOnLabel() {
        Platform.runLater(() -> {
            String labelText = stateLabel.getText();
            if (labelText.contains("Meal type")) {
                currState = AudioPromptState.FILTER;
            } else if (labelText.contains("Input your Ingredients")) {
                currState = AudioPromptState.INGREDIENTS;
            }
        });
    }

    public void setFilterAction() {
        currState = AudioPromptState.FILTER;
        stateLabel.setText("Voice: Meal type: Breakfast, Lunch, Dinner");
        stateLabel.setStyle(defaultFilterLabelStyle);
    }

    public void setIngredientAction() {
        currState = AudioPromptState.INGREDIENTS;
        stateLabel.setText("Voice: Input your Ingredients");
        stateLabel.setStyle(defaultFilterLabelStyle);
    }

    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }
}