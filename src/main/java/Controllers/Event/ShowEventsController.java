package Controllers.Event;

import Models.Event;
import Services.Event.EventService;
import Services.Event.TicketService;
import Services.User.UserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowEventsController {
    private final EventService es = new EventService();
    private ObservableList<Event> ol;
    public StackPane mainStackPane;
    public static int idE;
    public static String nameE;
    public static LocalDate dateE;

    public static int durationE;

    public String typeE;

    public double entryFeeE;

    public int capacityE;

    private final TicketService ts = new TicketService();
    private int c;
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private ImageView fruitImg;

    @FXML
    private TableView<Event> Eventtable;

    @FXML
    private TableColumn<Event, String> namecol;

    @FXML
    private TextField nameEfu;

    @FXML
    private Button searchB;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private TextField searchTF;

   /*
   @FXML
   private Label lidfu;

    */

    @FXML
    private TableColumn<Event, String> datecol;

    @FXML
    private TextField dateEfu;

    @FXML
    private TableColumn<Event, Integer> durationcol;

    @FXML
    private TextField durationEfu;


    @FXML
    private TableColumn<Event, Float> typecol;

    @FXML
    private TextField typeEfu;

    @FXML
    private TableColumn<Event, String> entryfeecol;

    @FXML
    private TextField entryFeeEfu;

    @FXML
    private TableColumn<Event, String> capacitycol;

    @FXML
    private TextField capacityEfu;

    @FXML
    void initialize() {
        try {
            List<Event> EventList = es.readE();
            ol = FXCollections.observableList(EventList);
            Eventtable.setItems(ol);
            namecol.setCellValueFactory(new PropertyValueFactory<>("nameE"));
            datecol.setCellValueFactory(new PropertyValueFactory<>("dateE"));
            durationcol.setCellValueFactory(new PropertyValueFactory<>("durationE"));
            typecol.setCellValueFactory(new PropertyValueFactory<>("typeE"));
            entryfeecol.setCellValueFactory(new PropertyValueFactory<>("entryFeeE"));
            capacitycol.setCellValueFactory(new PropertyValueFactory<>("capacityE"));

            // Add listener to the search field
            searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.trim().isEmpty()) {
                    refreshTableView();
                } else {
                    performSearch(newValue.trim().toLowerCase());
                }
            });

        } catch (SQLException e) {
            // Log the error and display an alert instead of throwing an exception.
            e.printStackTrace(); // For debugging, print stack trace.
            showAlert("Database Error", "An error occurred while loading events: " + e.getMessage());
        }
    }

    public int getEid(Event e) {
        idE = e.getIdE();
        return idE;
    }

    public String getname(Event e) {
        nameE = e.getNameE();
        return nameE;
    }

    public LocalDate getdateE(Event e) {
        dateE = e.getDateE();
        return dateE;
    }

    public int getdurationE(Event e) {
        durationE = e.getDurationE();
        return durationE;
    }

    public String gettypeE(Event e) {
        typeE = e.getTypeE();
        return typeE;
    }

    public double getentryfeeE(Event e) {
        entryFeeE = e.getEntryFeeE();
        return entryFeeE;
    }

    public int getcapacityE(Event e) {
        capacityE = e.getCapacityE();
        return capacityE;
    }
    public int Userid(Event e) {
        int Userid = UserService.currentlyLoggedInUser.getUserID();
        return Userid;
    }

    @FXML
    void searchButtonHandler(ActionEvent event) {
        String searchInput = searchTF.getText().trim().toLowerCase();
        if (searchInput.isEmpty()) {
            refreshTableView();
        } else {
            performSearch(searchInput);
        }
    }

    private void performSearch(String searchInput) {
        try {
            List<Event> allEvents = es.readE(); // Assuming readE() returns all events
            List<Event> searchResults = allEvents.stream()
                    .filter(e -> e.getNameE().toLowerCase().contains(searchInput) ||
                            e.getTypeE().toLowerCase().contains(searchInput))
                    .collect(Collectors.toList());

            if (searchResults.isEmpty()) {
                showAlert("Search Results", "No matching events found.");
            } else {
                ol = FXCollections.observableList(searchResults); // Assuming 'ol' is your ObservableList for TableView
                Eventtable.setItems(ol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not perform the search: " + e.getMessage());
        }
    }


    @FXML
    void listTickets(ActionEvent event) throws IOException {
        Event selectedEvent = Eventtable.getSelectionModel().getSelectedItem();
        idE = getEid(selectedEvent);
        nameE = getname(selectedEvent);
        dateE = getdateE(selectedEvent);
        durationE = getdurationE(selectedEvent);
        typeE = gettypeE(selectedEvent);
        entryFeeE = getentryfeeE(selectedEvent);
        capacityE = getcapacityE(selectedEvent);
        if (selectedEvent != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Tickets/ShowTickets.fxml"));
                typeEfu.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void addEvent(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/AddEvent.fxml"));
            typeEfu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        try {
            Event selectedEvent = Eventtable.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                Event eventt = new Event();
                eventt.setIdE(selectedEvent.getIdE());
                eventt.setNameE(nameEfu.getText());
                eventt.setDateE(LocalDate.parse(dateEfu.getText()));
                eventt.setDurationE(Integer.parseInt(durationEfu.getText()));
                eventt.setTypeE(typeEfu.getText());
                eventt.setEntryFeeE(Double.parseDouble(entryFeeEfu.getText()));
                eventt.setCapacityE(Integer.parseInt(capacityEfu.getText()));

                es.updateE(eventt);


                for (Event item : ol) {
                    if (item.getIdE() == eventt.getIdE()) {
                        item.setNameE(eventt.getNameE());
                        item.setDateE(eventt.getDateE());
                        item.setDurationE(eventt.getDurationE());
                        item.setTypeE(eventt.getTypeE());
                        item.setEntryFeeE(eventt.getEntryFeeE());
                        item.setCapacityE(eventt.getCapacityE());
                        break;
                    }
                }

                Eventtable.refresh();
                resetFields();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course updated successfully ");
                alert.showAndWait();
            }
        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void refreshTableView() {
        try {
            ObservableList<Event> observableList = FXCollections.observableList(es.readE());
            Eventtable.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void remove(ActionEvent event) throws SQLException {
        Event selectedEvent = Eventtable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                int idE = selectedEvent.getIdE();
                es.deleteE(idE);
                ol.remove(selectedEvent);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Event deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            refreshTableView();
            resetFields();
        }
    }

    private void resetFields() {

        nameEfu.clear();
        dateEfu.clear();
        durationEfu.clear();
        typeEfu.clear();
        entryFeeEfu.clear();
        capacityEfu.clear();
    }

    @FXML
    void returna(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/FrontOrBack.fxml"));
            entryFeeEfu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Event selectedEvent = Eventtable.getSelectionModel().getSelectedItem();
            int e = getEid(selectedEvent);
            if (selectedEvent != null) {
                nameEfu.setText(selectedEvent.getNameE());
                dateEfu.setText(String.valueOf(selectedEvent.getDateE()));
                durationEfu.setText(String.valueOf(selectedEvent.getDurationE()));
                typeEfu.setText(selectedEvent.getTypeE());
                entryFeeEfu.setText(String.valueOf(selectedEvent.getEntryFeeE()));
                capacityEfu.setText(String.valueOf(selectedEvent.getCapacityE()));
            }
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void onSortOrderChanged(ActionEvent event) {
        String selectedSortOrder = sortComboBox.getSelectionModel().getSelectedItem();
        if (selectedSortOrder != null) {
            switch (selectedSortOrder) {
                case "Sort by Date":
                    sortByDate();
                    break;
                case "Sort by Entry Fee":
                    sortByEntryFee();
                    break;
                case "Sort by Capacity":
                    sortByCapacity();
                    break;
            }
        }
    }

    private void sortByDate() {
        ol.sort(Comparator.comparing(Event::getDateE));
        Eventtable.refresh();
    }

    private void sortByEntryFee() {
        ol.sort(Comparator.comparingDouble(Event::getEntryFeeE));
        Eventtable.refresh();
    }

    private void sortByCapacity() {
        ol.sort(Comparator.comparingInt(Event::getCapacityE));
        Eventtable.refresh();
    }

    @FXML
    void print(ActionEvent event) throws SQLException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null)
        {
            try
            {
                // Create the document PDF
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Set background color
                PdfContentByte canvas = writer.getDirectContentUnder();
                canvas.setColorFill(new BaseColor(0, 0, 0, 30)); // Soft Yellow
                canvas.roundRectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight(), 10);
                canvas.fill();

                // Set font for the title
                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 36, Font.BOLD);
                BaseColor titleColor = new BaseColor(0x04, 0x1E, 0x32); // Dark Blue
                titleFont.setColor(titleColor);

                // Add title
                Paragraph title = new Paragraph("List Of Events", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(20);
                title.setSpacingAfter(20);
                document.add(title);

                // Add logo
                String imagePath = "C:\\xampp\\htdocs\\image\\logo.png";
                Image image = Image.getInstance(imagePath);
                image.setAbsolutePosition(10, document.getPageSize().getHeight() - 80);
                image.scaleToFit(100, 100);
                document.add(image);

                // Add company info
                Paragraph companyInfo = new Paragraph();
                companyInfo.add(new Chunk("Company Vinci\n", FontFactory.getFont(FontFactory.TIMES_BOLD, 16)));
                companyInfo.add("Pole Technologie , Ghazela\n");
                companyInfo.add("Ariana,Tunisie\n");
                companyInfo.add("Tél : +70 800 000\n");
                companyInfo.add("Email : vinci@gmail.con\n");
                companyInfo.add("Date : " + LocalDate.now().toString() + "\n \n \n");
                document.add(companyInfo);

                // Create and style the table
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1, 3, 2, 2, 3, 2, 2});
                table.setHeaderRows(1);
                table.getDefaultCell().setBackgroundColor(new BaseColor(119, 141, 169, 100)); // Blue

                // Add table headers
                addTableHeader(table, "ID", "Name Event", "Date Event", "Duration Event", "Type Event", "Event Entry Fee", "Event Capacity");

                // Add table rows
                List<Event> eventList = es.readE();
                addRows(table, eventList);

                document.add(table);

                String signaturePath = "C:\\xampp\\htdocs\\image\\signature.png";
                Image image1 = Image.getInstance(signaturePath);
                image1.setAbsolutePosition(500, document.getPageSize().getHeight() - 720);
                image1.scaleToFit(70, 70);
                document.add(image1);

                String handsignaturePath = "C:\\xampp\\htdocs\\image\\handsignature.png";
                Image image2 = Image.getInstance(handsignaturePath);
                image2.setAbsolutePosition(500, document.getPageSize().getHeight() - 790);
                image2.scaleToFit(70, 70);
                document.add(image2);

                document.close();
                writer.close();

                System.out.println("The PDF File Has Been Generated Successfully.");

                // Show an information alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Creation");
                alert.setHeaderText(null);
                alert.setContentText("The PDF File Has Been Download Successfully.");

                alert.showAndWait();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour ajouter les lignes de produits à la table
    private void addRows(PdfPTable table, List<Event> eventList)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //int id=0;
        for (Event event : eventList)
        {
            table.addCell(String.valueOf(event.getIdE()));
            table.addCell(event.getNameE());
            table.addCell(event.getDateE().format(formatter));
            table.addCell(String.valueOf(event.getDurationE()));
            table.addCell(event.getTypeE());
            table.addCell(String.valueOf(event.getEntryFeeE()));
            table.addCell(String.valueOf(event.getCapacityE()));
        }
    }

    private void addTableHeader(PdfPTable table, String... headers)
    {
        for (String header : headers)
        {
            PdfPCell cell = new PdfPCell();
            cell.setPadding(5);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }
}