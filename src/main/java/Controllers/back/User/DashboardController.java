package Controllers.back.User;

import Models.Users;
import Services.User.UserService;
import Test.MainFX;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class DashboardController {


    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField status;

    @FXML
    private ScrollPane itemlist; // The ScrollPane in your Dashboard.fxml

    @FXML
    private VBox vboxUserList; // The VBox that will contain your user items

    @FXML
    private ComboBox<String> sortcombo;

    @FXML
    private TextField searchField;
    private UserService us = new UserService();

    private ObservableList<Users> masterList = FXCollections.observableArrayList(); // Master list of all users
    private FilteredList<Users> filteredList; // Filtered list for displaying

    public void initialize() {
        vboxUserList = new VBox(5); // Spacing of 5 between items
        vboxUserList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxUserList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        try {
            List<Users> users = us.read();
            masterList = FXCollections.observableArrayList(users); // Convert to ObservableList
            filteredList = new FilteredList<>(masterList, p -> true); // Wrap with FilteredList
            loadUserItems(filteredList); // Now pass the FilteredList
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            masterList.setAll(us.read());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filteredList = new FilteredList<>(masterList, p -> true);
        sortcombo.setItems(FXCollections.observableArrayList("Role", "Recent Login", "Status"));
        sortcombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> sortUserList(newVal));
        loadUserItems(filteredList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (user.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (user.getEmailAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches email.
                } else if (user.getAccountStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches account status
                } else if (user.getRole().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches role
                }
                return false;
            });
            loadUserItems(filteredList); // Reload the user items with the filtered list
        });

    }

    private void sortUserList(String criterion) {
        Comparator<Users> comparator = null;

        switch (criterion) {
            case "Role":
                comparator = Comparator.comparing(Users::getRole);
                break;
            case "Recent Login":
                comparator = Comparator.comparing(Users::getLastLogin, Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            case "Status":
                comparator = (user1, user2) -> {
                    String status1 = user1.getAccountStatus();
                    String status2 = user2.getAccountStatus();
                    return status1.equals(status2) ? 0 : "Disabled".equals(status1) ? -1 : 1;
                };
                break;
            default:
                return; // If the criterion is unrecognized, do nothing
        }

        if (comparator != null) {
            List<Users> sortedList = masterList.stream().sorted(comparator).collect(Collectors.toList());
            filteredList = new FilteredList<>(FXCollections.observableArrayList(sortedList), p -> true);
            loadUserItems(filteredList);
        }
    }
    public void loadUserItems(FilteredList<Users> users) {
        vboxUserList.getChildren().clear();
        for (Users user : users) {
            if (user.getRole().contains("Admin")) {
                continue;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/item.fxml"));
                Node userItemNode = loader.load();
                UserItemController itemController = loader.getController();
                itemController.setUser(user, this); // Pass 'this' to the item controller ( passing a dash controller to each node)
                vboxUserList.getChildren().add(userItemNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void clearForm() {
        firstname.clear();
        lastname.clear();
        status.clear();
    }

    public void updateDetails(Users user) { // will show the selected item to display
        selectedUserID = user.getUserID();// getting the specific user saved while also getting info to show in the fields
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        status.setText(user.getAccountStatus());
    }


//selected user is needed to know which is user id is getting updated
    private int selectedUserID;

    @FXML
    void updateclicked() {
        try {
            Users userToUpdate = us.readUser(selectedUserID);
            userToUpdate.setFirstName(firstname.getText());
            userToUpdate.setLastName(lastname.getText());
            String accountStatus = status.getText();
            if (!"Active".equals(accountStatus) && !"Disabled".equals(accountStatus)) {
                us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "Account status must be 'Active' or 'Disabled'.");
                return;
            }
            userToUpdate.setAccountStatus(status.getText());
            us.update(userToUpdate);
            us.showAlert(Alert.AlertType.INFORMATION, "User updated", "The  user has been updated successfully.");
            clearForm();
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addclicked(MouseEvent event) {
            us.switchView(MainFX.primaryStage, "/back/Add.fxml");

    }

    @FXML
    void Analytics(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/back/Analytics.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/Art/FronClient.fxml");
    }

    @FXML
    void PDF(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new BaseColor(255, 204, 0)); // Using yellow color for title
                Paragraph title = new Paragraph("List Of Users", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(20);
                title.setSpacingAfter(10);
                document.add(title);

                // Add company info
                Paragraph companyInfo = new Paragraph();
                companyInfo.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK));
                companyInfo.add(new Phrase("Company Name\n"));
                companyInfo.add("Address Line 1\n");
                companyInfo.add("Address Line 2\n");
                companyInfo.add("Tel: +123 456 7890\n");
                companyInfo.add("Email: info@company.com\n");
                companyInfo.add("Date: " + LocalDate.now().toString() + "\n\n");
                document.add(companyInfo);

                // Create and style the table
                PdfPTable table = new PdfPTable(new float[]{1, 3, 3, 3, 3, 2});
                table.setWidthPercentage(100);
                addTableHeader(table, "ID", "First Name", "Last Name", "Email", "Role", "Status");
                addTableContent(table, new UserService().read());
                document.add(table);
                //Remminder to change this to C:\xampp\htdocs\image\xyellow.png
                // Add logo at the bottom center of the page
                String imagePath = "C:\\xampp\\htdocs\\image\\xyellow.png";
                Image image = Image.getInstance(imagePath);
                image.scaleToFit(100, 50); // Adjust scale as needed
                image.setAbsolutePosition((PageSize.A4.getWidth() - image.getScaledWidth()) / 2, document.bottomMargin());
                document.add(image);

                document.close();
                System.out.println("The PDF file has been generated successfully.");
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void addTableHeader(PdfPTable table, String... headers) {
        PdfPCell headerCell;
        for (String header : headers) {
            headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setPadding(8);
            table.addCell(headerCell);
        }
    }

    private void addTableContent(PdfPTable table, List<Users> userList) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
        BaseColor evenRowColor = new BaseColor(230, 240, 250); // Light Blue
        BaseColor oddRowColor = new BaseColor(250, 250, 250); // White
        boolean flip = false;

        for (Users user : userList) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(String.valueOf(user.getUserID()), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getFirstName(), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getLastName(), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getEmailAddress(), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getRole(), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(user.getAccountStatus(), cellFont));
            cell.setBackgroundColor(flip ? evenRowColor : oddRowColor);
            table.addCell(cell);

            flip = !flip; // Flip the row color for next row
        }
    }


}
