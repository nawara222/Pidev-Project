package Controllers.Client;

import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import Models.Auction;
import Services.AuctionSystem.AuctionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAuctionClient implements Initializable {

    @FXML
    private GridPane itemsContainer;
    @FXML
    private TextField donations;
    private AuctionService auctionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsContainer.setHgap(40);
        itemsContainer.setVgap(40);
        auctionService = new AuctionService();

        // Fetch data from the database
        List<Auction> auctions = null;
        try {
            auctions = auctionService.read();
            // Generate items based on the fetched data
            generateItems(auctions);

            // Calculate and display total donations
            calculateAndDisplayTotalDonations();
        } catch (SQLException | IOException e) {
            e.printStackTrace(); // Handle database exception appropriately
        }
    }

    private void generateItems(List<Auction> auctions) throws IOException {
        if (auctions != null) {
            int columnIndex = 0;
            int rowIndex = 0;
            for (Auction auction : auctions) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Clients/Items.fxml"));
                VBox item = loader.load();
                GridPane.setColumnIndex(item, columnIndex);
                GridPane.setRowIndex(item, rowIndex);
                itemsContainer.getChildren().add(item);

                columnIndex++;
                if (columnIndex == 2) {
                    columnIndex = 0;
                    rowIndex++;
                }

                ItemController itemController = loader.getController();
                itemController.setAuctionData(auction);
            }
        }
    }

    private void calculateAndDisplayTotalDonations() throws SQLException {
        List<Integer> lastBidPrices = auctionService.getLastBidPrices();
        double totalDonations = 0;
        for (Integer bidPrice : lastBidPrices) {
            totalDonations += bidPrice * 0.05; // 5% of each last bid price
        }
        donations.setText(String.format("%.2f", totalDonations));
    }
    private UserService us = new UserService();
    public void PaymentBT(ActionEvent actionEvent) {
        us.switchView(MainFX.primaryStage, "/Baskets/Basket.fxml");
    }

    public void auctionbutton(ActionEvent actionEvent) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Artist /ViewAuctionArtist.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Auction Clients/ViewAuctionClient.fxml");
    }

    public void eventbutton(ActionEvent actionEvent) {
    }

    public void CoursesBT(ActionEvent actionEvent) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Courses/showCoursesF.fxml");}
    }

    public void artworksbutton(ActionEvent actionEvent) {
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");

    }

    public void homebutton(ActionEvent actionEvent) {
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }
}
