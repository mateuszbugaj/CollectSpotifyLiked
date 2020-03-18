package sample;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Button startButton;
    @FXML
    private Label progressInfo;
    @FXML
    private TextArea console;

    private Browser browser = new Browser();

    public void startButton(){
        browser.setConsole(console);
        browser.setProgressInfo(progressInfo);
        browser.setProgressInfo(progressInfo);
        progressInfo.setVisible(true);


        if(userName.getText().isEmpty()){
            progressInfo.setTextFill(Color.RED);
            progressInfo.setText("Provide all login information");
        } else {
            userName.setDisable(true);
            password.setDisable(true);
            startButton.setDisable(true);
            progressInfo.setTextFill(Color.GREEN);
            progressInfo.setText("In progress...");
            browser.setLoginInfo(userName.getText(), password.getText());
            browser.runBrowser();
        }
    }

}
