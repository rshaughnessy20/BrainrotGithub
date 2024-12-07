package Pages;

import database.Session;
import database.UserDatabase;

import java.io.File;

import dashboard.DashboardPage;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media; // new shit
import javafx.scene.media.MediaPlayer; // new shit
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

//Import statements: Import specific classes from JavaFX (like Button, Label, Scene) and project files ('UserDatabase' and 'DashboardPage').
//This allows us to use these classes without fully qualifying each one, keeping the code concise.

public class LoginPage1 {
	// Class declaration: This defines 'LoginPage' as a public class, meaning it can be accessed from other classes across packages.
	
    private GridPane view;
    // Field declaration (private GridPane): This creates a 'view' object of type GridPane, which provides a flexible grid structure for UI elements. 
    // Benefit: Using a grid layout allows for organized placement of elements based on rows and columns.

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private MediaPlayer mediaPlayer;
    // Field declarations (private): These are the UI elements (text fields, password field, buttons).
    // Benefit: Declaring fields as private encapsulates them within this class, protecting the UI components from external modifications.

    private Stage stage = new Stage();
    // Field declaration (Stage object): Represents the main window or "stage" for this page. Each page needs a stage to display its content.
    // Benefit: Creating a new stage allows the LoginPage to open independently from other views.

    public LoginPage1() {
    	// Constructor: Initializes an instance of 'LoginPage', setting up the UI elements within the layout grid.
        
    	// Initialize grid
        view = new GridPane();
        // This creates a grid layout for our UI elements (like labels, buttons, etc.)
        // GridPane instantiation: Creates a new GridPane layout for structuring UI elements.
        // Benefit: GridPane allows for organized positioning of items via rows and columns, ideal for forms like login pages.

        view.setPadding(new Insets(10));
        // Insets setting: Sets padding around the grid, adding space between the grid’s borders and elements within it.
        // Benefit: Padding enhances visual clarity, giving the UI a cleaner look.

        view.setVgap(10); // Adds vertical space between the rows
        view.setHgap(10); // Adds horizontal space between columns
        view.setAlignment(Pos.CENTER);
        // Vertical and horizontal gap setting: Adds spacing between grid rows (Vgap) and columns (Hgap), making the UI more readable.

        // Add labels and fields
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
        
        registerButton = new Button("No account? Register Jonkler!");
        registerButton.setOnAction(e -> {
            RegistrationPage registrationPage = new RegistrationPage(stage);
            stage.setScene(new Scene(registrationPage.getView(), 800, 600));
        });
        // Register button with event handling: Creates a button that navigates to the RegistrationPage on click, setting the scene to display it.
        // Benefit: This structure allows users to easily transition from the login page to the registration page.

        // Add all the labels and fields to the grid
        view.add(usernameLabel, 0, 0); // Add the username label at row 0, column 0
        view.add(usernameField, 1, 0); // Add the username text field at row 0, column 1
        view.add(passwordLabel, 0, 1);  // Add the password label at row 0, column 1
        view.add(passwordField, 1, 1); // Add the password field at row 1, column 1
        view.add(loginButton, 1, 2); // Add the login button at row 2, column 1
        // Adding elements to the GridPane: Specifies each element’s grid position (column, row).
        // Benefit: Structured layout improves the user experience by organizing elements clearly.
        
        // Add lightning bolt images, lightning didn't work so now they're exploding brains
        ImageView leftLightning = new ImageView(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
        leftLightning.setFitWidth(100);
        leftLightning.setFitHeight(100);

        ImageView rightLightning = new ImageView(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
        rightLightning.setFitWidth(100);
        rightLightning.setFitHeight(100);

        // Add cool wizard image
        ImageView wizardImage = new ImageView(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/jonkler.gif"));
        wizardImage.setFitWidth(250);
        wizardImage.setFitHeight(300);

        // Add an EPIC TITLE with flashing animation
        Label titleLabel = new Label("EPIC JONKLER LOGIN");
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
        root.setStyle("-fx-background-color: green;");

        // sets up EPIC WIZARD TO MUSIC TO LOOP TILL THE SUN EXPLODES
        mediaPlayer = new MediaPlayer(new Media(new File("songs/Jonkler.mp3").toURI().toString()));
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();

        // Sets the scene
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Login Page");
        stage.show();
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        int userId = UserDatabase.validateUser(username, password); // Get userid from database
        if (userId != -1) {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Stop the music when logging in
            }

            // Save user ID in the session
            Session.getInstance().setCurrentUserId(userId);

            // Navigate to Dashboard
            new DashboardPage(stage);
        } else {
            System.out.println("Invalid username or password."); // Print error if the credentials are wrong
        }
    }


    public GridPane getView() {
        return view; // This returns the entire layout (grid) for the login page
    }
}
