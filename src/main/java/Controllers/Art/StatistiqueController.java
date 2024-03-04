package Controllers.Art;

import Models.art;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;
import Services.Art.ArtServices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class StatistiqueController implements Initializable {

    ArtServices artServices = new ArtServices();

    @FXML
    private Text creationDate;

    @FXML
    private Text nbreart;

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            int nbrArts = artServices.ConseilNumbers();
            nbreart.setText(String.valueOf(nbrArts));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            art art = artServices.getLastAddedArt();
            String date = String.valueOf(art.getDateCreation());
            creationDate.setText(date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
populatePieChart();
    }

    private void populatePieChart() {
        try {
            Map<Integer, Long> conseilCounts = artServices.getArtCountByCategory();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Map.Entry<Integer, Long> entry : conseilCounts.entrySet()) {
                int idCategorie = entry.getKey();
                long conseilCount = entry.getValue();
                String categoryName = artServices.getCategoryNames(idCategorie);

                // Create a label that includes the category name and the number of conseils
                String label = categoryName + " (" + conseilCount + " conseils)";

                // Add data to the PieChart with the customized label
                PieChart.Data data = new PieChart.Data(label, conseilCount);
                pieChartData.add(data);
            }
            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/addart.fxml"));
            pieChart.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

}
