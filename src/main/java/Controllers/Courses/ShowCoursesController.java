package Controllers.Courses;

import Models.Courses;
import Services.CoursesandWorkshops.CoursesService;
import Services.CoursesandWorkshops.WorkshopService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.input.KeyEvent;
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
import java.util.Comparator;
import java.util.List;


public class ShowCoursesController {
    private final CoursesService cs = new CoursesService();
    private ObservableList<Courses> ol;
    public StackPane mainStackPane;
    public static int idc;
    public static int nbwc;
    public static String cname;
    private final WorkshopService ws = new WorkshopService();
    private int c;
    @FXML
    private TextField searchforname;
    @FXML
    private VBox chosenFruitCard;

    @FXML
    void sortdesc(ActionEvent event)
    {
        ObservableList<Courses> items = coursetable.getItems();

        items.sort(Comparator.comparing(Courses::getPriceC, Comparator.reverseOrder()));

        coursetable.setItems(items);
    }

    @FXML
    void stat(ActionEvent event)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/stat.fxml"));
            typefu.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void sortasc(ActionEvent event)
    {
        ObservableList<Courses> items = coursetable.getItems();

        items.sort(Comparator.comparing(Courses::getPriceC));

        coursetable.setItems(items);

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
                Paragraph title = new Paragraph("List Of Courses", titleFont);
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
                table.setWidths(new float[]{1, 2, 2, 1, 2, 2, 4});
                table.setHeaderRows(1);
                table.getDefaultCell().setBackgroundColor(new BaseColor(119, 141, 169, 100)); // Blue

                // Add table headers
                addTableHeader(table, "id", "Name", "Description", "Price", "Type", "NB Of Workshops", "Image Path");

                // Add table rows
                List<Courses> courseslist = cs.read();
                addRows(table, courseslist);

                document.add(table);

                String signaturePath ="C:\\xampp\\htdocs\\image\\signature.png";
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

                System.out.println("The PDF file has been generated successfully.");

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour ajouter les lignes de produits à la table
    private void addRows(PdfPTable table, List<Courses> coursesList)
    {
       //int id=0;
        for (Courses courses : coursesList)
        {
            table.addCell(String.valueOf(courses.getId_C()));
            table.addCell(courses.getNameC());
            table.addCell(courses.getDescriptionC());
            table.addCell(String.valueOf(courses.getPriceC()));
            table.addCell(courses.getType());
            table.addCell(String.valueOf(courses.getNumberW()));
            table.addCell(courses.getImage_path());
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

    @FXML
    private ImageView fruitImg;

    @FXML
    private TableView<Courses> coursetable;

    @FXML
    private TableColumn<Courses, String> desccol;

    @FXML
    private TextField descfu;

    @FXML
    private Button button;

    @FXML
    private TableColumn<Courses, String> namecol;

    @FXML
    private TextField namefu;

    @FXML
    private TableColumn<Courses, Integer> nbwcol;


    @FXML
    private TableColumn<Courses, Float> pricecol;

    @FXML
    private TextField pricefu;

    @FXML
    private TableColumn<Courses, String> typecol;

    @FXML
    private TableColumn<Courses, String> imagepathcol;

    @FXML
    private TextField typefu;

    public static int nbw;
    @FXML
    void initialize()
    {
        try
        {
            List<Courses> coursesList = cs.read();
            ol = FXCollections.observableList(coursesList);
            coursetable.setItems(ol);
            namecol.setCellValueFactory(new PropertyValueFactory<>("nameC"));
            desccol.setCellValueFactory(new PropertyValueFactory<>("descriptionC"));
            pricecol.setCellValueFactory(new PropertyValueFactory<>("priceC"));
            typecol.setCellValueFactory(new PropertyValueFactory<>("type"));
            imagepathcol.setCellValueFactory((new PropertyValueFactory<>("image_path")));
            nbwcol.setCellValueFactory(cellData -> {
                int courseId = cellData.getValue().getId_C();
                try {
                    int workshopCount = ws.getWorkshopCountForCourse(courseId);
                    return new SimpleIntegerProperty(workshopCount).asObject();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new SimpleIntegerProperty(0).asObject();
                }
            });
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    public String getname(Courses c)
    {
        cname=c.getNameC();
        return cname;
    }
    public int getcnbw(Courses c)
    {
        nbwc=c.getNumberW();
        return nbwc;
    }

    @FXML
    void listworkshops(ActionEvent event) throws IOException
    {
        Courses selectedCourse = coursetable.getSelectionModel().getSelectedItem();
        idc=getcid(selectedCourse);
        nbwc=getcnbw(selectedCourse);
        cname= String.valueOf(getname(selectedCourse));
        if(selectedCourse !=null)
        {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Workshops/showWorkshops.fxml"));
                typefu.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void addCourse(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/addCourse.fxml"));
            typefu.getScene().setRoot(root);
        }
        catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        try {
            Courses selectedCourse = coursetable.getSelectionModel().getSelectedItem();
            int nbw=selectedCourse.getNumberW();
            if (selectedCourse != null) {
                Courses courses = new Courses();
                courses.setId_C(selectedCourse.getId_C());
                courses.setNameC(namefu.getText());
                courses.setDescriptionC(descfu.getText());
                courses.setPriceC(Float.parseFloat(pricefu.getText()));
                courses.setType(typefu.getText());
                courses.setNumberW(nbw);
                // courses.setImage_path();

                cs.update(courses);


                for (Courses item : ol) {
                    if (item.getId_C() == courses.getId_C()) {
                        item.setNameC(courses.getNameC());
                        item.setDescriptionC(courses.getDescriptionC());
                        item.setPriceC(courses.getPriceC());
                        item.setType(courses.getType());
                        item.setNumberW(nbw);

                        break;
                    }
                }

                refreshTableView();
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


    public void refreshTableView()
    {
        try
        {
            ObservableList<Courses> observableList = FXCollections.observableList(cs.read());
            coursetable.setItems(observableList);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void remove(ActionEvent event) throws SQLException
    {
        Courses selectedCourse = coursetable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null)
        {
            try
            {
                int id_C = selectedCourse.getId_C();
                cs.delete(id_C);
                ol.remove(selectedCourse);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course deleted successfully ");
                alert.showAndWait();
            }
            catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            refreshTableView();
            resetFields();
        }
    }
    private void resetFields()
    {
        namefu.clear();
        descfu.clear();
        pricefu.clear();
        typefu.clear();
    }

    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/frontOrBack.fxml"));
            pricefu.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }
    @FXML
    void searchcouseF(KeyEvent event)
    {
        String searchQuery = searchforname.getText(); // Replace searchTextField with the actual name of your TextField
        // Call the searchProducts method in your ConseilService
        List<Courses> matchingConseils = cs.searchCouses(searchQuery);

        // Update the UI with the matching Conseils
        ol.clear(); // Clear the current data
        ol.addAll(matchingConseils); // Add the matching Conseils to the observable list
        coursetable.setItems(ol); // Set the items in the TableView
    }





    @FXML
    void click(MouseEvent event)
    {
        if (event.getButton() == MouseButton.PRIMARY)
        {
            Courses selectedCourse = coursetable.getSelectionModel().getSelectedItem();
            int c=getcid(selectedCourse);
            if (selectedCourse != null)
            {
                namefu.setText(selectedCourse.getNameC());
                descfu.setText(selectedCourse.getDescriptionC());
                pricefu.setText(String.valueOf(selectedCourse.getPriceC()));
                typefu.setText(selectedCourse.getType());
            }
        }
    }
    public int getcid(Courses c)
    {
        idc=c.getId_C();
        return idc;
    }

}