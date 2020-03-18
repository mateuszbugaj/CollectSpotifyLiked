package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    static {
        try {
            LoginWindow loginWindow = new LoginWindow();
        } catch (CannotOpenLoginWindowException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
    }

    public static void main(String[] args) {
        String browserDriverPath = "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", browserDriverPath);
        launch(args);

    }
}
