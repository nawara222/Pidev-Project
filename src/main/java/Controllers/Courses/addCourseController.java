package Controllers.Courses;

import Models.Courses;
import Services.CoursesandWorkshops.CoursesService;
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

import static Controllers.Courses.AddCourseFController.imagePath;

public class addCourseController {
    private final CoursesService cs = new CoursesService();


    @FXML
    private VBox chosenFruitCard;

    @FXML
    private TextField descf;

    @FXML
    private TextField namef;

    @FXML
    private TextField pricef;

    @FXML
    private TextField typef;

    @FXML
    void addCourse(ActionEvent event)
    {  int Userid= UserService.currentlyLoggedInUser.getUserID();
        try
        {
            cs.create(new Courses(namef.getText(),descf.getText(),Float.parseFloat(pricef.getText()),typef.getText(),imagePath,Userid));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success");
            alert.setContentText("Course added successfully ");
            alert.showAndWait();
            namef.setText("");
            descf.setText("");
            pricef.setText("");
            typef.setText("");
        }
        catch(SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCourses.fxml"));
            namef.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void returnac(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCourses.fxml"));
            namef.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }

    }
}