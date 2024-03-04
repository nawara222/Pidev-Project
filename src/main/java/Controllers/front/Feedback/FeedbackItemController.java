package Controllers.front.Feedback;

import Models.Feedback;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FeedbackItemController {

    @FXML
    private Label Answer;

    @FXML
    private Label Question;

    @FXML
    private Label Type;

    @FXML
    private Label status;


private Feedback feedback;
    public void setFeedback(Feedback feedback) {
        Answer.setText(feedback.getAnswer());
        Question.setText(feedback.getQuestion());
        Type.setText(feedback.getType());
        status.setText(feedback.getStatus());
    }

}
