import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPage {
    private GridPane view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginPage(Stage stage) {
        view = new GridPane();
        view.setPadding(new Insets(10, 10, 10, 10));
        view.setVgap(8);
        view.setHgap(10);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginUser());

        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(loginButton, 1, 2);
    }

    public GridPane getView() {
        return view;
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (UserDatabase.validateUser(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}