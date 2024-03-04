package Controllers.Baskets;

import Controllers.Art.ItemsArtController;
import Controllers.Orders.AddOrderController;
import Models.Basket;
import Models.art;
import Services.OrdersAndBaskets.BasketService;
import Services.OrdersAndBaskets.OrderService;
import Services.User.UserService;
import Test.MainFX;
import Utils.EmailSend;
import jakarta.mail.MessagingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddQuantityBasketController {
    private OrderService OrderService = new OrderService();
    private BasketService basketService = new BasketService();
    public static int id;


    private art art;
    private ItemsArtController itemsArtController;
    @FXML
    private Button continueButton;
    @FXML
    private Button Home;
    @FXML
    private TableView<art> itemstab;
    @FXML
    private Button addBasketbutton;

    @FXML
    private Button addquantity3;

    @FXML
    private TableColumn<Basket, String> citya;


    @FXML
    private Button deletequantity2;

    @FXML
    private TableColumn<Basket, String> desca;

    @FXML
    private Button discount;

    @FXML
    private TableColumn<art, String> heighta;

    @FXML
    private TableColumn<art, String> materialsa;

    @FXML
    private TableColumn<art, String> pricea;

    @FXML
    private Label quantityLabel;

    @FXML
    private TableColumn<Basket, String> raritya;

    @FXML
    private Button shareButton;

    @FXML
    private TableColumn<art, String> titlea;

    @FXML
    private TextField totalPriceS;
    private ObservableList<art> artData = FXCollections.observableArrayList();
    @FXML
    private TableColumn<String, Basket> widtha;
    public static float totalPriceToPass;

    public void setCurrentArt(art currentArt) {
        this.art = currentArt;
    }


    public void setnewdata(art art) {
        // Assuming 'art' is the item you want to add to the TableView
        this.art = art; // You might still want to keep this, depending on other needs

        // Now, instead of just setting the private field, add 'art' to the 'artData' list
        artData.add(art); // This adds the new art to the observable list

        // Configure the TableView columns if not already done
        // It's okay to set these multiple times, but you could check if they've been set previously if performance is a concern
        titlea.setCellValueFactory(new PropertyValueFactory<>("title"));
        materialsa.setCellValueFactory(new PropertyValueFactory<>("materials"));
        heighta.setCellValueFactory(new PropertyValueFactory<>("height"));
        widtha.setCellValueFactory(new PropertyValueFactory<>("width")); // Make sure 'widtha' is correctly defined if you're using it for 'art'
        raritya.setCellValueFactory(new PropertyValueFactory<>("type")); // Assuming 'rarity' is a property of 'art'
        citya.setCellValueFactory(new PropertyValueFactory<>("city"));
        desca.setCellValueFactory(new PropertyValueFactory<>("description")); // Ensure this matches the property name in 'art'
        pricea.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateTotalPrice();
        // Set the ObservableList to the TableView
        itemstab.setItems(artData);
    }

    private void updateTotalPrice() {
        float totalPrice = 0f;
        for (art item : artData) {
            totalPrice += item.getPrice(); // Make sure getPrice() returns a float
        }
        // Use Locale.US to ensure the period is used as the decimal separator.
        totalPriceS.setText(String.format(Locale.US, "%.2f", totalPrice));
    }


    @FXML
    void addQuantity(ActionEvent event) {
        // Increment the quantity directly without using the basket ID
        int quantity = safeParseInt(quantityLabel.getText(), 1);
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
    }

    @FXML
    void deleteQuantity(ActionEvent event) {
        int quantity = safeParseInt(quantityLabel.getText(), 0);
        if (quantity > 1) { // Ensure quantity is greater than 1 before decrementing
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        } else {
            showErrorAlert("Minimum quantity reached.");
        }
    }

    @FXML
    void navigate(ActionEvent event) {
        // Navigation logic to move to another scene
    }

    @FXML
    public void addBasket(ActionEvent actionEvent) {
        int userId = UserService.currentlyLoggedInUser.getUserID();

        // Use NumberFormat with a US locale to ensure period is used as the decimal separator
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        float totalPrice;
        try {
            // Parse the totalPriceS text as a Number, then get its float value
            totalPrice = format.parse(totalPriceS.getText().trim()).floatValue();
        } catch (ParseException e) {
            showErrorAlert("Invalid total price format. Please ensure all items are priced correctly using a period (.) as the decimal separator.");
            return; // Exit the method early if there's an error
        }
        int Id_art = 17;
        Basket newBasket = new Basket();
        newBasket.setQuantity(1);
        newBasket.setUserid(userId);
        newBasket.setTotalPrice(totalPrice);
        newBasket.setId_art(Id_art);

        int basketId = basketService.create(newBasket);
        if (basketId > 0) {
            showInfoAlert("Basket added successfully with ID: " + basketId + " and total price: " + format.format(totalPrice));
        } else {
            showErrorAlert("Error adding basket to the database.");
        }
    }


    public void applyDiscount(float discountPercentage) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        float totalPrice;
        try {
            totalPrice = format.parse(totalPriceS.getText().trim()).floatValue();
        } catch (ParseException e) {
            showErrorAlert("Invalid total price format.");
            return;
        }

        float discountAmount = totalPrice * (discountPercentage / 100);
        float finalPrice = totalPrice - discountAmount;

        // Update the totalPriceS TextField with formatted final price
        totalPriceS.setText(format.format(finalPrice));

        showInfoAlert("A discount of " + discountPercentage + "% has been applied. New total price: " + format.format(finalPrice));
    }


    @FXML
    void onApplyDiscountClicked(ActionEvent event) {
        // Appliquer une réduction de 20%
        applyDiscount(20.0f);
    }

    @FXML
    void navigateC(ActionEvent event) {
        try {
            // Parse and set the total price before navigating
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            float totalPrice = format.parse(totalPriceS.getText().trim()).floatValue();
            totalPriceToPass = totalPrice; // Static variable to pass the total price

            // Load the AddOrder.fxml and set the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders/AddOrder.fxml"));
            Parent root = loader.load();

            // Assuming AddOrderController has a method called 'setTotalPrice'
            AddOrderController orderController = loader.getController();
            orderController.setTotalPrice(totalPriceToPass);

            // Set the scene
            Home.getScene().setRoot(root);
        } catch (IOException e) {
            showErrorAlert("Error navigating to order: " + e.getMessage());
        } catch (ParseException e) {
            showErrorAlert("Error parsing total price: " + e.getMessage());
        }
    }



    @FXML
    void ShareBasket(ActionEvent event) {
        // Ask the user for the recipient's email address
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Share Basket");
        dialog.setHeaderText("Enter recipient's email address:");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            // Retrieve price and quantity information
            int quantity = safeParseInt(quantityLabel.getText(), 0);
            float totalPrice;
            try {
                totalPrice = Float.parseFloat(totalPriceS.getText().trim());
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid total price format.");
                return;
            }

            // Generate the email content with the shared basket information and interface components
            String subject = "Shared Basket";
            String content = generateEmailContent(quantity, totalPrice);

            try {
                // Send the email with the generated content
                EmailSend.MailSender(email, subject, content);
                showInfoAlert("Basket shared successfully with " + email);
            } catch (MessagingException | UnsupportedEncodingException e) {
                showErrorAlert("Failed to share basket. Please try again later.");
            }
        });
    }

    private String generateEmailContent(int quantity, float totalPrice) {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/CSS/emailStyle.html")) {
            if (inputStream == null) {
                throw new IOException("Email template file not found.");
            }

            String htmlContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // Define the table with dynamic content
            String dynamicTable = "<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">" +
                    "<tr><th>Quantity</th><th>Total Price</th></tr>" +
                    "<tr><td>" + quantity + "</td><td>" + String.format("%.2f", totalPrice) + "</td></tr>" +
                    "</table>";

            // Find the closing tag of the last <span> that is within the <h1>
            htmlContent = htmlContent.replaceFirst("(?i)(<h1 class=\"v-text-align\"[^>]*>(?:<span[^>]*>)*.*?</span></span></span></span></span></span></span></h1>)", "$1" + dynamicTable);

            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to generate email content.";
        }
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to safely parse integers
    private int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue; // Return default value if parsing fails
        }
    }

    @FXML
    void applyLoyaltyDiscount(ActionEvent event) {
        try {
            // Retrieve the currently logged-in user ID
            int userId = UserService.currentlyLoggedInUser.getUserID();

            // Check the number of orders to determine if a discount should be applied
            int numberOfOrders = OrderService.getNumberOfOrdersByUser(userId);
            if (numberOfOrders >= 5) {
                // Retrieve the latest basket ID for the user
                Basket basket = basketService.getBasketByUserId(userId);
                int idB = basket.getIdB();
                // Apply the loyalty discount to the basket
                basketService.applyLoyaltyDiscount(idB);

                // Retrieve the updated basket from the database
                Basket updatedBasket = basketService.getBasketById(idB);

                // Update the UI with the new total price after discount
                totalPriceS.setText(String.format("%.2f", updatedBasket.getTotalPrice()));

                // Display success message
                showInfoAlert("Loyalty discount applied successfully.");
            } else {
                // Inform the user that they do not have enough orders to qualify for a discount
                showErrorAlert("You need to have at least 5 orders to qualify for a loyalty discount.");
            }
        } catch (Exception e) {
            // Display error message if something goes wrong
            showErrorAlert("Failed to apply loyalty discount: " + e.getMessage());
        }
    }


    public void homebutton(ActionEvent actionEvent) {
        UserService us = new UserService();
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }
}