package cse.project.team_3.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

class Login extends GridPane {

    private Button enterButton;
    private TextField userInput;
    private TextField passInput;

    Login() {
        this.setPrefSize(200, 150);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        userInput = new TextField();
        passInput = new TextField();

        enterButton = new Button("Enter");

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        this.add(usernameLabel, 0, 0);
        this.add(passwordLabel, 0, 1);
        this.add(userInput, 1, 0);
        this.add(passInput, 1, 1);
        this.add(enterButton, 1, 2);

        // Set padding and alignment
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setVgap(10);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(new ColumnConstraints(), columnConstraints);

        enterButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 2px; -fx-border-color: #333333;");
        enterButton.setPrefSize(80, 20);
        enterButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public TextField getUserInput() {
        return this.userInput;
    }

    public TextField getPassInput() {
        return this.passInput;
    }

    public Button getEnterButton() {
        return this.enterButton;
    }
}

class LoginViewWindow extends BorderPane {
    private Login login;

    LoginViewWindow() {
        login = new Login();
        this.setCenter(login);
    }
}

public class LoginView extends Application {

    LoginViewWindow root = new LoginViewWindow();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PantryPal Login");
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
