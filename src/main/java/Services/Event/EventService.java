package Services.Event;

import Models.Event;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IServiceE<Event> {

    private Connection connection;

    public EventService()
    {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void createE(Event event) throws SQLException {
        String sql = "INSERT INTO event (nameE, dateE, durationE, typeE, entryFeeE, capacityE, Userid) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getNameE());
            statement.setDate(2, Date.valueOf(event.getDateE()));
            statement.setInt(3, event.getDurationE());
            statement.setString(4, event.getTypeE());
            statement.setDouble(5, event.getEntryFeeE());
            statement.setInt(6, event.getCapacityE());
            statement.setInt(7, UserService.currentlyLoggedInUser.getUserID());

            statement.executeUpdate();
        }
    }



    @Override
    public void updateE(Event event) throws SQLException {
        String sql = "UPDATE event SET nameE = ?, dateE = ?, durationE = ? , typeE =? ,entryFeeE =?,capacityE =? WHERE idE = ?";

        PreparedStatement es = connection.prepareStatement(sql);
        es.setInt(7,event.getIdE());
        es.setString(1, event.getNameE());
        es.setDate(2, Date.valueOf(event.getDateE()));
        es.setInt(3, event.getDurationE());
        es.setString(4, event.getTypeE());
        es.setDouble(5,event.getEntryFeeE());
        es.setInt(6,event.getCapacityE());

        es.executeUpdate();
    }

    @Override
    public void deleteE(int idE) throws SQLException
    {
        String req = "DELETE  FROM event WHERE idE=?";
        PreparedStatement pre = connection.prepareStatement(req);

        pre.setInt(1,idE);
        int rowsAffected = pre.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("Event Deleted");
        }
        else
        {
            System.out.println("There's no Course with this ID");
        }

    }

    @Override
    public List<Event> readE() throws SQLException {
        String sql = "SELECT * FROM event";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> eventList = new ArrayList<>();
        while (rs.next()){
            Event e = new Event();
            e.setIdE(rs.getInt("idE"));
            e.setNameE(rs.getString("nameE"));
            e.setDateE(rs.getDate("dateE").toLocalDate());
            e.setDurationE(rs.getInt("durationE"));
            e.setTypeE(rs.getString("typeE"));
            e.setEntryFeeE(rs.getDouble("entryFeeE"));
            e.setCapacityE(rs.getInt("capacityE"));
            e.setUserid(rs.getInt("Userid"));;
            eventList.add(e);
        }
        return eventList;
    }
    @Override
    public List<Event> readEU() throws SQLException {
        String sql = "SELECT * FROM event ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Event> eventList = new ArrayList<>();
        while (rs.next()){
            Event e = new Event();
            e.setIdE(rs.getInt("idE"));
            e.setNameE(rs.getString("nameE"));
            e.setDateE(rs.getDate("dateE").toLocalDate());
            e.setDurationE(rs.getInt("durationE"));
            e.setTypeE(rs.getString("typeE"));
            e.setEntryFeeE(rs.getDouble("entryFeeE"));
            e.setUserid(rs.getInt("Userid"));;
            eventList.add(e);
        }
        return eventList;
    }


    // Method to get an event by its ID
    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM event WHERE idE = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Replace the placeholder code with your actual event object creation logic
                    Event event = new Event(
                            resultSet.getInt("idE"),
                            resultSet.getString("nameE"),
                            resultSet.getDate("dateE").toLocalDate(),
                            resultSet.getInt("durationE"),
                            resultSet.getString("typeE"),
                            resultSet.getDouble("entryFeeE"),
                            resultSet.getInt("capacityE")
                    );
                    return event;
                } else {
                    // Handle the case where no event with the given ID is found
                    return null;
                }
            }
        }
    }
}
