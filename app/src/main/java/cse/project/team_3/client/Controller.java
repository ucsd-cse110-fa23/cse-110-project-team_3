package cse.project.team_3.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Controller {
    private Model model;
    private View view;
    private RecipeList recipeList;

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

    public void setRecipeList(RecipeList recipeList) {
        this.recipeList = recipeList;
        recipeList.filter();
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

                RecipeList recipesDB = model.getRecipes(username, password);
                setRecipeList(recipesDB);
                Stage currentStage = (Stage) (((Button) event.getSource()).getScene().getWindow());
                currentStage.close();

                showRecipeListView();
            } catch (Exception e) {
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
                view.getRecipeListView();
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
            String response = model.performRequest("POST");
            System.out.println(response);
        });
        newAudioPrompt.getStopButton().setOnAction(e -> {
            if (newAudioPrompt.getStopCtr() == 0) {
                model.stopRecording();
            } else {
                String response = model.performRequest("PUT");
                System.out.println("Controller Response: " + response);
                Recipe newRecipe = new Recipe(response);
                try {
                    showRecipeView(newRecipe);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
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
            // AudioPrompt.setupAudioPrompt(new Stage(), this.view.getAudioPrompt());
            System.out.println("Add Button Pressed");
        });

        // Event handler for save button
        view.getRecipeListView().getRoot().getFooter().getSaveButton().setOnAction(e -> {
            // TODO: make this button save all recipes in the recipe list to a JSON object
            ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
            String username = this.view.getLoginView().getloginVW().getLogin().getUserInput().getText();
            String password = this.view.getLoginView().getloginVW().getLogin().getPassInput().getText();
            System.out.println("closing");
            String filepath = "login.txt";
            File file = new File(filepath);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader("login.txt"))) {
                    username = reader.readLine();
                    password = reader.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            model.recipeToDB(username, password, this.recipeList);
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
                        RecipeList recipesDB = model.getRecipes(user, pass);
                        setRecipeList(recipesDB);
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
}