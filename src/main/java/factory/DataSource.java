package factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private static final Logger LOGGER = LogManager.getLogger(DataSource.class.getName());

    public static Connection createDatabaseConnection() {
        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver);
            String url = "jdbc:derby:time_pro;create=true";
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Class not found for driver: " + Utilities.stackTraceToString(e));
        } catch (SQLException e) {
            LOGGER.error("SQL exception when connecting to database: " + Utilities.stackTraceToString(e));
        }
        return null;
    }

    public static Connection getDatabaseConnection() throws ClassNotFoundException, SQLException {
        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver);
            String url = "jdbc:derby:time_pro;";
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Class not found for driver: " + Utilities.stackTraceToString(e));
        } catch (SQLException e) {
            LOGGER.error("SQL exception when connecting to database: " + Utilities.stackTraceToString(e));
        }
        return null;
    }
}
