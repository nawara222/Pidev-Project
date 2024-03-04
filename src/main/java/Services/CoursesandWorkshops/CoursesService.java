package Services.CoursesandWorkshops;

import Models.Courses;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesService implements IServiceC<Courses> {

    private Connection connection;

    public CoursesService()
    {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Courses c) throws SQLException
    {
        String req = "INSERT INTO courses (nameC, descriptionC, priceC, type, numberW, image_path, Userid) VALUES (?, ?, ?, ?, ?,?,?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, c.getNameC());
        ps.setString(2, c.getDescriptionC());
        ps.setFloat(3, c.getPriceC());
        ps.setString(4, c.getType());
        ps.setInt(5, c.getNumberW());
        ps.setString(6, c.getImage_path());

        //ps.setInt(7, c.getUserid());
        ps.executeUpdate();
    }


    public List<Courses> searchCouses(String search)
    {
        List<Courses> CoursesList = new ArrayList<>();
        try
        {
            String query = "SELECT * FROM Courses WHERE nameC LIKE ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                Courses Courses = new Courses();
                Courses.setId_C(resultSet.getInt(1));
                Courses.setNameC(resultSet.getString(2));
                Courses.setDescriptionC(resultSet.getString(3));
                Courses.setPriceC(resultSet.getFloat(4));
                Courses.setType(resultSet.getString(5));
                Courses.setNumberW(resultSet.getInt(6));
                Courses.setImage_path(resultSet.getString(7));
                CoursesList.add(Courses);
            }
            preparedStatement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return CoursesList;
    }

    @Override
    public void update(Courses courses) throws SQLException {
        String sql = "UPDATE courses SET nameC = ?, descriptionC = ?, priceC = ? , type =? ,numberW =? WHERE id_C = ?";

        PreparedStatement cs = connection.prepareStatement(sql);
        cs.setInt(6,courses.getId_C());
        cs.setString(1, courses.getNameC());
        cs.setString(2, courses.getDescriptionC());
        cs.setFloat(3, courses.getPriceC());
        cs.setString(4, courses.getType());
        cs.setInt(5,courses.getNumberW());
        //cs.setInt(6,courses.getUserid());
        cs.executeUpdate();
    }

    @Override
    public void delete(int id_C) throws SQLException
    {
        String req = "DELETE  FROM courses WHERE id_C=?";
        PreparedStatement pre = connection.prepareStatement(req);

        pre.setInt(1,id_C);
        int rowsAffected = pre.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("course deleted");
        }
        else
        {
            System.out.println("there's no course with this id");
        }

    }

    @Override
    public List<Courses> read() throws SQLException {
        String sql = "SELECT * FROM courses";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Courses> coursesList = new ArrayList<>();
        while (rs.next()){
            Courses c = new Courses();
            c.setId_C(rs.getInt("id_C"));
            c.setNameC(rs.getString("nameC"));
            c.setDescriptionC(rs.getString("descriptionC"));
            c.setPriceC(rs.getFloat("priceC"));
            c.setType(rs.getString("type"));
            c.setNumberW(rs.getInt("numberW"));
            c.setImage_path(rs.getString("image_path"));
            c.setUserid(rs.getInt("userid"));
            coursesList.add(c);
        }
        return coursesList;
    }


    @Override
    public Map<String, Double> getCoursePercentageByType() throws SQLException
    {
        Map<String, Double> coursePercentage = new HashMap<>();
        String req = "SELECT type, COUNT(id_C) as course_count FROM courses GROUP BY type";

        // Calculate the total count of all courses
        long totalCourseCount = 0;
        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery("SELECT COUNT(id_C) as total_count FROM courses"))
        {
            if (res.next())
            {
                totalCourseCount = res.getLong("total_count");
            }
        }
        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery(req))
        {
            while (res.next())
            {
                String type = res.getString("type");
                long courseCount = res.getLong("course_count");
                double percentage = (courseCount * 100.0) / totalCourseCount;
                coursePercentage.put(type, percentage);
            }
        }
        return coursePercentage;
    }

    public Map<String, Long> getCourseCountByPriceRange() throws SQLException
    {
        Map<String, Long> courseCountByPriceRange = new HashMap<>();
        String query = "SELECT "
                + "CASE "
                + "    WHEN priceC BETWEEN 10 AND 20 THEN '10-20' "
                + "    WHEN priceC BETWEEN 20 AND 30 THEN '20-30' "
                + "    WHEN priceC BETWEEN 30 AND 40 THEN '30-40' "
                + "    WHEN priceC BETWEEN 40 AND 50 THEN '40-50' "
                + "    ELSE 'Unknown Range' "
                + "END AS priceRange, "
                + "COUNT(id_C) AS courseCount "
                + "FROM courses "
                + "GROUP BY priceRange";

        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery(query))
        {
            while (res.next())
            {
                String priceRange = res.getString("priceRange");
                long courseCount = res.getLong("courseCount");
                courseCountByPriceRange.put(priceRange, courseCount);
            }
        }

        return courseCountByPriceRange;
    }
}
