package Controllers.Art;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import Models.art;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import Services.Art.ArtServices;
import Services.Art.CategoryServices;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ShowMoreController {

    private static int idA;
    private art currentArt; // Assuming this is where you store the current art instance

    private boolean isPlayed = false;
    @FXML
    private Label lbl_duration;

    private Media media;
    private MediaPlayer mediaPlayer;
    @FXML
    private ImageView icon_player;

    private ArtServices cs = new ArtServices();

    @FXML
    private Text CatText;

    @FXML
    private Text CityText;

    @FXML
    private Slider slider;

    @FXML
    private Text MaterialsText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text heightText;

    @FXML
    private Text prixText;

    @FXML
    private Text rarityText;

    @FXML
    private Text titleText;

    @FXML
    private Text widthText;
    @FXML
    private MediaView mv_video;

    @FXML
    private HBox qrCodeImgModel;
    @FXML
    private Label CATEGORYA;

    @FXML
    private Label CITYA;

    @FXML
    private Label DESCRIPTIONA;

    @FXML
    private Label HEIGHTA;
    @FXML
    private ImageView qrCodeImg;

    @FXML
    private Label MATERIALSA;


    @FXML
    private Text pathvideo;

    public Text getPathvideo() {
        return pathvideo;
    }

    @FXML
    private Label PRICEA;

    @FXML
    private Label TITLEA;

    @FXML
    private Label TYPEA;

    @FXML
    private Label WIDTHA;

    @FXML
    private AnchorPane apid;
    private ShowMoreController showMoreController;

    public void showMoreController(ShowMoreController showMoreController) {
        this.showMoreController = showMoreController;
    }

    public void setDataa(art art) {
        CategoryServices categoryServices = new CategoryServices();
        titleText.setText(art.getTitle());
        MaterialsText.setText(art.getMaterials());
        heightText.setText(String.valueOf(art.getHeight()));
        widthText.setText(String.valueOf(art.getHeight())); // Assuming this is correct, it should be art.getWidth() normally
        rarityText.setText(art.getType());
        CityText.setText(art.getCity());
        descriptionText.setText(art.getDescription());
        prixText.setText(String.valueOf(art.getPrice()));
        int categoryId = art.getId_category();
        String categoryName = ""; // Default value
        try {
            categoryName = categoryServices.getCategoryName(categoryId);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error fetching category name: " + e.getMessage());
            alert.showAndWait();
        }
        // Set the category name in the label
        CatText.setText(categoryName);
        pathvideo.setText(art.getVideo());
        setCurrentArt(art);

    }

    @FXML
    void initialize() {
        pathvideo.setVisible(false);

    }

    // Helper method to display an alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goHomis(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Art/FronClient.fxml"));
            titleText.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void sliderPressed(MouseEvent event) {
        mediaPlayer.seek(Duration.seconds(slider.getValue()));
    }

    @FXML
    void togglePlayback(ActionEvent event) {
        // Retrieve the text from the TextField
        String videoPath = pathvideo.getText();  // get the path of video setted in field

        if (videoPath != null && !videoPath.isEmpty()) {
            if (mediaPlayer == null || !isPlayed) {
                // If mediaPlayer is not initialized or video is not played, start playing
                media = new Media(new File(videoPath).toURI().toString());   // convert this path to media
                mediaPlayer = new MediaPlayer(media);   // load this media in mediaplayer
                mv_video.setMediaPlayer(mediaPlayer);  // set this mediaplayer in mediaview
                Image image = new Image(String.valueOf(getClass().getResource("/images/pause.png")));
                icon_player.setImage(image);
                mediaPlayer.play();
                isPlayed = true;
                mediaPlayer.currentTimeProperty().addListener((((observable, oldValue, newValue) -> {
                    slider.setValue(newValue.toSeconds());
                })));
                mediaPlayer.setOnReady(() -> {
                    Duration totalDuration = media.getDuration();
                    slider.setValue(totalDuration.toSeconds());
                });
            } else {
                // If mediaPlayer is already initialized and video is played, toggle between pause and play
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    Image image = new Image(String.valueOf(getClass().getResource("/images/bouton-jouer.png")));
                    icon_player.setImage(image);
                    mediaPlayer.pause();
                    mediaPlayer.currentTimeProperty().addListener((((observable, oldValue, newValue) -> {
                        slider.setValue(newValue.toSeconds());
                        lbl_duration.setText("Duration : " + slider.getValue() + " / " + media.getDuration().toString());
                    })));

                    mediaPlayer.setOnReady(() -> {
                        Duration totalDuration = media.getDuration();
                        slider.setValue(totalDuration.toSeconds());
                        lbl_duration.setText("Duration : 00 / " + totalDuration);
                    });

                } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    Image image = new Image(String.valueOf(getClass().getResource("/images/pause.png")));
                    icon_player.setImage(image);
                    mediaPlayer.play();
                    mediaPlayer.currentTimeProperty().addListener((((observable, oldValue, newValue) -> {
                        slider.setValue(newValue.toSeconds());
                        lbl_duration.setText("Duration : " + slider.getValue() + " / " + media.getDuration().toString());
                    })));

                    mediaPlayer.setOnReady(() -> {
                        Duration totalDuration = media.getDuration();
                        slider.setValue(totalDuration.toSeconds());
                        lbl_duration.setText("Duration : " + slider.getValue() + " / " + media.getDuration().toString());
                    });
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No video file selected");
            alert.showAndWait();
        }
    }

    @FXML
    void CodeQr(MouseEvent event) {
        if (currentArt != null) {
            System.out.println("ID art : " + currentArt.getId_art());
            String phoneNumber = "50797955"; // The phone number to call
            String phoneNumberUri = "tel:" + phoneNumber;

            String text = phoneNumberUri; // Just the phone number URI

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix;
            try {
                bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageView qrCodeImg = (ImageView) ((Node) event.getSource()).getScene().lookup("#qrCodeImg");
                qrCodeImg.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                HBox qrCodeImgModel = (HBox) ((Node) event.getSource()).getScene().lookup("#qrCodeImgModel");
                qrCodeImgModel.setVisible(true);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No art selected."); // or handle this case appropriately
        }
    }

    public void setCurrentArt(art currentArt) {
        this.currentArt = currentArt;
    }


}



