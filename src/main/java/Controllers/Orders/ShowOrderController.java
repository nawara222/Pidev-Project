package Controllers.Orders;

import Models.Order;
import Services.OrdersAndBaskets.OrderService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
// Other imports as necessary


public class ShowOrderController {
    private final OrderService cs = new OrderService();
    private ObservableList<Order> ol;
    @FXML
    private Button statubutton;

    @FXML
    private DatePicker dateSO;

    @FXML
    private TableColumn<Order, String> datecol;

    @FXML
    private TableColumn<Order, Integer> idOcol;

    @FXML
    private ChoiceBox<String> sort;

    @FXML
    private TableView<Order> orderstable;

    @FXML
    private TextField priceSO;

    @FXML
    private TableColumn<Order, Float> pricecol;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Order, Order> deleteCol;


    @FXML
    void initialize() {
        loadOrders();
        setupDeleteColumn();
        setupSearchFunctionality();
        setupSortChoiceBox();
    }

    private void loadOrders() {
        try {
            List<Order> orders = cs.read();
            ol = FXCollections.observableList(orders);
            orderstable.setItems(ol);
            idOcol.setCellValueFactory(new PropertyValueFactory<>("idO"));
            datecol.setCellValueFactory(new PropertyValueFactory<>("dateC"));
            pricecol.setCellValueFactory(new PropertyValueFactory<>("totalP"));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot load orders: " + e.getMessage());
        }
    }

    private void setupDeleteColumn() {
        deleteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteCol.setCellFactory(param -> new TableCell<Order, Order>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);

                if (order == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        cs.delete(order.getIdO());
                        getTableView().getItems().remove(order);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Deleted", "Order deleted successfully.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Error", "Cannot delete order: " + e.getMessage());
                    }
                });
            }
        });
    }

    @FXML
    private void refreshButton() {
        try {
            ObservableList<Order> observableList = FXCollections.observableList(cs.read());
            orderstable.setItems(observableList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot refresh orders: " + e.getMessage());
        }
    }

    @FXML
    private void updateButtons(ActionEvent event) {
        Order order = orderstable.getSelectionModel().getSelectedItem();
        if (order != null) {
            try {
                LocalDate selectedDate = dateSO.getValue();
                if (selectedDate == null) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "No Date Selected", "Please select a date.");
                    return;
                }
                order.setDateC(selectedDate.toString());

                try {
                    float price = Float.parseFloat(priceSO.getText());
                    order.setTotalP(price);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid price.");
                    return;
                }

                cs.update(order, order.getIdO());
                refreshButton();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot update order: " + e.getMessage());
            }
            resetFields();
        }
    }

    @FXML
    private void resetFields() {
        dateSO.setValue(null);
        priceSO.clear();
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Order selectedOrder = orderstable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders/UpdateOrder.fxml"));
                    Parent root = loader.load();

                    UpdateOrderController updateOrderController = loader.getController();
                    updateOrderController.setOrder(selectedOrder);

                    Stage stage = (Stage) orderstable.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Navigation Error", null, "Unable to load the UpdateOrder scene: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void returnButton(ActionEvent event) {
        navigateTo("/Orders/AddOrder.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            orderstable.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", null, "Error loading " + fxmlPath + ": " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    @FXML
    void PDF(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            List<Order> OrdersList = cs.getAllOrders();

            try {
                // Créer le document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                Paragraph title = new Paragraph("List Of Your Orders", FontFactory.getFont(FontFactory.TIMES_BOLD, 20));
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50); // Ajouter une marge avant le titre pour l'éloigner de l'image
                title.setSpacingAfter(20);
                document.add(title);
                //Reminder to change this in every pc this is wrong !!!!
                String imagePath = "C:\\xampp\\htdocs\\image\\logo.png"; // Update this with the path to your image
                Image image = Image.getInstance(imagePath);
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
                PdfPTable table = new PdfPTable(4); // Correct number of columns
                table.setWidthPercentage(100); // Set the width of the table to 100%

                table.setWidths(new float[]{2, 2, 2, 2});
                // En-têtes de colonnes
                addTableHeader(table, "DateC", "TotalP", "IdB");

                // Ajouter les lignes de produits à la table
                addRows(table, OrdersList);

                // Ajouter la table au document
                document.add(table);

                // Fermer le document
                document.close();

                System.out.println("Your List of orders PDF has been generated successfully.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour ajouter les lignes de produits à la table
    private void addRows(PdfPTable table, List<Order> OrdersList) {
        for (Order order : OrdersList) {
            table.addCell(order.getDateC());
            table.addCell(String.valueOf(order.getIdB()));
            table.addCell(String.valueOf(order.getIdO()));
            table.addCell(String.valueOf(order.getTotalP()));

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
    private void handleDisplayStatisticsAction(ActionEvent event) {
        try {
            // Load the statistics FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders/Statistics.fxml"));
            Parent statisticsView = loader.load();

            // Get the current stage (or window) from the event
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the statistics view in the current stage
            stage.setScene(new Scene(statisticsView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading statistics view.", e.getMessage());
        }
    }

    private void setupSearchFunctionality() {
        // Assuming 'search' is the TextField used for searching
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            searchOrders(newValue); // Call searchOrders method whenever text changes
        });
    }

    // Modified searchOrders method to be called directly with the search text
    // Modify the searchOrders method to focus on searching by price
    private void searchOrders(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            orderstable.setItems(ol); // Reset to all orders if search text is empty
            return;
        }

        try {
            float searchPrice = Float.parseFloat(searchText);
            ObservableList<Order> filteredOrders = ol.filtered(order -> order.getTotalP() == searchPrice);
            orderstable.setItems(filteredOrders); // Update the table with filtered results
        } catch (NumberFormatException e) {
            // Handle the case where the search text is not a valid float
            orderstable.setItems(FXCollections.observableArrayList()); // Clear the table or handle differently
            showAlert(Alert.AlertType.ERROR, "Search Error", "Invalid Price", "Please enter a valid price value.");
        }
    }
    private void setupSortChoiceBox() {
        // Populate choice box with options
        sort.setItems(FXCollections.observableArrayList("Sort by Price", "Sort by Date"));

        // Handle choice selection to sort
        sort.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Sort by Price".equals(newVal)) {
                orderstable.getSortOrder().setAll(pricecol);
            } else if ("Sort by Date".equals(newVal)) {
                orderstable.getSortOrder().setAll(datecol);
            }
            orderstable.sort();
        });

        // Initial sorting setup, optional
        sort.getSelectionModel().select("Sort by Price");


}}

