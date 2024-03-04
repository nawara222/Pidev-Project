package Services.Art;

import java.sql.SQLException;
import java.util.List;

public interface IservicesC <C> {
    public  void addC(C c ) throws SQLException;
    public  void modifyC(C c ,int id_category ) throws SQLException;

    public  void deleteC(int id_category )throws SQLException;
    public List<C> displayC() throws SQLException;

    public String getCategoryName(int idCategory) throws SQLException;



}
