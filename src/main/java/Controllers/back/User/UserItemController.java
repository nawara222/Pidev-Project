package Controllers.back.User;

import Controllers.back.Feedback.DashboardFeedbackController;
import Models.Users;
import Services.User.FeedbackService;
import Services.User.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class UserItemController {

    @FXML
    private Label datecreated;
    @FXML
    private Button deletebutton;
    @FXML
    private Button updatebutton;
    @FXML
    private Button feedbackButton;
    @FXML
    private Label email;
    @FXML
    private Label firstname;
    @FXML
    private Label lastlogin;
    @FXML
    private Label lastname;
    @FXML
    private Label role;
    @FXML
    private Label status;

    private Users user; //
    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService();
    private DashboardController dashboardController;


    public void setUser(Users user, DashboardController dashboardController) {
        this.user = user;
        this.dashboardController = dashboardController; //communication between two controllers

        // Set the labels to the user's data
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        email.setText(user.getEmailAddress());
        role.setText(user.getRole());
        status.setText(user.getAccountStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        datecreated.setText(user.getDateCreated() != null ? user.getDateCreated().format(formatter) : "empty");
        lastlogin.setText(user.getLastLogin() != null ? user.getLastLogin().format(formatter) : "Never logged in");
        checkAndDisplayFeedbackButton();

        deletebutton.setOnAction(event -> deleteUser());
        updatebutton.setOnAction(event -> updateDetails());
        feedbackButton.setOnAction(event -> feedbackdetails());

    }

    private void  feedbackdetails(){
        try {
            // Set the selected user ID before loading the new view
            DashboardFeedbackController.selectedUserId = this.user.getUserID();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/DashboardFeedbacks.fxml"));
            Parent root = loader.load();

            // Get the stage and set the scene
            Stage stage = (Stage) feedbackButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading view: " + e.getMessage());
        }
    }

    private void checkAndDisplayFeedbackButton() {
        try {
            boolean hasFeedback = fs.getFeedbackByUserId(user.getUserID()) != null;
            feedbackButton.setVisible(hasFeedback);
        } catch (SQLException e) {
            e.printStackTrace();
            feedbackButton.setVisible(false);
        }
    }

    private void deleteUser() {
        try {
            us.delete(user.getUserID());
            dashboardController.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDetails() {
        dashboardController.updateDetails(user);
    }

}
