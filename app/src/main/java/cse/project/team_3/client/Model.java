package cse.project.team_3.client;

import javax.sound.sampled.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Thread recordingThread;

    private Map<String, String> headers;

    public Model() {
        // Initialize headers
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json"); // You can adjust the content type as needed
    }
    
    public String performRequest(String requestType, String mealType, String audioFileName) {
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
                    startRecording("audiofile.wav");
                    break;
                case "PUT":
                    // Stop recording logic
                    stopRecording();
                    break;
                default:
                    // Handle other request types if needed
                    break;
            }

            // Additional logic based on mealType
            if (mealType != null) {
                // Handle mealType logic if needed
            }

            // Send audio file if available
            if (audioFileName != null && requestType.equals("POST")) {
                File audioFile = new File(audioFileName);
                sendAudioToServer(audioFile);
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
                try {
                    DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();

                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

                    File audioFile = new File(filePath);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

                    // Send the audio file to the server using HTTP POST
                    sendAudioToServer(audioFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    stopRecording();
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
    }

    private void sendAudioToServer(File audioFile) {
    try {
        // Set up the connection
        URL url = new URL("http://localhost:8100/"); // Replace with your server endpoint
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up the boundary for the multipart/form-data
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

        // Write the file part
        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + audioFile.getName() + "\"").append("\r\n");
        writer.append("Content-Type: audio/wav").append("\r\n\r\n");
        writer.flush();

        // Write the audio file content
        try (FileInputStream fileInputStream = new FileInputStream(audioFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        // Write the closing boundary
        writer.append("\r\n").append("--" + boundary + "--").append("\r\n");
        writer.flush();

        // Get the server response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Successfully sent the file to the server
            // You can read the server response if needed
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } else {
            // Handle the error case
            System.out.println("Failed to send the file. Response Code: " + responseCode);
        }

        // Close the connection
        connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



