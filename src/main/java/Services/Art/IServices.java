package Services.Art;

import Models.art;
import Models.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServices <T>{
    public  void add(T t ) throws SQLException;
    public  void modify(T t ,int id_art ) throws SQLException;

    public  void delete(int id_art )throws SQLException;
    public List<T> display() throws SQLException;

    List<art> displayArtist() throws SQLException;

    public List<art> getOneArt() throws SQLException;

    public List<art> searchArt(String search);
    public List<art> getAllArt();


    int ConseilNumbers() throws SQLException;

    art getLastAddedArt() throws SQLException;


    public String getCategoryNames(int idCategory) throws SQLException;

    public Map<Integer, Long> getArtCountByCategory() throws SQLException ;
    public art getOneart (int idart) throws SQLException ;
    public List<art> getAllArts() throws SQLException;
    public List<art> searchArtByName(String name) throws SQLException ;

    public List<art> getArtByCategory(int categoryId) throws SQLException ;
    public art mapResultSetToArt(ResultSet resultSet) throws SQLException ;
    public List<category> getAllCategories() throws SQLException ;


    }
