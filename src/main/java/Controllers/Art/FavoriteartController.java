package Controllers.Art;

import Models.art;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class FavoriteartController {
    private List<art> favoriteList;

    @FXML
    private VBox favoriteListView;

    private void displayFavoriteList() {

        favoriteListView.getChildren().clear();

        // Check if the favoriteList is not null and not empty
        if (favoriteList != null && !favoriteList.isEmpty()) {
            for (art favoriteArt : favoriteList) {
                // Create a Label to display the details of each favorite art item
                Label favoriteLabel = new Label();
                favoriteLabel.setText("Title: " + favoriteArt.getTitle() + ", Category: " + favoriteArt.getId_category() + ", Price: " + favoriteArt.getPrice());

                // Add the Label to the favoriteListView
                favoriteListView.getChildren().add(favoriteLabel);
            }
        } else {
            // If the favoriteList is empty, display a message
            Label emptyLabel = new Label("No favorite art items added yet.");
            favoriteListView.getChildren().add(emptyLabel);
        }
    }
}