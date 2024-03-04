package Services.CoursesandWorkshops;

import Models.Workshop;
import Services.User.UserService;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkshopService implements IServiceW<Workshop>
{
    private Connection connection;
    Workshop workshop;

    public WorkshopService()
    {
        connection = MyDatabase.getInstance().getConnection();
    }



    @Override
    public void createw(Workshop workshop) throws SQLException
    {
        String sql = "INSERT INTO workshops (nameW, resources, description, duration, id_C, Userid) " +
                "VALUES (?, ?, ?, ?,?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, workshop.getNameW());
        statement.setString(2, workshop.getResources());
        statement.setString(3, workshop.getDescription());
        statement.setFloat(4, workshop.getDuration());
        statement.setInt(5, workshop.getId_C());
        statement.setInt(6, UserService.currentlyLoggedInUser.getUserID());
        statement.executeUpdate();
        updateWorkshopNumberW(workshop.getId_C());
    }



    private void updateWorkshopNumberW(int courseId) throws SQLException
    {
        int workshopCount = getWorkshopCountForCourse(courseId);
        String updateSql = "UPDATE courses SET numberW = ? WHERE id_C = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setInt(1, workshopCount);
            updateStatement.setInt(2, courseId);
            updateStatement.executeUpdate();
        }
    }

    public List<Workshop> searchWorkshop(String search) {
        List<Workshop> WorkshopList2 = new ArrayList<>();
        try {
            String query = "SELECT * FROM workshops WHERE nameW LIKE ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Workshop workshop = new Workshop();
                workshop.setId_W(resultSet.getInt(1));
                workshop.setNameW(resultSet.getString(2));
                workshop.setResources(resultSet.getString(3));
                workshop.setDescription(resultSet.getString(4));
                workshop.setDuration(resultSet.getFloat(5));
                workshop.setId_C(resultSet.getInt(6));
                //workshop.setUserid(resultSet.getInt(7));
                WorkshopList2.add(workshop);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return WorkshopList2;
    }


    public int getWorkshopCountForCourse(int courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM workshops WHERE id_C = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Workshop> readw() throws SQLException {
        String sql = "SELECT workshops.id_W, workshops.nameW, workshops.resources, " +
                "workshops.description, workshops.duration, workshops.id_C, workshops.Userid " +
                "FROM workshops JOIN courses ON workshops.id_C = courses.id_C";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Workshop> workshopList = new ArrayList<>();
        while (rs.next()) {
            Workshop w = new Workshop();
            w.setId_W(rs.getInt("id_W"));
            w.setNameW(rs.getString("nameW"));
            w.setResources(rs.getString("resources"));
            w.setDescription(rs.getString("description"));
            w.setDuration(rs.getFloat("duration"));
            w.setId_C(rs.getInt("id_C"));
            w.setUserid(rs.getInt("Userid"));
            workshopList.add(w);
        }
        return workshopList;
    }


    @Override
    public void updatew(Workshop workshop) throws SQLException
    {
        String sql = "UPDATE workshops SET nameW = ?, resources = ?, description = ?, duration = ?, id_C = ?  WHERE id_W = ?";

        PreparedStatement cs = connection.prepareStatement(sql);
        cs.setInt(6,workshop.getId_W());
        cs.setString(1,workshop.getNameW());
        cs.setString(2,workshop.getResources());
        cs.setString(3,workshop.getDescription());
        cs.setFloat(4,workshop.getDuration());
        cs.setInt(5,workshop.getId_C());
        //cs.setInt(7,workshop.getUserid());
        cs.executeUpdate();
    }


    @Override
    public void deletew(int id_W) throws SQLException
    {
        Workshop deletedWorkshop = getWorkshopById(id_W);
        String req = "DELETE FROM workshops WHERE id_W=?";
        try (PreparedStatement pre = connection.prepareStatement(req))
        {
            pre.setInt(1, id_W);
            int rowsAffected = pre.executeUpdate();
            if (rowsAffected > 0)
            {
                if (deletedWorkshop != null)
                {
                    updateWorkshopNumberW(deletedWorkshop.getId_C());
                    System.out.println("Workshop deleted successfully");
                }
            }
            else
            {
                System.out.println("There's no workshop with this id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    private Workshop getWorkshopById(int id_W) throws SQLException {
        String sql = "SELECT * FROM workshops WHERE id_W = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id_W);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Workshop workshop = new Workshop();
                workshop.setId_W(rs.getInt("id_W"));
                workshop.setId_C(rs.getInt("id_C"));
                return workshop;
            }
        }
        return null;
    }
}
