package Services.AuctionSystem;

import Models.Auction;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistService implements IService<Auction> {
    private Connection connection;

    public ArtistService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Auction auction) throws SQLException {
        int currentid = UserService.currentlyLoggedInUser.getUserID();
        String sql = "INSERT INTO auction (Auctionname, price, time, date,Userid) VALUES (?, ?, ?, ?, ?) " ;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setString(3, auction.getTime());
        statement.setString(4, auction.getDate());
        statement.setString(5, String.valueOf(currentid));

        statement.executeUpdate();
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
        int currentid = UserService.currentlyLoggedInUser.getUserID();
        String sql = "SELECT * FROM auction WHERE Userid= "+currentid;
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

