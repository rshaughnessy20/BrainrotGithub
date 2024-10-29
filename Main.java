import Pages.LoginPage;
import Pages.RegistrationPage;
import database.UserDatabase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
// These are import statements future Ryan: Imports classes from the 'Pages' package and JavaFX library (Application, Scene, Stage) to set up the main application window.
// There will be alot in the following files so I remember them for later

public class Main extends Application {
	// Class declaration: Extends JavaFX's 'Application' class, making 'Main' the entry point for launching the JavaFX application.
    @Override
    // Override annotation: Indicates that this method overrides 'start()' from the 'Application' superclass(which is a really fun word I had to look up), which JavaFX calls when the application starts.
    public void start(Stage primaryStage) throws Exception {

        
        // RegistrationPage registrationPage = new RegistrationPage(primaryStage);
    	
    	// This should call the testConnection method in UserDatabase, because I'm relearning
    	// why you should always testConnections before moving ahead, god help me.
        UserDatabase.testConnection();

        // This will create and show the login page
        new LoginPage();
        // Creates an instance of 'LoginPage', which sets up and displays the login window.
        // Benefit: Instantiating 'LoginPage' directly here keeps 'Main' clean and focused on startup, delegating UI logic to 'LoginPage'.

        
    }

    public static void main(String[] args) {
    	// Main method: Calls the 'launch()' method, which triggers the JavaFX application lifecycle and runs the 'start()' method.
        // Benefit: Standard JavaFX setup that simplifies app initialization by starting with 'Main' as the entry point.

        launch(args); // This launches the JavaFX application, which calls the 'start' method automatically
    }
}