package Controllers.Bid;

import Models.Bid;
import Services.AuctionSystem.BidService;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.sql.SQLException;

public class AddBidController {

    private ViewBidsController viewBidsController;
    private BidService bidService;
    private int auctionId;

    public AddBidController() {
        // You can initialize default values or perform other setup here if needed
    }
    public AddBidController(BidService bidService) {
        this.bidService = bidService;
    }

    @FXML
    private StackPane alertPopup;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField amountField;
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }
    // Setter method to inject ViewBidsController
    public void setViewBidsController(ViewBidsController viewBidsController) {
        this.viewBidsController = viewBidsController;
    }

    // Setter method to inject BidService
    public void setBidService(BidService bidService) {
        this.bidService = bidService;
    }

    @FXML
    void addBid(ActionEvent event) {
        if (UserService.currentlyLoggedInUser == null) {
            // Handle case where user is not logged in
            errorLabel.setText("Please log in before adding a bid.");
            return;
        }
        String amountStr = amountField.getText().trim();
        int Userid=UserService.currentlyLoggedInUser.getUserID();
        System.out.println(Userid);
        // Clear error label for fresh feedback
        errorLabel.setText("");

        // Validate input
        int bidAmount;
        try {
            bidAmount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid amount format. Please enter a number.");
            return;
        }

        try {
            // Use the injected bidService
            bidService.create(new Bid(auctionId, Userid, bidAmount));
            // Handle successful addition (e.g., clear fields, navigate)
            clearFields();
            showAlertPopup(); // Show success popup (avoid duplicate attempts)

            // Refresh the table view after adding a bid
            viewBidsController.populateTableView();
        } catch (SQLException e) {
            errorLabel.setText("Error adding bid: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage()); // Display message for prompting user to bid higher
        }
    }

    // Method to show the alert popup
    private void showAlertPopup() {
        alertPopup.setVisible(true); // Make the popup visible
        alertPopup.requestFocus(); // Focus on the popup to capture user input
    }

    // Method to clear all input fields
    private void clearFields() {
        amountField.clear(); // Clear amount field
    }

    // Method to dismiss the alert popup
    @FXML
    public void dismissAlert() {
        alertPopup.setVisible(false); // Hide the popup
        clearFields();
    }


    }

