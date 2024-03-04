package Services.User;

import Models.Feedback;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService implements IServiceUser<Feedback> {

    private Connection connection;

    public FeedbackService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (status, type, question, answer, user_satisfaction, id_U) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, feedback.getStatus());
            ps.setString(2, feedback.getType());
            ps.setString(3, feedback.getQuestion());
            ps.setString(4, feedback.getAnswer());
            ps.setString(5, feedback.getUserSatisfaction());
            ps.setInt(6, feedback.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Feedback feedback) throws SQLException {
        String sql = "UPDATE feedback SET status = ?, type = ?, question = ?, answer = ?, user_satisfaction = ? WHERE feedback_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, feedback.getStatus());
            ps.setString(2, feedback.getType());
            ps.setString(3, feedback.getQuestion());
            ps.setString(4, feedback.getAnswer());
            ps.setString(5, feedback.getUserSatisfaction());
            ps.setInt(6, feedback.getFeedbackId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int feedbackId) throws SQLException {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Feedback> read() throws SQLException {
        return null;
    }

    public Feedback getFeedbackByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM feedback WHERE id_U = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Feedback(
                            rs.getInt("feedback_id"),
                            rs.getString("status"),
                            rs.getString("type"),
                            rs.getString("question"),
                            rs.getString("answer"),
                            rs.getString("user_satisfaction"),
                            rs.getInt("id_U")
                    );
                }
            }
        }
        return null; // No feedback found for the given user ID
    }


    public List<Feedback> getFeedbacksByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM feedback WHERE id_U = ?";
        List<Feedback> feedbackList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback(
                            rs.getInt("feedback_id"),
                            rs.getString("status"),
                            rs.getString("type"),
                            rs.getString("question"),
                            rs.getString("answer"),
                            rs.getString("user_satisfaction"),
                            rs.getInt("id_U")
                    );
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }


}