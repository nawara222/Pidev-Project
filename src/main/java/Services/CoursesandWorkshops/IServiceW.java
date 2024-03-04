package Services.CoursesandWorkshops;

import java.sql.SQLException;
import java.util.List;

public interface IServiceW<T>
{

    void createw(T t) throws SQLException;
    void updatew(T t) throws SQLException;
    void deletew(int id_C) throws SQLException;
    List<T> readw() throws SQLException;
    int getWorkshopCountForCourse(int courseId) throws SQLException;
}
