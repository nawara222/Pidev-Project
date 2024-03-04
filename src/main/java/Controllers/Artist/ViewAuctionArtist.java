package Controllers.Artist;

import Controllers.Auction.UpdateAuctionController;
import Controllers.Client.ViewBidUsers;
import Models.Auction;
import Services.AuctionSystem.ArtistService;
import Services.AuctionSystem.AuctionService;
import Services.User.UserService;
import Test.MainFX;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAuctionArtist implements Initializable {

    @FXML
    private TextField donations;

    @FXML
    private TableView<Auction> auctionTableView;

    @FXML
    private TableColumn<Auction, String> nameColumn;

    @FXML
    private TableColumn<Auction, Integer> priceColumn;

    @FXML
    private TableColumn<Auction, String> timeColumn;

    @FXML
    private TableColumn<Auction, String> dateColumn;

    @FXML
    private TableColumn<Auction, Void> updateButtonColumn;

    @FXML
    private TableColumn<Auction, Void> deleteButtonColumn;

    @FXML
    private TableColumn<Auction, Void> viewBidsButtonColumn; // New column for View Bids button
    @FXML
    private final ArtistService ArtistService = new ArtistService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();
        populateTableView();
    }

    private void setupTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("auctionname"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        updateButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    handleUpdateButton(auction); // Call your update auction method here
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    // Create a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Auction");
                    alert.setContentText("Are you sure you want to delete this auction?");

                    // Show the confirmation dialog
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                ArtistService.delete(auction.getId());
                                auctionTableView.getItems().remove(auction);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Handle the exception appropriately, e.g., show an error message
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        viewBidsButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBidsButton = new Button("View Bids");

            {
                viewBidsButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    handleViewBidsButton(auction.getId()); // Call your view bids method here
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewBidsButton);
                }
            }
        });
    }

    private void populateTableView() {
        try {
            ObservableList<Auction> auctionData = FXCollections.observableArrayList(ArtistService.read());
            auctionTableView.setItems(auctionData);
            calculateAndDisplayTotalDonations();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @FXML
    private void handleUpdateButton(Auction auction) {
        Auction selectedAuction = auctionTableView.getSelectionModel().getSelectedItem();
        if (selectedAuction != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Admin/UpdateAuction.fxml"));
            Parent updateAuctionParent;
            try {
                updateAuctionParent = loader.load();
            } catch (IOException e) {
                e.printStackTrace(); // Handle I/O error appropriately
                return;
            }

            UpdateAuctionController updateAuctionController = loader.getController();
            updateAuctionController.setAuction(selectedAuction);

            Stage updateAuctionStage = new Stage();
            updateAuctionStage.setScene(new Scene(updateAuctionParent));
            updateAuctionStage.setTitle("Update Auction");

            // Add listener for successful update
            updateAuctionController.setOnAuctionUpdatedListener(() -> {
                Platform.runLater(() -> {
                    refreshTableView();
                    // Show success message if desired
                });
            });

            updateAuctionStage.show();
        } else {
            // Show error message or prompt user to select an auction
        }
    }


    @FXML
    public void addAuction() {
        try {
            // Correct the path to your AddAuctionArtist.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Admin/AddAuction.fxml"));
            Parent addAuctionParent = loader.load();

            Stage addAuctionStage = new Stage();
            addAuctionStage.setScene(new Scene(addAuctionParent));
            addAuctionStage.setTitle("Add Auction");
            addAuctionStage.showAndWait();

            refreshTableView();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error
        }
    }


    // Method to refresh the table view after adding an auction
    public void refreshTableView() {
        populateTableView();
    }

    // Method to handle view bids button click
    private void handleViewBidsButton(int auctionId) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction Clients/ViewBidClients.fxml"));
        Parent viewBidsParent;
        try {
            viewBidsParent = loader.load();
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O error appropriately
            return;
        }
        ViewBidUsers bidsController = loader.getController();

        // Pass the auction ID to the bid view controller
        bidsController.setAuctionId(auctionId);



        Stage viewBidsStage = new Stage();
        viewBidsStage.setScene(new Scene(viewBidsParent));
        viewBidsStage.setTitle("View Bids");
        viewBidsStage.show();
    }
    private final AuctionService auctionService = new AuctionService();
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
            us.switchView(MainFX.primaryStage, "/Auction Clients/ViewAuctionClient.fxml");
    }

    public void eventbutton(ActionEvent actionEvent) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Events/ShowEventsF.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Events/ShowEventsAF.fxml");
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
