package Pages;
import database.UserDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//Import statements: Imports necessary classes and packages from JavaFX and custom classes.
//- `UserDatabase`: Allows interaction with the database to manage user data, linking the UI with backend operations.
//- `Scene`, `Stage`, `GridPane`: Essential components for creating JavaFX layouts and scenes.
//- `Button`, `Label`, `PasswordField`, `TextField`: JavaFX UI controls for the registration form.

public class RegistrationPage {
	// Class declaration: Defines the `RegistrationPage` class, responsible for creating and managing the registration page UI.
    // Benefit: Centralizes all UI elements and logic for registration, making this class self-contained and focused.

    private GridPane view;
    // Field declaration (GridPane): Layout container for arranging UI elements in a grid format.
    // Benefit: Organizes UI elements row-by-row and column-by-column, making it ideal for forms like registration pages.

    private TextField usernameField;
    private PasswordField passwordField;
    private Button registerButton;
    private Button goToLoginButton;
    // Field declarations (private): UI components for user input (username and password fields) and navigation (buttons).

    public RegistrationPage(Stage stage) {
    	 // Constructor: Initializes the registration page, setting up the UI elements and their layout.

        view = new GridPane(); // Creates the layout grid for the registration page
        // Instantiation (GridPane): Sets up a grid for structuring UI elements, simplifying layout configuration.

        
        
        //view.setPadding(new Insets(10, 10, 10, 10)); // Adds padding around the edges of the grid
        //view.setVgap(8); // Vertical space between rows
        //view.setHgap(10); // Horizontal space between columns
        // GridPane configuration: Adds padding around the grid, with spacing between rows and columns for improved readability.


        view.setPadding(new Insets(10));
        // Insets setting: Sets padding around the grid, adding space between the grid’s borders and elements within it.
        // Benefit: Padding enhances visual clarity, giving the UI a cleaner look.

        view.setVgap(10); // Adds vertical space between the rows
        view.setHgap(10); // Adds horizontal space between columns
        view.setAlignment(Pos.CENTER);
        // Vertical and horizontal gap setting: Adds spacing between grid rows (Vgap) and columns (Hgap), making the UI more readable.

        // Labels and fields for username and password input
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        // UI element instantiation: Creates labels for username and password, and input fields for user data entry.
        // Benefit: Using TextField and PasswordField provides proper input handling for each type.


        // Button for registering a new user
        registerButton = new Button("Register");
        registerButton.setOnAction(e -> registerUser()); // When clicked, call the registerUser() method
        // Register button with event handling: Creates a button that calls `registerUser()` on click.
        // Benefit: The action handler immediately links the button with registration functionality, enabling responsive UI behavior.
        // Interaction: When clicked, `registerUser()` validates the input and interacts with `UserDatabase` to store user data.
        
        // Button to navigate back to the login page
        goToLoginButton = new Button("Go to Login");
        goToLoginButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(); // Create a new login page
            stage.close(); // Closes current stage
            
            stage.setScene(new Scene(loginPage.getView(), 800, 600)); // Switch to the login page scene
        });
        // Login button with event handling: Allows navigation to the login page when clicked.
        // Benefit: Event handling here improves the user experience by enabling smooth navigation.

        // Add all elements to the grid
        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(goToLoginButton, 1, 2);
        view.add(registerButton, 1, 3);
        // Adding elements to the grid: Specifies each element's location in the grid by column and row.
        // Benefit: Positioning elements within the grid makes it easy to align form components.

     // Add lightning bolt images, lightning didn't work so now they're exploding brains
        ImageView leftLightning = new ImageView(new Image("file:pictures/exploding-brain.gif"));
        leftLightning.setFitWidth(100);
        leftLightning.setFitHeight(100);

        ImageView rightLightning = new ImageView(new Image("file:pictures/exploding-brain.gif"));
        rightLightning.setFitWidth(100);
        rightLightning.setFitHeight(100);

        // Add cool wizard image
        ImageView wizardImage = new ImageView(new Image("file:pictures/wizard.jpg"));
        wizardImage.setFitWidth(250);
        wizardImage.setFitHeight(300);

        // Add an EPIC TITLE with flashing animation
        Label titleLabel = new Label("REGISTER DINGUS");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 5, 0.7, 0, 0);");

        // Create flashing effect for the title
        Timeline flashTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> titleLabel.setTextFill(Color.BLACK)),
            new KeyFrame(Duration.seconds(1.0), e -> titleLabel.setTextFill(Color.WHITE))
        );
        flashTimeline.setCycleCount(Timeline.INDEFINITE);
        flashTimeline.play();

        // Arranges the WIZARD layout
        VBox formLayout = new VBox(10, view, registerButton);
        formLayout.setAlignment(Pos.CENTER);

        HBox lightningLayout = new HBox(leftLightning, formLayout, rightLightning);
        lightningLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10, titleLabel, lightningLayout, wizardImage);
        mainLayout.setAlignment(Pos.CENTER);

        // Add COOL FUCKING PURPLE
        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: purple;");

        // Sets the scene
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Registration Page");
        stage.show();
    }

    public GridPane getView() {
        return view; // Returns the registration form layout
    }
    // Method returning the layout: Returns the grid layout for the page.
    // Benefit: By exposing the view, this allows other classes (like Main) to display this page's layout.
    
    private void registerUser() {
        // Get user input
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate input fields
        if (!username.isEmpty() && !password.isEmpty()) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/brainrottranslator", "root", "mha@auU,ta@+0J!")) {
                // Insert user into the 'user' table
                String userInsertQuery = "INSERT INTO user (name, password) VALUES (?, ?)";
                try (PreparedStatement userStmt = conn.prepareStatement(userInsertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    userStmt.setString(1, username);
                    userStmt.setString(2, password);
                    userStmt.executeUpdate();

                    // Get the generated userid
                    ResultSet rs = userStmt.getGeneratedKeys();
                    if (rs.next()) {
                        int userId = rs.getInt(1);

                        // Insert default leaderboard entry
                        String leaderboardInsertQuery = "INSERT INTO leaderboard (userid, quizscore, gametype) VALUES (?, 0, 0)";
                        try (PreparedStatement leaderboardStmt = conn.prepareStatement(leaderboardInsertQuery)) {
                            leaderboardStmt.setInt(1, userId);
                            leaderboardStmt.executeUpdate();
                        }
                    }
                }

                System.out.println("User registered successfully and leaderboard updated!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error during registration.");
            }
        } else {
            System.out.println("Please enter a valid username and password.");
        }
    }
}
