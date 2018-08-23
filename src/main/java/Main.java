import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import constants.DatabaseConstants;
import factory.DataSource;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;

public class Main {

    public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        // We assume if the directory exists then the user has the database created already
        if (!(new File("time_pro").exists())) {
            setUpDatabase();
        }

        String docBase = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.valueOf(webPort));

        tomcat.addWebapp("/", new File(docBase).getAbsolutePath());
        LOGGER.info("Configuring app with basedir: " + new File("./" + docBase).getAbsolutePath());
        LOGGER.info("Starting tomcat");
        tomcat.start();
        tomcat.getServer().await();
    }

    private static void setUpDatabase() throws Exception {
        Connection connection = DataSource.createDatabaseConnection();
        if (connection == null) {
            LOGGER.fatal("Could not connect to database. Shutting down.");
            System.exit(1);
        }
        for (String command : DatabaseConstants.CREATE_TABLES) {
            Statement statement = connection.createStatement();
            statement.execute(command);
        }
    }
}
