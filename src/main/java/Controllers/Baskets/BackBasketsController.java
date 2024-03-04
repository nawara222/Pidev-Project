package Controllers.Baskets;

import Models.Order;
import Services.OrdersAndBaskets.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;

import java.sql.SQLException;
import java.util.List;

public class BackBasketsController {

    @FXML
    private TableView<Order> orderstable;

    @FXML
    private TableColumn<Order, Integer> idorder;

    @FXML
    private TableColumn<Order, Integer> idbasket;

    @FXML
    private TableColumn<Order, Float> totalprice;

    @FXML
    private TableColumn<Order, Order> delete;

    @FXML
    private TableColumn<Order, Order> update;

    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        loadOrders();
        setupDeleteColumn();
        setupEditableColumns();
    }

    private void setupEditableColumns() {
        orderstable.setEditable(true);

        totalprice.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        totalprice.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            order.setTotalP(event.getNewValue()); // assuming you have a setter for totalP
            try {
                orderService.update(order, order.getIdO());
                orderstable.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error", "Cannot update order: " + e.getMessage());
            }
        });
    }

    private void loadOrders() {
        try {
            List<Order> orders = orderService.read();
            ObservableList<Order> ordersObservableList = FXCollections.observableList(orders);
            orderstable.setItems(ordersObservableList);
            idorder.setCellValueFactory(new PropertyValueFactory<>("idO"));
            idbasket.setCellValueFactory(new PropertyValueFactory<>("idB"));
            totalprice.setCellValueFactory(new PropertyValueFactory<>("totalP"));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot load orders: " + e.getMessage());
        }
    }

    private void setupDeleteColumn() {
        delete.setCellFactory(param -> new TableCell<Order, Order>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    try {
                        orderService.delete(order.getIdO());
                        orderstable.getItems().remove(order);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Error", "Cannot delete order: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}