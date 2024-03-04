package Controllers.Art;

import Models.art;
import Models.category;
import Services.Art.ArtServices;
import Services.Art.CategoryServices;
import Services.User.UserService;
import Test.MainFX;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


public class DisplayArtController {
    private final ArtServices ps = new ArtServices();
    private ObservableList<art> ol;
    private final CategoryServices categoryServices = new CategoryServices();


    int id = 0;
    @FXML
    private TextField searchforTitle;
    @FXML
    private TableView<art> ArtTableView;
    @FXML
    private TextField pathart;

    @FXML
    private TableColumn<art, String> titlec;

    @FXML
    private TableColumn<art, String> materialsc;

    @FXML
    private TableColumn<art, Double> widthc;

    @FXML
    private TableColumn<art, Double> heightc;

    @FXML
    private TableColumn<art, String> cityc;

    @FXML
    private TableColumn<art, String> typec;

    @FXML
    private TableColumn<art, String> descriptionc;
    @FXML
    private ComboBox<category> categoriChoix;
    @FXML
    private TableColumn<art, String> path;
    @FXML
    private TextField paths;
    @FXML
    private TableColumn<?, ?> path1;

    @FXML
    private TextField pathVideo;

    @FXML
    private TableColumn<art, Float> priceT;
    @FXML
    private TextField titleu;
    @FXML
    private TextField matiralsu;
    @FXML
    private TextField widthu;

    @FXML
    private TextField heightu;


    @FXML
    private TextField typeu;
    @FXML
    private TextField cityu;

    @FXML
    private TextField descriptionu;
    @FXML
    private TextField prices;
    @FXML
    private TableColumn<art, String> categoryT;
    @FXML
    private Button addIMAGES;

    @FXML
    private Button Sort;

    @FXML
    private ImageView addImages;

    @FXML
    private Pane backImages;
    @FXML
    private ImageView imageARTS;


    @FXML
    private Button deleteC;


    @FXML
    private Button updateC;

    @FXML
    void initialize() {
        try {
            List<art> artList = ps.displayArtist();
            ol = FXCollections.observableList(artList);
            ArtTableView.setItems(ol);
            titlec.setCellValueFactory(new PropertyValueFactory<>("title"));
            materialsc.setCellValueFactory(new PropertyValueFactory<>("materials"));
            widthc.setCellValueFactory(new PropertyValueFactory<>("width"));
            heightc.setCellValueFactory(new PropertyValueFactory<>("height"));
            typec.setCellValueFactory(new PropertyValueFactory<>("type"));
            cityc.setCellValueFactory(new PropertyValueFactory<>("city"));
            descriptionc.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceT.setCellValueFactory(new PropertyValueFactory<>("price"));
            categoryT.setCellValueFactory(cellData -> {
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
                // Set the selected category in the combo box
                return new SimpleStringProperty(categoryName);
            });
            path.setCellValueFactory(new PropertyValueFactory<>("path_image"));
            path1.setCellValueFactory(new PropertyValueFactory<>("video"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        List<category> allCategories = null;
        try {
            allCategories = categoryServices.displayC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        categoriChoix.setItems(FXCollections.observableList(allCategories));

    }

    @FXML
    void delete(ActionEvent event) {
        art selectedart = ArtTableView.getSelectionModel().getSelectedItem();
        if (selectedart != null) {
            try {
                int id_art = selectedart.getId_art();
                ps.delete(id_art);
                ol.remove(selectedart);
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


    @FXML
    void rereturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/addart.fxml"));
            titleu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }

    }

    @FXML
    void update(ActionEvent event) {
        // Récupérer l'objet Conseil sélectionné dans le TableView
        art art = ArtTableView.getSelectionModel().getSelectedItem();
        // Vérifier si un élément a été effectivement sélectionné
        if (art != null) {
            // Mettre à jour les propriétés du Conseil avec les valeurs des champs de texte
            art.setTitle(titleu.getText());
            art.setMaterials(matiralsu.getText());
            art.setHeight(Double.parseDouble(heightu.getText()));
            art.setWidth(Double.parseDouble(widthu.getText()));
            art.setType(typeu.getText());
            art.setCity(cityu.getText());
            art.setDescription(descriptionu.getText());
            art.setWidth(Float.parseFloat(prices.getText()));
            art.setId_category((categoriChoix.getValue()).getId_category());
            String newImagePath = paths.getText();
            art.setPath_image(newImagePath);
            art.setVideo(pathVideo.getText());
            //System.out.println("New Image Path: " + newImagePath);
            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                ps.modify(art, id); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
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
            initialize();
            //btn_ajouter.setDisable(false);
        }
    }

    // Méthode pour rafraîchir la TableView après la modification
    private void refreshTableView() {
        try {
            ObservableList<art> observableList = FXCollections.observableList(ps.display());
            ArtTableView.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    // Méthode pour réinitialiser les champs de texte
    private void resetFields() {
        titleu.clear();
        matiralsu.clear();
        heightu.clear();
        widthu.clear();
        typeu.clear();
        cityu.clear();
        descriptionu.clear();
        prices.clear();
        categoriChoix.getItems().clear();
        paths.clear();
        imageARTS.setImage(null);
        pathVideo.clear();


    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            art art = ArtTableView.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (art != null) {
                // Récupérer les données du Panier et les afficher dans les champs de texte appropriés
                id = art.getId_art();
                titleu.setText(art.getTitle());
                matiralsu.setText(art.getMaterials());
                heightu.setText(String.valueOf(art.getHeight()));
                widthu.setText(String.valueOf(art.getWidth()));
                typeu.setText(art.getType());
                cityu.setText(art.getCity());
                descriptionu.setText(art.getDescription());
                prices.setText(String.valueOf(art.getPrice()));
                int categoryId = art.getId_category();
                // Find the category object that corresponds to the categoryId
                category selectedCategory = null; // Initialize to null
                for (category cat : categoriChoix.getItems()) {
                    if (cat.getId_category() == categoryId) {
                        selectedCategory = cat;
                        break;
                    }
                }
                // Set the selected category in the combo box
                categoriChoix.setValue(selectedCategory);
                paths.setText(art.getPath_image());
// Load the image from the specified path
                String imagePath = art.getPath_image();
                File imageFile = new File(imagePath);
                Image image = new Image(imageFile.toURI().toString());

// Set the image to the ImageView
                imageARTS.setImage(image);
                pathVideo.setText(art.getVideo());
            }
        }
    }

    @FXML
    void go_cat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Categorie/categorieM.fxml"));
            cityu.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }


    @FXML
    void searchForArt(KeyEvent event) {
        String searchQuery = searchforTitle.getText(); // Replace searchTextField with the actual name of your TextField

        // Call the searchProducts method in your ConseilService
        List<art> matchingConseils = ps.searchArt(searchQuery);

        // Update the UI with the matching Conseils
        ol.clear(); // Clear the current data
        ol.addAll(matchingConseils); // Add the matching Conseils to the observable list
        ArtTableView.setItems(ol); // Set the items in the TableView
    }


    @FXML
    void UpdateImages(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")); // Add more supported image formats if needed
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            paths.setText(selectedFile.getPath());
            String destinationFolder = "C:\\xampp\\htdocs\\image"; // Change the destination folder path
            File destinationFile = new File(destinationFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Load the selected image

            Image image = new Image(destinationFile.toURI().toString());
            // Set the image to the ImageView
            imageARTS.setImage(image);
        } else {
            // Handle the case where no file was selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No image file selected");
            alert.showAndWait();
        }
    }

    @FXML
    void Sort(ActionEvent event) {
        // Retrieve the items from the TableView
        ObservableList<art> items = ArtTableView.getItems();

        // Sort the items by title using Comparator
        items.sort(Comparator.comparing(art::getPrice));

        // Update the TableView with the sorted items
        ArtTableView.setItems(items);
    }
    @FXML
    void Sortdecroissant(ActionEvent event) {
        // Retrieve the items from the TableView
        ObservableList<art> items = ArtTableView.getItems();

        // Sort the items by price in descending order using Comparator
        items.sort(Comparator.comparing(art::getPrice, Comparator.reverseOrder()));

        // Update the TableView with the sorted items
        ArtTableView.setItems(items);
    }
    @FXML
    void PDF(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            List<art> artList = ps.getAllArts();

            try {
                // Créer le document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                Paragraph title = new Paragraph("List Of Your ART works", FontFactory.getFont(FontFactory.TIMES_BOLD, 20));
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50); // Ajouter une marge avant le titre pour l'éloigner de l'image
                title.setSpacingAfter(20);
                document.add(title);
                String imagePath = "C:\\xampp\\htdocs\\image\\logo.png"; // Update this with the path to your image
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
                // Positionner l'image en haut à gauche
                image.setAbsolutePosition(10, document.getPageSize().getHeight() -80);
// Set the scale of the image
                image.scaleToFit(100, 100); // Set the dimensions as needed

// Add the image to the document
                document.add(image);
                Paragraph companyInfo = new Paragraph();
                companyInfo.add(new Chunk("Company Vinci\n", FontFactory.getFont(FontFactory.TIMES_BOLD, 16)));
                companyInfo.add("Pole Technologie , Ghazela\n");
                companyInfo.add("Ariana,Tunisie\n");
                companyInfo.add("Tél : +70 800 000\n");
                companyInfo.add("Email :vinci@gmail.con\n");
                companyInfo.add("Date : " + LocalDate.now().toString() + "\n \n \n");
                document.add(companyInfo);

                // Créer la table des produits
                PdfPTable table = new PdfPTable(9); // Adjusted to match the number of columns in the header
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 4,2, 2, 2, 2, 2, 2, 2}); // Définir la largeur des colonnes
                // En-têtes de colonnes
                addTableHeader(table, "Title", "Materials", "Height", "Width", "type", "city", "Description","id_category","price");

                // Ajouter les lignes de produits à la table
                addRows(table, artList);

                // Ajouter la table au document
                document.add(table);

                // Fermer le document
                document.close();

                System.out.println("Le fichier PDF de la facture a été généré avec succès.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour ajouter les lignes de produits à la table
    private void addRows(PdfPTable table, List<art> artList) {
        for (art art : artList) {
            table.addCell(art.getTitle());
            table.addCell(art.getMaterials());
            table.addCell(String.valueOf(art.getHeight()));
            table.addCell(String.valueOf(art.getWidth()));
            table.addCell(art.getType());
            table.addCell(art.getCity());
            table.addCell(art.getDescription());
            table.addCell(String.valueOf(art.getId_category()));
            table.addCell(String.valueOf(art.getPrice()));

        }
    }

    // Méthode pour ajouter les en-têtes de colonnes à la table
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setPadding(5);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

    @FXML
    void play_videos(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Art/uplaodvideoFront.fxml"));
            Parent playVideoRoot = fxmlLoader.load();

            UplaodvideoFrontController video = fxmlLoader.getController();
            video.DisplayArtController(this);
            video.togglePlaybacks(event);

            Stage playVideoStage = new Stage();
            playVideoStage.setScene(new Scene(playVideoRoot));
            playVideoStage.setTitle("Video");
            playVideoStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }
    @FXML
    void excel(ActionEvent event) throws IOException {

        ExcelHandler handler = new ExcelHandler();
        String filePath = "art.xlsx";
        handler.writeExcelFile(filePath);
        try {
            handler.readExcelFile(filePath);
            Image originalImage = new Image(String.valueOf(getClass().getResource("/images/success.png")));
            double targetWidth = 50; // Set the desired width
            double targetHeight = 50; // Set the desired height
            Image resizedImage = new Image(originalImage.getUrl(), targetWidth, targetHeight, true, true);
            Notifications notification = Notifications.create();
            notification.graphic(new ImageView(resizedImage));
            notification.text("Fichier Excel est enregistré");
            notification.title("Succés");
            notification.hideAfter(Duration.seconds(4));
            notification.position(Pos.BOTTOM_RIGHT);
            notification.darkStyle();
            notification.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public TextField getPathVideo()
    {
        return pathVideo;
    }
    private UserService us = new UserService();

    @FXML
    void settings(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
    }

    @FXML
    void Logout(ActionEvent event) {
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void gohome(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

}