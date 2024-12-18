package dashboard;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import database.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MatchItPage {
    private static final String URL = "jdbc:mysql://localhost:3306/BrainrotTranslator"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "mha@auU,ta@+0J!"; // Database password
    private Stage stage; // The primary stage for this page
    private Pane root; // Root container for UI elements
    private MediaPlayer mediaPlayer; // BIG SHOT
	private MediaPlayer mediaPlayer1; // correct ding
    private MediaPlayer mediaPlayer2; // incorrect buzzer
    private HashMap<Integer, String> imageMap = new HashMap<>(); // Stores image names associated with IDs
    private HashMap<Integer, String> quoteMap = new HashMap<>(); // Stores quotes associated with IDs
    private int matches = 0; // Keeps track of the number of successful matches
    private Random random = new Random(); // Random object for positioning elements

    // Constructor initializes the UI and loads pairs
    public MatchItPage(Stage stage) {
        this.stage = stage; // Store the stage reference
        setupUI(); // Set up the basic UI layout
        //this.currentUserId = Session.getInstance().getCurrentUserId(); // Retrieve user ID from the session
        loadRandomPairs(); // Load random image-quote pairs
    }

    // Sets up the basic layout of the game
    private void setupUI() {
        // Root pane for the game content
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Title Label
        Label title = new Label("Match the Picture to its Quote!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: darkblue;");
        title.setLayoutX(250);
        title.setLayoutY(10);
        root.getChildren().add(title);
        
        // sets up CORRECT AND INCORRECT BUZZER
        mediaPlayer1 = new MediaPlayer(new Media(new File("resources/correct.mp3").toURI().toString()));
        mediaPlayer2 = new MediaPlayer(new Media(new File("resources/incorrect.mp3").toURI().toString()));


        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> { // Navigates back to DashboardPage
        	stopMediaPlayers();
        	new DashboardPage(stage);
        });
        HBox backBox = new HBox(backButton); // Container for the back button
        backBox.setAlignment(Pos.CENTER); // Center-align the button
        backBox.setPadding(new Insets(10));

        // Main container that includes the game and the back button
        VBox mainContainer = new VBox();
        mainContainer.getChildren().addAll(root, backBox); // Add game content and back button
        VBox.setVgrow(root, Priority.ALWAYS); // Make the game area take up remaining space

        // sets up EPIC BIGSHOT MUSIC TO LOOP TILL THE SUN EXPLODES
        mediaPlayer = new MediaPlayer(new Media(new File("songs/BIGSHOT.mp3").toURI().toString()));
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
        
        // Create the scene
        Scene scene = new Scene(mainContainer, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Match-It Game");
        stage.show();
    }


    // Loads random image-quote pairs from the database
    private void loadRandomPairs() {
        imageMap.clear(); // Clear previous data
        quoteMap.clear(); // Clear previous data
        root.getChildren().removeIf(node -> node instanceof ImageView || node instanceof Label && node.getUserData() != null); // Remove old nodes
        //root.getChildren().removeIf(node -> node instanceof Label || node instanceof ImageView && node.getUserData() != null); // Remove old nodes
        matches = 0; // Reset match count

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Connect to the database
        	// DOING IDMATCH-IT DIDN'T WORK SINCE IT ONLY SAW IT AS IDMATCH-IT
        	// WAS GONNA FOWARD ENGINEER AGAIN  BUT FOUND A SOLUTION ONLINE BABY
        	String query = "SELECT `idmatch-it`, `quote`, `imagename` FROM `Match-It` ORDER BY RAND() LIMIT 5"; // Query to fetch 5 random pairs
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery(); // Execute the query
                while (rs.next()) {
                    int id = rs.getInt("idmatch-it"); // Get the ID
                    String quote = rs.getString("quote"); // Get the quote
                    String imageName = rs.getString("imagename"); // Get the image name
                    imageMap.put(id, imageName); // Store the image name
                    quoteMap.put(id, quote); // Store the quote
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the error stack trace
            return; // Exit method if an error occurs
        }

        displayPairs(); // Display the fetched pairs
    }

    // Displays the image-quote pairs on the screen
    private void displayPairs() {
        List<Integer> ids = new ArrayList<>(imageMap.keySet()); // Get the list of IDs

        for (int id : ids) { // Loop through each ID
            placeImage(id); // Place the corresponding image
            placeQuote(id); // Place the corresponding quote
        }
    }

    // Places an image on the screen at a random position
    private void placeImage(int id) {
        String imageName = imageMap.get(id); // Get the image name

        // Create an ImageView for the image
        ImageView imageView = new ImageView(new Image(new File("pictures/" + imageName).toURI().toString()));
        imageView.setFitWidth(100); // Set the width of the image
        imageView.setFitHeight(100); // Set the height of the image

        setRandomPosition(imageView); // Set a random position for the image
        makeDraggable(imageView, id); // Make the image draggable

     // Define behavior when a draggable item is dragged over the imageView
        imageView.setOnDragOver(event -> {
            if (event.getGestureSource() != imageView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        
        // Define behavior when a draggable item is dropped on the label
        imageView.setOnDragDropped(event -> {
            int draggedId = Integer.parseInt(event.getDragboard().getString()); // Get the ID of the dragged item
            if (draggedId == id) { // Check if the IDs match
                root.getChildren().remove(imageView); // Remove the label
                root.getChildren().removeIf(node -> node instanceof Label && Integer.valueOf(draggedId).equals(node.getUserData())); // Remove the matching image
                matches++; // Increment the match count
                mediaPlayer1.seek(Duration.ZERO);
                mediaPlayer1.play(); // plays correct buzzer
                updateMatchItScore(50);
                if (matches == 5) { // Check if all matches are completed
                    Platform.runLater(this::loadRandomPairs); // Reload pairs
                }
            } else {
                // Apply glow and shake effect for incorrect match
                applyGlowAndShakeEffect(imageView);
                mediaPlayer2.seek(Duration.ZERO);
                mediaPlayer2.play(); // plays incorrect buzzer
                updateMatchItScore(-50);
                root.getChildren().stream()
                    .filter(node -> node instanceof ImageView && Integer.valueOf(draggedId).equals(node.getUserData()))
                    .findFirst()
                    .ifPresent(quoteLabel -> applyGlowAndShakeEffect(quoteLabel));
            }
            event.setDropCompleted(true); // Indicate the drop was completed
            event.consume(); // Consume the event
        });

        root.getChildren().add(imageView); // Add the image to the root
    }

    // Places a quote on the screen at a random position
    private void placeQuote(int id) {
        String quote = quoteMap.get(id); // Get the quote

        // Create a Label for the quote
        Label quoteLabel = new Label(quote);
        quoteLabel.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-padding: 5px;"); // Style the label

        setRandomPosition(quoteLabel); // Set a random position for the quote
        makeDraggable(quoteLabel, id); // Make the quote draggable

        // Define behavior when a draggable item is dragged over the label
        quoteLabel.setOnDragOver(event -> {
            if (event.getGestureSource() != quoteLabel && event.getDragboard().hasString()) { // Accept the drag if it has valid data
                event.acceptTransferModes(TransferMode.MOVE); // Accept the move transfer mode
            }
            event.consume(); // Consume the event
        });

        // Define behavior when a draggable item is dropped on the label
        quoteLabel.setOnDragDropped(event -> {
            int draggedId = Integer.parseInt(event.getDragboard().getString()); // Get the ID of the dragged item
            if (draggedId == id) { // Check if the IDs match
                root.getChildren().remove(quoteLabel); // Remove the label
                root.getChildren().removeIf(node -> node instanceof ImageView && Integer.valueOf(draggedId).equals(node.getUserData())); // Remove the matching image
                matches++; // Increment the match count
                updateMatchItScore(50);
                mediaPlayer1.seek(Duration.ZERO);
                mediaPlayer1.play(); // plays correct buzzer
                if (matches == 5) { // Check if all matches are completed
                    Platform.runLater(this::loadRandomPairs); // Reload pairs
                }
            } else {
                // Apply glow and shake effect for incorrect match
                applyGlowAndShakeEffect(quoteLabel);
                mediaPlayer2.seek(Duration.ZERO);
                mediaPlayer2.play(); // plays incorrect buzzer
                updateMatchItScore(-50);
                root.getChildren().stream()
                    .filter(node -> node instanceof ImageView && Integer.valueOf(draggedId).equals(node.getUserData()))
                    .findFirst()
                    .ifPresent(imageView -> applyGlowAndShakeEffect(imageView));
            }
            event.setDropCompleted(true); // Indicate the drop was completed
            event.consume(); // Consume the event
        });

        root.getChildren().add(quoteLabel); // Add the label to the root
    }

    private void applyGlowAndShakeEffect(javafx.scene.Node node) {
        // Apply a red glow effect
        node.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        // Create a shake animation
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);

        // Create a timeline to reset the effect after a few seconds
        Timeline resetTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            node.setStyle(""); // Reset styles
            node.setTranslateX(0); // Reset position
        }));

        // Play animations
        shake.play();
        resetTimeline.play();
    }
    
    // Sets a random position for a node on the screen
    private void setRandomPosition(javafx.scene.Node node) {
        double x, y; // Variables for position
        boolean intersects; // Flag for intersection
        do {
            x = 50 + random.nextDouble() * (root.getWidth() - 200); // Generate a random x position
            y = 50 + random.nextDouble() * (root.getHeight() - 200); // Generate a random y position
            node.setLayoutX(x); // Set the x position
            node.setLayoutY(y); // Set the y position
            intersects = root.getChildren().stream().anyMatch(other -> other.getBoundsInParent().intersects(node.getBoundsInParent())); // Check for intersections
        } while (intersects); // Repeat until no intersections
    }

    // Makes a node draggable
    private void makeDraggable(javafx.scene.Node node, int id) {
        node.setOnDragDetected(event -> {
            ClipboardContent content = new ClipboardContent(); // Create clipboard content
            content.putString(String.valueOf(id)); // Store the ID as a string
            node.startDragAndDrop(TransferMode.MOVE).setContent(content); // Start the drag-and-drop operation
            event.consume(); // Consume the event
        });
        node.setUserData(id); // Store the ID as user data
    }
    
    private void updateMatchItScore(int scoreChange) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        	int currentUserId = Session.getInstance().getCurrentUserId();
            // Update the gametype directly for the current user in the leaderboard
            String updateQuery = "UPDATE leaderboard SET gametype = gametype + ? WHERE userid = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, scoreChange);
                updateStmt.setInt(2, currentUserId);
                int rowsAffected = updateStmt.executeUpdate();

                // Debugging output to verify which user and rows are affected
                System.out.println("Current User ID: " + currentUserId);
                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected == 0) {
                    // If no rows were updated, insert a new record for the user
                    String insertQuery = "INSERT INTO leaderboard (userid, quizscore, gametype) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, currentUserId);
                        insertStmt.setInt(2, 0); // Default quizscore for Match-It
                        insertStmt.setInt(3, scoreChange);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // You may want to update the UI here to indicate an error occurred
            System.out.println("Error updating the Match-It score.");
        }
    }
    
    //Adding this to stop the music
    private void stopMediaPlayers() {
        mediaPlayer.stop();
    }
}
