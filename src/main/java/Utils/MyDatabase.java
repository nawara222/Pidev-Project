package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL ="jdbc:mysql://localhost:3306/pidevvinci";
    private final String USER ="root";
    private final String PASS ="";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private static MyDatabase instance;
    public MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null )
                instance = new MyDatabase();
        return instance;
    }
}
