package Controllers.Auction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Models.Auction;
import Services.AuctionSystem.AuctionService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateAuctionController {

    private final AuctionService auctionService = new AuctionService();


    @FXML private TextField AuctionNametf;
    @FXML private TextField priceTf;
    @FXML private DatePicker dateAuction;
    @FXML private TextField Auctiontime;
    @FXML private TextField description;
    @FXML private ImageView imageView;
    @FXML private Button Uploadimg;
    private String imgPath; // To store the path of the uploaded image

    private Auction auctionToUpdate;

    private OnAuctionUpdatedListener onAuctionUpdatedListener; // For update notification

    public void setOnAuctionUpdatedListener(OnAuctionUpdatedListener listener) {
        this.onAuctionUpdatedListener = listener;
    }

    public void setAuction(Auction auction) {
        this.auctionToUpdate = auction;
        if (auction != null) {
            AuctionNametf.setText(auction.getAuctionname());
            priceTf.setText(String.valueOf(auction.getPrice()));
            dateAuction.setValue(LocalDate.parse(auction.getDate()));
            Auctiontime.setText(auction.getTime());
            description.setText(auction.getDescription());
            imgPath = auction.getImgpath(); // Set the image path
            if (imgPath != null && !imgPath.isEmpty()) {
                Image image = new Image(imgPath);
                imageView.setImage(image);
            }
        }
    }

    @FXML
    void updateAuction(ActionEvent event) {
        try {
            String newName = AuctionNametf.getText();
            int newPrice = Integer.parseInt(priceTf.getText());
            String newTime = Auctiontime.getText();
            String newDescription = description.getText();
            LocalDate newDate = dateAuction.getValue();

            auctionToUpdate.setAuctionname(newName);
            auctionToUpdate.setPrice(newPrice);
            auctionToUpdate.setTime(newTime);
            auctionToUpdate.setDate(newDate.toString());
            auctionToUpdate.setDescription(newDescription);
            if (imgPath != null && !imgPath.isEmpty()) {
                auctionToUpdate.setImgpath(imgPath);
            }

            auctionService.update(auctionToUpdate);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Auction updated successfully");
            alert.showAndWait();

            if (onAuctionUpdatedListener != null) {
                onAuctionUpdatedListener.onAuctionUpdated(); // Notify listener
            }

            handleClose(event); // Close the dialog
        } catch (SQLException e) {
            handleError(e);
        }
    }
    @FXML
    void handleUploadimg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        // Configure the file chooser, for example, set the title, initial directory, and filters
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imgPath = selectedFile.toURI().toString();
            imageView.setImage(new Image(imgPath));
        }
    }
    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void handleError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public interface OnAuctionUpdatedListener {
        void onAuctionUpdated();
    }
}
