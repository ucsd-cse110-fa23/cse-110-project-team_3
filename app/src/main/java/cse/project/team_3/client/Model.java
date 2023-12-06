package cse.project.team_3.client;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.time.LocalDateTime;

import javax.sound.sampled.*;
import cse.project.team_3.client.AudioPrompt.AudioPromptState;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

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

    public boolean performLogin(String requestType, String user, String pass) {
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
                    if (serverRunning() == true) {
                        boolean responsePost = createIsValid(user, pass);
                        return responsePost;
                    }
                    break;
                case "GET":
                    if (serverRunning() == true) {
                        boolean responseGet = loginIsValid(user, pass);
                        return responseGet;
                    }
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
            } else {
                // Handle the error case
                System.out.println("Failed to send the request. Response Code: " + responseCode);
            }
            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Return null if there is an error
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
                    response.append(line + "\n");
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

                    // Simulate recording for 5 seconds
                    for (int i = 0; i < 5; i++) {
                        // Check for interruption during sleep
                        if (Thread.interrupted()) {
                            throw new InterruptedException("Recording thread interrupted during sleep.");
                        }

                        Thread.sleep(1000); // Sleep for 1 second
                    }
                } catch (InterruptedException ex) {
                    // Handle the interruption gracefully
                    System.out.println("Recording thread stopped.");
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

        if (recordingThread != null && recordingThread.isAlive()) {
            recordingThread.interrupt();
            try {
                recordingThread.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.view.getAudioPrompt().setStopCtr(1);

        isRecording = false;
        this.view.getAudioPrompt().setRecordingState(isRecording);

        if (this.view.getAudioPrompt().getCurrState() == AudioPromptState.FILTER) {
            this.view.getAudioPrompt().setIngredientAction();
            this.view.getAudioPrompt().setCurrentStateBasedOnLabel();
        } else {
            try {
                // Check if both mealTypeAudioFile and ingredientAudioFile is null before
                // combining
                if (mealTypeAudioFile != null && ingredientAudioFile != null) {
                    combineAndSendAudioFiles(mealTypeAudioFile, ingredientAudioFile);
                } else {
                    // Handle the case where ingredientAudioFile is not available
                    System.out.println("Both audio files need to be created.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.view.getAudioPrompt().setFilterAction();
            this.view.getAudioPrompt().setCurrentStateBasedOnLabel();
        }

    }

    private void combineAndSendAudioFiles(File mealTypeAudioFile, File ingredientAudioFile) throws IOException {
        // Combine the two audio files into a single file
        File combinedAudioFile = new File("combinedAudio.wav");

        try {
            AudioInputStream mealTypeStream = AudioSystem.getAudioInputStream(mealTypeAudioFile);
            AudioInputStream ingredientStream = AudioSystem.getAudioInputStream(ingredientAudioFile);

            AudioInputStream combinedStream = new AudioInputStream(
                    new SequenceInputStream(mealTypeStream, ingredientStream),
                    mealTypeStream.getFormat(),
                    mealTypeStream.getFrameLength() + ingredientStream.getFrameLength());

            // Write the combined audio to the file
            AudioSystem.write(
                    combinedStream,
                    AudioFileFormat.Type.WAVE,
                    combinedAudioFile);
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Send the combined audio file to the server
        sendPOST(combinedAudioFile);
    }

    private void sendPOST(File uploadFile) throws IOException {
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

            // Handle the response from the server
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract recipe and image from JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String generatedRecipe = jsonResponse.getString("recipe");
                String imageURL = jsonResponse.getString("imageURL");

                // Print generated recipe and imageURL
                System.out.println("Recipe: " + generatedRecipe);
                System.out.println("Image URL: " + imageURL);

                // Update UI with recipe and image
                Platform.runLater(() -> view.getRecipeView().getRoot().updateRecipeDetails(generatedRecipe, imageURL));
            }
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

    public boolean loginIsValid(String username, String password) {
        // check with db to see if login is valid
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
            MongoCollection<Document> recipeCollection = recipeDB.getCollection(username);
            // find one document with new Document
            Document recipe = recipeCollection.find(new Document(username, password)).first();
            if (recipe != null) {
                return true;
            }
            return false;
        }
    }

    public boolean createIsValid(String username, String password) {
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection(username);
        long documentCount = recipeCollection.countDocuments();
        if (documentCount > 0) {
            System.out.println("have account");
            return false;
        } else {
            MongoCollection<Document> recipeCollection1 = recipeDB.getCollection(username);
            Document newLogin = new Document("_id", new ObjectId());
            newLogin.append(username, password);
            recipeCollection1.insertOne(newLogin);
            System.out.println("dont have account");

            // add dummy recipe for testing
            // Document newRecipe = new Document("_id", new ObjectId());
            // newRecipe.append("Recipe Name", "test")
            // .append("Directions", "test123")
            // .append("Meal", "Breakfast")
            // .append("Ingredients", "cheese")
            // .append("Date", LocalDateTime.now().toString());
            // recipeCollection1.insertOne(newRecipe);

        }
        return true;
    }

    public RecipeList getRecipes(String user, String pass) {
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection(user);
        MongoCursor<Document> cursor = recipeCollection.find().iterator();
        RecipeList tempList = new RecipeList();
        while (cursor.hasNext()) {
            Recipe temp = recipeHelper(cursor.next());
            if (temp.getTitle() != null) {
                tempList.add(temp);
            }
        }
        return tempList;
    }

    public Recipe recipeHelper(Document cursor) {
        String name = cursor.getString("Recipe Name");
        String directions = cursor.getString("Directions");
        String meal = cursor.getString("Meal");
        String ingredients = cursor.getString("Ingredients");
        String date = cursor.getString("Date");
        Recipe temp = new Recipe(name, directions, meal, ingredients);
        if (name != null) {
            LocalDateTime dateCreated = LocalDateTime.parse(date);
            temp.setDateCreated(dateCreated);
        }
        return temp;
    }

    public void recipeToDB(String username, String password, RecipeList recipeList) {
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection(username);
        recipeCollection.drop();

        // add login info
        Document newLogin = new Document("_id", new ObjectId());
        newLogin.append(username, password);
        recipeCollection.insertOne(newLogin);
        // add recipes
        for (Recipe recipe : recipeList.getRecipeList()) {
            Document newRecipe = new Document("_id", new ObjectId());
            newRecipe.append("Recipe Name", recipe.getTitle())
                    .append("Directions", recipe.getBody())
                    .append("Meal", recipe.getMealType())
                    .append("Ingredients", recipe.getIngredients())
                    .append("Date", recipe.getDateCreated().toString());
            recipeCollection.insertOne(newRecipe);
        }
    }

    public boolean serverRunning() {
        String serverHost = "localhost";
        int serverPort = 8100;
        try (Socket socket = new Socket(serverHost, serverPort)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
