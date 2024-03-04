package Controllers.Workshops;

import Models.Workshop;
import Services.CoursesandWorkshops.WorkshopService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateWorkshopFController {

    private WorkshopService cs = new WorkshopService();
    private  workshopItemController workshopItemController;
    private static UpdateWorkshopFController instancew;
    private Workshop selectedWorkshopID;


    @FXML
    private TextField descriptionwFU;

    @FXML
    private TextField durationwFU;

    @FXML
    private TextField namewFU;

    @FXML
    private TextField resourceswFU;

    @FXML
    private TextField searchfw;

    @FXML
    void initialize() throws SQLException
    {
        selectedWorkshopID=workshopItemController.w;
        if (selectedWorkshopID != null)
        {
            namewFU.setText(selectedWorkshopID.getNameW());
            descriptionwFU.setText(selectedWorkshopID.getDescription());
            resourceswFU.setText(selectedWorkshopID.getResources());
            durationwFU.setText(String.valueOf(selectedWorkshopID.getDuration()));
        }
        else
        {
            System.out.println("courseItemController is not initialized.");
        }
    }

    public void setInstancew(Workshop workshop) {
        this.selectedWorkshopID = workshop;
        namewFU.setText(selectedWorkshopID.getNameW());
        descriptionwFU.setText(selectedWorkshopID.getDescription());
        resourceswFU.setText(selectedWorkshopID.getResources());
        durationwFU.setText(String.valueOf(selectedWorkshopID.getDuration()));
    }

    @FXML
    void returncF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
            resourceswFU.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void updateworkshopF(ActionEvent event)
    {
        try
        {
            if (selectedWorkshopID != null)
            {
                selectedWorkshopID.setNameW(namewFU.getText());
                selectedWorkshopID.setResources(resourceswFU.getText());
                selectedWorkshopID.setDuration(Float.parseFloat(durationwFU.getText()));
                selectedWorkshopID.setDescription(descriptionwFU.getText());

                cs.updatew(selectedWorkshopID);

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
    public static void setInstancew(UpdateWorkshopFController instance, Workshop selectedWorkshop)
    {
        UpdateWorkshopFController.instancew = instance;
        instance.setSelectedWorkshop(selectedWorkshop);
    }

    private void setSelectedWorkshop(Workshop selectedWorkshop)
    {
        this.selectedWorkshopID = selectedWorkshop;
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
    void searchbuttonw(ActionEvent event)
    {

    }

    @FXML
    void viewcartbuttonw(ActionEvent event) {

    }

}
