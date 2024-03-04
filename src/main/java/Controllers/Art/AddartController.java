package Controllers.Art;


import Models.art;
import Models.category;
import Services.User.UserService;
import Test.MainFX;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
import java.util.List;
import java.util.ResourceBundle;

public class AddartController implements Initializable {
    private final ArtServices artps = new ArtServices();
    private final CategoryServices categoryServices = new CategoryServices();

    @FXML
    private Button addIMAGES;
    @FXML
    private Label ImageEmty;

    @FXML
    private TextField TF_video;

    @FXML
    private Button Stat;

    @FXML
    private ImageView imageART;

    @FXML
    private ImageView addImage;
    @FXML
    private TextField pathArt;

    @FXML
    private Pane backImage;
    @FXML
    private Button home;
    @FXML
    private TextField titlef;
    @FXML
    private TextField materialsf;
    @FXML
    private TextField heightf;

    @FXML
    private TextField widthf;
    @FXML
    private TextField typef;
    @FXML
    private TextField cityf;
    @FXML
    private TextField descriptionf;
    @FXML
    private TextField pricef;
    @FXML
    private Button back;

    @FXML
    private ComboBox<category> typeCategry;
    @FXML
    private Label categoryempty;

    @FXML
    private Label cityempty;

    @FXML
    private Label heightempty;
    @FXML
    private Label materialsempty;
    @FXML
    private Label priceempty;
    @FXML
    private Label descriptionempty;
    @FXML
    private Label rarityempty;
    @FXML
    private Label titleempty;
    @FXML
    private Label typeempty;
    @FXML
    private Label widthempty;

    private boolean isValid = true;

    @FXML
    void add(ActionEvent event) {
        try {
            setVisibility();
            //Items retrieving text input from a text field
            String title = titlef.getText().trim();
            String material = materialsf.getText().trim();
            double height = heightf.getText().isEmpty() ? 0.0d : Double.parseDouble(heightf.getText().trim());
            double width = widthf.getText().isEmpty() ? 0.0d : Double.parseDouble(widthf.getText().trim());
            String type = typef.getText().trim();
            String city = cityf.getText().trim();
            String description = descriptionf.getText().trim();
            float price = pricef.getText().isEmpty() ? 0.0f : Float.parseFloat(pricef.getText().trim());
            category SelectedCategory = typeCategry.getValue();    // getting all combo category values
            String path_art = pathArt.getText().trim();
            String Video = TF_video.getText().trim();
            int Userid= UserService.currentlyLoggedInUser.getUserID();
            // Validate fields
            if (title.isEmpty()) {
                titleempty.setVisible(true);
                isValid = false;
            }
            if (material.isEmpty()) {
                materialsempty.setVisible(true);
                isValid = false;
            }
            if (height == 0.0d) {
                heightempty.setVisible(true);
                isValid = false;
            }
            if (width == 0.0d) {
                widthempty.setVisible(true);
                isValid = false;
            }
            if (type.isEmpty()) {
                rarityempty.setVisible(true);
                isValid = false;

            }

            if (city.isEmpty()) {
                cityempty.setVisible(true);
                isValid = false;

            }
            if (description.isEmpty()) {
                descriptionempty.setVisible(true);
                isValid = false;

            }
            if (price == 0.0f) {
                priceempty.setVisible(true);
                isValid = false;

            }
            if (SelectedCategory == null) {
                categoryempty.setVisible(true);
                isValid = false;
            }
            if (path_art.isEmpty()) {
                ImageEmty.setVisible(true);
                isValid = false;
            }
            if (!isValid)
                return;

            // Add art if all validations pass
            artps.add(new art(
                    title,
                    material,
                    height,
                    width,
                    type,
                    city,
                    description, price,
                    SelectedCategory.getId_category(),
                    path_art,
                    Video,
                    Userid
                    ));
            //sendSMS();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Art added successfully");
            alert.showAndWait();

        } catch (SQLException e) {
            showError("Error adding art: " + e.getMessage());
        }


    }

    @FXML
    void display(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/displayArt.fxml"));
            titlef.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setVisibility();
        List<category> allCategories = null;
        try {
            allCategories = categoryServices.displayC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set categories as items in the ComboBox
        typeCategry.setItems(FXCollections.observableList(allCategories));

    }

    public void setVisibility() {
        titleempty.setVisible(false);
        materialsempty.setVisible(false);
        heightempty.setVisible(false);
        widthempty.setVisible(false);
        rarityempty.setVisible(false);
        cityempty.setVisible(false);
        descriptionempty.setVisible(false);
        priceempty.setVisible(false);
        categoryempty.setVisible(false);
        ImageEmty.setVisible(false);
        pathArt.setVisible(false);

    }


    @FXML
    void gotoHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/FronClient.fxml"));
            materialsf.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void AddImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")); // Add more supported image formats if needed
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            pathArt.setText(selectedFile.getPath());
            String destinationFolder = "C:\\xampp\\htdocs\\image"; // Change the destination folder path
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
    void seeStat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/Statistique.fxml"));
            typef.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }
   /* private void sendSMS() {
        try {
            String ACCOUNT_SID = "ACd798b277e43df20066cdae91b223ec94";
            String AUTH_TOKEN = "2825d0e9d9eada564ddda2509a6c9961";
            String TWILIO_PHONE_NUMBER = "+16504371703";
            String recipientPhoneNumber = "+21656694187"; // Tunisian phone number format

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(
                    new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    "You added new work in VINCI\n"
            ).create();

            System.out.println("SMS sent successfully!");

        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
*/
    @FXML
    void uploadVideo(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")); // Corrected extensions
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            TF_video.setText(selectedFile.getPath());
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
    void auctionbutton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Artist/ViewAuctionArtist.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Auction Clients/ViewAuctionClient.fxml");
    }
    @FXML
    void Coursesbutton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Courses/showCoursesF.fxml");}

    }


}
