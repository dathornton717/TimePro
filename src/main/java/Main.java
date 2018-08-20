import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import constants.DatabaseConstants;
import factory.DataSource;
import org.apache.catalina.startup.Tomcat;

public class Main {

    public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));

    public static void main(String[] args) throws Exception {
        setUpDatabase();

        String docBase = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.valueOf(webPort));

        tomcat.addWebapp("/", new File(docBase).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + docBase).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void setUpDatabase() throws Exception {
        Connection connection = DataSource.createDatabaseConnection();
        for (String command : DatabaseConstants.CREATE_TABLES) {
            Statement statement = connection.createStatement();
            statement.execute(command);
        }
    }
}
