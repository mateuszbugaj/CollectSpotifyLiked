package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginWindow extends Stage {
    public LoginWindow() throws CannotOpenLoginWindowException {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
            setTitle("CollectSpotifyLiked");
            setScene(new Scene(root, 300, 400));
            setResizable(false);
            show();
        } catch (Exception e){
            e.fillInStackTrace();
            throw new CannotOpenLoginWindowException();
        }
    }

}
