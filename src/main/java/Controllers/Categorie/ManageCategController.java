package Controllers.Categorie;
import Models.category;
import Services.User.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import Services.Art.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageCategController {
    CategoryServices categoryServices = new CategoryServices();
    private ObservableList<category> ol;
    int idC = 0 ;


    @FXML
    private Button clear;

    @FXML
    private Button addt;

    @FXML
    private TableColumn<?, ?> dates;

    @FXML
    private TextField datet;

    @FXML
    private Button deelets;

    @FXML
    private Button modifys;

    @FXML
    private ComboBox<String> typeCat;


    @FXML
    private TableView<category> showCategory;

    @FXML
    private TableColumn<?, ?> types;

    @FXML
    private TextField names;

    public void initialize() {
        // Initialize the ObservableList and get the items list from the ComboBox
        ObservableList<String> typeCategory = FXCollections.observableArrayList("Sculpture", "Paintings", "Photography", "Drawing");
        typeCat.setItems(typeCategory);

    }
    @FXML
    void displayC(ActionEvent event) {
        try {
            List<category> categoryList = categoryServices.displayC();
            ol = FXCollections.observableList(categoryList);
            showCategory.setItems(ol);
            types.setCellValueFactory(new PropertyValueFactory<>("name"));
            dates.setCellValueFactory(new PropertyValueFactory<>("date"));


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void addC(ActionEvent event) {
        int Userid= UserService.currentlyLoggedInUser.getUserID();
        try {
            categoryServices.addC(new category(typeCat.getValue(),datet.getText(),Userid));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success !");
            alert.setContentText("added!!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);

        }

    }

    @FXML
    void deleteC(ActionEvent event)
    {
        category selectedcategory= showCategory.getSelectionModel().getSelectedItem();
        if (selectedcategory != null)
        {
            try {
                int id_category = selectedcategory.getId_category();
                categoryServices.deleteC(id_category);
                ol.remove(selectedcategory);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Art deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Cannot delete a Category !");
                alert.showAndWait();
            }
            refreshTableView();
            resetFields();
        }
    }

    private void refreshTableView() {
        try {
            ObservableList<category> observableList = FXCollections.observableList(categoryServices.displayC());
            showCategory.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    // Méthode pour réinitialiser les champs de texte
    private void resetFields() {
        typeCat.getItems().clear();
        datet.clear();

    }

    @FXML
    void clear(ActionEvent event) {
        resetFields();
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton()== MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            category category = showCategory.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (category != null) {
                // Récupérer les données du Panier et les afficher dans les champs de texte appropriés
                idC = category.getId_category();
                typeCat.setValue(category.getName());
                datet.setText(category.getDate());

            }
        }
    }

    @FXML
    void modifyC(ActionEvent event) {
        // Get the selected category from the TableView
        category category = showCategory.getSelectionModel().getSelectedItem();

        // Check if a category has been selected
        if (category != null) {
            initialize();
            // Update the name of the category with the value selected in the ComboBox
            category.setName(typeCat.getValue());

            // Update the date of the category with the value entered in the TextField
            category.setDate(datet.getText());

            // Call the method to update the category in the database
            try {
                categoryServices.modifyC(category, idC); // Assuming you have an update method in your service
                // Refresh the TableView after modification
                initialize();
                refreshTableView();
            } catch (SQLException e) {
                System.out.println("Cannot update a Category !");
                // Handle the error as needed
            }

            // Reset the fields and disable the add button
            resetFields();
            addt.setDisable(true);

        }
        initialize();
    }





    @FXML
    void goArt(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/displayArt.fxml"));
            datet.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

}
