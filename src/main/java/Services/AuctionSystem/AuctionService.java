package Services.AuctionSystem;

import Models.Auction;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionService implements IService<Auction> {
    private Connection connection;

    public AuctionService() {
        connection = MyDatabase.getInstance().getConnection();}
    public List<Integer> getLastBidPrices() throws SQLException {
        String sql = "SELECT idAuction, MAX(bidamount) as last_bid_price FROM bid GROUP BY idAuction";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Integer> lastBidPrices = new ArrayList<>();

        while (rs.next()) {
            lastBidPrices.add(rs.getInt("last_bid_price"));
        }

        rs.close();
        statement.close();
        return lastBidPrices;
    }
    public Auction getAuctionById(int auctionId) throws SQLException {
        Auction auction = null;
        String sql = "SELECT * FROM auction WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, auctionId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            auction = new Auction(
                    resultSet.getInt("id"),
                    resultSet.getInt("price"),
                    resultSet.getInt("userid"),
                    resultSet.getString("time"),
                    resultSet.getString("date"),
                    resultSet.getString("Auctionname"),
                    resultSet.getString("imgpath"),
                    resultSet.getString("description")
            );
        }

        resultSet.close();
        statement.close();

        return auction;
    }
    @Override
    public void create(Auction auction) throws SQLException {
        String sql = "INSERT INTO auction (Auctionname, price, time, date, Userid, imgpath, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setString(3, auction.getTime());
        statement.setString(4, auction.getDate());
        statement.setInt(5, UserService.currentlyLoggedInUser.getUserID());
        statement.setString(6, auction.getImgpath());
        statement.setString(7, auction.getDescription());

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Creating auction failed, no rows affected.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                auction.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating auction failed, no ID obtained.");
            }
        }

        statement.close();
    }
    @Override
    public void update(Auction auction) throws SQLException {
        String sql = "UPDATE auction SET Auctionname = ?, price = ?, time = ?, date = ?, imgpath = ?, description = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setString(3, auction.getTime());
        statement.setString(4, auction.getDate());
        statement.setString(5, auction.getImgpath());
        statement.setString(6, auction.getDescription());
        statement.setInt(7, auction.getId());

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Auction updated successfully");
        } else {
            System.err.println("Auction update failed");
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM auction WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Auction deleted successfully (ID: " + id + ")");
        } else {
            System.err.println("Auction deletion failed for ID: " + id);
        }
    }

    @Override
    public List<Auction> read() throws SQLException {
        String sql = "SELECT * FROM auction";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Auction> auctions = new ArrayList<>();

        while (rs.next()) {
            Auction auction = new Auction();
            auction.setId(rs.getInt("id"));
            auction.setPrice(rs.getInt("price"));
            auction.setTime(rs.getString("time"));
            auction.setDate(rs.getString("date"));
            auction.setAuctionname(rs.getString("Auctionname"));
            auction.setImgpath(rs.getString("imgpath"));
            auction.setDescription(rs.getString("description"));
            auctions.add(auction);
        }


        rs.close();
        statement.close();
        return auctions;
    }



    }

