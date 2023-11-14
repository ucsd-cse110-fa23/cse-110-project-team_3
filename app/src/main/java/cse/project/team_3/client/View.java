package cse.project.team_3.client;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

public class View extends FlowPane {
    private Button startButton;
    private Button stopButton;
    private Label recordingLabel;

    // Buttons for breakfast,lunch,dinner
    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;


    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public View() {
        // Set properties for the flowpane
        this.setPrefSize(800, 800);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(500);

        breakfastButton = new Button("Breakfast");
        breakfastButton.setStyle(defaultButtonStyle); // Assuming defaultButtonStyle is defined

        lunchButton = new Button("Lunch");
        lunchButton.setStyle(defaultButtonStyle);

        dinnerButton = new Button("Dinner");
        dinnerButton.setStyle(defaultButtonStyle);

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(breakfastButton, lunchButton, startButton, stopButton, recordingLabel);
        // this.getChildren().addAll(startButton, stopButton, recordingLabel);        
    }

    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);        
    }

    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }

    public void setBreakfastButton(EventHandler<ActionEvent> eventHandler) {
        breakfastButton.setOnAction(eventHandler);
    }

    public void setLunchButton(EventHandler<ActionEvent> eventHandler) {
        lunchButton.setOnAction(eventHandler);
    }

    public void setDinnerButton(EventHandler<ActionEvent> eventHandler) {
        dinnerButton.setOnAction(eventHandler);
    }
}


  