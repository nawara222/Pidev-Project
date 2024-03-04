package Controllers.Art;

import Models.art;
import Models.category;
import Services.User.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Services.Art.ArtServices;
import Services.Art.CategoryServices;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;



public class ManageArtistController implements Initializable {
    private final ArtServices artps = new ArtServices();

    private final CategoryServices categoryServices = new CategoryServices();
    private ObservableList<art> oll;
    int id = 0;



    @FXML
    private TextField idT;
    @FXML
    private TextField TypeA;

    @FXML
    private Button addIMAGES;

    @FXML
    private ImageView addImage;


    @FXML
    private TextField path_video;

    public TextField getPath_video()
    {
        return path_video;
    }


    @FXML
    private Pane backImage;


    @FXML
    private ImageView imageART;
    @FXML
    private ComboBox<category> combo_category;

    @FXML
    private TextField materialsA;
    @FXML
    private TextField heightA;
    @FXML
    private TextField widthA;

    @FXML
    private TextField search_bar;

    @FXML
    private TextField cityA;
    @FXML
    private TextField descA;
    @FXML
    private TextField priceV;
    @FXML
    private TableColumn<art, String> titleV;
    @FXML
    private TableColumn<art, String> materialsv;

    @FXML
    private TableColumn<art, String> col_video;

    @FXML
    private TableColumn<art, Double> heightv;

    @FXML
    private TableColumn<art, Double> widtht;
    @FXML
    private TableColumn<art, String> typet;


    @FXML
    private TableColumn<art, String> col_category;

    @FXML
    private TableColumn<art, String> cityt;
    @FXML
    private TableColumn<art, String> descriptiont;
    @FXML
    private TableColumn<art, Float> priceT;

    @FXML
    private Button show;
    @FXML
    private Button addart;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Button deleteart;

    @FXML
    private ImageView fruitImg;

    @FXML
    private TableView<art> showTbleArtis;

    @FXML
    private HBox update;

    @FXML
    private Button updateA;

    @FXML
    private BorderPane sheet;

    @FXML
    private AnchorPane modechanger;

    @FXML
    private TableColumn<art, String > path;

    @FXML
    private TextField pathI;


    @FXML
    void addArt(ActionEvent event) {
        int Userid= UserService.currentlyLoggedInUser.getUserID();
        try {
            category SelectedCategory = combo_category.getValue();
            if (idT.getText().isEmpty() || materialsA.getText().isEmpty() || heightA.getText().isEmpty() || widthA.getText().isEmpty() ||
                    TypeA.getText().isEmpty() || cityA.getText().isEmpty() || descA.getText().isEmpty() || priceV.getText().isEmpty() || priceV.getText().isEmpty() || path_video.getText().isEmpty() ||
                    SelectedCategory == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return;}
            artps.add(new art(idT.getText(), materialsA.getText(), Double.parseDouble(heightA.getText()), Double.parseDouble(widthA.getText()), TypeA.getText(), cityA.getText(), descA.getText(),Float.parseFloat(priceV.getText()),SelectedCategory.getId_category(),pathI.getText(),path_video.getText(),Userid));

            //show(event);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Art added successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        resetFields();
    }


    @FXML
    void show(ActionEvent event) {
        try {
            List<art> artList =artps.display();
            oll = FXCollections.observableList(artList);
            showTbleArtis.setItems(oll);
            titleV.setCellValueFactory(new PropertyValueFactory<>("title"));
            materialsv.setCellValueFactory(new PropertyValueFactory<>("materials"));
            heightv.setCellValueFactory(new PropertyValueFactory<>("width"));
            widtht.setCellValueFactory(new PropertyValueFactory<>("height"));
            typet.setCellValueFactory(new PropertyValueFactory<>("type"));
            cityt.setCellValueFactory(new PropertyValueFactory<>("city"));
            descriptiont.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceT.setCellValueFactory(new PropertyValueFactory<>("price"));
            col_category.setCellValueFactory(cellData -> {
                int categoryId = cellData.getValue().getId_category();
                String categoryName = ""; // Default value
                try {
                    categoryName = categoryServices.getCategoryName(categoryId);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error fetching category name: " + e.getMessage());
                    alert.showAndWait();
                }
                // Convert the string category name to ObservableValue<Integer>
                return new SimpleStringProperty(categoryName);
            });
            path.setCellValueFactory(new PropertyValueFactory<>("path_image"));
            col_video.setCellValueFactory(new PropertyValueFactory<>("video"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void deleteArt(ActionEvent event) {
        art selectedart= showTbleArtis.getSelectionModel().getSelectedItem();
        if (selectedart != null)
        {
            try {
                int id_art = selectedart.getId_art();
                artps.delete(id_art);
                oll.remove(selectedart);
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
            // Create an instance of ArtServices
            ArtServices artServices = new ArtServices();
            // Call the display method on the instance
            ObservableList<art> observableList = FXCollections.observableList(artServices.display());
            // Set the items in the TableView
            showTbleArtis.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private void resetFields() {
        idT.clear();
        materialsA.clear();
        materialsA.clear();
        heightA.clear();
        widthA.clear();
        TypeA.clear();
        cityA.clear();
        descA.clear();
        priceV.clear();
       // combo_category.getItems().clear();
        pathI.clear();
        path_video.clear();
        imageART.setImage(null);
    }

    @FXML
    void clear(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/FronClient.fxml"));
            materialsA.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

    @FXML
    void update(ActionEvent event) {
        // Récupérer l'objet Conseil sélectionné dans le TableView
        art art = showTbleArtis.getSelectionModel().getSelectedItem();

        // Vérifier si un élément a été effectivement sélectionné
        if (art != null) {
            // Mettre à jour les propriétés du Conseil avec les valeurs des champs de texte
            art.setTitle(idT.getText());
            art.setMaterials(materialsA.getText());
            art.setHeight(Double.parseDouble(heightA.getText()));
            art.setWidth(Double.parseDouble(widthA.getText()));
            art.setType(TypeA.getText());
            art.setCity(cityA.getText());
            art.setDescription(descA.getText());
            art.setPrice(Float.parseFloat(priceV.getText()));
            art.setId_category((combo_category.getValue()).getId_category());
            String newImagePath = pathI.getText();
            art.setPath_image(newImagePath);
            art.setVideo(path_video.getText());


            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                artps.modify(art,id);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Art Updated successfully ");
                alert.showAndWait();
                refreshTableView();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }
            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            refreshTableView();
        }
        }
    @FXML
    void clicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            art art = showTbleArtis.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (art != null) {
                // Récupérer les données d'arts et les afficher dans les champs de texte appropriés
                id = art.getId_art();
                idT.setText(art.getTitle());
                materialsA.setText(art.getMaterials());
                heightA.setText(String.valueOf(art.getHeight()));
                widthA.setText(String.valueOf(art.getWidth()));
                TypeA.setText(art.getType());
                cityA.setText(art.getCity());
                descA.setText(art.getDescription());
                priceV.setText(String.valueOf(art.getPrice()));
                path_video.setText(art.getVideo());
                int categoryId = art.getId_category();

                // Find the category object that corresponds to the categoryId
                category selectedCategory = null; // Initialize to null
                for (category cat : combo_category.getItems()) {
                    if (cat.getId_category() == categoryId) {
                        selectedCategory = cat;
                        break;
                    }
                }

                // Set the selected category in the combo box
                combo_category.setValue(selectedCategory);
                pathI.setText(art.getPath_image());

// Load the image from the specified path
                String imagePath = art.getPath_image();
                File imageFile = new File(imagePath);
                Image image = new Image(imageFile.toURI().toString());

// Set the image to the ImageView
                imageART.setImage(image);
            }
            }
        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<category> allCategories = null ;
        try {
            allCategories = categoryServices.displayC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        combo_category.setItems(FXCollections.observableList(allCategories));
    }

    @FXML
    void go_cat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Categorie/categorieM.fxml"));
            materialsA.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

    @FXML
    void searchArt(KeyEvent event) {
        String searchQuery = search_bar.getText(); // Replace searchTextField with the actual name of your TextField

        // Call the searchProducts method in your ConseilService
        List<art> matchingConseils = artps.searchArt(searchQuery);

        // Update the UI with the matching Conseils
        oll.clear(); // Clear the current data
        oll.addAll(matchingConseils); // Add the matching Conseils to the observable list
        showTbleArtis.setItems(oll); // Set the items in the TableView
    }

    @FXML
    void AddImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")); // Add more supported image formats if needed
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            pathI.setText(selectedFile.getPath());
            String destinationFolder = "C:\\xampp\\htdocs\\ImageArt"; // Change the destination folder path
            File destinationFile = new File(destinationFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Load the selected image

            Image image = new Image(destinationFile.toURI().toString());
            // Set the image to the ImageView
            imageART.setImage(image);
        } else {
            // Handle the case where no file was selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No image file selected");
            alert.showAndWait();
        }
    }
    @FXML
    void go_statistique(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/Statistique.fxml"));
            materialsA.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }


    @FXML
    void go_Sort(ActionEvent event) {
        // Retrieve the items from the TableView
        ObservableList<art> items = showTbleArtis.getItems();

        // Sort the items by title using Comparator
        items.sort(Comparator.comparing(art::getPrice));

        // Update the TableView with the sorted items
        showTbleArtis.setItems(items);
    }

    @FXML
    void play_video(ActionEvent event) {
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Art/uploadVideo.fxml"));
                Parent playVideoRoot = fxmlLoader.load();

                UploadVideoController video = fxmlLoader.getController();
                video.setManageArtistController(this);
                video.Lire_video(event);

                Stage playVideoStage = new Stage();
                playVideoStage.setScene(new Scene(playVideoRoot));
                playVideoStage.setTitle("Video");
                playVideoStage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());

            }
        }
    }
    @FXML
    void uploadVideo(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")); // Corrected extensions
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            path_video.setText(selectedFile.getPath());
            String destinationFolder = "C:\\xampp\\htdocs\\image"; // Set the correct path
            File destinationFile = new File(destinationFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            // Handle the case where no file was selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No video file selected");
            alert.showAndWait();
        }
    }

}






