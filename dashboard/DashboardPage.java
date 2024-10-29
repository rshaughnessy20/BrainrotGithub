package dashboard;
//REMEMBER THIS IS JUST TO SEE HOW IT WOULD LOOK SO IT ISN'T CONNECTED TO ANYTHING YET
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Import statements: Import various JavaFX components to create the user interface for the dashboard page.
//- `Insets`: Adds padding around elements.
//- `Pos`: Positions elements within containers.
//- `Scene`: Represents the entire scene graph (UI layout).
//- `Button`, `Label`, `TextArea`: UI components for interaction and display.
//- `BorderPane`: A layout manager that divides the UI into regions (top, bottom, left, right, center).
//- `VBox`: Arranges elements vertically in a box layout.
public class DashboardPage {
	// Class declaration: Defines `DashboardPage`, the UI component for the dashboard screen.
    // Benefit: Encapsulates dashboard logic and components in a single class, enabling easy reuse and modification.
	
    private Stage stage;
    private Scene scene;
    // Private fields: Hold references to the `Stage` (window) and `Scene` (UI layout) for this dashboard page.
    // Benefit: Storing these references allows the class to control the appearance and behavior of the dashboard.
    
    public DashboardPage(Stage stage) {
        this.stage = stage;
        setupUI();
    }
    // Constructor (DashboardPage): Initializes the dashboard with a `Stage` (window) passed in as a parameter.
    // - Benefit: Assigns the stage instance to the class and calls `setupUI()` to build the UI, separating layout from logic.
    // - Interaction: The `stage` reference is used throughout `setupUI()` to set up the dashboard page display.

    private void setupUI() {
        BorderPane root = new BorderPane();
        // `BorderPane` layout manager: Creates a root container with five regions (top, bottom, left, right, center).
        // Benefit: Allows specific arrangement of UI components, making it easy to structure the dashboard layout.
        
        // Center Text Area for Typing Input
        TextArea typingArea = new TextArea();
        typingArea.setPromptText("Please enter brainrot...");
        typingArea.setWrapText(true);
        typingArea.setPrefSize(400, 200);
        root.setCenter(typingArea);
        // Center Region: Creates a `TextArea` in the center region of `BorderPane` for typing input.
        // - `setPromptText`: Displays placeholder text until the user types.
        // - `setWrapText`: Wraps text within the text area, improving readability.
        // - `setPrefSize`: Sets preferred dimensions of the text area.
        // - Benefit: The center region provides an intuitive, focused area for user input.

        // Left - Quiz Leaderboard and Button
        VBox quizBox = new VBox(10);
        quizBox.setPadding(new Insets(10));
        quizBox.setAlignment(Pos.TOP_CENTER);
        // `VBox` layout manager: Organizes elements in a vertical arrangement with 10px spacing.
        // - `setPadding`: Adds 10px padding around the box to create space.
        // - `setAlignment`: Aligns the content at the top center, making the layout consistent and visually appealing.
        // Benefit: `VBox` is used to easily stack the label and button for the quiz section in a vertical arrangement.
        
        Label quizLabel = new Label("Quiz Leaderboard");
        Button playQuizButton = new Button("Play Quiz");
        quizBox.getChildren().addAll(quizLabel, playQuizButton);
        root.setLeft(quizBox);
        // This is the Quiz Leaderboard Section, right now it's just the box and button for what it'll look like:
        // - `Label`: Displays "Quiz Leaderboard" as a title for this section.
        // - `Button`: Creates a "Play Quiz" button, which can later be connected to quiz functionality.
        // - `getChildren().addAll`: Adds label and button to `VBox`.
        // - `setLeft`: Places `quizBox` in the left region of `BorderPane`.
        // Benefit: Modular design of each leaderboard section makes the UI code readable and allows easy extension if needed.

        // Right - Match-It Leaderboard and Button
        VBox matchItBox = new VBox(10);
        matchItBox.setPadding(new Insets(10));
        matchItBox.setAlignment(Pos.TOP_CENTER);
        // `VBox` layout manager: Configures the right section of the dashboard similarly to the left.
        // - Benefit: Consistent styling across leaderboard sections keeps the UI visually cohesive and user-friendly.
        
        Label matchItLabel = new Label("Match-It Leaderboard");
        Button playMatchItButton = new Button("Play Match-It");
        matchItBox.getChildren().addAll(matchItLabel, playMatchItButton);
        root.setRight(matchItBox);
        // Match-It Leaderboard Section:
        // - `Label`: Displays "Match-It Leaderboard".
        // - `Button`: Adds a button to start the "Match-It" game.
        // - `getChildren().addAll`: Adds components to `VBox`.
        // - `setRight`: Places `matchItBox` in the right region of `BorderPane`.
        // Benefit: A separate section for each game provides clear navigation for the user and easy integration of additional games if needed.


        // Set up the main scene
        scene = new Scene(root, 600, 400);
        // Scene instantiation: Creates a new `Scene` using `root` (the `BorderPane` layout) with a width of 600px and height of 400px.
        // Benefit: A separate `Scene` for the dashboard allows flexibility if other scenes need to be added.
    
        stage.setScene(scene);
        stage.setTitle("User Dashboard");
        stage.show();
        // Configures and displays the `Stage` (window) with the dashboard UI.
        // - `setTitle`: Sets the window title to "User Dashboard".
        // - `show()`: Renders and displays the stage with the dashboard content.
        // Benefit: Separating UI setup from display logic (`setupUI()` vs. `DashboardPage()`) enhances modularity and readability.
    } // I'm using Stage alot because while I'm reteaching myself stage is what comes up alot
}