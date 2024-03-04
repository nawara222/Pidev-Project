    package Controllers.Bid;

    import Models.Bid;
    import Services.AuctionSystem.BidService;
    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
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
    import java.util.ResourceBundle;

    public class ViewBidsController implements Initializable {

        private int auctionId; // Auction ID associated with the bids
        @FXML
        private Label highestbid;
        @FXML
        private TableView<Bid> bidTableView;
        @FXML
        private TableColumn<Bid, Void> updateButtonColumn;
        @FXML
        private TableColumn<Bid, Void> deleteButtonColumn;
        @FXML
        private TableColumn<Bid, Integer> idColumn;
        @FXML
        private TableColumn<Bid, Integer> AmountColumn;
        @FXML
        private TableColumn<Bid, Integer> UserColumn;
        @FXML
        private Button closeButton;

        private BidService bidService; // Initialize it later

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setupTableView();
        }


        private void setupTableView() {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idbid"));
            AmountColumn.setCellValueFactory(new PropertyValueFactory<>("bidAmount"));
            UserColumn.setCellValueFactory(new PropertyValueFactory<>("userid"));

            updateButtonColumn.setCellFactory(param -> new TableCell<>() {
                private final Button updateButton = new Button("Update");

                {
                    updateButton.setOnAction(event -> {
                        Bid bid = getTableView().getItems().get(getIndex());
                        handleUpdateButton(bid);
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
                        Bid bid = getTableView().getItems().get(getIndex());
                        try {
                            bidService.delete(bid.getIdbid());
                            bidTableView.getItems().remove(bid);
                        } catch (SQLException e) {
                            // Handle the exception appropriately, e.g., show an error message
                            e.printStackTrace();
                        }
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
        }

        public void setAuctionId(int auctionId) {
            this.auctionId = auctionId;
            // Initialize BidService here after auctionId is set
            bidService = new BidService(auctionId);
            populateTableView();
        }

        public void populateTableView() {
            try {
                // Fetch the highest bid for the auction
                int highestBid = bidService.getHighestBidForAuction(auctionId);

                // Set the highest bid to the label
                highestbid.setText("Highest Bid: $" + highestBid);

                // Populate the TableView with bid data
                ObservableList<Bid> bidData = FXCollections.observableArrayList(bidService.read());
                bidTableView.setItems(bidData);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }

        @FXML
        private void handleCloseButton() {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }

        private void handleUpdateButton(Bid bid) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bid/UpdateBid.fxml"));
                Parent updateBidParent = loader.load();

                // Get the controller instance
                UpdateBidController updateBidController = loader.getController();

                // Instantiate the BidService with auctionId
                BidService bidService = new BidService(this.auctionId);

                // Set the BidService instance in the controller
                updateBidController.setBidService(bidService);

                // Set the Bid object in the controller
                updateBidController.setBid(bid);

                Stage updateBidStage = new Stage();
                updateBidStage.setScene(new Scene(updateBidParent));
                updateBidStage.setTitle("Update Bid");

                // Add listener for successful update
                updateBidController.setOnBidUpdatedListener(() -> Platform.runLater(this::populateTableView));

                updateBidStage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle I/O error appropriately
            }
        }

        @FXML
        private void AddBidController() {
            try {
                // Load the Add Bid view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bid/AddBid.fxml"));
                Parent addBidParent = loader.load();

                // Get the controller associated with the Add Bid view
                AddBidController addBidController = loader.getController();

                // Inject the BidService into the AddBidController
                addBidController.setBidService(bidService); // Assuming 'bidService' is already defined in this controller
                addBidController.setAuctionId(auctionId);
                // Set the ViewBidsController in AddBidController
                addBidController.setViewBidsController(this);

                // Create a new stage for the Add Bid view
                Stage addBidStage = new Stage();
                addBidStage.setScene(new Scene(addBidParent));
                addBidStage.setTitle("Add Bid");

                // Show the Add Bid view
                addBidStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

