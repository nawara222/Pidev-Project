package Controllers.front.User;

import Services.User.UserService;
import Test.MainFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;

import java.sql.SQLException;

public class LoginController{

    private final UserService us = new UserService();
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    public void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (us.login(email, password)) {

            if (rememberMeCheckbox.isSelected()) {
                us.rememberUser(email, password);
            } else {
                us.clearRememberedUser();
            }

            if ("Disabled".equals(UserService.currentlyLoggedInUser.getAccountStatus())) {
                us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Your account is disabled.");
                return;
            }
            String role = UserService.currentlyLoggedInUser.getRole();
            String redirectPath = "/Art/FronClient.fxml";
            if ("UserAdmin".equals(role)) {
                redirectPath = "/back/Dashboard.fxml";
            } else if ("ArtAdmin".equals(role)) {
                redirectPath = "/Art/ManageArtist.fxml";
            }
            else if ("Artist".equals(role)) {
                redirectPath = "/Art/FronClient.fxml";
            }
            else if ("Amateur".equals(role)) {
                redirectPath = "/Art/FronClient.fxml";

            }
            else if ("AuctionAdmin".equals(role)) {
                redirectPath = "/Auction Admin/ViewAuction.fxml";

            }

            try {
                us.updateLastLoginTimestamp(email);

                us.switchView(MainFX.primaryStage, redirectPath);

            } catch (SQLException e) {
                us.showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading: " + e.getMessage());
            }
        } else {
            us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email or password.");
        }
    }


    @FXML
    void clickforget(MouseEvent event) {
        us.switchView(MainFX.primaryStage, "/front/ForgetPassword.fxml");
    }

}




