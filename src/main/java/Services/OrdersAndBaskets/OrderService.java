package Services.OrdersAndBaskets;

import Models.Order;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService implements IService<Order> {
    private final Connection connection;

    public OrderService(){connection = MyDatabase.getInstance().getConnection();}

    @Override
    public int create(Order order) throws SQLException {

        System.out.println("Debug: idB being inserted = " + order.getIdB());
        if (!isIdBValid(order.getIdB())) {
            throw new SQLException("Foreign key constraint violation: idB " + order.getIdB() + " does not exist in the basket table.");
        }
        String sql = "INSERT INTO `order` (totalP, dateC, status, idB,Userid) VALUES (?, ?, ?, ? , ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setFloat(1, order.getTotalP());
            pstmt.setString(2, order.getDateC());
            pstmt.setString(3, order.getStatus());
            pstmt.setInt(4, order.getIdB());
            pstmt.setInt(5, UserService.currentlyLoggedInUser.getUserID());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setIdO(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
        return order.getIdO();
    }

    private boolean isIdBValid(int idB) throws SQLException {
        String sql = "SELECT COUNT(*) FROM basket WHERE idB = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idB);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // If the count is not zero, then the idB exists
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    @Override
    public void update(Order order, int idO) throws SQLException {
        String sql = "UPDATE `order` SET totalP=?, dateC=?, idB=? WHERE idO=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, order.getTotalP());
            ps.setString(2, order.getDateC());
            ps.setInt(3, order.getIdB());
            ps.setInt(4, idO);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int idO) throws SQLException {
        String sql = "DELETE FROM `order` WHERE idO=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idO);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting order failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Order> read() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.idO, o.totalP, o.dateC, o.status, b.idB " +
                "FROM `order` o " +
                "JOIN basket b ON o.idB = b.idB";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int idO = rs.getInt("idO");
                float totalP = rs.getFloat("totalP");
                String dateC = rs.getString("dateC");
                int idB = rs.getInt("idB");


                Order order = new Order(idO,totalP, dateC, idB);
                // Assuming you have a method to set basket details in Order class
                orders.add(order);
            }
        }
        return orders;
    }

    public Order getOrder(int idO) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE idO = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idO); // Set the idO parameter in the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract the order details from the ResultSet
                    float totalP = rs.getFloat("totalP");
                    String dateC = rs.getString("dateC");
                    String status = rs.getString("status");
                    int idB = rs.getInt("idB");

                    // Create and return a new Order object with the extracted details
                    return new Order(totalP, dateC, idB);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to allow the caller to handle it
        }
        return null; // Return null if no order is found with the specified ID
    }
    public List<Order> getAllOrders() throws SQLException {
        List<Order> ordersList = new ArrayList<>();
        String req = "SELECT * FROM `order`"; // Make sure the table name is correct
        try (PreparedStatement ps = connection.prepareStatement(req); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Assuming column names in your database are idO, totalP, dateC, status, idB
                // Adjust column names as per your database schema
                Order order = new Order(

                        rs.getFloat("totalP"), // Make sure this matches the actual column name in your DB
                        rs.getString("dateC"), // Make sure this matches the actual column name in your DB
                        rs.getInt("idB")
                );

                // No need to set totalP and dateC again unless they're different from what's used in constructor
                // If they're different, you'll need to correct either the constructor call or the repeated setting here

                ordersList.add(order);
            }
        } // try-with-resources will auto close ps and rs even if an exception is thrown
        return ordersList;
    }
    public int getNumberOfOrdersByUser(int userId) {
        String query = "SELECT COUNT(*) AS orderCount FROM `order` WHERE Userid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("orderCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}