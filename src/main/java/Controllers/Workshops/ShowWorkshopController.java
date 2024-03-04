package Controllers.Workshops;

import Controllers.Courses.ShowCoursesController;
import Models.Workshop;
import Services.CoursesandWorkshops.WorkshopService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowWorkshopController
{
    private final WorkshopService ws = new WorkshopService();
    private ObservableList<Workshop> ol;
    List<Workshop> workshopList;


    @FXML
    private VBox chosenFruitCard;

    @FXML
    private TableColumn<Workshop,String> desccol;

    @FXML
    private TextField descfwu;

    @FXML
    private TableColumn<Workshop,Float> durationcol;

    @FXML
    private TextField durationu;

    @FXML
    private ImageView fruitImg;

    @FXML
    private TableColumn<Workshop,Integer > namecolw;

    @FXML
    private TextField namewu;

    @FXML
    private TableColumn<Workshop,String> ressourcescol;

    @FXML
    private TextField ressourcesu;

    @FXML
    private TableView<Workshop> workshoptable;

    @FXML
    private TextField searchforname;

    @FXML
    private Label coursename;

    public List<Workshop>workshopwithid =new ArrayList<>();
    @FXML
    void initialize()
    {
        try
        {
            coursename.setText(String.valueOf(ShowCoursesController.cname));
            workshopList = ws.readw();
            ol = FXCollections.observableArrayList();
            for (Workshop workshop : workshopList)
            {
                if (workshop.getId_C() == ShowCoursesController.idc)
                {
                    workshopwithid.add(workshop);
                    ol.add(workshop);
                }
            }
            workshoptable.setItems(ol);
            namecolw.setCellValueFactory(new PropertyValueFactory<>("nameW"));
            ressourcescol.setCellValueFactory(new PropertyValueFactory<>("resources"));
            desccol.setCellValueFactory(new PropertyValueFactory<>("description"));
            durationcol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }


    @FXML
    void addworkshop(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/addWorkshop.fxml"));
            ressourcesu.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void click(MouseEvent event)
    {
        if (event.getButton() == MouseButton.PRIMARY)
        {
            Workshop selectedworkshop = workshoptable.getSelectionModel().getSelectedItem();
            if (selectedworkshop != null)
            {
                descfwu.setText(selectedworkshop.getDescription());
                ressourcesu.setText(selectedworkshop.getResources());
                namewu.setText(selectedworkshop.getNameW());
                durationu.setText(String.valueOf(selectedworkshop.getDuration()));
            }
        }
    }

    @FXML
    void confirmw(ActionEvent event)
    {
        try
        {
            Workshop selectedworkshop = workshoptable.getSelectionModel().getSelectedItem();
            if (selectedworkshop != null)
            {
                selectedworkshop.setNameW(namewu.getText());
                selectedworkshop.setResources(ressourcesu.getText());
                selectedworkshop.setDescription(descfwu.getText());
                selectedworkshop.setDuration(Float.parseFloat(durationu.getText()));

                ws.updatew(selectedworkshop);

                resetFields();
                workshoptable.refresh();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Workshop updated successfully");
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please select a workshop to update.");
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

    private void resetFields()
    {
        namewu.clear();
        descfwu.clear();
        ressourcesu.clear();
        durationu.clear();
    }

    @FXML
    void removew(ActionEvent event)
    {
        Workshop selectedworkshop = workshoptable.getSelectionModel().getSelectedItem();
        if (selectedworkshop != null)
        {
            try
            {
                int id_W = selectedworkshop.getId_W();
                ws.deletew(id_W);
                ol.remove(selectedworkshop);
                ShowCoursesController.nbwc--;


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            workshoptable.refresh();
            resetFields();
        }
    }

    @FXML
    void searchworkshopF(KeyEvent event) throws SQLException
    {
        String searchQuery = searchforname.getText();
        List<Workshop> matchingWorkshops = ws.searchWorkshop(searchQuery);
        if (searchQuery.isEmpty())
        {
            ol.clear();
            ol.addAll(workshopwithid);
        }
        else
        {
            ol.clear();
            ol.addAll(matchingWorkshops);
        }
        workshoptable.setItems(ol);
    }

    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCourses.fxml"));
            ressourcesu.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

}
