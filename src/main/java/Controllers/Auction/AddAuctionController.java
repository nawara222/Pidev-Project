package Controllers.Auction;

import Controllers.Artist.ViewAuctionArtist;
import Models.Auction;
import Services.AuctionSystem.AuctionService;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddAuctionController {

    private ViewAuctionController viewAuctionController;

    private final AuctionService auctionService = new AuctionService();
    private Image uploadedImage;
    @FXML
    private TextField AuctionNametf;
    @FXML
    ImageView imageView;
    @FXML
    private TextField price;
    @FXML
    private Button Uploadimg;
    @FXML
    private StackPane alertPopup;

    @FXML
    private Label errorLabel;
    @FXML
    private String imgPath;

    @FXML
    private TextField description;
    @FXML
    private DatePicker dateAuction;

    @FXML
    private TextField Auctiontime;



    // Setter method to inject ViewAuctionController

    private ViewAuctionArtist viewAuctionArtist;

    // Setter method to inject ViewAuctionArtist
    public void setViewAuctionArtist(ViewAuctionArtist viewAuctionArtist) {
        this.viewAuctionArtist = viewAuctionArtist;
        this.viewAuctionController = null; // Ensure that the ViewAuctionController reference is null
    }

    public void setViewAuctionController(ViewAuctionController viewAuctionController) {
        this.viewAuctionController = viewAuctionController;
    }

    public void initialize() {
        // Set up event handler for the Uploadimg
        Uploadimg.setOnAction(event -> handleUploadimg());
    }

    private void handleUploadimg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\xampp\\htdocs\\image"));

        fileChooser.setTitle("Choose Image File");
        // Set extension filters, if you want to restrict to certain file types


        File selectedFile = fileChooser.showOpenDialog(Uploadimg.getScene().getWindow());
        if (selectedFile != null) {
            // Set the image path for later use
            imgPath = selectedFile.toURI().toString();

            // Create a new Image and set it to the ImageView
            uploadedImage = new Image(imgPath);
            imageView.setImage(uploadedImage);
        }
    }
    @FXML
    public void addAuction(ActionEvent event) {
        String auctionName = AuctionNametf.getText().trim();
        String priceStr = price.getText().trim();
        String auctionTime = Auctiontime.getText().trim();
        LocalDate auctionDate = dateAuction.getValue();
        String imgPathStr = imgPath.trim();
        String descriptionStr = description.getText().trim();

        // Clear error label for fresh feedback
        errorLabel.setText("");

        // Validate input
        int priceInt;

        try {
            priceInt = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid price  Please enter numbers.");
            return;
        }

        if (auctionName.isEmpty() || auctionTime.isEmpty() || auctionDate == null
                || imgPathStr.isEmpty() || descriptionStr.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }
        try {
            auctionService.create(new Auction(priceInt, UserService.currentlyLoggedInUser.getUserID(), auctionTime, auctionDate.toString(), auctionName, imgPathStr, descriptionStr));

            // Handle successful addition (e.g., clear fields, navigate)
            clearFields();
            showAlertPopup(); // Show success popup (avoid duplicate attempts)

            // Refresh the table view after adding an auction

        } catch (SQLException e) {
            errorLabel.setText("Error adding auction: " + e.getMessage());
        }
    }


    // Method to show the alert popup
    private void showAlertPopup() {
        alertPopup.setVisible(true); // Make the popup visible
        alertPopup.requestFocus(); // Focus on the popup to capture user input
    }

    // Method to clear all input fields
    private void clearFields() {
        AuctionNametf.clear(); // Clear auction name field
        price.clear(); // Clear price field
        Auctiontime.clear(); // Clear Auction time field
        dateAuction.getEditor().clear(); // Clear Date picker
        description.clear(); // Clear description field
    }

    // Method to dismiss the alert popup
    @FXML
    public void dismissAlert() {
        alertPopup.setVisible(false); // Hide the popup
        clearFields();
    }



}
