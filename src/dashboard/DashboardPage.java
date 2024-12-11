package dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image; // new shit
import javafx.scene.image.ImageView; // new shit
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox; // new shit
import javafx.scene.layout.VBox;
import javafx.scene.text.Font; // new shit
import javafx.scene.text.FontWeight; // new shit
import javafx.scene.paint.Color; // new shit
import javafx.scene.layout.Background; // new shit
import javafx.scene.layout.BackgroundFill; // new shit
import javafx.scene.layout.CornerRadii; // new shit
import javafx.stage.Stage;
import javafx.animation.KeyFrame; // new shit for songs
import javafx.animation.Timeline; // new shit for songs
import javafx.scene.media.Media; // new shit for songs
import javafx.scene.media.MediaPlayer; // new shit for songs
import javafx.util.Duration; // new shit for songs
import java.io.File; // new shit for songs

import java.sql.*; // For the sql

import database.Session;

//Import statements: Import various JavaFX components to create the user interface for the dashboard page.
//- `Insets`: Adds padding around elements.
//- `Pos`: Positions elements within containers.
//- `Scene`: Represents the entire scene graph (UI layout).
//- `Button`, `Label`, `TextArea`: UI components for interaction and display.
//- `BorderPane`: A layout manager that divides the UI into regions (top, bottom, left, right, center).
//- `VBox`: Arranges elements vertically in a box layout.
public class DashboardPage {
	private static final String URL = "jdbc:mysql://localhost:3306/BrainrotTranslator";
    private static final String USER = "root";
    private static final String PASSWORD = "mha@auU,ta@+0J!";
    private TextArea typingArea; // THIS WAS A PAIN IN THE ASS
    private TextArea resultArea; // Had to change it to TextArea because label caused problems
    private ImageView slangImageView;
	// Class declaration: Defines `DashboardPage`, the UI component for the dashboard screen.
    // Benefit: Encapsulates dashboard logic and components in a single class, enabling easy reuse and modification.
	
	private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
	
    private Stage stage;
    private Scene scene;
    // Private fields: Hold references to the `Stage` (window) and `Scene` (UI layout) for this dashboard page.
    // Benefit: Storing these references allows the class to control the appearance and behavior of the dashboard.
    
    public DashboardPage(Stage stage) {
        this.stage = stage;
        int userId = Session.getInstance().getCurrentUserId(); // Retrieve user ID from the session
        setupUI();
        setupMedia();
    }
    // Constructor (DashboardPage): Initializes the dashboard with a `Stage` (window) passed in as a parameter.
    // - Benefit: Assigns the stage instance to the class and calls `setupUI()` to build the UI, separating layout from logic.
    // - Interaction: The `stage` reference is used throughout `setupUI()` to set up the dashboard page display.
    
    // Quiz Leaderboard Section
    private GridPane createQuizLeaderboard() {
        GridPane quizGrid = new GridPane();
        quizGrid.setHgap(10); // Horizontal gap between columns
        quizGrid.setVgap(10); // Vertical gap between rows
        quizGrid.setPadding(new Insets(10));
        
        Label quizLabel = new Label("Quiz Leaderboard");
        quizLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(quizLabel, 0, 0, 2, 1); // Spans 2 columns in the first row

        GridPane quizLeaderboard = new GridPane();
        quizLeaderboard.setPrefHeight(175);
        quizLeaderboard.setPrefWidth(150);
        GridPane.setConstraints(quizLeaderboard, 0, 1, 2, 1); // Spans 2 columns

        Button playQuizButton = new Button("Play Quiz");
        playQuizButton.setOnAction(e -> {
            stopMediaPlayers();
            new QuizPage(stage);
        });
        GridPane.setConstraints(playQuizButton, 0, 2); // Button below the leaderboard

        quizGrid.getChildren().addAll(quizLabel, quizLeaderboard, playQuizButton);
        populateLeaderboard(quizLeaderboard, "quizscore");
        return quizGrid;
    }
    
    // Match-It Leaderboard Section
    private GridPane createMatchItLeaderboard() {
        GridPane matchItGrid = new GridPane();
        matchItGrid.setHgap(10);
        matchItGrid.setVgap(10);
        matchItGrid.setPadding(new Insets(10));
        
        Label matchItLabel = new Label("Match-It Leaderboard");
        matchItLabel.setTextFill(Color.WHITE);
        GridPane.setConstraints(matchItLabel, 0, 0, 2, 1);

        GridPane matchItLeaderboard = new GridPane();
        matchItLeaderboard.setPrefHeight(175);
        matchItLeaderboard.setPrefWidth(150);
        GridPane.setConstraints(matchItLeaderboard, 0, 1, 2, 1);

        Button playMatchItButton = new Button("Play Match-It");
        playMatchItButton.setOnAction(e -> {
            stopMediaPlayers();
            new MatchItPage(stage);
        });
        GridPane.setConstraints(playMatchItButton, 0, 2);

        matchItGrid.getChildren().addAll(matchItLabel, matchItLeaderboard, playMatchItButton);
        populateLeaderboard(matchItLeaderboard, "gametype");
        return matchItGrid;
    }

    // Populate Leaderboard with a GridPane
    private void populateLeaderboard(GridPane leaderboard, String scoreColumn) {
        leaderboard.getChildren().clear(); // Clear previous entries
        leaderboard.setHgap(5); // Set horizontal gap
        leaderboard.setVgap(2); // Set vertical gap
        leaderboard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;"); // White background with black border

        String query = String.format(
            "SELECT u.name, l.%s " +
            "FROM user u " +
            "JOIN leaderboard l ON u.userid = l.userid " +
            "ORDER BY l.%s DESC LIMIT 10;", 
            scoreColumn, scoreColumn);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Adds headers
            leaderboard.add(new Label("Name"), 0, 0);
            leaderboard.add(new Label("Score"), 1, 0);

            int row = 1; // Starts adding entries below headers
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt(scoreColumn);

                // Adds name and score to the GridPane
                leaderboard.add(new Label(name), 0, row);
                leaderboard.add(new Label(String.valueOf(score)), 1, row);
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            leaderboard.add(new Label("Error loading leaderboard."), 0, 1);
        }
    }


    private void setupUI() {
        BorderPane root = new BorderPane();
        // `BorderPane` layout manager: Creates a root container with five regions (top, bottom, left, right, center).
        // Benefit: Allows specific arrangement of UI components, making it easy to structure the dashboard layout.

        // Sets the background color of the root layout to black
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        // Create a title label and set it to Comic Sans because it's funny
        Label titleLabel = new Label("Brainrot Translator");
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 20, 0));
        titleBox.setStyle("-fx-background-color: black;");
        
        // Add the title box to the top region of the BorderPane
        root.setTop(titleBox);

        // Finally got it to not go from top to bottom took a WHOLE lotta bullshit
        typingArea = new TextArea();
        typingArea.setPromptText("Please enter brainrot...");
        typingArea.setWrapText(true);
        typingArea.setPrefSize(300, 150);
        typingArea.setMaxHeight(150);
        
        // Result area and image view for display
        resultArea = new TextArea();
        resultArea.setWrapText(true);
        resultArea.setEditable(false);
        resultArea.setPrefHeight(150);

        // Initialize slangImageView and set default size
        slangImageView = new ImageView();
        slangImageView.setFitWidth(200);
        slangImageView.setPreserveRatio(true);

        // Search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchSlang());

        // VBox for Search Components
        VBox centerBox = new VBox(10, typingArea, searchButton, resultArea, slangImageView); // Add slangImageView to VBox
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        root.setCenter(centerBox);

        /*           GIFS WERE MAKING THE DEFINITION NOT WORK
        // Arrange GIFs and TextArea in an HBox with spacing and alignment
        HBox centerBox = new HBox(50, leftGif, typingArea, rightGif); // adds spacing in between
        centerBox.setAlignment(Pos.CENTER); // Align contents in the center horizontally
		*/
        // Add the HBox to the center region of BorderPane
        //root.setCenter(centerBox);
        
        // Left and Right Leaderboard Sections
        root.setLeft(createQuizLeaderboard());
        root.setRight(createMatchItLeaderboard());
        
        // Set up the main scene
        scene = new Scene(root, 800, 500);
        
        stage.setScene(scene);
        // Scene instantiation: Creates a new `Scene` using `root` (the `BorderPane` layout) with a width of 600px and height of 400px.
        // Benefit: A separate `Scene` for the dashboard allows flexibility if other scenes need to be added.
        
        stage.setTitle("User Dashboard");
        stage.show();
        // Configures and displays the `Stage` (window) with the dashboard UI.
        // - `setTitle`: Sets the window title to "User Dashboard".
        // - `show()`: Renders and displays the stage with the dashboard content.
        // Benefit: Separating UI setup from display logic (`setupUI()` vs. `DashboardPage()`) enhances modularity and readability.
    } // I'm using Stage alot because while I'm reteaching myself stage is what comes up alot
    
    private void setupMedia() {
        // Set the players on the path to Specialz
        mediaPlayer1 = new MediaPlayer(new Media(new File("songs/130Specialz.mp3").toURI().toString()));
        mediaPlayer2 = new MediaPlayer(new Media(new File("songs/131Specialz.mp3").toURI().toString()));
        mediaPlayer3 = new MediaPlayer(new Media(new File("songs/134Specialz.mp3").toURI().toString()));

        // Set each song to loop until the sun explodes
        mediaPlayer1.setOnEndOfMedia(() -> mediaPlayer1.seek(Duration.ZERO));
        mediaPlayer2.setOnEndOfMedia(() -> mediaPlayer2.seek(Duration.ZERO));
        mediaPlayer3.setOnEndOfMedia(() -> mediaPlayer3.seek(Duration.ZERO));

        // Have only the first one be unmuted at first
        mediaPlayer1.setMute(false);
        mediaPlayer2.setMute(true);
        mediaPlayer3.setMute(true);
        

        mediaPlayer1.play();
        mediaPlayer2.play();
        mediaPlayer3.play();

        setupAlternatingAudio();
    }

    private void setupAlternatingAudio() {
    	// Switches song for max brainrot
    	// Decreased time to 9 seconds for more noticable changes
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(9), e -> switchToMedia(mediaPlayer1)),
            new KeyFrame(Duration.seconds(18), e -> switchToMedia(mediaPlayer2)),
            new KeyFrame(Duration.seconds(27), e -> switchToMedia(mediaPlayer3))
        );

        timeline.setCycleCount(Timeline.INDEFINITE); // Loops the sequence INFINITELY
        timeline.play();
    }

    private void switchToMedia(MediaPlayer mediaPlayer) {
        // Mute all players
        mediaPlayer1.setMute(true);
        mediaPlayer2.setMute(true);
        mediaPlayer3.setMute(true);

        // Unmute only the specified player
        mediaPlayer.setMute(false);
    }
    
    //Adding this to stop the music
    private void stopMediaPlayers() {
        mediaPlayer1.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();
    }
    private void searchSlang() {
    	// Fetches the text entered in the typing area like a dog and trims the extra whitespace
        String enteredText = typingArea.getText().trim();
        
     // Checks if the entered text is not empty (this apparently helps prevents unnecessary database queries for empty inputs)
        if (!enteredText.isEmpty()) {
        	// Tries to connect to the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            	// SQL query should select the "definition" and "imagename" of a slang word that matches what was entered
                String query = "SELECT defintion, imagename FROM dictionary WHERE slang = ?";
                // This protects against SQL injection using parameterized queries, I know I probably won't need it but just in case
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                	// Binds the entered text to the first parameter in the SQL statement ("slang = ?")
                    stmt.setString(1, enteredText);
                    // Executes the query and stores the result in the ResultSet object "rs"
                    ResultSet rs = stmt.executeQuery();
                    // Checks if a record was found in the result set (basically means chekc that slang was found)
                    if (rs.next()) {
                    	// Retrieves the "definition" and "image name" from the result set
                        String definition = rs.getString("defintion");
                        String imageName = rs.getString("imagename");
                     // Calls "displayResult" method to update UI with the retrieved definition and image
                        displayResult(definition, imageName);
                    } else {
                    	// If no record is found, gives this message and gets rid of previous image
                        resultArea.setText("No definition found for this slang. The translator doesn't accept special characters like '.");
                        slangImageView.setImage(null);
                    }
                }
            } catch (SQLException e) { // Catches SQL-related exceptions
                e.printStackTrace(); // Logs the error to the console
                resultArea.setText("Error connecting to the database.");
            }
        } else {
        	// If nothing is entered, gives you this
            resultArea.setText("Please enter a slang word.");
        }
    }

    private void displayResult(String definition, String imageName) {
    	// Displays the retrieved slang definition in the result area
        resultArea.setText(definition);

        // Checks if image name is empty or null to avoid loading a non-existent image
        if (imageName == null || imageName.isEmpty()) {
            System.out.println("Image name is empty or null.");
            // Sets a placeholder image in case no image is associated with the slang word
            slangImageView.setImage(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
            return; // Exits the method, no more processing needed
        }

        // Constructs the file path for the image using the specified directory and image name
        String imagePath = "file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/" + imageName;
        System.out.println("Attempting to load image from: " + imagePath);
        // Creates a File object to verify the image file exists at the specified path
        File imageFile = new File("C:/Users/Aweso/Downloads/Brainrot Translator/pictures/" + imageName);
        // Checks if the file does not exist or is not a valid file (helps stop loading errors)
        if (!imageFile.exists() || !imageFile.isFile()) {
            System.out.println("File does not exist at the specified path.");
            // Sets a placeholder image if the file is missing or invalid
            slangImageView.setImage(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
            return; // Exits the method, no more processing needed
        }

        try {
        	// Ensures slangImageView is not null before attempting to load an image (avoiding the f---ing NullPointerException)
            if (slangImageView == null) {
                System.out.println("Error: slangImageView is null.");
                return; // Exits if slangImageView is not properly initialized
            }
            
            // Neat trick, it loads the image asynchronously (background loading), which reduces UI lag if image files are large
            Image image = new Image(imagePath, true); // Set background loading to true
            slangImageView.setImage(image);
            System.out.println("Image loaded successfully.");
        } catch (NullPointerException npe) { // Catches NullPointerExceptions
            System.out.println("NullPointerException while loading image: " + npe.getMessage());
            npe.printStackTrace(); // Print full stack trace for more context
            // Sets a placeholder image to handle the error gracefully
            slangImageView.setImage(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
        } catch (Exception e) { // Catches unexpected exceptions during image loading
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for unexpected errors
            // Sets a placeholder image to inform user of loading issue without crashing the application
            slangImageView.setImage(new Image("file:/C:/Users/Aweso/Downloads/Brainrot Translator/pictures/exploding-brain.gif"));
        }
    }
}