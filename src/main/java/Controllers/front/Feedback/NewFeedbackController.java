package Controllers.front.Feedback;

import Models.Feedback;
import Services.User.FeedbackService;
import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class NewFeedbackController {
    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService(); // Assuming a default constructor exists that sets up a connection

    @FXML
    private TextArea feedbackQuestion;

    @FXML
    private ComboBox<String> feedbackType;



    @FXML
    public void initialize() {
        feedbackType.getItems().addAll("Bug Report", "Feature Request", "General Feedback");
        feedbackType.getSelectionModel().selectFirst();
    }
    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Feedback.fxml");
    }

    @FXML
    void submitButton(ActionEvent event) {
        if (feedbackQuestion.getText().isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Form Error", "Please complete all fields.");
            return;
        }

        // Create a new feedback object from the form inputs
        Feedback newFeedback = new Feedback();
        newFeedback.setUserId(UserService.currentlyLoggedInUser.getUserID());
        newFeedback.setType(feedbackType.getValue());
        newFeedback.setQuestion(feedbackQuestion.getText());
        newFeedback.setStatus("Open");
        newFeedback.setAnswer(""); // Assuming no answer has been provided yet

        // Analyze the sentiment of the feedback question and set user satisfaction
        String sentiment = newFeedback.analyzeSentiment();
        newFeedback.setUserSatisfaction(sentiment); // Set the analyzed sentiment

        try {
            fs.create(newFeedback); // Save the new feedback to the database
            us.showAlert(Alert.AlertType.INFORMATION, "Feedback Submitted", "Your feedback has been submitted successfully.");

            // Clear the form fields after successful submission
            feedbackQuestion.clear();
            feedbackType.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            us.showAlert(Alert.AlertType.ERROR, "Submission Error", "There was an error submitting your feedback.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }



}


