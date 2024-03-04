package Controllers.Art;

import Controllers.Baskets.AddQuantityBasketController;
import Models.art;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Services.Art.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemsArtController {
    private List<art> favoriteList = new ArrayList<>();
    private AddQuantityBasketController addQuantityBasketController;
    private FronClientController fronClientController;
    private static int idA;

    private art art;


    @FXML
    private Label CITY;

    @FXML
    private ImageView image;
    @FXML
    private Label CATEGORY;
    public static art selectedart  ;

    @FXML
    private Button showmore;
    @FXML
    private Label PRICE;

    @FXML
    private Label TITLE;
    private art currentArt;
    private ShowMoreController showMoreController;

    public void setData(art art) {
        this.art = art;
        CategoryServices categoryServices = new CategoryServices();
        TITLE.setText(art.getTitle());
        PRICE.setText(String.valueOf(art.getPrice()));
        int categoryId = art.getId_category();
        String categoryName = ""; // Default value
        try {
            categoryName = categoryServices.getCategoryName(categoryId);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error fetching category name: " + e.getMessage());
            alert.showAndWait();
        }
        CATEGORY.setText(categoryName);

        // Display the image if the path is not null
        if (art.getPath_image() != null) {
            Image images = new Image("file:" + art.getPath_image());
            image.setImage(images);
        }


    }

    @FXML
    void showmore(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Art/showMore.fxml"));
            Parent root = loader.load();

            // Get the controller
            ShowMoreController showMoreController = loader.getController();

            // Pass the art data to the controller
            showMoreController.setDataa(art);

            // Set the scene
            CATEGORY.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }


    public static int getIdA(art art) {
        return art.getId_art();
    }


    @FXML
    void favoriteadd(ActionEvent event) {
        if (art != null) {
            // Add the art object to the favorite list
            favoriteList.add(art);

            // Show a notification that the product has been added to favorites
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product added to favorites!");
            alert.showAndWait();
        } else {
            // Handle case when no art object is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No art object selected to add to favorites!");
            alert.showAndWait();
        }
    }
    @FXML
    void additem(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Baskets/Basket.fxml"));
            Parent root = loader.load();

            // Get the controller
            AddQuantityBasketController addQuantityBasketController1 = loader.getController();

            // Pass the art data to the controller
            addQuantityBasketController1.setnewdata(art);

            // Set the scene
            CATEGORY.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }


}





