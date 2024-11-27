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
    private HashMap<Integer, String> imageMap = new HashMap<>(); // Stores image names associated with IDs
    private HashMap<Integer, String> quoteMap = new HashMap<>(); // Stores quotes associated with IDs
    private int matches = 0; // Keeps track of the number of successful matches
    private Random random = new Random(); // Random object for positioning elements

    // Constructor initializes the UI and loads pairs
    public MatchItPage(Stage stage) {
        this.stage = stage; // Store the stage reference
        setupUI(); // Set up the basic UI layout
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

        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> new DashboardPage(stage)); // Navigates back to DashboardPage
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
        ImageView imageView = new ImageView(new Image(new File("C:/Users/Aweso/Downloads/Brainrot Translator/pictures/" + imageName).toURI().toString()));
        imageView.setFitWidth(100); // Set the width of the image
        imageView.setFitHeight(100); // Set the height of the image

        setRandomPosition(imageView); // Set a random position for the image
        makeDraggable(imageView, id); // Make the image draggable

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
                if (matches == 5) { // Check if all matches are completed
                    Platform.runLater(this::loadRandomPairs); // Reload pairs
                }
            }
            event.setDropCompleted(true); // Indicate the drop was completed
            event.consume(); // Consume the event
        });

        root.getChildren().add(quoteLabel); // Add the label to the root
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
}
