package Services.Event;

import Models.Ticket;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements IServiceT<Ticket> {

    private Connection connection;

    public TicketService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void createT(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (idU, idE, qrCodeT) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ticket.getIdU()); // Make sure this method exists and returns the correct value
            statement.setInt(2, ticket.getIdE());
            statement.setString(3, ticket.getQrCodeT());
            statement.executeUpdate();
        }
    }







    public Ticket getTicketByEventId(int idE) throws SQLException {
        String sql = "SELECT * FROM tickets WHERE idE = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idE);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Assuming the Ticket constructor takes QR code and Event ID
                    String qrCode = resultSet.getString("qrCode");
                    int eventID = resultSet.getInt("idE");

                    // Create and return a new Ticket object
                    return new Ticket(qrCode, eventID, 1);
                } else {
                    // No ticket found for the given Event ID
                    return null;
                }
            }
        }
    }




    // Method to delete a ticket by its ID
    @Override
    public void deleteT(int idT) throws SQLException {
        String sql = "DELETE FROM ticket WHERE idT = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idT);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Ticket deleted successfully");
            } else {
                System.out.println("No Ticket with this ID found");
            }
        }
    }

    @Override
    public List<Ticket> readT() throws SQLException {
        String sql = "SELECT ticket.idT, ticket.idE, ticket.qrCodeT FROM ticket JOIN event ON ticket.idE = event.idE";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            List<Ticket> ticketList = new ArrayList<>();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setIdT(rs.getInt("idT"));
                t.setQrCodeT(rs.getString("qrCodeT"));
                t.setIdE(rs.getInt("idE"));
                ticketList.add(t);
            }
            return ticketList;
        }
    }




}
