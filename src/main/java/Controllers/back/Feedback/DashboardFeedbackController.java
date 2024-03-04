package Controllers.back.Feedback;

import Models.Feedback;
import Services.User.FeedbackService;
import Services.User.UserService;
import Test.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardFeedbackController {
    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService();

    @FXML
    private TextArea adminanswer;


    @FXML
    private ScrollPane itemlist;

    @FXML
    private VBox vboxFeedbackList;

    @FXML
    private TextField searsh;
    public static int selectedUserId;

    private ObservableList<Feedback> masterFeedbackList = FXCollections.observableArrayList();
    private FilteredList<Feedback> filteredFeedbackList;

    public void initialize() {
        vboxFeedbackList = new VBox(5); // Spacing of 5 between items
        vboxFeedbackList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxFeedbackList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        try {
            masterFeedbackList.setAll(fs.getFeedbacksByUserId(selectedUserId));
            filteredFeedbackList = new FilteredList<>(masterFeedbackList, p -> true);

            // Add the listener to the search TextField
            searsh.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredFeedbackList.setPredicate(feedback -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return feedback.getType().toLowerCase().contains(lowerCaseFilter);
                });
                loadFeedbackItems();
            });

            loadFeedbackItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadFeedbackItems() {
        vboxFeedbackList.getChildren().clear();
        try {

            for (Feedback feedback : filteredFeedbackList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/Feeditem.fxml"));
                Node feedbackNode = loader.load();
                FeedbackItemController itemController = loader.getController();
                itemController.setFeedback(feedback, this);
                vboxFeedbackList.getChildren().add(feedbackNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void back(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void updateclicked(Feedback feedback) {
        String newAnswer = adminanswer.getText();
        if(newAnswer.isEmpty()) {
            us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "answer field empty'.");
            return;
        }
        try {
            if(feedback != null) {
                feedback.setAnswer(newAnswer); // Set the new answer
                feedback.setStatus("Closed");
                fs.update(feedback); // Update the feedback in the database

                adminanswer.clear();
                initialize();

                us.showAlert(Alert.AlertType.INFORMATION, "Feedback Updated", "The feedback has been updated successfully.");
            } else {
                us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "couldnt load feedback to update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
        }



    }



}
