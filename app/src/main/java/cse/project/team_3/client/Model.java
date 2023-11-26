package cse.project.team_3.client;

import java.io.BufferedReader;
import java.nio.file.Files;
import javax.sound.sampled.*;

import cse.project.team_3.client.AudioPrompt.AudioPromptState;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Thread recordingThread;
    private Map<String, String> headers;
    private boolean isRecording = false;
    private View view; // Add a reference to the View
    private File mealTypeAudioFile;
    private File ingredientAudioFile;

    public Model() {
        // Initialize headers
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json"); // You can adjust the content type as needed
    }

    public void setView(View view) {
        this.view = view;
    }

    public String performRequest(String requestType) {
        try {
            URL url = new URL("http://localhost:8100/"); // Replace with your server endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            connection.setRequestMethod(requestType);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // Handle request based on requestType
            switch (requestType) {
                case "POST":
                    // Start recording logic
                    if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.FILTER) {
                        startRecording("mealTypeAudio.wav");
                    } else if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.INGREDIENTS) {
                        startRecording("ingredientAudio.wav");
                    }
                    break;
                case "PUT":
                    // Stop recording logic
                    stopRecording();
                    break;
                default:
                    // Handle other request types if needed
                    break;
            }

            // Get the server response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully sent the request to the server
                // You can read the server response if needed
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                // Handle the error case
                System.out.println("Failed to send the request. Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if there is an error
    }

    private void startRecording(String filePath) {
        if (recordingThread == null || !recordingThread.isAlive()) {
            recordingThread = new Thread(() -> {
                File audioFile = new File(filePath);
                audioFormat = getAudioFormat();
                try {
                    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();

                    // Flag to indicate whether recording is in progress
                    isRecording = true;
                    this.view.getAudioPrompt().setRecordingState(isRecording);

                    if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.FILTER) {
                        mealTypeAudioFile = audioFile;
                    } else if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.INGREDIENTS) {
                        ingredientAudioFile = audioFile;
                    }

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(
                            targetDataLine);

                    // the file that will contain the audio data
                    AudioSystem.write(
                            audioInputStream,
                            AudioFileFormat.Type.WAVE,
                            audioFile);

                    Thread.sleep(5 * 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            recordingThread.start();
        }
    }

    public void stopRecording() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }

        isRecording = false;
    
        this.view.getAudioPrompt().setRecordingState(isRecording);
        if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.FILTER) {
            this.view.getAudioPrompt().setIngredientAction();
            this.view.getAudioPrompt().setCurrentStateBasedOnLabel();
            try {
                sendPOST(mealTypeAudioFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.view.getAudioPrompt().setCurrentStateBasedOnLabel();
            try {
                sendPOST(ingredientAudioFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
    }

    private static void sendPOST(File uploadFile) throws IOException {
        final int wav = 1;
        final String POST_URL = "http://localhost:8100/" + wav;

        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        URLConnection connection = new URL(POST_URL).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + uploadFile.getName() + "\"")
                    .append(CRLF);
            writer.append("Content-Length: " + uploadFile.length()).append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(uploadFile.toPath(), output);
            output.flush();

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println("Response code: [" + responseCode + "]");
        }
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
}
