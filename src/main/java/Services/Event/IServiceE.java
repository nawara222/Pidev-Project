package Services.Event;

import Models.Event;

import java.sql.SQLException;
import java.util.List;

public interface IServiceE <T> {
    void createE(T t) throws SQLException;
    void updateE(T t) throws SQLException;
    void deleteE(int id_C) throws SQLException;
    List<T> readE() throws SQLException;

    List<Event> readEU() throws SQLException;
}
