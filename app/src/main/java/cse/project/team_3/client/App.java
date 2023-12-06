
package cse.project.team_3.client;

import java.io.File;

import cse.project.team_3.server.MyServer;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        // AppFrame root = new AppFrame();
        MyServer.startServer();

        clearFolder();
        // RecipeList recipeList = new RecipeList();
        // Recipe recipe1 = new Recipe("Pizza", "Pizza recipe", "Lunch", "Tomatoes");
        // Recipe recipe2 = new Recipe("French Fries", "French Fry recipe", "Lunch", "Potatos");
        // Recipe recipe3 = new Recipe("Pasta", "Pasta recipe", "Dinner", "Pasta");
        // recipeList.add(recipe1);
        // recipeList.add(recipe2);
        // recipeList.add(recipe3);

        Model loginModel = new Model();
        View view = new View();
        Controller controller = new Controller(loginModel, view);
        //controller.setRecipeList(recipeList);
        controller.beginApp();

        //// Set the title of the app
        // primaryStage.setTitle("AudioRecorder");
        // // Create scene of mentioned size with the border pane
        // primaryStage.setScene(new Scene(view, 370, 200));
        // // Make window non-resizable
        // primaryStage.setResizable(true);
        // // Show the app
        // primaryStage.show();
    }
    public void clearFolder() {
        File dir = new File("Images");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}