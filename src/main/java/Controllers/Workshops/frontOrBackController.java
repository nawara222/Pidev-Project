package Controllers.Workshops;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class frontOrBackController {


    @FXML
    private Label lab;

    @FXML
    void goBack(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCourses.fxml"));
            lab.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void goFront(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCoursesF.fxml"));
            lab.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void goCalendar(ActionEvent event)
    {
       /* try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendarView.fxml"));
            Parent root = loader.load();
            SettingsController calendarController = loader.getController();
            calendarController.getCalendarView().initialize();
            lab.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }*/
    }
}
