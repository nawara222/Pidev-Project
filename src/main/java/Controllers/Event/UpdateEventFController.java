package Controllers.Event;

import Models.Event;
import Services.Event.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;


public class UpdateEventFController {
    private Event selectedEventsID;
    private EventService es = new EventService();
    private  EventItemController eventItemController;
    private static UpdateEventFController instance;



    @FXML
    private TextField descriptioncFU;

    @FXML
    private TextField nameEFU;

    @FXML
    private TextField dateEFU;

    @FXML
    private TextField durationEFU;

    @FXML
    private TextField typeEFU;

    @FXML
    private TextField entryFeeEFU;

    @FXML
    private TextField capacityEFU;

    @FXML
    private TextField searchfT;


    @FXML
    void initialize() throws SQLException
    {
        if (selectedEventsID != null)
        {
            nameEFU.setText(selectedEventsID.getNameE());
            dateEFU.setText(String.valueOf(selectedEventsID.getDateE()));
            durationEFU.setText(String.valueOf(selectedEventsID.getDurationE()));
            typeEFU.setText(String.valueOf(selectedEventsID.getTypeE()));
            entryFeeEFU.setText(String.valueOf(selectedEventsID.getEntryFeeE()));
            capacityEFU.setText(String.valueOf(selectedEventsID.getCapacityE()));
        }
        else
        {
            System.out.println("courseItemController is not initialized.");
        }
    }
    @FXML
    void updateEventF(ActionEvent event) throws SQLException
    {
        try
        {
            if (selectedEventsID != null)
            {
                selectedEventsID.setNameE(nameEFU.getText());
                selectedEventsID.setDateE(LocalDate.parse(dateEFU.getText()));
                selectedEventsID.setDurationE(Integer.parseInt(durationEFU.getText()));
                selectedEventsID.setTypeE(typeEFU.getText());
                selectedEventsID.setEntryFeeE(Double.parseDouble(entryFeeEFU.getText()));
                selectedEventsID.setCapacityE(Integer.parseInt(capacityEFU.getText()));

                es.updateE(selectedEventsID);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Event updated successfully ");
                alert.showAndWait();
            }
        }
        catch (SQLException | NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public static void setInstance(UpdateEventFController instance, Event selectedEvent)
    {
        UpdateEventFController.instance = instance;
        instance.setSelectedEvent(selectedEvent);

    }
    private void initializeFieldsWithSelectedEventData() {
        if (selectedEventsID != null) {
            nameEFU.setText(selectedEventsID.getNameE());
            dateEFU.setText(String.valueOf(selectedEventsID.getDateE()));
            durationEFU.setText(String.valueOf(selectedEventsID.getDurationE()));
            typeEFU.setText(selectedEventsID.getTypeE());
            entryFeeEFU.setText(String.valueOf(selectedEventsID.getEntryFeeE()));
            capacityEFU.setText(String.valueOf(selectedEventsID.getCapacityE()));
        } else {
            System.out.println("No event selected.");
        }
    }
    private void setSelectedEvent(Event selectedEvent) {
        this.selectedEventsID = selectedEvent;
        initializeFieldsWithSelectedEventData(); // Call this method to initialize fields
    }

    @FXML
    void aboutUsButton(ActionEvent event) {

    }

    @FXML
    void artworksButton(ActionEvent event) {

    }

    @FXML
    void auctionButton(ActionEvent event) {

    }

    @FXML
    void courseButton(ActionEvent event) {

    }

    @FXML
    void gotOfOb(ActionEvent event) {

    }

    @FXML
    void homeButton(ActionEvent event) {

    }

    @FXML
    void returnEF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            nameEFU.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("Error" + e.getMessage());
        }

    }

    @FXML
    void searchButtonE(ActionEvent event) {

    }



    @FXML
    void viewCartButtonE(ActionEvent event) {

    }
}
