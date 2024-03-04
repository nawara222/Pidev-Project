package Controllers.Event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class FrontOrBackController {


    @FXML
    private Label lab;

    @FXML
    void goBack(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAdmin.fxml"));
            lab.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void goFrontArtist(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            lab.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void goFrontAmateur(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAF.fxml"));
            lab.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }
}
