package Controllers.Art;

import Models.art;
import Models.category;
import Services.Art.ArtServices;
import Services.User.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FronClientController {
    @FXML
    private Button Coursesid;

    @FXML
    private Button Eventid;
    @FXML
    private Button Auctionid;
    @FXML
    private Button Paymentid;
    @FXML
    private Button Artworksid;
    @FXML
    private Button homy;
    @FXML
    private ComboBox<category> Category;

    @FXML
    private GridPane artGrid;

    @FXML
    private ScrollPane artItems;

    @FXML
    private TextField searchf;
    private ArtServices cs = new ArtServices();

    @FXML
    private Button loginButton;

    @FXML
    private Button Signupbutton;

    @FXML
    private Button settingsbutton;

    @FXML
    void login(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
    }

    @FXML
    void signup(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
    }

    @FXML
    void settings(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
    }

    public void CoursesBT(ActionEvent actionEvent) {

            us.switchView(MainFX.primaryStage, "/Courses/showCoursesF.fxml");
    }

    public void PaymentBT(ActionEvent actionEvent) {
        us.switchView(MainFX.primaryStage, "/Baskets/Basket.fxml");
    }


    @FXML
    void artworksbutton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Art/addart.fxml");}
        else
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
    void effect(MouseEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {
        if (UserService.currentlyLoggedInUser.getRole().equals("Artist")){
            us.switchView(MainFX.primaryStage, "/Events/ShowEventsF.fxml");}
        else
            us.switchView(MainFX.primaryStage, "/Events/ShowEventsAF.fxml");
    }



    @FXML
    void homebutton(ActionEvent event) {

    }

    @FXML
    void searchbutton(ActionEvent event) {

    }

    @FXML
    private Button LoginArtist;
    private UserService us = new UserService();

    @FXML
    void initialize() throws SQLException, IOException {

        artGrid.setHgap(30); // Horizontal gap between items
        artGrid.setVgap(30); // Vertical gap between items
        artItems.setContent(artGrid);
        artItems.setFitToWidth(true);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Set the desired height
        artGrid.getRowConstraints().add(rowConstraints);
        if (UserService.currentlyLoggedInUser != null) {

            loginButton.setVisible(false); // Hide login button
            Signupbutton.setVisible(false); // Hide login button
            settingsbutton.setVisible(true);
            Coursesid.setDisable(false);
            Eventid.setDisable(false);
            Auctionid.setDisable(false);
            Paymentid.setDisable(false);
            Artworksid.setDisable(false);
            homy.setDisable(false);

        } else {
            loginButton.setVisible(true); // Show login button
            Signupbutton.setVisible(true); // Hide login button
            settingsbutton.setVisible(false);
            Coursesid.setDisable(true);
            Eventid.setDisable(true);
            Auctionid.setDisable(true);
            Paymentid.setDisable(true);
            Artworksid.setDisable(true);
            homy.setDisable(true);
        }
        try {
            // Retrieve all categories
            List<category> categories = cs.getAllCategories();

            // Create "All Categories" option
            category allCategoriesOption = new category();
            allCategoriesOption.setId_category(-1);
            allCategoriesOption.setName("All Categories");

            // Add "All Categories" option as the first item in the ComboBox
            Category.getItems().add(allCategoriesOption);

            // Add the retrieved categories to the ComboBox
            Category.getItems().addAll(categories);

            // Select the first category by default
            Category.getSelectionModel().selectFirst();

            // Load initial data
            resetArtList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void artListF(List<art> artList) {
        artGrid.getChildren().clear();
        int columnCount = 0;
        int rowCount = 0;

        for (art art : artList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Art/ItemsArt.fxml"));
                Node artnode = loader.load();
                ItemsArtController itemController = loader.getController();
                itemController.setData(art); // Pass the art data to ItemsArtController
                artGrid.add(artnode, columnCount, rowCount);
                columnCount++;

                if (columnCount == 3) {
                    columnCount = 0;
                    rowCount++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    void GoFrontArtist(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/addart.fxml"));
            artGrid.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateArtGrid(List<art> artList) throws IOException {
        artGrid.getChildren().clear();

        int rowIndex = 0;
        int colIndex = 0;
        for (art art : artList) {
            Node artnode;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Art/ItemsArt.fxml"));
                artnode = loader.load();
                ItemsArtController itemController = loader.getController();
                itemController.setData(art);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            artGrid.add(artnode, colIndex, rowIndex);
            colIndex++;
            if (colIndex == 3) {
                colIndex = 0;
                rowIndex++;
            }
        }
        // After updating the grid, set fit to width of the ScrollPane
        artItems.setFitToWidth(true);
    }

    void resetArtList() {
        artGrid.getChildren().clear();

        try {
            List<art> initialart = cs.display(); // Replace with your method to get the initial list
            updateArtGrid(initialart);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

  @FXML
    void searchArt(KeyEvent event) throws IOException {
        String searchQuery = searchf.getText();

        if (searchQuery.isEmpty()) {
            resetArtList();
        } else {
            try {
                List<art> matchingArtworks = cs.searchArtByName(searchQuery);
                updateArtGrid(matchingArtworks);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the SQL exception appropriately
            }
        }
    }

    @FXML
    void filterone(ActionEvent event) {
        category selectedCategory = Category.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            int categoryId = selectedCategory.getId_category();

            try {
                List<art> artworksInCategory;
                if (categoryId == -1) { // All Categories selected
                    artworksInCategory = cs.display(); // Get all artworks
                } else {
                    artworksInCategory = cs.getArtByCategory(categoryId); // Get artworks by category
                }
                updateArtGrid(artworksInCategory);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }





}