package Controllers.Workshops;

import Controllers.Courses.ShowCoursesController;
import Controllers.Courses.courseItemController;
import Models.Workshop;
import Services.CoursesandWorkshops.WorkshopService;
import Services.User.UserService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.captcha.Captcha;

import java.io.IOException;
import java.sql.SQLException;

public class AddworkshopFController
{
    private final WorkshopService ws = new WorkshopService();

    @FXML
    private TextField captchaTF;

    @FXML
    private Label captchaincorrect;

    @FXML
    private Label descriptionempty;

    @FXML
    private Label durationempty;

    @FXML
    private Label nameempty;

    @FXML
    private Label resourcesempty;

    @FXML
    private TextField descriptionwF;

    @FXML
    private TextField durationwF;

    @FXML
    private TextField namewF;

    @FXML
    private TextField resourceswF;

    @FXML
    private TextField searchfw;

    @FXML
    private ImageView cap;
    private Captcha captcha;
    private Boolean isValid = true;


    @FXML
    public void initialize()
    {
        setvisibility();
        captcha = setCaptcha();
    }

    public void setvisibility()
    {
        nameempty.setVisible(false);
        descriptionempty.setVisible(false);
        durationempty.setVisible(false);
        resourcesempty.setVisible(false);
        captchaincorrect.setVisible(false);
        //imageId.setVisible(false);
        //imageView.setVisible(false);
    }

    public Captcha setCaptcha()
    {
        Captcha captchaV = new Captcha.Builder(250, 150)
                .addText()
                .addBackground()
                .addNoise()
                .addBorder()
                .build();

        System.out.println(captchaV.getImage());
        Image image = SwingFXUtils.toFXImage(captchaV.getImage(), null);
        cap.setImage(image);
        return captchaV;
    }


    @FXML
    void addworkshopF(ActionEvent event)
    {
        try
        {
            setvisibility();
            String name = namewF.getText().trim();
            String resources = resourceswF.getText().trim();
            float duration = durationwF.getText().isEmpty() ? 0.0f : Float.parseFloat(durationwF.getText().trim());
            String desc = descriptionwF.getText().trim();
            String cap = captchaTF.getText().trim();
            int Userid= UserService.currentlyLoggedInUser.getUserID();
           // String pathimage = imageId.getText().trim();

           // imageView.setVisible(true);
            if (name.isEmpty())
            {
                nameempty.setVisible(true);
                isValid = false;
            }
            if (resources.isEmpty())
            {
                resourcesempty.setVisible(true);
                isValid = false;
            }
            if (duration==0.0f)
            {
                durationempty.setVisible(true);
                isValid = false;
            }
            if (desc.isEmpty())
            {
                descriptionempty.setVisible(true);
                isValid = false;
            }if (cap.isEmpty())
            {
                captchaincorrect.setVisible(true);
                isValid = false;
            }

            if(!isValid)
            {
                return;
            }
            if (!captcha.isCorrect(captchaTF.getText()))
            {
                captcha = setCaptcha();
                captchaTF.setText("");
                namewF.setText("");
                resourceswF.setText("");
                descriptionwF.setText("");
                durationwF.setText("");
               // imageId.setText("");
               // imageView.setVisible(false);
                return;
            }
            //if(captcha.isCorrect(captchaTF.getText())&& isValid)
            //{
                ws.createw(new Workshop(namewF.getText(), resourceswF.getText(), descriptionwF.getText(), Float.parseFloat(durationwF.getText()), courseItemController.idc,Userid));
                ShowCoursesController.nbwc++;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("success");
                alert.setContentText("Workshop added successfully ");
                alert.showAndWait();
                namewF.setText("");
                resourceswF.setText("");
                descriptionwF.setText("");
                durationwF.setText("");
            //}
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
            namewF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void aboutusbutton(ActionEvent event)
    {

    }

    @FXML
    void artworksbutton(ActionEvent event)
    {

    }

    @FXML
    void auctionbutton(ActionEvent event)
    {

    }

    @FXML
    void eventbutton(ActionEvent event)
    {

    }

    @FXML
    void gotofOb(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event) {

    }

    @FXML
    void returncF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
            namewF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void searchbuttonw(ActionEvent event) {

    }

    @FXML
    void viewcartbuttonw(ActionEvent event) {

    }

}
