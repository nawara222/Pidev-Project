package Controllers.Event;

import Models.Event;
import Services.Event.EventService;
import Services.User.UserService;
import Test.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowEventsFController
{
    private EventService es = new EventService();


    @FXML
    private TextField searchf;

    @FXML
    private GridPane eventsGrid;

    @FXML
    private ScrollPane eventsItems;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private ObservableList<Event> observableEvents;

    @FXML
    private Button searchButton;

    private Controllers.Tickets.ShowTicketsFController ShowTicketsFController;

    @FXML
    void addEventButtonF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/AddEventF.fxml"));
            eventsGrid.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }



    // Method to update the GridPane with a list of events
    private void eventListF(List<Event> eventList) {
        eventsGrid.getChildren().clear();
        int columnCount = 0;
        int rowCount = 0;

        for (Event event : eventList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/EventItem.fxml"));
                Node eventNode = loader.load();
                EventItemController itemController = loader.getController();
                itemController.setShowEventsFController(this);
                itemController.setData(event);

                RowConstraints row = new RowConstraints();
                row.setPrefHeight(360);
                eventsGrid.add(eventNode, columnCount, rowCount);
                GridPane.setMargin(eventNode, new Insets(10));
                columnCount++;

                if (columnCount == 3) {
                    columnCount = 0;
                    rowCount++;
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace for debugging
            }
        }
    }


    @FXML
    void gotOfOb(ActionEvent event)
    {

    }

    @FXML
    void initialize() {
        // Initialize the observable list first to avoid NullPointerException
        observableEvents = FXCollections.observableArrayList();

        // Set grid properties
        eventsGrid.setHgap(30); // Horizontal gap between items
        eventsGrid.setVgap(0); // Vertical gap between items
        eventsItems.setContent(eventsGrid);
        eventsItems.setFitToWidth(true);

        // Set minimum height for each row
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Adjust this value as needed
        eventsGrid.getRowConstraints().add(rowConstraints);

        // Call refreshEventsList to populate observableEvents and update the grid
        refreshEventsList();

        // Add listener to the search field for dynamic search
        searchf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                refreshEventsList();
            } else {
                performSearch(newValue.trim().toLowerCase());
            }
        });

        // Initialize the sort ComboBox
        sortComboBox.setItems(FXCollections.observableArrayList("Sort by Date", "Sort by Entry Fee", "Sort by Capacity"));
        sortComboBox.getSelectionModel().selectFirst(); // Optionally set a default sort

        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            onSortOrderChanged(new ActionEvent(sortComboBox, null));
        });
    }


    @FXML
    void onSortOrderChanged(ActionEvent event) {
        String selectedSortOrder = sortComboBox.getValue(); // Use getValue() for ComboBox selection
        if (selectedSortOrder != null) {
            switch (selectedSortOrder) {
                case "Sort by Date":
                    observableEvents.sort(Comparator.comparing(Event::getDateE));
                    break;
                case "Sort by Entry Fee":
                    observableEvents.sort(Comparator.comparingDouble(Event::getEntryFeeE));
                    break;
                case "Sort by Capacity":
                    observableEvents.sort(Comparator.comparingInt(Event::getCapacityE));
                    break;
                default:
                    // Optionally handle the default case
                    break;
            }
            refreshEventsGrid(); // Refresh the GridPane to reflect the sort order
        }
    }


    // Additional methods used for sorting, assuming they exist.
    private void sortByDate() {
        observableEvents.sort(Comparator.comparing(Event::getDateE));
    }

    private void sortByEntryFee() {
        observableEvents.sort(Comparator.comparingDouble(Event::getEntryFeeE));
    }

    private void sortByCapacity() {
        observableEvents.sort(Comparator.comparingInt(Event::getCapacityE));
    }



    private void refreshEventsGrid() {
        eventListF(observableEvents);
    }


    public void refreshEventsList() {
        try {
            List<Event> eventList = es.readE();
            observableEvents.setAll(eventList); // Update the observable list
            refreshEventsGrid();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private UserService us = new UserService();
    @FXML
    void aboutUsButton(ActionEvent event) {

    }
    @FXML
    void artworksButton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Art/addart.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");


    }

    @FXML
    void auctionButton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Artist/ViewAuctionArtist.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Auction Clients/ViewAuctionClient.fxml");
    }


    @FXML
    void effect(MouseEvent event)
    {

    }
    @FXML
    public void courseButton(ActionEvent actionEvent) {

        us.switchView(MainFX.primaryStage, "/Courses/showCoursesF.fxml");
    }


    @FXML
    void homeButton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/FronClient.fxml"));
            eventsItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void searchButtonHandler(ActionEvent event) {
        String searchInput = searchf.getText().trim().toLowerCase();
        if (searchInput.isEmpty()) {
            refreshEventsList();
        } else {
            performSearch(searchInput);
        }
    }


    private void performSearch(String searchInput) {
        try {
            List<Event> allEvents = es.readE(); // Fetch all events from the service
            List<Event> filteredEvents = allEvents.stream()
                    .filter(e -> e.getNameE().toLowerCase().contains(searchInput) ||
                            e.getTypeE().toLowerCase().contains(searchInput))
                    .collect(Collectors.toList());

            if (filteredEvents.isEmpty()) {
                System.out.println("No events found matching the search criteria.");
                // You may want to update the UI to reflect that no results were found
            } else {
                eventListF(filteredEvents); // Update the grid with the filtered list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while performing search: " + e.getMessage());
            // You may want to show an alert instead of printing to the console
        }
    }

    @FXML
    void viewCartButton(ActionEvent event) {

    }

}
