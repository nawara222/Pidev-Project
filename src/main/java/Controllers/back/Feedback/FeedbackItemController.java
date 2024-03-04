package Controllers.back.Feedback;

import Models.Feedback;
import Services.User.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FeedbackItemController {

    @FXML
    private Label Answer;

    @FXML
    private Label Question;

    @FXML
    private Label Type;

    @FXML
    private Button deletebutton;

    @FXML
    private HBox itemC;

    @FXML
    private Label status;

    @FXML
    private Button updatebutton;

    @FXML
    private Label satisfaction;


    private Feedback feedback; // The User object for this item
    private FeedbackService fs = new FeedbackService();
    private DashboardFeedbackController dashboardFeedbackController;

    public void setFeedback(Feedback feedback, DashboardFeedbackController dashboardFeedbackController) {
        this.feedback = feedback;
        this.dashboardFeedbackController = dashboardFeedbackController;

        // Set the question, type, and satisfaction labels
        Question.setText(feedback.getQuestion());
        Type.setText(feedback.getType());
        status.setText(feedback.getStatus());
        satisfaction.setText(feedback.getUserSatisfaction()); // Add this line to display user satisfaction

        if (feedback.getAnswer() != null && !feedback.getAnswer().isEmpty()) {
            Answer.setText(feedback.getAnswer());
            updatebutton.setVisible(false);
        } else {
            Answer.setText("No answer yet.");
            updatebutton.setVisible(true);
        }

        // Set the action for the delete button
        deletebutton.setOnAction(event -> handleDeleteFeedback());

        // Set the action for the update button if it's visible
        if (updatebutton.isVisible()) {
            updatebutton.setOnAction(event -> dashboardFeedbackController.updateclicked(feedback));
        }
    }

    public void handleDeleteFeedback() {
        try {
            fs.delete(feedback.getFeedbackId());
            dashboardFeedbackController.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error (show alert to the user, log error, etc.)
        }
    }


}
