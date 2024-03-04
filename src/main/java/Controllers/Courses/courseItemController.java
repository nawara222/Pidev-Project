package Controllers.Courses;

import Controllers.Workshops.ShowWorkshopsFController;
import Models.Courses;
import Services.CoursesandWorkshops.CoursesService;
import Services.User.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.sql.SQLException;

public class courseItemController
{
    CoursesService cs = new CoursesService();
    private ShowWorkshopsFController showWorkshopsFController;
    public static int idc;
    private Courses course;
    public Courses selectedcourse;
    public static int i;
    UpdateCourseFController controller = new UpdateCourseFController();
    private String imagePath;
    private ShowCoursesFController showCoursesFController;
    public void updateImage(Image image) {
        imageC.setImage(image);
    }

    public Courses getSelectedCourse()
    {
        return course;
    }

    @FXML
    public static AnchorPane apid;

    @FXML
    private Label descCI;

    @FXML
    private Label nameCI;

    @FXML
    private Label nbwCI;

    @FXML
    private Label priceCI;

    @FXML
    private Label typeCI;

    @FXML
    private Button showlistbutton;

    @FXML
    private Button deletecoursebuttonF;

    @FXML
    private Button editcoursebuttonF;

    @FXML
    private ImageView imageC;

    public void setParent(ShowCoursesFController showCoursesFController)
    {
        this.showCoursesFController=showCoursesFController;
    }

    public void setInstance(ShowCoursesFController showCoursesFController)
    {
        this.showCoursesFController=showCoursesFController;
    }


    public void get_idc(Courses course)
    {
        idc=course.getId_C();
    }

    public void setData(Courses course)
    {
        int loggedInUserId= UserService.currentlyLoggedInUser.getUserID();
        System.out.println("Logged in User ID: " + loggedInUserId);
        System.out.println("Course User ID: " + course.getUserid());
        //this.showWorkshopsFController=showWorkshopsFController;
        nameCI.setText(course.getNameC());
        descCI.setText(course.getDescriptionC());
        priceCI.setText(String.valueOf(course.getPriceC()));
        typeCI.setText(course.getType());
        nbwCI.setText(String.valueOf(course.getNumberW()));
        Image image = new Image(course.getImage_path());
        imageC.setImage(image);
        double size = 330; // Adjust the size according to your needs
        double cornerRadius = 30; // Adjust the corner radius for squircle shape

        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(cornerRadius * 2);
        clip.setArcHeight(cornerRadius * 2);

        imageC.setClip(clip);

        showlistbutton.setOnAction(event -> {
            try
            {
                idc=course.getId_C();
                Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
                descCI.getScene().setRoot(root);

            }
            catch (IOException e)
            {
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        });

        deletecoursebuttonF.setOnAction(event->{
            try
            {
                cs.delete(course.getId_C());
                showCoursesFController.refreshCoursesList();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course deleted successfully ");
                alert.showAndWait();
            }
            catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        editcoursebuttonF.setOnAction(event -> {
            try
            {
                i = course.getId_C();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Courses/updateCourseF.fxml"));
                Parent root = loader.load();
                UpdateCourseFController updateCourseFController = loader.getController();
                updateCourseFController.setInstance(course);

                // Get the updated image from UpdateCourseFController
                Image updatedImage = updateCourseFController.getUploadedImage();
                // Update the image in courseItemController
                updateImage(updatedImage);

                descCI.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        if (course.getUserid() == loggedInUserId) {
            // User is the creator, show update and delete buttons
            deletecoursebuttonF.setVisible(true);
            editcoursebuttonF.setVisible(true);
        } else {
            // User is not the creator, hide update and delete buttons
            deletecoursebuttonF.setVisible(false);
            editcoursebuttonF.setVisible(false);
        }
    }


    public void updateDetails()
    {

    }
    private void deletecourse()
    {

    }


    @FXML
    void gotolistworkshops(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
            descCI.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();  // Add this line to print the exception details
        }
    }

    private void resetFields() {
    }


}
