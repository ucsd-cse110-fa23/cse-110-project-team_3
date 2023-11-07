// Recipe.java
package app;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Insets;
import java.io.*;
import javax.sound.sampled.*;

class AppFrame extends FlowPane {
    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Thread recordingThread;

    // Buttons for breakfast,lunch,dinner
    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;
    public String lastSelectedMealType = "";

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    AppFrame() {

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

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
        });

        breakfastButton.setOnAction(e -> {
            lastSelectedMealType = "Breakfast";
        });

        lunchButton.setOnAction(e -> {
            lastSelectedMealType = "Lunch";
        });

        dinnerButton.setOnAction(e -> {
            lastSelectedMealType = "Dinner";
        });
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        if (recordingThread == null || !recordingThread.isAlive()) {
            recordingThread = new Thread(() -> {
                try {
                    // the format of the TargetDataLine
                    DataLine.Info dataLineInfo = new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat);
                    // the TargetDataLine used to capture audio data from the microphone
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();
                    recordingLabel.setVisible(true);

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(
                            targetDataLine);

                    // the file that will contain the audio data
                    File audioFile = new File("recording.wav");
                    AudioSystem.write(
                            audioInputStream,
                            AudioFileFormat.Type.WAVE,
                            audioFile);

                    String transcribedText = Whisper.transcribeAudio(audioFile);

                    // Add breakfast, lunch, dinner buttons to the prompt
                    transcribedText = "Goal: " + lastSelectedMealType + ";" + transcribedText
                            + "split it into title, ingredients, instructions";

                    // Call the ChatGPT class to generate a response based on the transcribed text
                    String response = ChatGPT.generateResponse(transcribedText);

                    // Do something with the response, e.g., display it in the UI
                    System.out.println("ChatGPT Response: " + response);

                    Thread.sleep(5 * 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    stopRecording();
                }
            });
            recordingThread.start();
        }

    }

    private void stopRecording() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
        recordingLabel.setVisible(false);
    }

    // TEST AUDIO RECORDING FEATURE
    public static void testAudioRecordingFeature() {
        // Initialize the app frame
        AppFrame appFrame = new AppFrame();

        // Simulate button clicks and check for expected behavior
        System.out.println("Testing start recording...");
        appFrame.startButton.fire(); // Simulate start button click
        if (!appFrame.recordingLabel.isVisible()) {
            System.out.println("Test failed: Recording label should be visible after starting recording.");
        } else {
            System.out.println("Test passed: Recording label is visible after starting recording.");
        }

        // Wait for the recording to be processed
        try {
            Thread.sleep(6 * 1000); // Wait for recording thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate stop button click and check for expected behavior
        System.out.println("Testing stop recording...");
        appFrame.stopButton.fire(); // Simulate stop button click
        if (appFrame.recordingLabel.isVisible()) {
            System.out.println("Test failed: Recording label should not be visible after stopping recording.");
        } else {
            System.out.println("Test passed: Recording label is not visible after stopping recording.");
        }

        // TODO: Add additional checks for file creation, audio transcription, and
        // ChatGPT response
        // You might need to mock objects and responses to fully test these components
    }
}

public class Recipe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Audio Recorder");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 370, 120));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
