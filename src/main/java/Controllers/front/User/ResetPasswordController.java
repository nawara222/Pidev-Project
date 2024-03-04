package Controllers.front.User;

import Services.User.PasswordHasher;
import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static Services.User.UserService.currentlyLoggedInUser;

public class ResetPasswordController {
    @FXML
    private TextField ancientpass;

    @FXML
    private TextField confirmpass;

    @FXML
    private TextField newpass;

    private UserService us = new UserService();

    @FXML
    void Settings(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
    }

    @FXML
    void Save(ActionEvent event){
        // Retrieve the password values
        String oldPassword = ancientpass.getText();
        String newPassword = newpass.getText();
        String confirmPassword = confirmpass.getText();

        // Check if any of the fields are empty
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        // Check if the old password is correct
        if(!PasswordHasher.checkPassword(oldPassword, currentlyLoggedInUser.getPassword())) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The old password is incorrect.");
            return;
        }

        // Check if the new password is at least 3 characters long
        if (newPassword.length() < 3) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The new password must be at least 3 characters long.");
            return;
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            us.showAlert(Alert.AlertType.ERROR, "Error", "The new passwords do not match.");
            return;
        }

        // Update the password
        try {
            currentlyLoggedInUser.setPassword(newPassword);
            // Assuming you have a method in UserService to update the user in the database
            us.updatePassword(newPassword);
            us.showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");

            ancientpass.clear();
            newpass.clear();
            confirmpass.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            us.showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating password: " + e.getMessage());
        }
    }
    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        currentlyLoggedInUser = null;
        us.clearRememberedUser();
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void Feedback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Feedback.fxml");
    }
}
