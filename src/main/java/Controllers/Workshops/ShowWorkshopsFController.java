package Controllers.Workshops;

//import com.google.protobuf.AbstractProtobufList;

import Controllers.Courses.courseItemController;
import Models.Workshop;
import Services.CoursesandWorkshops.WorkshopService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowWorkshopsFController
{
    private WorkshopService ws = new WorkshopService();


    @FXML
    private TextField searchf;

    @FXML
    private GridPane workshopsGrid;

    @FXML
    private ScrollPane workshopsItems;
    public List<Workshop> listw = new ArrayList<>();

    @FXML
    void initialize()
    {
        refreshWorkshopList();

        workshopsGrid.setHgap(-20); // Horizontal gap between items
        workshopsGrid.setVgap(-20); // Vertical gap between items

        workshopsItems.setContent(workshopsGrid);
        workshopsItems.setFitToWidth(true);

        // Set minimum height for each row
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Set the desired height
        workshopsGrid.getRowConstraints().add(rowConstraints);
        try
        {
            List<Workshop> workshopList = ws.readw();
            loadworkshopItems(workshopList);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    void resetWorkshopList()
    {
        workshopsGrid.getChildren().clear();

        try
        {
            List<Workshop> listw = ws.readw();
            loadworkshopItems(listw);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }



    @FXML
    void searchworkshopF(KeyEvent event) throws IOException {
        String searchQuery = searchf.getText();

        if (searchQuery.isEmpty()) {
            resetWorkshopList();
        } else {
            List<Workshop> matchingWorkshops = ws.searchWorkshop(searchQuery);
            loadworkshopItems(matchingWorkshops);
        }
    }

    public void refreshWorkshopList() {
        try
        {
            List<Workshop> workshoplist = ws.readw();
            loadworkshopItems(workshoplist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addworkshopF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/addworkshopF.fxml"));
            workshopsItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }


    private void loadworkshopItems(List<Workshop> workshops)
    {
        workshopsGrid.getChildren().clear();
        int rowIndex = 0;
        int colIndex = 0;
        for (Workshop workshop : workshops)
        {
            if (workshop.getId_C() == courseItemController.idc)
            {
                try
                {
                    listw.add(workshop);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshops/workshopItem.fxml"));
                    Node workshopnode = loader.load();
                    workshopItemController itemController = loader.getController();
                    itemController.setDataw(workshop, this);

                    workshopsGrid.add(workshopnode, colIndex, rowIndex);
                    colIndex++;
                    if (colIndex == 4)
                    {
                        colIndex = 0;
                        rowIndex++;
                    }
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
                workshopsItems.setContent(workshopsGrid);
                workshopsItems.setFitToWidth(true);
            }
        }
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
    void effect(MouseEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event)
    {

    }

    @FXML
    void gototcourses(MouseEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCoursesF.fxml"));
            workshopsItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void searchbuttonw(ActionEvent event)
    {


    }

    @FXML
    void viewcartbuttonw(ActionEvent event) {

    }

}
