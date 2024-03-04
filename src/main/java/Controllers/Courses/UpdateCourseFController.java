package Controllers.Courses;

import Models.Courses;
import Services.CoursesandWorkshops.CoursesService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UpdateCourseFController {
    private Courses selectedCourse;
    private CoursesService cs = new CoursesService();
    private courseItemController courseItemController;
    public static String imagePathu;
    private Image uploadedImage;

    @FXML
    private TextField descriptioncFU;

    @FXML
    private TextField namecFU;

    @FXML
    private TextField pricecFU;

    @FXML
    private TextField searchfw;

    @FXML
    private TextField typecFU;

    @FXML
    private Label imageid;

    @FXML
    private ImageView imageview;

    @FXML
    void initialize() throws SQLException
    {
        if (selectedCourse != null)
        {
            namecFU.setText(selectedCourse.getNameC());
            descriptioncFU.setText(selectedCourse.getDescriptionC());
            typecFU.setText(selectedCourse.getType());
            pricecFU.setText(String.valueOf(selectedCourse.getPriceC()));
        }
        else
        {
            System.out.println("courseItemController is not initialized.");
        }
        uploadbutton.setOnAction(event -> handleUploadButton());
    }
    @FXML
    private Button uploadbutton;

    private void handleUploadButton()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\xampp\\htdocs\\image"));

        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadbutton.getScene().getWindow());
        if (selectedFile != null)
        {
            imagePathu = selectedFile.toURI().toString();
            imageid.setText(imagePathu);

            uploadedImage = new Image(imagePathu);
            imageview.setImage(uploadedImage);
            imageview.setVisible(true);
        }
    }

    @FXML
    void updatecourseF(ActionEvent event) throws SQLException
    {
        try
        {
            if (selectedCourse != null)
            {
                selectedCourse.setNameC(namecFU.getText());
                selectedCourse.setType(typecFU.getText());
                selectedCourse.setPriceC(Float.parseFloat(pricecFU.getText()));
                selectedCourse.setDescriptionC(descriptioncFU.getText());
                selectedCourse.setImage_path(imageid.getText());
                cs.update(selectedCourse);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course updated successfully ");
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

    public void setInstance(Courses selectedCourseID)
    {
        this.selectedCourse = selectedCourseID;
        namecFU.setText(selectedCourseID.getNameC());
        descriptioncFU.setText(selectedCourseID.getDescriptionC());
        typecFU.setText(selectedCourseID.getType());
        pricecFU.setText(String.valueOf(selectedCourseID.getPriceC()));
        imageid.setText(selectedCourseID.getImage_path());
    }

    public Image getUploadedImage() {
        return uploadedImage;
    }

    private void setSelectedCourse(Courses selectedCourse)
    {
        this.selectedCourse = selectedCourse;
    }

    @FXML
    void aboutusbutton(ActionEvent event) {

    }

    @FXML
    void artworksbutton(ActionEvent event) {

    }

    @FXML
    void auctionbutton(ActionEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event) {

    }

    @FXML
    void returncF(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCoursesF.fxml"));
            pricecFU.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }

    }

    @FXML
    void searchbuttonw(ActionEvent event) {

    }


    @FXML
    void viewcartbuttonw(ActionEvent event) {

    }

}