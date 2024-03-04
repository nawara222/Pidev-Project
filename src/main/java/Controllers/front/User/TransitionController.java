/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.front.User;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransitionController {

    @FXML
    private VBox vbox;
    private Parent fxml;


    @FXML
    private void on_signup(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(10);
        t.play();
        t.setOnFinished(e -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("/front/Signup.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                Logger.getLogger(TransitionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void on_signin(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished(e -> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("/front/Login.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                Logger.getLogger(TransitionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


}
