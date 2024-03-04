package Models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Feedback {
    private int feedbackId;
    private String status;
    private String type;
    private String question;
    private String answer;
    private String userSatisfaction;
    private int userId;  // id_U

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(
            Arrays.asList("happy", "satisfied", "amazing", "good", "great", "positive", "thankful", "enjoyed"));
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(
            Arrays.asList("unhappy", "bad", "disappointed", "poor", "negative", "terrible", "horrible", "hate"));

    public String analyzeSentiment() {
        String lowerCaseAnswer = this.answer.toLowerCase();
        int positiveScore = 0;
        int negativeScore = 0;

        // Tokenize the answer into words
        String[] words = lowerCaseAnswer.split("\\s+");

        // Count positive and negative words
        for (String word : words) {
            if (POSITIVE_WORDS.contains(word)) {
                positiveScore++;
            } else if (NEGATIVE_WORDS.contains(word)) {
                negativeScore++;
            }
        }

        // Determine sentiment
        if (positiveScore > negativeScore) {
            return "Positive";
        } else if (positiveScore < negativeScore) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }


    // Constructor
    public Feedback(int feedbackId, String status, String type, String question, String answer, String userSatisfaction, int userId) {
        this.feedbackId = feedbackId;
        this.status = status;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.userSatisfaction = userSatisfaction;
        this.userId = userId;
    }

    // Empty Constructor
    public Feedback() {
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserSatisfaction() {
        return userSatisfaction;
    }

    public void setUserSatisfaction(String userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // toString method
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", userSatisfaction='" + userSatisfaction + '\'' +
                ", userId=" + userId +
                '}';
    }
}
