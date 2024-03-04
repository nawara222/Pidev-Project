package Services.OrdersAndBaskets;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    int create(T t) throws SQLException;
    void update(T t, int idD) throws SQLException;
    void delete(int idD) throws SQLException;
    List<T> read() throws SQLException;
}
