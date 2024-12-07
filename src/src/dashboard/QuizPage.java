package dashboard;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import database.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame; // Gives me animations to let me hide images from you
import javafx.animation.Timeline; // Controls the duration of the animations
import javafx.scene.image.Image; // image shit
import javafx.scene.image.ImageView; // image shit

public class QuizPage {
    private Stage stage;
    private static final String URL = "jdbc:mysql://localhost:3306/BrainrotTranslator";
    private static final String USER = "root";
    private static final String PASSWORD = "mha@auU,ta@+0J!";
    private TextArea typingArea; // area where we anwser
    private TextArea questionArea; // anwser where we're questioned
    private MediaPlayer mediaPlayer; // BIG SHOT
	private MediaPlayer mediaPlayer1; // correct ding
    private MediaPlayer mediaPlayer2; // incorrect buzzer
    private ImageView feedbackImageView; // Displays feedback images for correct/incorrect answers
    private int currentQuestionId = -1; // Tracks the current question ID from the database
    // The value -1 serves as an initial flag to indicate that no question has been loaded yet.
    private int currentUserId = 1; // Placeholder: Set the current user's ID here
    private int wrongAttempts = 0; // Tracks incorrect attempts for the current question

    
    public QuizPage(Stage stage) {
        this.stage = stage;
        this.currentUserId = Session.getInstance().getCurrentUserId(); // Retrieve user ID from the session
        setupUI();
        loadRandomQuestion(); // Load a question when the page opens
    }

    private void setupUI() {
        BorderPane root = new BorderPane(); // Main layout container
        root.setPadding(new Insets(10));
        
        // Sets the background color of the root layout to black
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Box we get questions from
        questionArea = new TextArea();
        questionArea.setWrapText(true);
        questionArea.setEditable(false); // Let's me not edit, duh
        questionArea.setPrefHeight(50);

        // Area to type your answer
        typingArea = new TextArea();
        typingArea.setWrapText(true);
        typingArea.setPrefSize(300, 10);

        // Title
        Label titleLabel = new Label("Fill in the blank!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setTextFill(Color.WHITE);

        HBox titleBox = new HBox(titleLabel); // Container for title
        titleBox.setAlignment(Pos.CENTER); // Puts my title at the center
        root.setTop(titleBox); // Sets the title on top

        // Answer button
        Button answerButton = new Button("Answer");
        answerButton.setOnAction(e -> checkAnswer());

        // VBox for Components
        VBox centerBox = new VBox(10, questionArea, typingArea, answerButton);
        centerBox.setAlignment(Pos.CENTER); // center allign
        centerBox.setPadding(new Insets(20)); // adds padding
        root.setCenter(centerBox); // Puts at center of layout

        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> new DashboardPage(stage)); // Navigates back to DashboardPage
        HBox backBox = new HBox(backButton); // Container for the back button
        backBox.setAlignment(Pos.CENTER); // Center-align the button
        backBox.setPadding(new Insets(10));
        root.setBottom(backBox); // Place the back button at the bottom
        
        // sets up CORRECT AND INCORRECT BUZZER
        mediaPlayer1 = new MediaPlayer(new Media(new File("resources/correct.mp3").toURI().toString()));
        mediaPlayer2 = new MediaPlayer(new Media(new File("resources/incorrect.mp3").toURI().toString()));
        
        // Feedback ImageView for CORRECT and INCORRECT images
        feedbackImageView = new ImageView(); // Initialize the ImageView
        feedbackImageView.setFitWidth(300);
        feedbackImageView.setFitHeight(200);
        feedbackImageView.setVisible(false); // Hides the image until it's too late
        centerBox.getChildren().add(feedbackImageView); // Add the ImageView to the layout

        // sets up EPIC BIGSHOT MUSIC TO LOOP TILL THE SUN EXPLODES
        mediaPlayer = new MediaPlayer(new Media(new File("songs/BIGSHOT.mp3").toURI().toString()));
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        //mediaPlayer.play(); decided it was too pleasant to listen to
        
        Scene scene = new Scene(root, 800, 500); // set DIMENSIONS
        stage.setScene(scene); // Attach the scene to the stage, you know this Ryan come on
        stage.setTitle("Quiz Page"); // Do I have to explain this to you too Ryan?
        stage.show();
    }

    private void loadRandomQuestion() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // / SQL query to fetch a random question
            String query = "SELECT questionid, question FROM questions ORDER BY RAND() LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery(); // Execute the query
                if (rs.next()) {
                    currentQuestionId = rs.getInt("questionid"); // Get question ID
                    String question = rs.getString("question"); // Get question text
                    questionArea.setText(question); // Display the question
                } else {
                    questionArea.setText("No questions available."); // Handles no questions case
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            questionArea.setText("Error connecting to the database."); // Handle database connection errors
        }
    }

    private void checkAnswer() {
        String enteredAnswer = typingArea.getText().trim(); // Gets user input

        if (!enteredAnswer.isEmpty() && currentQuestionId != -1) { // Check if an answer is provided and a question is loaded
        	// since there is no -1 id in the database, if it's at -1 we know we haven't loaded a question yet
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            	// SQL query to check the correct answer
                String query = "SELECT anwser FROM questions WHERE questionid = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, currentQuestionId); // Bind question ID
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String correctAnswer = rs.getString("anwser"); // Fetch the correct answer
                        if (correctAnswer.equalsIgnoreCase(enteredAnswer)) { // Check if the answer is correct
                        	// Resets the correct sound buzzer each time
                            mediaPlayer1.seek(Duration.ZERO);
                        	mediaPlayer1.play(); // plays correct buzzer
                        	showFeedbackImage("resources/no-cap.jpg"); // Gives me the correct image for a second
                        	updateQuizScore(100); // Increase score
                        	questionArea.setText("Correct! Here's a new question:"); // not really necassary but whatever
                            loadRandomQuestion(); // Load a new question
                        } else {
                        	// Resets the incorrect sound buzzer each time
                        	mediaPlayer2.seek(Duration.ZERO);
                        	mediaPlayer2.play(); // plays incorrect buzzer
                        	showFeedbackImage("resources/cap.jpg"); // Gives me the incorrect image for a second
                        	updateQuizScore(-50); // Decrease score
                            wrongAttempts++; // adds a wrong attempt point
                            
                            if (wrongAttempts == 3) {
                                showAnswerAndNext(correctAnswer);
                                wrongAttempts = 3;
                            } else {
                                typingArea.setText("Incorrect! Try again.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                questionArea.setText("Error checking the answer."); // Handles errors
            }
        } else {
            questionArea.setText("Please type an answer.");  // Prompts for input if empty
        }

        typingArea.clear(); // Clear the input field after each attempt
    }
    
    private void updateQuizScore(int scoreChange) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Update the quizscore directly for the current user in the leaderboard
            String updateQuery = "UPDATE leaderboard SET quizscore = quizscore + ? WHERE userid = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, scoreChange);
                updateStmt.setInt(2, currentUserId);
                int rowsAffected = updateStmt.executeUpdate();
                
                // CHECKS WHICH USERID I AM WHICH IT DOESN'T
                System.out.println("Current User ID: " + currentUserId);
                System.out.println("Rows affected: " + rowsAffected);
                
                if (rowsAffected == 0) {
                    // If no rows were updated, insert a new record for the user
                    String insertQuery = "INSERT INTO leaderboard (userid, quizscore, gametype) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, currentUserId);
                        insertStmt.setInt(2, scoreChange);
                        insertStmt.setInt(3, 0); // Default gametype for Quiz
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            questionArea.setText("Error updating the score.");
        }
    }
    
    // Displays feedback images (CORRECT or INCORRECT) for 1 second, needed to learn a whole new thing for this
    private void showFeedbackImage(String imagePath) {
        feedbackImageView.setImage(new Image(new File(imagePath).toURI().toString())); // Load the image
        feedbackImageView.setVisible(true); // Make the image visible

        // Uses a Timeline to assassinate the image after 1 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> feedbackImageView.setVisible(false)));
        timeline.setCycleCount(1);  // Run the animation once
        timeline.play(); // Starts the animation
    }
    
    // Shows correct anwser after three wrong ones
    private void showAnswerAndNext(String correctAnswer) {
        questionArea.setText("Correct Answer: " + correctAnswer);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(5), e -> loadRandomQuestion())
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}

// IF WRONG THREE TIMES LOSE POINTS AND MOVE ON, SHOW ANWSER
