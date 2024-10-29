package Pages;
import database.UserDatabase;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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

        
        
        view.setPadding(new Insets(10, 10, 10, 10)); // Adds padding around the edges of the grid
        view.setVgap(8); // Vertical space between rows
        view.setHgap(10); // Horizontal space between columns
        // GridPane configuration: Adds padding around the grid, with spacing between rows and columns for improved readability.


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
            stage.setScene(new Scene(loginPage.getView(), 400, 300)); // Switch to the login page scene
        });
        // Login button with event handling: Allows navigation to the login page when clicked.
        // Benefit: Event handling here improves the user experience by enabling smooth navigation.

        // Add all elements to the grid
        view.add(usernameLabel, 0, 0);
        view.add(usernameField, 1, 0);
        view.add(passwordLabel, 0, 1);
        view.add(passwordField, 1, 1);
        view.add(registerButton, 1, 2);
        view.add(goToLoginButton, 1, 3);
        // Adding elements to the grid: Specifies each element's location in the grid by column and row.
        // Benefit: Positioning elements within the grid makes it easy to align form components.

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
        // Retrieving user input: Gets text values from the input fields for registration.
        // Interaction: These values will be used to create a new user entry in the database.

        // Check if both fields are filled
        if (!username.isEmpty() && !password.isEmpty()) {
            // Add the new user to the database
            UserDatabase.addUser(username, password);
            // Database interaction: Calls `addUser` from `UserDatabase` to insert a new user with the provided credentials.
            // Benefit: Adding a new user only if fields are valid prevents empty or invalid entries in the database.
            // Interaction: `UserDatabase` serves as the link between the UI and the database, handling data storage.
            System.out.println("User registered successfully!"); // Print success message
        } else {
            System.out.println("Please enter a valid username and password."); // Print error message if fields are empty
        }
        // Validation and registration: Adds user to the database if fields are valid; otherwise, prints an error message.
        // Benefit: The database check ensures only valid entries are added, keeping data integrity.
    }
}
