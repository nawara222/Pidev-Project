package Controllers.Event;

import Controllers.Tickets.ShowTicketAFController;
import Controllers.Tickets.ShowTicketsFController;
import Controllers.Tickets.TicketItemController;
import Models.Event;
import Services.Event.EventService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EventItemAController {
    EventService es = new EventService();
    private ShowTicketsFController showTicketsFController;
    public static int idE;
    private Event event;
    public Event selectedevent;
    private ShowEventsFController showEventsFController;
    public static int i;
    UpdateEventFController controller = new UpdateEventFController();

    public Event getSelectedevent()
    {
        return event;
    }

    @FXML
    public static AnchorPane apid;

    @FXML
    private Label nameEIA;

    @FXML
    private Label dateEI;

    @FXML
    private Label durationEI;

    @FXML
    private Label typeEI;

    @FXML
    private Label entryFeeEI;

    @FXML
    private Label capacityEI;


    @FXML
    private Button participateEA;

    private ShowEventsAFController showEventsAFController;
    private Event currentEvent;


    public void getIdE(Event event)
    {
        idE=event.getIdE();
    }




    public void setData(Event event)
    {

        nameEIA.setText(event.getNameE());
        dateEI.setText(String.valueOf(event.getDateE()));
        durationEI.setText(String.valueOf(event.getDurationE()));
        typeEI.setText(event.getTypeE());
        entryFeeEI.setText(String.valueOf(event.getEntryFeeE()));
        capacityEI.setText(String.valueOf(event.getCapacityE()));


        participateEA.setOnAction(actionEvent -> {
            try {
                idE = event.getIdE();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tickets/ShowTicketAF.fxml"));
                Parent root = loader.load();

                ShowTicketAFController ticketController = loader.getController();
                ticketController.setEvent(event); // Pass the event to the ticket controller

                nameEIA.getScene().setRoot(root); // Change scene
            } catch (IOException e) {
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        });

    }

    public void setShowEventsAFController(ShowEventsAFController controller) {
        this.showEventsAFController = controller;
    }


    @FXML
    void participate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tickets/ShowTicketAF.fxml"));
            Parent root = loader.load();

            // Get the TicketItemController and set the current event
            TicketItemController ticketController = loader.getController();
            ticketController.setCurrentEvent(this.currentEvent);

            // Now show the ticket in the scene or a new window as per your requirement
            // ...

        } catch(IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the ticket view: " + e.getMessage());
        }
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
        // You can now pass this event to TicketItemController when participate button is clicked
    }




    private void resetFields() {
    }
}
