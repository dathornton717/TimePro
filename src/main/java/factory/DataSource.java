package factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
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

    private static DataSource dataSource;
    private ComboPooledDataSource comboPooledDataSource;

    private DataSource()
    throws IOException, SQLException, PropertyVetoException {
        comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost/time_pro");
        comboPooledDataSource.setUser("admin");
        comboPooledDataSource.setPassword("admin");
    }

    public static DataSource getInstance()
    throws IOException, SQLException, PropertyVetoException {
        if (dataSource == null) {
            dataSource = new DataSource();
        }
        return dataSource;
    }

    public Connection getConnection()
    throws SQLException {
        return comboPooledDataSource.getConnection();
    }


}
