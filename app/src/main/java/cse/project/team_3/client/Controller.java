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

        this.model.setView(this.view);
    }
    
    private void handleStartButton(ActionEvent event){
        String response = model.performRequest("POST", this.view.getAudioPrompt().getCurrState(), "audiofile.wav");
    }

    private void handleStopButton(ActionEvent event){
        String response = model.performRequest("PUT", this.view.getAudioPrompt().getCurrState(), null);
    }

    private void handleAddButton(ActionEvent event) {
        AudioPrompt.setupAudioPrompt(new Stage(), this.view.getAudioPrompt());
         
        // // Create a new recipe through the controller
        // createRecipe(
        //         enteredDetails.getTitle(),
        //         enteredDetails.getMealType(),
        //         enteredDetails.getDateCreated(),
        //         enteredDetails.getBody());
    }
}