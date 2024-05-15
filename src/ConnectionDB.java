import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static ConnectionDB instance;
    private Connection con;

    private ConnectionDB() {
        String dbLink = "jdbc:mysql://localhost:3306/project_pbo";
        String dbUser = "root";
        String dbPass = "";
        try {
            con = DriverManager.getConnection(dbLink, dbUser, dbPass);
        } catch (SQLException e) {
            throw new IllegalStateException("DB Errors: ", e);
        }
    }

    public static Connection getConnection() {
        if (instance == null) {
            instance = new ConnectionDB();
        }
        return instance.con;
    }

}
