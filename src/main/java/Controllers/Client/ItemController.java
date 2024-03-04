package Controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import Models.Auction;

import java.io.IOException;

public class ItemController {
    @FXML
    private TextField auctionname;
    @FXML
    private TextField amountField;
    @FXML
    private ImageView imageView;
    private Auction auction;

    public void setAuctionData(Auction auction) {
        this.auction = auction;
        updateItemUI();
    }

    private void updateItemUI() {
        if (auction != null) {
            auctionname.setText(auction.getAuctionname());
            amountField.setText(String.valueOf(auction.getPrice()));
            // Load and display the image
            if (auction.getImgpath() != null && !auction.getImgpath().isEmpty()) {
                String imagePath = auction.getImgpath(); // Adjust path as necessary
                Image image = new Image(imagePath);
                imageView.setImage(image);
            }
        }
    }

    public void Participate(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Clients/ViewBidClients.fxml"));
            Parent bidViewParent = loader.load();

            // Get the controller associated with the bid view
            ViewBidUsers bidsController = loader.getController();

            // Pass the auction ID to the bid view controller
            bidsController.setAuctionId(auction.getId());

            Stage bidStage = new Stage();
            bidStage.setScene(new Scene(bidViewParent));
            bidStage.setTitle("Bids for Auction: " + auction.getAuctionname());
            bidStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O error appropriately
        }
    }
}
