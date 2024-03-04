package Services.Art;

import Models.art;
import Models.category;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.*;

public class ArtServices implements IServices <art> {
    private Connection con;
    public ArtServices(){ con = MyDatabase.getInstance().getConnection();}

    @Override
    public void add(art a) throws SQLException {
        // SQL query to insert art data into the database
        String req = "INSERT INTO art ( title, materials, height, width, type, city, description,price, id_category, path_image, video, Userid) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?)";
        //analyse->comuling-->optimizing-->executing
        // Using try-with-resources to ensure proper resource management
        try (PreparedStatement pstmt = con.prepareStatement(req)) {
            // Setting the parameters for the prepared statement
            pstmt.setString(1, a.getTitle());
            pstmt.setString(2, a.getMaterials());
            pstmt.setDouble(3, a.getHeight()); // Corrected index from 4 to 3
            pstmt.setDouble(4, a.getWidth());
            pstmt.setString(5, a.getType());
            pstmt.setString(6, a.getCity());
            pstmt.setString(7, a.getDescription());
            pstmt.setFloat(8, a.getPrice());
            pstmt.setInt(9, a.getId_category());
            pstmt.setString(10, a.getPath_image());
            pstmt.setString(11, a.getVideo());
            pstmt.setInt(12, UserService.currentlyLoggedInUser.getUserID());
            // Executing the update operation
            pstmt.executeUpdate();

        }}

    @Override
    public void modify(art newart, int id_art) throws SQLException {
        String req = "UPDATE art SET title=?, materials=?, height=?, width=?, type=?, city=?, description=? ,price=? ,id_category=?,path_image=?,video=? WHERE id_art=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, newart.getTitle());
        pre.setString(2, newart.getMaterials());
        pre.setDouble(3, newart.getHeight());
        pre.setDouble(4, newart.getWidth());
        pre.setString(5, newart.getType());
        pre.setString(6, newart.getCity());
        pre.setString(7, newart.getDescription());
        pre.setFloat(8, newart.getPrice());
        pre.setInt(9, newart.getId_category());
        pre.setString(10, newart.getPath_image());
        pre.setString(11, newart.getVideo());
        pre.setInt(12, id_art);

        pre.executeUpdate();
    }


    public void delete(int id_art)throws SQLException {

            String req = "delete  from art where id_art=?";
            PreparedStatement pre = con.prepareStatement(req);

            pre.setInt(1,id_art);
            int rowsAffected = pre.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("art deleted with success.");
            } else {
                System.out.println("Attention !! no one have this  id .");
            }
        }



    @Override
    public List<art> display() throws SQLException {
        List<art> arts = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM art");

        while (res.next()){
            art a = new art();
            a.setId_art(res.getInt(1));
            a.setTitle(res.getString(2));
            a.setMaterials(res.getString(3));
            a.setHeight(res.getDouble(4));
            a.setWidth(res.getDouble(5));
            a.setType(res.getString(6));
            a.setCity(res.getString(7));
            a.setDescription(res.getString(8));
            a.setPrice(res.getFloat(9));
            a.setId_category(res.getInt(10));
            a.setPath_image(res.getString(11));
            a.setVideo(res.getString(13));
            arts.add(a);
        }

        return arts;
    }
    @Override
    public List<art> displayArtist() throws SQLException {
        List<art> arts = new ArrayList<>();

        Statement stmt = con.createStatement();
        int currentid = UserService.currentlyLoggedInUser.getUserID();
        ResultSet res = stmt.executeQuery("SELECT * FROM art where Userid ="+currentid);

        while (res.next()){
            art a = new art();
            a.setId_art(res.getInt(1));
            a.setTitle(res.getString(2));
            a.setMaterials(res.getString(3));
            a.setHeight(res.getDouble(4));
            a.setWidth(res.getDouble(5));
            a.setType(res.getString(6));
            a.setCity(res.getString(7));
            a.setDescription(res.getString(8));
            a.setPrice(res.getFloat(9));
            a.setId_category(res.getInt(10));
            a.setPath_image(res.getString(11));
            a.setVideo(res.getString(13));
            arts.add(a);
        }

        return arts;
    }

    @Override
    public List<art> getOneArt() throws SQLException {
        List<art> getOneArt = new ArrayList<>() ;
        String req = "SELECT A.*, C.name AS name FROM art A INNER JOIN category C ON A.id_category = C.id_category";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(req);
        while (res.next()){
            art a = new art();
            a.setId_art(res.getInt(1));
            a.setTitle(res.getString(2));
            a.setMaterials(res.getString(3));
            a.setHeight(res.getDouble(4));
            a.setWidth(res.getDouble(5));
            a.setType(res.getString(6));
            a.setCity(res.getString(7));
            a.setDescription(res.getString(8));
            a.setPrice(res.getFloat(9));
            a.setId_category(res.getInt(10));
            a.setPath_image(res.getString(11));
            a.setVideo(res.getString(12));

            getOneArt.add(a);
        }
        return getOneArt;
    }
    public List<art> searchArt(String search) {
        List<art> artList = new ArrayList<>();
        try {
            String query = "SELECT * FROM art WHERE title LIKE ? ";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcours du résultat de la requête
            while (resultSet.next()) {
                art art = new art();
                art.setId_art(resultSet.getInt(1));
                art.setTitle(resultSet.getString(2));
                art.setMaterials(resultSet.getString(3));
                art.setHeight(resultSet.getDouble(4));
                art.setWidth(resultSet.getDouble(5));
                art.setType(resultSet.getString(6));
                art.setCity(resultSet.getString(7));
                art.setDescription(resultSet.getString(8));
                art.setPrice(resultSet.getFloat(9));
                art.setId_category(resultSet.getInt(10));
                art.setPath_image(resultSet.getString(11));
                art.setVideo(resultSet.getString(12));
                artList.add(art);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artList;
    }
    public art getArtById(int id) throws SQLException {
        art art = null;
        String query = "SELECT * FROM art WHERE id_art = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a record is found, create an art object and populate its fields
            if (resultSet.next()) {
                art = new art();
                art.setId_art(resultSet.getInt(1));
                art.setTitle(resultSet.getString(2));
                art.setMaterials(resultSet.getString(3));
                art.setHeight(resultSet.getDouble(4));
                art.setWidth(resultSet.getDouble(5));
                art.setType(resultSet.getString(6));
                art.setCity(resultSet.getString(7));
                art.setDescription(resultSet.getString(8));
                art.setPrice(resultSet.getFloat(9));
                art.setId_category(resultSet.getInt(10));
                art.setPath_image(resultSet.getString(11));
                art.setVideo(resultSet.getString(12));
            }
        }
        return art;
    }
    public List<art> getAllArt() {
        List<art> artList = new ArrayList<>();
        String query = "SELECT * FROM artworks"; // Adjust query based on your database schema

        try (
             PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Create art objects from the result set and add them to the list
                art art = new art();
                art.setId_art(resultSet.getInt("id"));
                art.setTitle(resultSet.getString("title"));
                // Set other attributes accordingly

                artList.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as necessary
        }

        return artList;
    }
    @Override
    public int ConseilNumbers() throws SQLException {
        int count = 0;
        String req = "SELECT COUNT(*) AS total_arts FROM art";

        try (Statement stmt = con.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(req);

            if (resultSet.next()) {
                count = resultSet.getInt("total_arts");
            }
        }
        return count;
    }
@Override
    public art getLastAddedArt() throws SQLException {
        art art = new art();
        String req = "SELECT * FROM art ORDER BY dateCreation DESC LIMIT 1";

        try (Statement stmt = con.createStatement(); ResultSet res = stmt.executeQuery(req)) {
            while (res.next()) {
                art.setDateCreation(res.getTimestamp("dateCreation"));
            }
        }

        return art;
    }
    @Override
    public String getCategoryNames(int idCategory) throws SQLException {
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

    public Map<Integer, Long> getArtCountByCategory() throws SQLException {
        Map<Integer, Long> ArtCounts = new HashMap<>();
        String req = "SELECT id_category, COUNT(id_art) as art_count FROM art GROUP BY id_category";

        try (Statement stmt = con.createStatement(); ResultSet res = stmt.executeQuery(req)) {
            while (res.next()) {
                int idCategorie = res.getInt("id_category");
                long ArtCount = res.getLong("art_count");
                ArtCounts.put(idCategorie, ArtCount);
            }
        }

        return ArtCounts;
    }
    public art getOneart(int idart) throws SQLException {

        String req = "SELECT * FROM commande where id_art= ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, idart);

        ResultSet rs = ps.executeQuery();
        art art = new art();

        while (rs.next()) {
            art.setTitle(rs.getString("title"));
            art.setMaterials(rs.getString("materials"));
            art.setHeight(rs.getDouble("height"));
            art.setWidth(rs.getDouble("width"));
            art.setType(rs.getString("type"));
            art.setCity(rs.getString("city"));
            art.setDescription(rs.getString("description"));
            art.setPrice(rs.getInt("price"));
            art.setId_category(rs.getInt("category"));


        }
        ps.close();
        return art;
    }
    public List<art> getAllArts() throws SQLException {
        List<art> artList = new ArrayList<>();
        String req = "SELECT * FROM art"; // Assuming your table name is 'art'
        PreparedStatement ps = con.prepareStatement(req);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            art art = new art();
            art.setTitle(rs.getString("title"));
            art.setMaterials(rs.getString("materials"));
            art.setHeight(rs.getDouble("height"));
            art.setWidth(rs.getDouble("width"));
            art.setType(rs.getString("type"));
            art.setCity(rs.getString("city"));
            art.setDescription(rs.getString("description"));
            art.setPrice(rs.getInt("price"));
            art.setPrice(rs.getInt("id_category"));
            artList.add(art);
        }

        ps.close();
        return artList;
    }

    public List<art> searchArtByName(String name) throws SQLException {
        String query = "SELECT * FROM art WHERE title LIKE ?";
        List<art> matchingArtworks = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%"); // Set the search name
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                art artwork = new art();
                // Populate artwork object from resultSet
                artwork.setTitle(resultSet.getString("title"));
                artwork.setMaterials(resultSet.getString("materials"));
                artwork.setHeight(resultSet.getDouble("height"));
                artwork.setWidth(resultSet.getDouble("width"));
                artwork.setType(resultSet.getString("type"));
                artwork.setCity(resultSet.getString("city"));
                artwork.setDescription(resultSet.getString("description"));
                artwork.setPrice(resultSet.getFloat("price"));
                artwork.setId_category(resultSet.getInt("id_category"));

                // Retrieve image path from the resultSet
                String imagePath = resultSet.getString("path_image");
                artwork.setPath_image(imagePath); // Set the image path in the artwork object

                // Add artwork object to matchingArtworks list
                matchingArtworks.add(artwork);
            }
        }
        return matchingArtworks;
    }

    public List<art> getArtByCategory(int categoryId) throws SQLException {
        String query = "SELECT * FROM art WHERE id_category = ?";
        List<art> artworksInCategory = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                art artwork = mapResultSetToArt(resultSet);
                artworksInCategory.add(artwork);
            }
        }
        return artworksInCategory;
    }

    // Helper method to map ResultSet to art object
    public art mapResultSetToArt(ResultSet resultSet) throws SQLException {
        art artwork = new art();
        artwork.setId_art(resultSet.getInt("id_art"));
        artwork.setTitle(resultSet.getString("title"));
        artwork.setMaterials(resultSet.getString("materials"));
        artwork.setHeight(resultSet.getDouble("height"));
        artwork.setWidth(resultSet.getDouble("width"));
        artwork.setType(resultSet.getString("type"));
        artwork.setCity(resultSet.getString("city"));
        artwork.setDescription(resultSet.getString("description"));
        artwork.setPrice(resultSet.getFloat("price"));
        artwork.setId_category(resultSet.getInt("id_category"));
        // Retrieve image path from the resultSet
        String imagePath = resultSet.getString("path_image");
        artwork.setPath_image(imagePath); // Set the image path in the artwork object
        // Set any other properties as needed
        return artwork;
    }

    public List<category> getAllCategories() throws SQLException {
        List<category> categories = new ArrayList<>();
        String query = "SELECT * FROM category"; // Adjust the query based on your database schema

        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                category category = new category();
                category.setId_category(resultSet.getInt("id_category"));
                category.setName(resultSet.getString("name"));
                // Set other attributes accordingly

                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as necessary
        }

        return categories;
    }



}

