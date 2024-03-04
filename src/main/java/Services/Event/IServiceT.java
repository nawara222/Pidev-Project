package Services.Event;

import java.sql.SQLException;
import java.util.List;
public interface IServiceT <T>{
    void createT(T t) throws SQLException;
    void deleteT(int id_C) throws SQLException;
    List<T> readT() throws SQLException;
}
