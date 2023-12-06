package cse.project.team_3.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Controller {
    private Model model;
    private View view;
    private RecipeList recipeList;
    Boolean isRecording;
    private Thread recordingThread;
    private File mealTypeAudioFile;
    private File ingredientAudioFile;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    public Controller(Model model, View view) {

        this.model = model;
        this.view = view;
        this.recipeList = new RecipeList();

        this.view.getLoginView().getloginVW().getLogin().getEnterButton().setOnAction(this::handleEnterButton);
        this.view.getLoginView().getloginVW().getLogin().getCreateButton().setOnAction(this::handleCreateButton);
        boolean temp = handleServerStatus(null);
        this.view.getLoginView().getloginVW().getLogin().setServerStatus(temp);

        this.model.setView(this.view);
    }

    public void setRecipeList(RecipeList recipeList) throws MalformedURLException, IOException, URISyntaxException, InterruptedException {
        this.recipeList = recipeList;
        addImages();
        recipeList.filter();
    }
    public void addImages() throws MalformedURLException, IOException, URISyntaxException, InterruptedException {
        for (int i = 0; i < recipeList.size(); i++) {
            Recipe curr = recipeList.get(i);
            curr.setImageURL(ClientDallE.generateImage(curr.getTitle()));
        }
    }

    private void handleAddButton(ActionEvent event) {
        AudioPrompt.setupAudioPrompt(new Stage(), this.view.getAudioPrompt());

        // // Create a new recipe through the controller
        // createRecipe(
        // enteredDetails.getTitle(),
        // enteredDetails.getMealType(),
        // enteredDetails.getDateCreated(),
        // enteredDetails.getBody());
    }

    private void handleEnterButton(ActionEvent event) {
        // validate credentials method -> database through Model
        // conditional if true -> setup recipe UI, else -> error UI
        String username = this.view.getLoginView().getloginVW().getLogin().getUserInput().getText();
        String password = this.view.getLoginView().getloginVW().getLogin().getPassInput().getText();
        boolean response = model.performLogin("GET", username, password);
        if (response != false) {
            try {
                boolean stayLoggedIn = this.view.getLoginView().getloginVW().getLogin().getCheckBox();
                if (stayLoggedIn) {
                    PrintWriter writer = new PrintWriter("login.txt", "UTF-8");
                    writer.println(username);
                    writer.println(password);
                    writer.close();
                }
                // TODO: Pull account details from database and add existing recipes to
                // recipeList
                Stage currentStage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                currentStage.close();
                showRecipeListView();
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            System.out.println("Invalid login");
        }
    }

    private void handleCreateButton(ActionEvent event) {
        String username = this.view.getLoginView().getloginVW().getLogin().getUserInput().getText();
        String password = this.view.getLoginView().getloginVW().getLogin().getPassInput().getText();
        boolean response = model.performLogin("POST", username, password);
        if (response != false) {
            try {
                boolean stayLoggedIn = this.view.getLoginView().getloginVW().getLogin().getCheckBox();
                if (stayLoggedIn) {
                    PrintWriter writer = new PrintWriter("login.txt", "UTF-8");
                    writer.println(username);
                    writer.println(password);
                    writer.close();
                }
                // TODO: Add account to database
                showRecipeListView();
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            System.out.println("Login already exists");
            // load in saved recipe list from account
        }

    }

    public void showAudioPrompt() {
        AudioPrompt newAudioPrompt = new AudioPrompt();
        AudioPrompt.setupAudioPrompt(new Stage(), newAudioPrompt);
        this.view.setAudioPrompt(newAudioPrompt);
        newAudioPrompt.getStartButton().setOnAction(e -> {
            if (newAudioPrompt.getCurrState() == 0) {
                startRecording("mealTypeAudio.wav");

            } else if (newAudioPrompt.getCurrState() == 1) {
                startRecording("ingredientAudio.wav");
            }
        });
        newAudioPrompt.getStopButton().setOnAction(e -> {
            if (newAudioPrompt.getStopCtr() == 0) {
                try {
                    stopRecording();
                } catch (IOException | URISyntaxException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            } else{
                String[] whisperResponse = {"error", "error"};
                try {
                    whisperResponse = stopRecording();
                } catch (IOException | URISyntaxException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                String prompt = "Make me a " + whisperResponse[0] + " recipe using " + whisperResponse[1];
                String chatGPTResponse = "error";
                String dallEResponse = "error";
                try {
                    chatGPTResponse = callChatGPT(prompt);
                    dallEResponse = callDallE(chatGPTResponse);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                view.getAudioPrompt().setStopCtr(1);
                ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                String[] recipeArray = new String[4];
                recipeArray[0] = chatGPTResponse;
                recipeArray[1] = whisperResponse[0];
                recipeArray[2] = whisperResponse[1];
                recipeArray[3] = dallEResponse;
                Recipe recipe;
                try {
                    recipe = new Recipe(recipeArray);
                    showRecipeView(recipe);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                updateRecipeListView();
            }
        });
    }

    /*
     * This method handles displaying the Recipe List View UI
     * It should be called whenever something should lead to this UI being shown
     */
    public void showRecipeListView() throws Exception {
        view.getRecipeListView().start(new Stage());
        populateWithExistingRecipes();

        // Event handler for add button
        view.getRecipeListView().getRoot().getFooter().getAddButton().setOnAction(e -> {
            showAudioPrompt();
            //AudioPrompt.setupAudioPrompt(new Stage(), this.view.getAudioPrompt());
            System.out.println("Add Button Pressed");
        });

        // Event handler for save button
        view.getRecipeListView().getRoot().getFooter().getSaveButton().setOnAction(e -> {
            // TODO: make this button save all recipes in the recipe list to a JSON object
            ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
        });
        view.getRecipeListView().getRoot().getFooter().getSortDrop().setOnAction(e -> {
            String state = view.getRecipeListView().getRoot().getFooter().getSortDrop().getValue();
            recipeList.setSortTag(state);
            updateRecipeListView();
        });
        view.getRecipeListView().getRoot().getFooter().getFilterDrop().setOnAction(e -> {
            String state = view.getRecipeListView().getRoot().getFooter().getFilterDrop().getValue();
            recipeList.setFilterTag(state);
            updateRecipeListView();
        });
    }

    /*
     * This method handles displaying the Recipe View UI
     * It should be called whenever somethign should lead to this UI being shown
     * 
     * @param recipe The recipe that will be used to construct the UI
     */
    public void showRecipeView(Recipe recipe) throws Exception {
        view.setRecipeView(new RecipeView(recipe));
        view.getRecipeView().start(new Stage());

        // Event handler for the save recipe button
        view.getRecipeView().getRoot().getFooter().getSaveRecipeButton().setOnAction(e -> {
            // Add the recipe to the recipeList and then update the Recipe List View UI
            this.recipeList.add(view.getRecipeView().getRecipe());
            updateRecipeListView();
            // Close the Recipe View UI
            ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
        });

        // Event handler for the discard recipe button
        view.getRecipeView().getRoot().getFooter().getDiscardRecipeButton().setOnAction(e -> {
            // Close the Recipe View UI
            ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
        });
    }

    /*
     * This method handles the start up of the app
     * It should only be called in the main method
     */
    public void beginApp() throws Exception {
        String filepath = "login.txt";
        File file = new File(filepath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
                String user = reader.readLine();
                String pass = reader.readLine();
                if (model.serverRunning() == true) {
                    if (model.loginIsValid(user, pass)) {
                        showRecipeListView();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            view.getLoginView().start(new Stage());
        }

    }

    /*
     * This method handles creating recipes to be inserted into the Recipe List View
     * UI
     * It should be called whenever a new recipe is being added to the UI
     * 
     * @param recipe The recipe that will be added to the Recipe List View UI
     */
    public void createRecipe(Recipe recipe) {
        // Create a new recipe
        RecipeViewRecipe recipeViewRecipe = new RecipeViewRecipe(recipe);
        // Add recipe to recipelist
        view.getRecipeListView().getRoot().getRecipeList().getChildren().add(recipeViewRecipe);

        // Event handler for the delete button on each recipe
        Button deleteButton = recipeViewRecipe.getDeleteButton();
        deleteButton.setOnAction(e1 -> {
            recipeList.remove(recipe.getTitle());
            updateRecipeListView();
        });

        // Event handler for the view button on each recipe
        Button viewButton = recipeViewRecipe.getViewButton();
        viewButton.setOnAction(e1 -> {
            // Show the detailed view of the recipe
            try {
                showRecipeView(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Removing the recipe here simplifies other operations
            // It gets added back if the recipe is saved after viewing
            recipeList.remove(recipe.getTitle());
            updateRecipeListView();
        });
    }

    /*
     * This method handles populating the Recipe List View UI with existing recipes
     * It simply adds every recipe in recipeList to the UI
     */
    public void populateWithExistingRecipes() {
        for (int i = 0; i < this.recipeList.visibleSize(); i++) {
            createRecipe(this.recipeList.getVisible(i));
        }
    }

    /*
     * This method handles updating the Recipe List View UI
     * It should be called anytime a recipe is added or removed from recipeList
     */
    public void updateRecipeListView() {
        recipeList.sort();
        recipeList.filter();
        view.getRecipeListView().clearRecipes();
        populateWithExistingRecipes();
    }

    public boolean handleServerStatus(ActionEvent event) {
        if (model.serverRunning()) {
            return true;
        }
        return false;
    }

    public void startRecording(String filePath) {
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
                    view.getAudioPrompt().setRecordingState(isRecording);

                    if (view.getAudioPrompt().getCurrState() == 0) {
                        mealTypeAudioFile = audioFile;
                    } else if (view.getAudioPrompt().getCurrState() == 1) {
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
    public String[] stopRecording() throws IOException, URISyntaxException {
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

        view.getAudioPrompt().setStopCtr(1);
        
        isRecording = false;
        view.getAudioPrompt().setRecordingState(isRecording);

        if (view.getAudioPrompt().getCurrState() == 0) {
            view.getAudioPrompt().setIngredientAction();
            view.getAudioPrompt().setCurrentStateBasedOnLabel();
        } else {
            // Check if both mealTypeAudioFile and ingredientAudioFile is null before
            // combining
            if (mealTypeAudioFile != null && ingredientAudioFile != null) {
                String[] whisperResponse = new String[2];
                whisperResponse[0] = ClientWhisper.transcribeAudio(mealTypeAudioFile);
                whisperResponse[1] = ClientWhisper.transcribeAudio(ingredientAudioFile);
                return whisperResponse;
            } else {
                // Handle the case where ingredientAudioFile is not available
                System.out.println("Both audio files need to be created.");
            }
            view.getAudioPrompt().setFilterAction();
            view.getAudioPrompt().setCurrentStateBasedOnLabel();
        }
        String[] error = {"error", "error"};
        return error;
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
    public String callChatGPT(String prompt) throws Exception {
        return ClientChatGPT.generateResponse(prompt);
    }
    public String callDallE(String prompt) throws IOException, InterruptedException, URISyntaxException {
        return ClientDallE.generateImage(prompt);
    }
}