package Controllers.Client;

import Controllers.ChatClient.ChatClientController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import Models.Auction;
import Models.Bid;
import Services.AuctionSystem.AuctionService;
import Services.AuctionSystem.BidService;
import Test.BidClient;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewBidUsers implements Initializable {
    @FXML
    private ImageView ImageView;
    @FXML
    private TextArea auctiondescription;
    @FXML
    private Label highestbid;
    @FXML
    private Button chatbutton;
    @FXML
    private Button usersbutton;
    @FXML
    private TableView<Bid> bidTableView;

    @FXML
    private TableColumn<Bid, Integer> idColumn;

    @FXML
    private TableColumn<Bid, Integer> AmountColumn;

    @FXML
    private TableColumn<Bid, Integer> UserColumn;
    private BidService bidService = null;

    private int auctionId;
    private BidClient bidClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();

        // Initialize and connect the bid client
        try {
            bidClient = new BidClient("localhost", 8001, auctionId, this);
            bidClient.connectBidClient(auctionId);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle connection error
        }
    }

    private void setupTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idbid"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("bidAmount"));
        UserColumn.setCellValueFactory(new PropertyValueFactory<>("userid"));
    }

    public void populateTableView(Bid newBid) {
        try {
            // Fetch the highest bid for the auction
            int highestBid = bidService.getHighestBidForAuction(auctionId);

            // Set the highest bid to the label
            highestbid.setText("Highest $" + highestBid);

            // Add the new bid to the TableView
            ObservableList<Bid> bidData = FXCollections.observableArrayList(bidService.read());
            bidTableView.setItems(bidData);

            // Add the new bid to the TableView
            refreshHighestBid();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }



    public void updateTableView(Bid newBid) {
        Platform.runLater(() -> {
            // Check if the bid is already in the TableView based on bid ID
            for (Bid bid : bidTableView.getItems()) {
                if (bid != null && bid.getIdbid() == newBid.getIdbid()) {
                    return; // If the bid is found, exit the method to avoid adding it again
                }
            }
            bidTableView.getItems().add(newBid); // Add the bid if it's not already in the list
            refreshHighestBid();
        });
    }



    public void refreshHighestBid() {
        if (bidTableView != null && bidTableView.getItems() != null) {
            Bid highestBid = bidTableView.getItems()
                    .stream()
                    .filter(Objects::nonNull) // Filter out null values
                    .max(Comparator.comparingInt(Bid::getBidAmount))
                    .orElse(null);

            if (highestBid != null) {
                highestbid.setText("Highest $" + highestBid.getBidAmount());
            } else {
                highestbid.setText("No bids available");
            }
        }
    }


    public BidClient getBidClient() {
        return bidClient;
    }

    @FXML
    private void AddBidUsers() {
        try {
            // Load the Add Bid view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Clients/AddBidClients.fxml"));
            Parent addBidParent = loader.load();

            // Get the controller associated with the Add Bid view
            AddBidUsers addBidUsers = loader.getController();
            addBidUsers.setAuctionId(this.auctionId);
            // Inject the BidService into the AddBidUsers
            addBidUsers.setBidService(bidService);

            // Set the ViewBidUsers in AddBidUsers
            addBidUsers.setViewBidUsers(this);

            // Create a new stage for the Add Bid view
            Scene scene = new Scene(addBidParent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Bid");

            // Show the Add Bid view
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
        // Initialize BidService here after auctionId is set
        bidService = new BidService(auctionId);

        try {
            AuctionService auctionService = new AuctionService();
            Auction auction = auctionService.getAuctionById(auctionId);
            if (auction != null) {
                String imagePath = auction.getImgpath();
                Image image = new Image(imagePath);
                ImageView.setImage(image);

                // Set the auction description
                auctiondescription.setText(auction.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        // Connect the bid client after setting the auctionId
        try {
            bidClient.connectBidClient(auctionId);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle connection error
        }

        // Populate the table view with existing bids
        populateTableView(null);
    }

    @FXML
    public void Userschat(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Clients/Userschat.fxml"));
            Parent chatWindow = loader.load();

            ChatClientController chatController = loader.getController();
            chatController.setAuctionId(this.auctionId); // Pass the auctionId to the chat controller

            Stage stage = new Stage();
            stage.setScene(new Scene(chatWindow));
            stage.setTitle("Auction Chat: " + this.auctionId);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

}
