package Controllers.back.User;

import Models.Users;
import Services.User.UserService;
import Test.MainFX;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class AnalyticsController {

    private UserService us = new UserService();
    @FXML
    private PieChart activeUsersPieChart;

    @FXML
    private BarChart<String, Number> barChart;

    public void initialize() {

        try {
            List<Users> users = us.read();
            setupPieChart(users);
            setupBarChart(users);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML. Here's the detailed error message: " + e.getMessage());
        }
    }
    private void setupPieChart(List<Users> users) {
        long highestUserId = users.stream()
                .mapToLong(Users::getUserID)
                .max()
                .orElse(0);
        long activeCount = users.stream()
                .filter(user -> "Active".equals(user.getAccountStatus()) && !"Admin".equals(user.getRole()))
                .count();
        long adminCount = users.stream()
                .filter(user -> "Admin".equals(user.getRole()))
                .count();
        long totalEverRegistered = highestUserId - adminCount;
        long inactiveCount = totalEverRegistered - activeCount;

        PieChart.Data activeData = new PieChart.Data("Active", activeCount);
        PieChart.Data inactiveData = new PieChart.Data("Inactive", inactiveCount);



        activeUsersPieChart.setData(FXCollections.observableArrayList(activeData, inactiveData));

        for (PieChart.Data data : activeUsersPieChart.getData()) {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ",
                            Bindings.format("%.1f%%", data.pieValueProperty().divide(totalEverRegistered).multiply(100)),
                            " (", data.pieValueProperty().asObject(), " users)"
                    )
            );
        }

        // Set the chart title and other properties
        activeUsersPieChart.setTitle("User Activity Status");
        activeUsersPieChart.setLegendVisible(true);
        activeUsersPieChart.setLegendSide(Side.BOTTOM);
        activeUsersPieChart.setLabelsVisible(true);
        activeUsersPieChart.setLabelLineLength(10);
        activeData.getNode().setStyle("-fx-pie-color: #64DD17;"); // Example color
        inactiveData.getNode().setStyle("-fx-pie-color: #DD2C00;"); // Example color
    }


    private void setupBarChart(List<Users> users) {
        barChart.setTitle("User Role Distribution");
        barChart.setLegendVisible(false); // Set to true if you want to display the legend
        barChart.setAnimated(true);
        long amateurCount = users.stream()
                .filter(user -> "Amateur".equals(user.getRole()))
                .count();
        long artistCount = users.stream()
                .filter(user -> "Artist".equals(user.getRole()))
                .count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Role Counts");

        XYChart.Data<String, Number> amateurData = new XYChart.Data<>("Amateur", amateurCount);
        XYChart.Data<String, Number> artistData = new XYChart.Data<>("Artist", artistCount);

        series.getData().addAll(amateurData, artistData);
        barChart.getData().clear();
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
        barChart.setAnimated(true);

        // Apply different colors to bars and add data labels
        amateurData.getNode().setStyle("-fx-bar-fill: #e9967a;"); // Dark Salmon
        artistData.getNode().setStyle("-fx-bar-fill: #6a5acd;"); // Slate Blue

        // Add data labels and style axes
        addDataLabelsToBars(series);
        styleChartAxes();


    }

    private void addDataLabelsToBars(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Label label = new Label(data.getYValue() + " Users");
            label.setFont(Font.font("SansSerif", FontWeight.NORMAL, 10));
            label.setTextFill(Color.WHITE);

            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    displayLabelForData(data, label);
                }
            });
        }
    }

    private void styleChartAxes() {
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

        xAxis.setLabel("User Role");
        yAxis.setLabel("Count");
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLabelFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        yAxis.setTickLabelFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setTickLabelFill(Color.CHOCOLATE);

        // Adjust the padding and margins
        barChart.setPadding(new Insets(15, 12, 15, 12));
    }


    private void displayLabelForData(XYChart.Data<String, Number> data, Label label) {
        Node node = data.getNode();
        node.parentProperty().addListener((obs, oldParent, newParent) -> {
            if (newParent != null) {
                Group parentGroup = (Group) newParent;
                parentGroup.getChildren().add(label);
            }
        });

        node.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
            label.setLayoutX(Math.round(newBounds.getMinX() + newBounds.getWidth() / 2 - label.prefWidth(-1) / 2));
            label.setLayoutY(Math.round(newBounds.getMinY() - label.prefHeight(-1) * 0.5));
        });
    }


    @FXML
    void back(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }

}
