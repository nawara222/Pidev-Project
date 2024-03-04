package Controllers.Tickets;

import Controllers.Event.ShowEventsController;
import Models.Event;
import Models.Ticket;
import Services.Event.EventService;
import Services.Event.TicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowTicketsController
{
    private final TicketService ts = new TicketService();
    private ObservableList<Ticket> ol;
    List<Ticket> ticketList;
    List<Ticket> ticketList1;



    @FXML
    private VBox chosenFruitCard;

    @FXML
    private TableView<Ticket> tickettable;


    @FXML
    private TableColumn<Ticket,Float> useridcol;


    @FXML
    private ImageView fruitImg;

    /**@FXML
    private TableColumn<Ticket,Integer > eventnamecol;**/


    @FXML
    private TableColumn<Ticket,String> qrcodecol;

    @FXML
    private TableColumn<Ticket,String> eventidcol;

    @FXML
    private Label eventname;



    @FXML
    void initialize() {
        try {
            eventname.setText(String.valueOf(ShowEventsController.nameE));
            ticketList = ts.readT();
            ol = FXCollections.observableArrayList();

            for (Ticket ticket : ticketList) {
                if (ticket.getIdE() == ShowEventsController.idE) {
                    ol.add(ticket);
                }
            }

            tickettable.setItems(ol);
            useridcol.setCellValueFactory(new PropertyValueFactory<>("IdU"));
            /**eventnamecol.setCellValueFactory(param -> {
                Ticket selectedTicket = (Ticket) param.getValue();
                try {
                    return new SimpleStringProperty(getNameE(selectedTicket.getIdE()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });**/
            qrcodecol.setCellValueFactory(new PropertyValueFactory<>("qrCodeT"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    public String getNameE(int eventId) throws SQLException {
        // Assuming there is a method to get an Event by ID
        EventService eventService = new EventService();
        Event event = eventService.getEventById(eventId);

        if (event != null) {
            return event.getNameE();
        } else {
            // Handle the case where the event is not found
            return "Event Not Found";
        }
    }







    @FXML
    void click(MouseEvent event)
    {
        if (event.getButton() == MouseButton.PRIMARY)
        {
            Ticket selectedticket = tickettable.getSelectionModel().getSelectedItem();
            if (selectedticket != null)
            {
                //eventnamecol.setText(selectedticket.getNameE());
                eventidcol.setText(String.valueOf(selectedticket.getIdU()));
                qrcodecol.setText(selectedticket.getQrCodeT());
                useridcol.setText(String.valueOf(selectedticket.getIdU()));
            }
        }
    }

    @FXML
    void homeButton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            eventname.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }

    }



    @FXML
    void removet(ActionEvent event)
    {
        Ticket selectedticket = tickettable.getSelectionModel().getSelectedItem();
        if (selectedticket != null)
        {
            try
            {
                int idT = selectedticket.getIdT();
                ts.deleteT(idT);
                ol.remove(selectedticket);


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Ticket deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            tickettable.refresh();
        }
    }

    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsAdmin.fxml"));
            eventname.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

}
