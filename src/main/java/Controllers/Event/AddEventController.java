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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddEventController {
    private final EventService es = new EventService();


    @FXML
    private VBox chosenFruitCard;

    @FXML
    private TextField eventNameTF;

    @FXML
    private TextField eventDateTF;

    @FXML
    private TextField eventDurationTF;

    @FXML
    private TextField eventTypeTF;

    @FXML
    private TextField eventEntryFeeTF;

    @FXML
    private TextField eventCapacityTF;

    @FXML
    void addEvent(ActionEvent event)
    {
        try
        {
            es.createE(new Event(eventNameTF.getText(), LocalDate.parse(eventDateTF.getText()),Integer.parseInt(eventDurationTF.getText()),eventTypeTF.getText(), Double.parseDouble(eventEntryFeeTF.getText()), Integer.parseInt(eventCapacityTF.getText()), UserService.currentlyLoggedInUser.getUserID()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Event added successfully ");
            alert.showAndWait();
            eventNameTF.setText("");
            eventDateTF.setText("");
            eventDurationTF.setText("");
            eventTypeTF.setText("");
            eventEntryFeeTF.setText("");
            eventCapacityTF.setText("");
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
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAdmin.fxml"));
            eventNameTF.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("Error" +e.getMessage());
        }
    }

    @FXML
    void returnac(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAdmin.fxml"));
            eventNameTF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("Error" +e.getMessage());
        }

    }
}

