package Controllers.Tickets;

import Controllers.Event.EventItemController;
import Models.Event;
import Models.Ticket;
import Services.Event.EventService;
import Services.Event.TicketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowTicketsFController
{
    private TicketService ts = new TicketService();
    private EventService es = new EventService();


    @FXML
    private TextField searchfT;

    @FXML
    private GridPane TicketsGrid;

    @FXML
    private ScrollPane TicketsItems;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    void initialize()
    {
        refreshTicketsList();

        TicketsGrid.setHgap(-20); // Horizontal gap between items
        TicketsGrid.setVgap(-20); // Vertical gap between items

        TicketsItems.setContent(TicketsGrid);
        TicketsItems.setFitToWidth(true);

        // Set minimum height for each row
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Set the desired height
        TicketsGrid.getRowConstraints().add(rowConstraints);
        try {
            List<Ticket> ticketList = ts.readT();
            loadTicketItems(ticketList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTicketsList() {
        try {
            List<Ticket> ticketList = ts.readT();
            loadTicketItems(ticketList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void loadTicketItems(List<Ticket> tickets) {
        TicketsGrid.getChildren().clear();
        int columnCount = 0;
        int rowCount = 0;

        for (Ticket ticket : tickets) {
            if (ticket.getIdE() == EventItemController.idE) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tickets/TicketItem.fxml"));
                    Node ticketNode = loader.load();
                    TicketItemController itemController = loader.getController();

                    // Assuming you have a method in TicketItemController to set the event data

                    Event event = es.getEventById(ticket.getIdE()); // Fetch event data from the database
                    if (event != null) {
                        itemController.setCurrentEvent(event);
                    }

                    TicketsGrid.add(ticketNode, columnCount, rowCount);
                    columnCount++;
                    if (columnCount == 4) {
                        columnCount = 0;
                        rowCount++;
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exception as needed
                }
            }
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
    void effect(MouseEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event)
    {

    }

    @FXML
    void gototEvents(MouseEvent event) {

    }


    @FXML
    void homeButton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            TicketsItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }

    }

    @FXML
    void searchButtonT(ActionEvent event)
    {


    }

    @FXML
    void viewCartButtonT(ActionEvent event) {

    }

    @FXML
    void returnEF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/Events/ShowEventsF.fxml"));
            TicketsGrid.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("Error" + e.getMessage());
        }

    }

}
