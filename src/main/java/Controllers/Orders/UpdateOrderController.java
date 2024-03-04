package Controllers.Orders;

import Models.Order;
import Services.OrdersAndBaskets.OrderService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UpdateOrderController {

    private final OrderService cs = new OrderService();
    private Order order; // Field to store the order to update

    @FXML
    private Button updateButton;
    @FXML
    private DatePicker dateCu;
    @FXML
    private TextField idOu;

    @FXML
    private TextField totalPu;


    public void setOrder(Order order) {
        if (order == null) {
            System.out.println("The selected order is null.");
            return;
        }

        this.order = order;
        idOu.setText(String.valueOf(order.getIdO())); // Set Order ID in its TextField
        totalPu.setText(String.format("%.2f", order.getTotalP())); // Set Total Price in its TextField, formatted to 2 decimal places

        // Use the correct format for the date string you're trying to parse
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Corrected to match the input date string format

        try {
            // Attempt to parse the date string into a LocalDate object
            LocalDate date = LocalDate.parse(order.getDateC(), formatter);
            dateCu.setValue(date); // Set the parsed LocalDate into the DatePicker
        } catch (DateTimeParseException e) {
            System.out.println("Failed to parse the date: " + e.getMessage());
            // Here you can log the error, show an alert to the user, or handle the error in a way that fits your application
        }
    }


    @FXML
    void updateButton(ActionEvent event) {
        System.out.println(order.getIdO());
        try {
            int idB = order.getIdB(); // Get idB from the existing order object
            int idO = order.getIdO(); // Get idO from the existing order object
            float totalP = Float.parseFloat(totalPu.getText()); // Parse the total price from the text field
            LocalDate date = dateCu.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust as necessary
            String dateC = date.format(formatter);

            // Update the order using the service
            Order updatedOrder = new Order(totalP, dateC, idB);
            cs.update(updatedOrder, idO);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Order updated successfully");
            alert.showAndWait();

            resetFields();

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid numeric value for total price.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void resetFields() {
        totalPu.clear();
        dateCu.setValue(null);
    }
    @FXML
    void returnButton(ActionEvent event) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Orders/ShowOrder.fxml"));
            totalPu.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }

    }
}
