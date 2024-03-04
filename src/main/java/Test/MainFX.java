package Test;

import Services.User.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {


    private UserService us = new UserService();
    public static Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        String role = us.autoLogin(); // us is an instance of UserService

        if (role != null) {
            String fxmlFile;
            if ("UserAdmin".equals(role)) {
                fxmlFile = "/back/Dashboard.fxml";
            } else {
                fxmlFile = "/front/MainWindow.fxml";
            }
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            primaryStage.setScene(new Scene(root));
        } else {
            // Show login or transition view
            Parent root = FXMLLoader.load(getClass().getResource("/Art/FronClient.fxml"));
            primaryStage.setScene(new Scene(root));
        }
        primaryStage.setTitle("VinciApp");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


