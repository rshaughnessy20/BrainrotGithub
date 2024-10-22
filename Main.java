import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import RegistrationPage.java;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        RegistrationPage registrationPage = new RegistrationPage(primaryStage);
        primaryStage.setScene(new Scene(registrationPage.getView(), 400, 300));
        primaryStage.setTitle("User Registration");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}