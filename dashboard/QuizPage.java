package dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuizPage {
    private Stage stage;
	private static final String URL = "jdbc:mysql://localhost:3306/BrainrotTranslator";
    private static final String USER = "root";
    private static final String PASSWORD = "mha@auU,ta@+0J!";
    private TextArea typingArea;
    private TextArea questionArea;

    public QuizPage(Stage stage) {
        this.stage = stage;
        setupUI();
    }

    private void setupUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Question area and image view for display
        questionArea = new TextArea();
        questionArea.setWrapText(true);
        questionArea.setEditable(false);
        questionArea.setPrefHeight(50);
        
        // Area to type your anwser
        typingArea = new TextArea();
        typingArea.setWrapText(true);
        typingArea.setPrefSize(300, 10);

        // Title
        Label titleLabel = new Label("Fill in the blank!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);

        // Answer button
        Button anwserButton = new Button("Anwser");
        anwserButton.setOnAction(e -> searchQuestion());
        
        // VBox for Search Components
        VBox centerBox = new VBox(10, questionArea, typingArea/*searchButton, resultArea, slangImageView*/); // Add slangImageView to VBox
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));
        root.setCenter(centerBox);
        
        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> new DashboardPage(stage)); // Navigates back to DashboardPage
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.CENTER);
        backBox.setPadding(new Insets(10));
        root.setBottom(backBox);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Quiz Page");
        stage.show();
    }
    private void searchQuestion() {
    	// Fetches the text entered in the typing area like a dog and trims the extra whitespace
        String enteredText = typingArea.getText().trim();
        
     // Checks if the entered text is not empty (this apparently helps prevents unnecessary database queries for empty inputs)
        if (!enteredText.isEmpty()) {
        	// Tries to connect to the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            	// SQL query should select the "definition" and "imagename" of a slang word that matches what was entered
                String query = "SELECT anwser FROM questions WHERE question = ?";
                // This protects against SQL injection using parameterized queries, I know I probably won't need it but just in case
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                	// Binds the entered text to the first parameter in the SQL statement ("slang = ?")
                    stmt.setString(1, enteredText);
                    // Executes the query and stores the result in the ResultSet object "rs"
                    ResultSet rs = stmt.executeQuery();
                    // Checks if a record was found in the result set (basically means chekc that slang was found)
                    if (rs.next()) {
                    	// Retrieves the "definition" and "image name" from the result set
                        String anwser = rs.getString("defintion");
                        //String imageName = rs.getString("imagename");
                     // Calls "displayResult" method to update UI with the retrieved definition and image
                        displayResults(anwser);
                    } else {
                    	// If no record is found, gives this message and gets rid of previous image
                        questionArea.setText("No definition found for this slang. The translator doesn't accept special characters like '.");
                    }
                }
            } catch (SQLException e) { // Catches SQL-related exceptions
                e.printStackTrace(); // Logs the error to the console
                questionArea.setText("Error connecting to the database.");
            }
        } else {
        	// If nothing is entered, gives you this
            questionArea.setText("Please enter a slang word.");
        }
    }
    private void displayResults(String anwser) {
    	// Displays the retrieved slang definition in the result area
        questionArea.setText(anwser);
    }
}


// have it play goku doing the thug shaker
	// have it play for other songs too
