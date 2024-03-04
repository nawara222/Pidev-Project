package Controllers.Orders;


import Models.Order;
import Services.OrdersAndBaskets.OrderService;
import Services.User.UserService;
import Test.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.SQLException;
import java.util.List;

public class StatisticsController {

    @FXML
    private PieChart ordersOverFiftyPieChart;

    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        try {
            loadPieChartData();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (show alert or log)
        }
    }

    private void loadPieChartData() throws SQLException {
        List<Order> orders = orderService.read(); // Replace with your method to fetch orders
        int ordersOverFiftyCount = (int) orders.stream().filter(order -> order.getTotalP() > 50).count();
        int ordersFiftyOrBelowCount = orders.size() - ordersOverFiftyCount;

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Orders Over 50", ordersOverFiftyCount),
                        new PieChart.Data("Orders 50 or Below", ordersFiftyOrBelowCount));

        ordersOverFiftyPieChart.setData(pieChartData);
        ordersOverFiftyPieChart.setTitle("Orders Statistics");
    }
    @FXML
    void gohome(ActionEvent event) {
         UserService us = new UserService();
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    public void homebutton(ActionEvent actionEvent) {
        UserService us = new UserService();
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }
}
