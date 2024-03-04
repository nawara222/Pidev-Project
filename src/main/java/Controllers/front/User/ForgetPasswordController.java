package Controllers.front.User;

import Models.Users;
import Services.User.PasswordHasher;
import Services.User.UserService;
import Test.MainFX;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;


public class ForgetPasswordController {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_EMAIL = "nxtpixel01@gmail.com";
    private static final String EMAIL_PASSWORD = "tngi xfph mxii uvgi";
    private String temporarypass;
    private final UserService us = new UserService();


    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> selectionBox;

    @FXML
    void SendButton(ActionEvent event) {
        String selectedMethod = selectionBox.getValue();
        if (selectedMethod == null || (selectedMethod.equals("Email") && emailField.getText().trim().isEmpty()) || (selectedMethod.equals("SMS") && phoneField.getText().trim().isEmpty())) {
            us.showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a method (Email or SMS) and enter your email or phone number.");
            return;
        }

        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        try {
            Users user = us.readUserByEmail(email);
            if (user != null) {
                String TP = generateOTP();
                temporarypass = TP;
                String hashedOtp = PasswordHasher.hashPassword(TP);
                us.updatePassword(user, hashedOtp);

                if (selectedMethod.equals("Email")) {
                    sendEmail(email, TP); // Send OTP via email
                } else {
                    sendSMSToUser(phone, TP); // Send OTP via SMS
                }
                us.showAlert(Alert.AlertType.INFORMATION, "Notification", "One-time password sent successfully.");
            } else {
                us.showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
            }
        } catch (SQLException e) {
            us.showAlert(Alert.AlertType.ERROR, "Database Error", "There was a problem accessing the user data.");
            e.printStackTrace(); // Log this error
        } catch (MessagingException | UnsupportedEncodingException e) {
            us.showAlert(Alert.AlertType.ERROR, "Communication Error", "Failed to send one-time password.");
            e.printStackTrace(); // Log this error
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int TP = 100000 + random.nextInt(900000);
        return String.valueOf(TP);
    }

    private VonageClient client = VonageClient.builder()
            .apiKey("a2bedbc3") // Replace with your actual API key
            .apiSecret("qRRZ4Y4uKIts790o") // Replace with your actual API secret
            .build();
    private void sendSMSToUser(String recipientPhoneNumber, String TP) {
        TextMessage message = new TextMessage(
                "Vonage APIs", // This should be your Vonage virtual number
                recipientPhoneNumber,
                "Your temporary password is: " + TP
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
    private void sendEmail(String toEmail, String subject) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        // Correct the MimeMessage import if necessary
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "Mail"));

        // Specify the correct Message class for RecipientType
        message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);

        // Set content to HTML
        message.setContent(getHtmlContent(temporarypass), "text/html; charset=utf-8");

        Transport.send(message);
    }


    private String getHtmlContent(String TP) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/styles/email.html");
            if (inputStream == null) {
                throw new IOException("Failed to load email template. File not found.");
            }

            String htmlContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                htmlContent = reader.lines().collect(Collectors.joining("\n"));
            }

            htmlContent = htmlContent.replace("{TP}", TP);
            return htmlContent;
        } catch (IOException e) {
            // Log the error or display a message to the user
            throw new RuntimeException("Failed to load email template: " + e.getMessage(), e);
        }
    }



    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
    }

}
