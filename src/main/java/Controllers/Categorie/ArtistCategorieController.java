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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Services.Art.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ArtistCategorieController {
    CategoryServices categoryServices = new CategoryServices();
    private ObservableList<category> ol;
    int idC = 0 ;
    @FXML
    private TextField nom;
    @FXML
    private DatePicker datee;

    @FXML
    private TableColumn<?, ?> namee;
    @FXML
    private TableColumn<?, ?> dateT;
    @FXML
    private TextField categorynew;

    @FXML
    private ImageView add;

    @FXML
    private Button afficher;

    @FXML
    private Button ajouter;

    @FXML
    private VBox chosenFruitCard;




    @FXML
    private ImageView fruitImg;

    @FXML
    private HBox nameA;




    @FXML
    private TableView<category> showtablec;

    @FXML
    private Button update;


    @FXML
    void adding(ActionEvent event) {
        try {
            int Userid= UserService.currentlyLoggedInUser.getUserID();
            categoryServices.addC(new category(categorynew.getText(),String.valueOf(datee.getValue()),Userid));
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
    void displayc(ActionEvent event) {
        try {
            List<category> categoryList = categoryServices.displayC();
            ol = FXCollections.observableList(categoryList);//// Creating an observable list from the categoryList
            showtablec.setItems(ol);// Setting the observable list to the TableView showtablec
            namee.setCellValueFactory(new PropertyValueFactory<>("name"));
            dateT.setCellValueFactory(new PropertyValueFactory<>("date"));


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }

    @FXML
    void updaten(ActionEvent event) {
        category category = showtablec.getSelectionModel().getSelectedItem();

        // Vérifier si un élément a été effectivement sélectionné
        if (category != null) {
            // Mettre à jour les propriétés d'art avec les valeurs des champs de texte
            category.setName(categorynew.getText());
            category.setDate(String.valueOf(datee.getValue()));


            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                categoryServices.modifyC(category,idC); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
                refreshTableView();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }

            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            //addC().setDisable(true);
        }

    }
    @FXML
    void deleteC(ActionEvent event) {
        category selectedcategory= showtablec.getSelectionModel().getSelectedItem();
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
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            refreshTableView();
            resetFields();
        }
    }
    private void refreshTableView() {
        try {
            ObservableList<category> observableList = FXCollections.observableList(categoryServices.displayC());
            showtablec.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    // Méthode pour réinitialiser les champs de texte
    private void resetFields() {
        categorynew.clear();
        datee.getEditor().clear();

    }
    @FXML
    void clickeed (MouseEvent event) {
        if (event.getButton()== MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            category category = showtablec.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (category != null) {
                // Récupérer les données du Panier et les afficher dans les champs de texte appropriés
                idC = category.getId_category();
                categorynew.setText(category.getName());


            }
        }

    }



    @FXML
    void goback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/ManageArtist.fxml"));
            datee.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }


}

