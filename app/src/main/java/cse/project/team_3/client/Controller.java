package cse.project.team_3.client;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        this.view.getAudioPrompt().getStartButton().setOnAction(this::handleStartButton);
        this.view.getAudioPrompt().getStopButton().setOnAction(this::handleStopButton);
        this.view.getRecipeListAppFrame().getAddButton().setOnAction(this::handleAddButton);
        this.view.getLoginView().getloginVW().getLogin().getEnterButton().setOnAction(this::handleEnterButton);
        this.view.getLoginView().getloginVW().getLogin().getCreateButton().setOnAction(this::handleCreateButton);

        this.model.setView(this.view);
    }

    private void handleStartButton(ActionEvent event) {
        String response = model.performRequest("POST");
    }

    private void handleStopButton(ActionEvent event) {
        String response = model.performRequest("PUT");
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

        if (model.loginIsValid(username, password))
            RecipeListView.setupRecipeList(new Stage(), this.view.getRecipeListAppFrame());
        // load in saved recipe list from account
        else
            System.out.println("User and Pass not found");
    }

    private void handleCreateButton(ActionEvent event) {
        String username = this.view.getLoginView().getloginVW().getLogin().getUserInput().getText();
        String password = this.view.getLoginView().getloginVW().getLogin().getPassInput().getText();

        if (model.createIsValid(username, password))
            RecipeListView.setupRecipeList(new Stage(), this.view.getRecipeListAppFrame());
        // load in saved recipe list from account
        else
            System.out.println("Already have an account");
    }
}