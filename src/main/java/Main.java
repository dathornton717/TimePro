import java.io.File;
import java.util.Optional;
import org.apache.catalina.startup.Tomcat;

public class Main {

    public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));

    public static void main(String[] args) throws Exception {
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
}
