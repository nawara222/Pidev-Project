package Controllers.Event;

import Models.Event;
import Services.Event.EventService;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddEventFController {
    private final EventService es = new EventService();


    @FXML
    private TextField eventNameTFF;

    @FXML
    private TextField eventDateTFF;

    @FXML
    private TextField eventDurationTFF;

    @FXML
    private TextField eventTypeTFF;

    @FXML
    private TextField eventEntryFeeTFF;

    @FXML
    private TextField eventCapacityTFF;

    @FXML
    private TextField searchEventTF;


    @FXML
    void addEventF(ActionEvent event)
    {int loggedInUserId= UserService.currentlyLoggedInUser.getUserID();
        try
        {
            es.createE(new Event(eventNameTFF.getText(), LocalDate.parse(eventDateTFF.getText()),Integer.parseInt(eventDurationTFF.getText()),eventTypeTFF.getText(), Double.parseDouble(eventEntryFeeTFF.getText()), Integer.parseInt(eventCapacityTFF.getText()),loggedInUserId));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Event added successfully ");
            alert.showAndWait();
            eventNameTFF.setText("");
            eventDateTFF.setText("");
            eventDurationTFF.setText("");
            eventTypeTFF.setText("");
            eventEntryFeeTFF.setText("");
            eventCapacityTFF.setText("");
        }
        catch(SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            eventNameTFF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("Error" +e.getMessage());
        }
    }


    @FXML
    void returnEF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            eventNameTFF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
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
    void eventButton(ActionEvent event) {

    }

    @FXML
    void gotOfOb(ActionEvent event) {

    }

    @FXML
    void homeButton(ActionEvent event) {

    }

    @FXML
    void searchButtonE(ActionEvent event) {

    }

    @FXML
    void viewCartButtonE(ActionEvent event) {

    }

}

