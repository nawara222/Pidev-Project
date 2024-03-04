package Controllers.Bid;

import Models.Bid;
import Services.AuctionSystem.BidService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateBidController {
    public UpdateBidController() {
        // Default constructor
    }

    private BidService bidService;

    @FXML
    private TextField idTf;

    @FXML
    private TextField priceTf;
    @FXML
    private UpdateBidListener updateBidListener;

    private Bid bidToUpdate;


    public UpdateBidController(BidService bidService) {
        this.bidService = bidService;
    }

    public void setBid(Bid selectedBid) {
        this.bidToUpdate = selectedBid;
        if (selectedBid != null) {
            idTf.setText(String.valueOf(selectedBid.getIdbid()));
            priceTf.setText(String.valueOf(selectedBid.getBidAmount()));
        }
    }

    public void setOnBidUpdatedListener(UpdateBidListener listener) {
        this.updateBidListener = listener;
    }

    @FXML
    public void updateBid() {
        try {
            int id = Integer.parseInt(idTf.getText());
            int newPrice = Integer.parseInt(priceTf.getText());

            bidToUpdate.setIdbid(id);
            bidToUpdate.setBidAmount(newPrice);
            bidService.update(bidToUpdate);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Bid updated successfully");
            alert.showAndWait();

            if (updateBidListener != null) {
                updateBidListener.onBidUpdated();
            }

            handleClose();
        } catch (SQLException e) {
            handleError(e);
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) idTf.getScene().getWindow();
        stage.close();
    }

    private void handleError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void setBidService(BidService bidService) {
        this.bidService = bidService;
    }

    public interface UpdateBidListener {
        void onBidUpdated();
    }
}
