package Controllers.front.User;

import Models.Users;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

public class SignupController {

    private final UserService us = new UserService();
    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ImageView captchaImageView;
    @FXML
    private TextField captchaInputField;
    private String captchaText;


    @FXML
    public void clicksignup(ActionEvent event) {

        if (!captchaInputField.getText().equalsIgnoreCase(captchaText)) {
            us.showAlert(Alert.AlertType.ERROR, "CAPTCHA Error", "The CAPTCHA is incorrect. Please try again.");
            generateCaptcha(); // Regenerate the CAPTCHA
            clearForm();
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = (String) roleComboBox.getValue();


        if (firstName.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Form Error", "First name is required.");
            return;
        }

        // Last Name validation
        if (lastName.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Form Error", "Last name is required.");
            return;
        }

        // Email validation
        if (email.isEmpty() || !email.matches("^[\\w.-]+@esprit\\.tn$")) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email must not be empty & end with @esprit.tn");
            return;
        }

        // Password validation
        if (password == null || password.length() < 3) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 3 characters long");
            return;
        }

        // Role selection validation
        if (role == null || role.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Role", "You must select a role");
            return;
        }

        try {
            Users newUser = new Users();
            newUser.setFirstName(firstNameField.getText());
            newUser.setLastName(lastNameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setEmailAddress(emailField.getText());
            newUser.setRole((String) roleComboBox.getValue());
            newUser.setAccountStatus("Active");
            newUser.setDateCreated(LocalDateTime.now());


            us.create(newUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("User created successfully");
            alert.showAndWait();

            clearForm();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }

    public void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        generateCaptcha();
        captchaInputField.clear();
    }

    @FXML
    public void initialize() {
        generateCaptcha();
    }

    private void generateCaptcha() {
        captchaText = generateRandomText(6);
        Image captchaImage = createCaptchaImage(captchaText);
        captchaImageView.setImage(captchaImage);
    }

    private String generateRandomText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    private Image createCaptchaImage(String text) {
        int width = 200;
        int height = 70;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        drawComplexBackground(g2d, width, height);

        Random random = new Random();
        Font[] fonts = {new Font("Serif", Font.BOLD, 28), new Font("SansSerif", Font.BOLD, 28),
                new Font("Monospaced", Font.BOLD, 28), new Font("Dialog", Font.BOLD, 28)};

        int x = 10;
        int y = (height / 2) + 10;

        for (char c : text.toCharArray()) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.setFont(fonts[random.nextInt(fonts.length)]);

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(random.nextInt(25) - 12), 0, 0); // Rotate around the origin
            affineTransform.translate(x, y); // Translate to position the character
            g2d.setTransform(affineTransform);

            g2d.drawString(String.valueOf(c), 0, 0); // Draw character at the origin
            x += 30 + random.nextInt(5); // Increment x for the next character
        }

        drawForegroundNoise(g2d, width, height);

        g2d.dispose();

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void drawComplexBackground(Graphics2D g2d, int width, int height) {
        Random random = new Random();
        // Draw random lines
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int x2 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int y2 = random.nextInt(height);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.drawLine(x1, y1, x2, y2);
        }
        // Draw random circles
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int radius = random.nextInt(height / 4);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 50)); // Semi-transparent
            g2d.fillOval(x, y, radius, radius);
        }

        for (int i = 0; i < 3; i++) {
            int x1 = random.nextInt(width), y1 = random.nextInt(height);
            int x2 = random.nextInt(width), y2 = random.nextInt(height);
            int ctrlX1 = random.nextInt(width), ctrlY1 = random.nextInt(height);
            int ctrlX2 = random.nextInt(width), ctrlY2 = random.nextInt(height);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.setStroke(new BasicStroke(1.5f + random.nextFloat())); // Variable stroke width
            g2d.draw(new CubicCurve2D.Float(x1, y1, ctrlX1, ctrlY1, ctrlX2, ctrlY2, x2, y2));
        }
    }

    private void drawForegroundNoise(Graphics2D g2d, int width, int height) {
        Random random = new Random();
        // Draw foreground noise, such as random lines or squiggles over the text
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 2; i++) {
            int xs = random.nextInt(width), ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 4), ye = ys + random.nextInt(height / 4);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100)); // Semi-transparent
            g2d.drawLine(xs, ys, xe, ye);
        }
    }


}
