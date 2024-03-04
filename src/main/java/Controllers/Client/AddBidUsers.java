package Controllers.Client;

import Services.User.UserService;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import Models.Bid;
import Services.AuctionSystem.BidService;

import java.sql.SQLException;

public class AddBidUsers {

    private ViewBidUsers ViewBidUsers;
    private BidService bidService;

    @FXML
    private StackPane alertPopup;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField amountField;
    private int auctionId;

    // Setter method to inject ViewBidUsers
    public void setViewBidUsers(ViewBidUsers ViewBidUsers) {
        this.ViewBidUsers = ViewBidUsers;
    }

    // Setter method to inject BidService
    public void setBidService(BidService bidService) {
        this.bidService = bidService;
    }
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }
    @FXML
    void addBid(ActionEvent event) {
        System.out.println("Add bid button clicked.");
        String amountStr = amountField.getText().trim();
        errorLabel.setText("");

        try {
            int bidAmount = Integer.parseInt(amountStr);
            int userId = UserService.currentlyLoggedInUser.getUserID(); // You will need to implement this method to get the user ID.
            System.out.println("Creating new bid object...");
            Bid newBid = new Bid(this.auctionId, userId, bidAmount);
            newBid.setUserid(userId); // Assume you have a setter for the user ID in your Bid class.
            System.out.println("New bid object created: " + newBid);
            // Here, bidService.create should return the generated ID for the new bid.
            System.out.println("Calling createBidAndReturnId...");
            int bidId = bidService.createBidAndReturnId(newBid);
            System.out.println("Bid ID received: " + bidId);
            newBid.setIdbid(bidId);  // Set the generated ID to the newBid object.

            // Convert the fully populated Bid object to JSON.
            String bidJson = new Gson().toJson(newBid);

            if (ViewBidUsers.getBidClient() != null) {
                ViewBidUsers.getBidClient().sendBid(bidJson);
            }

            ViewBidUsers.populateTableView(newBid);
            clearFields();
            showAlertPopup();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid amount format. Please enter a number.");
        } catch (SQLException e) {
            errorLabel.setText("Error adding bid: " + e.getMessage());
        } catch (Exception e) {
            // Handle exceptions
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
