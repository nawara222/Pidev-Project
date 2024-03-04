package Services.User;

import java.sql.SQLException;
import java.util.List;

public interface IServiceUser<T> {
    void create(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int userId) throws SQLException;

    List<T> read() throws SQLException;



}