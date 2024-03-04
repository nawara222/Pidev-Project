package Services.User;

import Models.Users;
import Utils.MyDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class UserService implements IServiceUser<Users> {

    private Connection connection = MyDatabase.getInstance().getConnection();
    public static Users currentlyLoggedInUser = null;
    private Preferences prefs = Preferences.userNodeForPackage(UserService.class);
    private static int currentUserId;

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
    public void rememberUser(String email, String password) {
        prefs.put("email", email);
        prefs.put("password", password);
    }

    public void clearRememberedUser() {
        prefs.remove("email");
        prefs.remove("password");
    }

    public String autoLogin() {
        String email = prefs.get("email", null);
        String password = prefs.get("password", null); // Assuming you store a hashed token instead of a plain password.

        if (email != null && password != null) {
            try {
                Users user = readUserByEmail(email);
                // Here checkPassword should compare the hashed token with the hashed password stored in the database.
                if (user != null && PasswordHasher.checkPassword(password, user.getPassword())) {
                    currentlyLoggedInUser = user;
                    return user.getRole();
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while trying to auto-login: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

//singleton
    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Users user) throws SQLException {
        String sql = "insert into users (firstname, lastname, password, email_address, role, account_status)" +
                " values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getEmailAddress());
        ps.setString(5, user.getRole());
        ps.setString(6, user.getAccountStatus());
        ps.executeUpdate();
    }

    @Override
    public void update(Users user) throws SQLException {
        String sql = "update users set firstname = ?, lastname = ?, password = ?, email_address = ?, role = ?, account_status = ?, last_login = ? where user_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getEmailAddress());
        ps.setString(5, user.getRole());
        ps.setString(6, user.getAccountStatus());
        ps.setObject(7, user.getLastLogin());
        ps.setInt(8, user.getUserID());
        ps.executeUpdate();
    }

    @Override
    public void delete(int userId) throws SQLException {
        String sql = "delete from users where user_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    @Override
    public List<Users> read() throws SQLException {
        String sql = "SELECT user_id, firstname, lastname, email_address, role, account_status, date_created, last_login FROM users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Users> users = new ArrayList<>();
        while (rs.next()) {
            Users user = new Users(
                    rs.getInt("user_id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email_address"),
                    rs.getString("role"),
                    rs.getString("account_status"),
                    rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                    rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null
            );
            users.add(user);
        }
        return users;
    }

    public Users authenticate(String emailAddress) throws SQLException {
        String sql = "SELECT * FROM users WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, emailAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Users(
                            resultSet.getInt("user_id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("password"), // This should be the hashed password
                            resultSet.getString("email_address"),
                            resultSet.getString("role"),
                            resultSet.getString("account_status"),
                            resultSet.getObject("date_created", LocalDateTime.class),
                            resultSet.getObject("last_login", LocalDateTime.class)
                    );
                } else {
                    return null;
                }
            }
        }
    }


    public void updateLastLoginTimestamp(String email) throws SQLException {
        String sqlUpdate = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE email_address = ?";
        try (PreparedStatement p = connection.prepareStatement(sqlUpdate)) {
            p.setString(1, email);
            p.executeUpdate();
        }
    }

    public boolean login(String email, String password) {
        try {
            Users user = authenticate(email);
            if (user != null && PasswordHasher.checkPassword(password, user.getPassword())) {
                currentlyLoggedInUser = user;
                UserService.setCurrentUserId(user.getUserID());
                System.out.println("Current user ID: " + UserService.getCurrentUserId());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Users readUser(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Users(
                            rs.getInt("user_id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("password"),
                            rs.getString("email_address"),
                            rs.getString("role"),
                            rs.getString("account_status"),
                            rs.getTimestamp("date_created").toLocalDateTime(),
                            rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }


    public boolean isEmailUnique(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email_address = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // If count is 0, email is unique
                    return rs.getInt(1) == 0;
                }
                return false;
            }
        }
    }


    public void switchView(Stage stage, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "View Error", "Cannot load view: " + e.getMessage());
        }
    }


    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updatePassword(String newPassword) throws SQLException {
        String hashedPassword = PasswordHasher.hashPassword(newPassword);
    String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, hashedPassword);
        ps.setInt(2, currentlyLoggedInUser.getUserID());
        ps.executeUpdate();
    }}

    public Users readUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email_address = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Users(
                            rs.getInt("user_id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("password"),
                            rs.getString("email_address"),
                            rs.getString("role"),
                            rs.getString("account_status"),
                            rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                            rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }

    public void updatePassword(Users user, String hashedOtp) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hashedOtp);
            ps.setInt(2, user.getUserID());
            ps.executeUpdate();
        }
    }




}




