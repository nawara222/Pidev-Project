package Services.CoursesandWorkshops;

import Models.Courses;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServiceC<T>
{
    Map<String, Double> getCoursePercentageByType()throws SQLException;
    List<Courses> searchCouses(String search);
    void create(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id_C) throws SQLException;
    List<T> read() throws SQLException;
    Map<String, Long> getCourseCountByPriceRange() throws SQLException;

}
