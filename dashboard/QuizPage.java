package dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class QuizPage {
    private Stage stage;

    public QuizPage(Stage stage) {
        this.stage = stage;
        setupUI();
    }

    private void setupUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Title
        Label titleLabel = new Label("Quiz Page");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);

        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> new DashboardPage(stage)); // Navigates back to DashboardPage
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.CENTER);
        backBox.setPadding(new Insets(10));
        root.setBottom(backBox);

        // Placeholder for quiz content
        Label quizPlaceholder = new Label("Quiz content goes here.");
        quizPlaceholder.setStyle("-fx-font-size: 18px;");
        root.setCenter(quizPlaceholder);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Quiz Page");
        stage.show();
    }
}


// have it play goku doing the thug shaker
	// have it play for other songs too
