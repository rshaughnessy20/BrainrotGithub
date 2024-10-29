package Pages; 
import database.UserDatabase;
import dashboard.DashboardPage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//Import statements: Import specific classes from JavaFX (like Button, Label, Scene) and project files ('UserDatabase' and 'DashboardPage').
//This allows us to use these classes without fully qualifying each one, keeping the code concise.

public class LoginPage {
	// Class declaration: This defines 'LoginPage' as a public class, meaning it can be accessed from other classes across packages.

    private GridPane view;
    // Field declaration (private GridPane): This creates a 'view' object of type GridPane, which provides a flexible grid structure for UI elements. 
    // Benefit: Using a grid layout allows for organized placement of elements based on rows and columns.

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton; // Made a button for registration
    private Button dashboardButton; // Made a button for dashboard, just for testing
    // Field declarations (private): These are the UI elements (text fields, password field, buttons).
    // Benefit: Declaring fields as private encapsulates them within this class, protecting the UI components from external modifications.

    private Stage stage = new Stage();
    // Field declaration (Stage object): Represents the main window or "stage" for this page. Each page needs a stage to display its content.
    // Benefit: Creating a new stage allows the LoginPage to open independently from other views.


    public LoginPage() {
    	// Constructor: Initializes an instance of 'LoginPage', setting up the UI elements within the layout grid.

        view = new GridPane(); // This creates a grid layout for our UI elements (like labels, buttons, etc.)
        // GridPane instantiation: Creates a new GridPane layout for structuring UI elements.
        // Benefit: GridPane allows for organized positioning of items via rows and columns, ideal for forms like login pages.
        
        view.setPadding(new Insets(10, 10, 10, 10)); // Adds padding (space) around the grid
        // Insets setting: Sets padding around the grid, adding space between the grid’s borders and elements within it.
        // Benefit: Padding enhances visual clarity, giving the UI a cleaner look.
        
        view.setVgap(8); // Adds vertical space between the rows
        view.setHgap(10); // Adds horizontal space between columns
        // Vertical and horizontal gap setting: Adds spacing between grid rows (Vgap) and columns (Hgap), making the UI more readable.


        Label usernameLabel = new Label("Username:"); // A label that says "Username"
        usernameField = new TextField(); // A text field where the user enters their username
        Label passwordLabel = new Label("Password:"); // A label that says "Password"
        passwordField = new PasswordField(); // A password field where the user enters their password
        // Label and text field instantiations: Creates labels and fields for username and password input.
        // Benefit: Using specific field types (TextField for general text, PasswordField for password input) ensures proper input handling and security.
        
        loginButton = new Button("Login"); // This is the button that users will click to log in
        loginButton.setOnAction(e -> loginUser()); // When the button is clicked, call the loginUser() method
        // Button instantiation and event handling: Creates a login button and sets an event listener for click actions.
        // Benefit: Lambda expressions allow for concise event-handling code (e -> loginUser()), enhancing readability.

        // Add all the labels and fields to the grid
        view.add(usernameLabel, 0, 0); // Add the username label at row 0, column 0
        view.add(usernameField, 1, 0); // Add the username text field at row 0, column 1
        view.add(passwordLabel, 0, 1); // Add the password label at row 1, column 0
        view.add(passwordField, 1, 1); // Add the password field at row 1, column 1
        view.add(loginButton, 1, 2); // Add the login button at row 2, column 1
        // Adding elements to the GridPane: Specifies each element’s grid position (column, row).
        // Benefit: Structured layout improves the user experience by organizing elements clearly.
        
        registerButton = new Button("No account? Register stupid!");
        registerButton.setOnAction(e -> {
            RegistrationPage registrationPage = new RegistrationPage(stage);
            stage.setScene(new Scene(registrationPage.getView(), 400, 300)); // Show registration page
        });
        // Register button with event handling: Creates a button that navigates to the RegistrationPage on click, setting the scene to display it.
        // Benefit: This structure allows users to easily transition from the login page to the registration page.

        
        // Arrange grid and button in a vertical box
        VBox layout = new VBox(10, view, registerButton);
        layout.setPadding(new Insets(10));
        stage.setScene(new Scene(layout, 400, 300));
        stage.setTitle("Login Page");
        stage.show();
        // Getting tired gonna make comments less long
        
    }
        //showView(); // Call this method to show the login page window, went a different way will keep this just in case

    public GridPane getView() {
        return view; // This returns the entire layout (grid) for the login page
    }
    
    private void showView() {
        // Set up the stage (window) and show the login page
        stage.setScene(new Scene(view, 400, 300)); // Create a new scene using the grid layout and set its size (400x300 pixels)
        stage.setTitle("Login Page"); // Set the window's title to "Login Page"
        stage.show(); // Display the window
    }

    private void loginUser() {
        // Get the text the user typed into the fields
        String username = usernameField.getText(); 
        String password = passwordField.getText();
        
        // Check if the username and password are correct
        if (UserDatabase.validateUser(username, password)) {
        	new DashboardPage(stage); // Why isn't it linking to dashboard? Nevermind fixed it.
        } else {
            System.out.println("Invalid username or password."); // Print error if the credentials are wrong
        }
    }
}
