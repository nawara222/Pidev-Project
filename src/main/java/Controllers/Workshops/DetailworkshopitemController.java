package Controllers.Workshops;

import Models.Workshop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class DetailworkshopitemController
{
    workshopItemController wic;
    private Workshop currentWorkshop;
    private Workshop selectedWorkshopID;

    @FXML
    private ImageView meetLinkImage;


    @FXML
    private Label resourceswFU;

    @FXML
    private Label descriptionwFU;

    @FXML
    private Label durationwFU;

    @FXML
    private Label namewFU;

    @FXML
    private Label carosserie;
    private int carouselIndex = 0;



    private String[] carouselTexts = {
            "Embark on a journey of skill acquisition through our engaging workshops.",
            "Immerse yourself in the expertise of our highly skilled artists.",
            "Connect with like-minded individuals, forging friendships through shared interests."
    };
    public void initialize()
    {
        startTextCarousel();
    }
    private void startTextCarousel() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            carosserie.setText(carouselTexts[carouselIndex]);
            carouselIndex = (carouselIndex + 1) % carouselTexts.length;
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @FXML
    void CodeQr(MouseEvent event) {
        if (currentWorkshop != null) {
            System.out.println("ID workshop : " + currentWorkshop.getId_W());

            String googleMeetLink = "https://meet.google.com/hnt-bqtz-ear";

            Image meetLinkImage = new Image("https://meet.google.com/");

            this.meetLinkImage.setImage(meetLinkImage);

            this.meetLinkImage.setOnMouseClicked(e -> {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(googleMeetLink));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            this.meetLinkImage.setCursor(Cursor.HAND);

            this.meetLinkImage.setVisible(true);
        } else {
            System.out.println("No art selected."); // or handle this case appropriately
        }
    }
    public void setCurrentWorkshop(Workshop currentWorkshop) {
        this.currentWorkshop = currentWorkshop;
    }

    public void setInstancew(Workshop workshop)
    {
        this.selectedWorkshopID = workshop;
        namewFU.setText(selectedWorkshopID.getNameW());
        descriptionwFU.setText(selectedWorkshopID.getDescription());
        resourceswFU.setText(selectedWorkshopID.getResources());
        durationwFU.setText(String.valueOf(selectedWorkshopID.getDuration()));
    }


    @FXML
    void paiement(ActionEvent event) {

    }

    @FXML
    void aboutusbutton(ActionEvent event) {

    }

    @FXML
    void artworksbutton(ActionEvent event) {

    }

    @FXML
    void auctionbutton(ActionEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event)
    {


    }
    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshopsF.fxml"));
            namewFU.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }


    }


    @FXML
    void viewcartbutton(ActionEvent event) {

    }

}

