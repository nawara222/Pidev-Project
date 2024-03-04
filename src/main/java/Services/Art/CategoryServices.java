package Services.Art;

import Models.category;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServices implements IservicesC <category> {

    private Connection con;

    public CategoryServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void addC(category c) throws SQLException {
        String req = "INSERT INTO category (name, date,Userid) VALUES (?, ?, ? )";

        // Using try-with-resources to ensure proper resource management
        try (PreparedStatement pstmt = con.prepareStatement(req)) {
            // Setting the parameters for the prepared statement
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getDate());
            pstmt.setInt(3, UserService.currentlyLoggedInUser.getUserID());
            // Executing the update operation
            pstmt.executeUpdate();

        }
    }

    @Override
    public void modifyC(category c, int id_category) throws SQLException {
        String req = "UPDATE category SET name=?, date=? WHERE id_category=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, c.getName());
        pre.setString(2, c.getDate());
        pre.setInt(3, id_category); // Set the value for the third parameter

        pre.executeUpdate();
    }


    @Override
    public void deleteC(int id_category) throws SQLException {
        String req = "delete  from category where id_category=?";
        PreparedStatement pre = con.prepareStatement(req);

        pre.setInt(1, id_category);
        int rowsAffected = pre.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("category deleted with success.");
        } else {
            System.out.println("Attention !! no one have this  id .");
        }

    }

    @Override
    public List<category> displayC() throws SQLException {
        List<category> categories = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM category");

        while (res.next()) {
            category c = new category();
            c.setId_category(res.getInt(1));
            c.setName(res.getString(2));
            c.setDate(res.getString(3));
            categories.add(c);
        }
        return categories;
    }

    @Override
    public String getCategoryName(int idCategory) throws SQLException {
        String categoryName = null;
        String req = "SELECT name FROM category WHERE id_category = ?";
        PreparedStatement preparedStatement = con.prepareStatement(req);
        //Sets the value of the first parameter in the SQL query to the idCategory parameter.
        preparedStatement.setInt(1, idCategory);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            categoryName = resultSet.getString("name");
        }
        return categoryName;
    }


}
