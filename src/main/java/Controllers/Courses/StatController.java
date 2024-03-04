package Controllers.Courses;

import Services.CoursesandWorkshops.CoursesService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class StatController
{
    CoursesService cs = new CoursesService();


    @FXML
    private BarChart<String,Long> barChart;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private ImageView fruitImg;

    @FXML
    private StackPane mainStackPane;

    @FXML
    private PieChart pieChart;

    @FXML
    private BorderPane updateButton;

    private String[] sliceColors = {
            "rgba(119, 141, 169, 1)",    // #778da9
            "rgba(65, 90, 119, 1)",      // #415a77
            "rgba(170, 170, 170, 1)",    // #aaaaaa
            "rgba(201, 164, 61, 1)",     // #c9a43d
            "rgba(4, 30, 50, 1)"         // #041e32
    };

    @FXML
    void showBarChart(ActionEvent event)
    {
        try
        {
            Map<String, Long> courseCountByPriceRange = cs.getCourseCountByPriceRange();

            XYChart.Series<String, Long> series = new XYChart.Series<>();
            for (Map.Entry<String, Long> entry : courseCountByPriceRange.entrySet())
            {
                String priceRange = entry.getKey();
                Long courseCount = entry.getValue();

                XYChart.Data<String, Long> data = new XYChart.Data<>(priceRange, courseCount);
                series.getData().add(data);

                Node node = data.getNode();

                if (node != null)
                {
                    int colorIndex = series.getData().indexOf(data) % sliceColors.length;
                    String color = sliceColors[colorIndex];
                    String barStyle = "-fx-pie-color: " + color + ";";
                    node.setStyle(barStyle);
                }
            }
            barChart.getData().clear();
            barChart.getData().add(series);

            barChart.getXAxis().setLabel("Price Range");
            barChart.getXAxis().setTickLabelRotation(45);

            barChart.getYAxis().setLabel("Number of Courses");

            barChart.setTitle("Course Count by Price Range");
            barChart.setLegendVisible(false);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void showPieChart(ActionEvent event)
    {
        try
        {
            Map<String, Double> coursePercentage = cs.getCoursePercentageByType();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Map.Entry<String, Double> entry : coursePercentage.entrySet()) {
                String typeName = entry.getKey();
                double percentage = entry.getValue();

                PieChart.Data data = new PieChart.Data(typeName + " (" + String.format("%.2f%%", percentage) + ")", percentage);
                pieChartData.add(data);
            }

            pieChart.getData().clear();
            pieChart.getData().addAll(pieChartData);

            pieChart.setLegendVisible(true);
            pieChart.setLabelsVisible(true);

            Text titleText = new Text("Course Distribution by Type");
            titleText.setFont(Font.font("Cambria", FontWeight.BOLD, 18));
            pieChart.setTitle(titleText.getText());

            for (int i = 0; i < pieChartData.size(); i++)
            {
                PieChart.Data data = pieChartData.get(i);
                Node node = data.getNode();
                node.setStyle("-fx-pie-color: " + sliceColors[i % sliceColors.length] + ";");
            }
            pieChart.setLegendVisible(false);
            pieChart.setLabelsVisible(true);

            pieChart.setPrefSize(600, 400);  // You can adjust the size as neede
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCourses.fxml"));
            pieChart.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }
}
