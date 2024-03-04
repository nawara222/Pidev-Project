package Controllers.front.Feedback;

import Models.Feedback;
import Services.User.FeedbackService;
import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FeedbackController{

    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService();

    @FXML
    private ScrollPane itemlist;



    @FXML
    private VBox vboxFeedbackList;


    public void initialize() {
        vboxFeedbackList = new VBox(5);
        vboxFeedbackList.setFillWidth(true);
        itemlist.setContent(vboxFeedbackList);
        itemlist.setFitToWidth(true);
        try {
            List<Feedback> feedbacks = fs.getFeedbacksByUserId(UserService.currentlyLoggedInUser.getUserID());
            loadFeedbackItems(feedbacks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFeedbackItems(List<Feedback> feedbacks) {
        vboxFeedbackList.getChildren().clear(); // Clear any existing items
        for (Feedback feedback : feedbacks) {
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/feedbackitem.fxml"));
            Node feedbackNode = loader.load();
            FeedbackItemController itemController = loader.getController();
            itemController.setFeedback(feedback);
            vboxFeedbackList.getChildren().add(feedbackNode);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    void Settings(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
    }



    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser = null;
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void Reset(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/ResetPassword.fxml");
    }


    @FXML
    void newfeedback(ActionEvent event){us.switchView(MainFX.primaryStage, "/front/NewFeedback.fxml");}
}
