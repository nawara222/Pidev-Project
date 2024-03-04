package Controllers.Workshops;

import Models.Workshop;
import Services.CoursesandWorkshops.WorkshopService;
import Services.User.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class workshopItemController
{
    WorkshopService ws = new WorkshopService();
    public static int f;
    Workshop workshop;
    public static Workshop w;
    private ShowWorkshopsFController showWorkshopsFController;


    @FXML
    private AnchorPane apid;

    @FXML
    private Label descwi;

    @FXML
    private Label durationwI;

    @FXML
    private Label namewI;

    @FXML
    private Label resourceswi;
    @FXML
    private Button qr;
    @FXML
    private Button deleteworkshopbuttonF;

    @FXML
    private Button editworkshopbuttonF;
    public static Workshop o;
    public Workshop getSelectedWorkshop()
    {
        return workshop;
    }

    public void setParent(ShowWorkshopsFController showWorkshopsFController)
    {
        this.showWorkshopsFController=showWorkshopsFController;
    }


    public void setDataw(Workshop workshop,ShowWorkshopsFController showWorkshopsFController)
    {   int loggedInUserId= UserService.currentlyLoggedInUser.getUserID();
        namewI.setText(workshop.getNameW());
        descwi.setText(workshop.getDescription());
        durationwI.setText(String.valueOf(workshop.getDuration()));
        resourceswi.setText(workshop.getResources());

        if (workshop.getUserid() == loggedInUserId) {
            // User is the creator, show update and delete buttons
            deleteworkshopbuttonF.setVisible(true);
            editworkshopbuttonF.setVisible(true);
        } else {
            // User is not the creator, hide update and delete buttons
            deleteworkshopbuttonF.setVisible(false);
            editworkshopbuttonF.setVisible(false);
        }
        deleteworkshopbuttonF.setOnAction(event->{
            try
            {
                ws.deletew(workshop.getId_W());
                showWorkshopsFController.refreshWorkshopList();
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
        editworkshopbuttonF.setOnAction(event -> {
            try
            {
                w=getSelectedWorkshop();
                f = workshop.getId_C();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshops/updateWorkshopF.fxml"));
                Parent root = loader.load();
                UpdateWorkshopFController updateWorkshopFController = loader.getController();
                updateWorkshopFController.setInstancew(workshop);
                namewI.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                e.printStackTrace();  // Add this line to print the exception details
            }
        });
        qr.setOnAction(event ->{
            try
            {
                o=getSelectedWorkshop();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshops/detailworkshopitem.fxml"));
                Parent root = loader.load();
                DetailworkshopitemController detailworkshopitemController = loader.getController();
                detailworkshopitemController.setCurrentWorkshop(workshop);
                detailworkshopitemController.setInstancew(workshop);

                namewI.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
