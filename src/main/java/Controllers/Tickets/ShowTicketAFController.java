package Controllers.Tickets;

import Models.Event;
import Models.Ticket;
import Services.Event.TicketService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ShowTicketAFController {
    TicketService ts = new TicketService();
    public static int f;
    Ticket ticket;
    public static Ticket t;

    @FXML
    private AnchorPane apid;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Button deleteTicketButtonF;

    private Image qrImage;

    private Event currentEvent;


    @FXML
    private ImageView qrCodeImage;

    @FXML
    private Button searchB;

    @FXML
    private TextField searchf;



    public Ticket getSelectedTicket() {
        return ticket;
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        System.out.println(currentEvent);
        System.out.println("Initializing with currentEvent: " + currentEvent);
        generateAndDisplayQRCode(1);
    }

    public void setEvent(Event event) {
        System.out.println("Setting event: " + event);
        this.currentEvent = event;
        generateAndDisplayQRCode(1);
    }


    private void generateAndDisplayQRCode(int userId) {
        if (currentEvent != null) {
            String qrCodeData = "Event ID: " + currentEvent.getIdE() +
                    "\nUser ID: " + userId +
                    "\nName: " + currentEvent.getNameE() +
                    "\nDate: " + currentEvent.getDateE() +
                    "\nDuration: " + currentEvent.getDurationE() +
                    "\nType: " + currentEvent.getTypeE() +
                    "\nEntry Fee: " + currentEvent.getEntryFeeE() +
                    "\nCapacity: " + currentEvent.getCapacityE();

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                // Set the QR image to the ImageView
                qrCodeImageView.setImage(SwingFXUtils.toFXImage(qrImage, null));

                String fileName = "QRCode_EventID_" + currentEvent.getIdE() + "_UserID_" + userId + ".png";
                String directoryPath = "C:\\xampp\\htdocs\\QRcode"; // Set your directory path
                String fullPath = Paths.get(directoryPath, fileName).toString();
                saveQRCodeImage(qrImage, fullPath);

                // Create a new ticket object with the QR code image path and user ID
                Ticket ticket = new Ticket();
                ticket.setIdE(currentEvent.getIdE());
                ticket.setIdU(userId); // Set the user ID
                ticket.setQrCodeT(fullPath); // Set the path to the QR code image

                // Save the ticket to the database
                ts.createT(ticket);

            } catch (WriterException | SQLException e) {
                showAlert("Error", "Could not generate QR code: " + e.getMessage());
            } catch (IOException e) {
                showAlert("Error", "Could not save QR code image: " + e.getMessage());
            }
        }
    }



    // Method signature corrected to accept a String path
    public String saveQRCodeImage(BufferedImage bufferedImage, String pathToFile) throws IOException {
        File qrCodeFile = new File(pathToFile);
        if (!qrCodeFile.getParentFile().exists()) {
            qrCodeFile.getParentFile().mkdirs(); // Create directories if they do not exist
        }
        ImageIO.write(bufferedImage, "PNG", qrCodeFile);
        return qrCodeFile.getAbsolutePath(); // Return the path where the file was saved
    }









    /** void CodeQr() {
         if (currentEvent != null) {
             System.out.println("ID Event : " + currentEvent.getIdE());

             String text = "ID Ticket: " + currentEvent.getIdE()
                     + "\nEvent Name: " + currentEvent.getNameE() + "\nEvent Date: "
                     + currentEvent.getDateE() + "\nEvent Duration: " + currentEvent.getDurationE()
                     + "\nEvent Type: " + currentEvent.getTypeE() + "\nEvent Entry Fee: " + currentEvent.getEntryFeeE()
                     + "\nEvent Capacity: " + currentEvent.getCapacityE();

             QRCodeWriter qrCodeWriter = new QRCodeWriter();
             BitMatrix bitMatrix;
             try {
                 bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
                 BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                 // Directly use qrCodeImageView since it's a class member
                 qrCodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                 // Assuming qrCodeImgModel is an HBox that you want to show, ensure it's defined in the controller
                 // qrCodeImgModel.setVisible(true);
             } catch (WriterException e) {
                 e.printStackTrace();
             }
         } else {
             System.out.println("No event selected."); // Adjust this message as needed
         }
     }**/


   /** private void setQRCode() {
        if (currentEvent != null) {
            // Here you will use your currentEvent to generate the QR code data
            String qrCodeData = "Event ID: " + currentEvent.getIdE() +
                    "\nName: " + currentEvent.getNameE() +
                    "\nDate: " + currentEvent.getDateE().toString() +
                    "\nDuration: " + currentEvent.getDurationE() +
                    "\nType: " + currentEvent.getTypeE() +
                    "\nEntry Fee: " + currentEvent.getEntryFeeE() +
                    "\nCapacity: " + currentEvent.getCapacityE();

            int size = 250; // size of QR code
            try {
                BitMatrix matrix = new MultiFormatWriter().encode(
                        qrCodeData,
                        BarcodeFormat.QR_CODE, size, size
                );

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
                Image qrImage = SwingFXUtils.toFXImage(bufferedImage, null);
                qrCodeImageView.setImage(qrImage); // Set the QR image to your ImageView
            } catch (WriterException e) {
                showAlert("Error", "Could not generate QR code: " + e.getMessage());
            }
        }
    }**/




    /**private void generateAndDisplayQRCode() {
        if (currentEvent != null) {
            String qrCodeData = generateQRCodeData(currentEvent); // Generate data for QR code
            try {
                Image qrImage = generateQRCodeImage(qrCodeData, 200, 200);
                qrCodeImageView.setImage(qrImage); // Set the generated image to ImageView
            } catch (WriterException e) {
                showAlert("Error", "Could not generate QR code: " + e.getMessage());
            }
        }
    }

    // Helper method to generate QR code data string
    private String generateQRCodeData(Event event) {
        // Concatenate event details into a string
        return "Event ID: " + event.getIdE() +
                "\nName: " + event.getNameE() +
                "\nDate: " + event.getDateE() +
                "\nDuration: " + event.getDurationE() +
                "\nType: " + event.getTypeE() +
                "\nEntry Fee: " + event.getEntryFeeE() +
                "\nCapacity: " + event.getCapacityE();
    }

    private Image generateQRCodeImage(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
        generateAndDisplayQRCode();
    }

    @FXML
    void onParticipateButtonClicked(ActionEvent event) {
        if (currentEvent != null) {
            try {
                Ticket newTicket = new Ticket();
                // Set ticket details based on the currentEvent and other necessary information
                // ...

                ts.createT(newTicket); // Save the new ticket to the database
                showAlert("Success", "Your ticket has been generated and added to the database.");
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to create ticket: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No event selected for this ticket.");
        }
    }**/

    @FXML
    void returnEF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAF.fxml"));
            qrCodeImageView.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("Error" + e.getMessage());
        }

    }


    @FXML
    void aboutUsButton(ActionEvent event) {

    }

    @FXML
    void artworksButton(ActionEvent event) {

    }

    @FXML
    void auctionButton(ActionEvent event) {

    }

    @FXML
    void courseButton(ActionEvent event) {

    }

    @FXML
    void gotOfOb(ActionEvent event) {

    }

    @FXML
    void homeButton(ActionEvent event) {

    }

    @FXML
    void searchButtonHandler(ActionEvent event) {

    }

    @FXML
    void viewCartButton(ActionEvent event) {

    }

}






