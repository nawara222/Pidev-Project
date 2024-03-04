package Controllers.back.User;

import Models.Users;
import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddController {

    @FXML
    private TextField email;

    @FXML
    private TextField firstname;


    @FXML
    private TextField lastname;

    @FXML
    private TextField password;

    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    private UserService us = new UserService();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Artist", "Amateur","Admin");
        statusComboBox.getItems().addAll("Active", "Disabled");
        roleComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
    }


    @FXML
    void add(ActionEvent event) {
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String email = this.email.getText();
        String selectedRole = roleComboBox.getValue();
        String selectedStatus = statusComboBox.getValue();
        String userPassword = password.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || userPassword.length() < 3) {
             us.showAlert(Alert.AlertType.ERROR, "Form Error", "Please complete all fields.");
            return;
        }


        Users newUser = new Users();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(email);
        newUser.setRole(selectedRole);
        newUser.setAccountStatus(selectedStatus);
        newUser.setPassword(userPassword);

        try {
             us.create(newUser); // Assuming the UserService create method adds the user to the database
             us.showAlert(Alert.AlertType.INFORMATION, "User Added", "The new user has been added successfully.");
             us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");

        } catch (SQLException e) {
             us.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the new user,Email duplicate");
        }
    }

    @FXML
    void backbutton(ActionEvent event) {us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");}
}
