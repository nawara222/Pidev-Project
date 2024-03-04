package Controllers.Event;

import Controllers.Tickets.ShowTicketsFController;
import Models.Event;
import Services.Event.EventService;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class EventItemController {
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
    private Label nameEI;

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
    private Button showlistbutton;

    @FXML
    private Button deleteeventbuttonF;

    @FXML
    private Button editeventbuttonF;

    public void getIdE(Event event)
    {
        idE=event.getIdE();
    }


    public void setData(Event event)
    {  int loggedInUserId= UserService.currentlyLoggedInUser.getUserID();
        this.showTicketsFController=showTicketsFController;
        nameEI.setText(event.getNameE());
        dateEI.setText(String.valueOf(event.getDateE()));
        durationEI.setText(String.valueOf(event.getDurationE()));
        typeEI.setText(event.getTypeE());
        entryFeeEI.setText(String.valueOf(event.getEntryFeeE()));
        capacityEI.setText(String.valueOf(event.getCapacityE()));

        showlistbutton.setOnAction(eventt -> {
            try
            {
                idE=event.getIdE();
                Parent root = FXMLLoader.load(getClass().getResource("/Tickets/ShowTicketsF.fxml"));
                nameEI.getScene().setRoot(root);

            }
            catch (IOException e)
            {
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        });
        deleteeventbuttonF.setOnAction(eventt -> {
            try {
                es.deleteE(event.getIdE());
                showEventsFController.refreshEventsList(); // Refresh the list
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Event deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        editeventbuttonF.setOnAction(eventt -> {
            try
            {
                i = event.getIdE();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events/UpdateEventF.fxml"));
                Parent root = loader.load();
                UpdateEventFController updateCourseFController = loader.getController();
                UpdateEventFController.setInstance(updateCourseFController, event);
                nameEI.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        if (event.getUserid() == loggedInUserId) {
            // User is the creator, show update and delete buttons
            deleteeventbuttonF.setVisible(true);
            editeventbuttonF.setVisible(true);
        } else {
            // User is not the creator, hide update and delete buttons
            deleteeventbuttonF.setVisible(false);
            editeventbuttonF.setVisible(false);
        }
    }

    public void setShowEventsFController(ShowEventsFController controller) {
        this.showEventsFController = controller;
    }

    public void updateDetails()
    {

    }
    private void deleteEvent()
    {

    }


    @FXML
    void gotolistEvents(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Tickets/ShowTicketsF.fxml"));
            nameEI.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();  // Add this line to print the exception details
        }
    }

    private void resetFields() {
    }
}
