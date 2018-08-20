package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    public static Connection createDatabaseConnection()
        throws SQLException, ClassNotFoundException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        String url = "jdbc:derby:time_pro;create=true";
        return DriverManager.getConnection(url);
    }

    public static Connection getDatabaseConnection() throws ClassNotFoundException, SQLException {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(driver);
        String url = "jdbc:derby:time_pro;";
        return DriverManager.getConnection(url);
    }
}
