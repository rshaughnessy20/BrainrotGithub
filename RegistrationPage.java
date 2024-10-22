import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegistrationPage {
    private GridPane view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button registerButton;
    private Button goToLoginButton;

    public RegistrationPage(Stage stage) {
        view = new GridPane();
        view.setPadding(new Insets(10, 10, 10, 10));
        view.setVgap(8);
        view.setHgap(10);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser());

        goToLoginButton = new Button("Go to Login");
        goToLoginButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage.getView(), 400, 300));
        });

        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(registerButton, 1, 2);
        view.add(goToLoginButton, 1, 3);
    }

    public GridPane getView() {
        return view;
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (!username.isEmpty() && !password.isEmpty()) {
            UserDatabase.addUser(username, password);
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Please enter a valid username and password.");
        }
    }
}