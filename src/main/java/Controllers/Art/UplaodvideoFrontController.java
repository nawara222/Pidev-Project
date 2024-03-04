package Controllers.Art;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

public class UplaodvideoFrontController {
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false ;

    @FXML
    private ImageView icon_player;

    @FXML
    private Label lbl_duration;

    @FXML
    private Label lll;

    @FXML
    private MediaView mv_video;

    @FXML
    private Slider slider;

    @FXML
    private HBox sliderr;

    private DisplayArtController displayArtController;
    public void DisplayArtController(DisplayArtController displayArtController)
    {
        this.displayArtController  = displayArtController ;
    }


    @FXML
    void sliderPressed(MouseEvent event) {
        mediaPlayer.seek(Duration.seconds(slider.getValue()));
    }

    @FXML
    void togglePlaybacks(ActionEvent event) {
        // Retrieve the text from the TextField
        String videoPath = displayArtController.getPathVideo().getText();  // get the path of video setted in field

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
                mediaPlayer.setOnReady(() ->{
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

                    mediaPlayer.setOnReady(() ->{
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

                    mediaPlayer.setOnReady(() ->{
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


}