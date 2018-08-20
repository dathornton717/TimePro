package rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import factory.DataSource;
import factory.Factory;
import utility.Tuple;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Provider
@Path("/")
public class PutResource {

    @PUT
    @Path("add-swimmers")
    public Response addSwimmers(String input)
    throws Exception {

        Map<String, List<String>> teamMap = new HashMap<String, List<String>>();
        String teamName = "";
        String[] lines = input.split("\\r?\\n");
        for (String line : lines) {
            if (line.contains("TEAM:")) {
                line = line.substring(5);
                if (!teamMap.containsKey(line)) {
                    teamMap.put(line, new ArrayList<String>());
                }
                teamName = line;
            }
            else {
                if (teamName.equals("")) {
                    throw new IllegalArgumentException("Illegal file format.");
                }
                teamMap.get(teamName).add(line);
            }
        }

        for (String team : teamMap.keySet()) {
            List<String> swimmers = teamMap.get(team);
            for (String swimmer : swimmers) {
                int spaceIndex = swimmer.indexOf(' ');
                if (spaceIndex == -1) {
                    throw new IllegalArgumentException("Illegal file format.");
                }

                String firstName = swimmer.substring(0, spaceIndex);
                String lastName = swimmer.substring(spaceIndex + 1);

                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(teamName);

                JsonNodeFactory factory = JsonNodeFactory.instance;
                ObjectNode node = factory.objectNode();
                node.put("firstName", firstName);
                node.put("lastName", lastName);
                node.put("teamName", team);
                addSwimmer(node.toString());
            }
        }

        return Response.ok().build();
    }

    @PUT
    @Path("add-swimmer")
    public Response addSwimmer(String input)
    throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(input);
        String firstName = jsonNode.get("firstName").asText();
        String lastName = jsonNode.get("lastName").asText();
        String teamName = jsonNode.get("teamName").asText();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        ChromeDriver driver = new ChromeDriver(chromeOptions);

        sendCommandForDownloadChromeHeadLess((HttpCommandExecutor)((RemoteWebDriver) driver).getCommandExecutor(),((RemoteWebDriver) driver).getSessionId(), System.getProperty("user.dir"));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat calFormat = new SimpleDateFormat("MM/dd/yyyy");
        String presentDate = calFormat.format(calendar.getTime());

        driver.get("https://www.usaswimming.org/Home/times/individual-times-search");
        driver.findElementById("UsasTimeSearchIndividual_Index_Div_1FirstName").sendKeys(firstName);
        driver.findElementById("UsasTimeSearchIndividual_Index_Div_1LastName").sendKeys(lastName);
        driver.findElementById("UsasTimeSearchIndividual_Index_Div_1StartDate").sendKeys("01/01/2000");
        driver.findElementById("UsasTimeSearchIndividual_Index_Div_1EndDate").sendKeys(presentDate);
        WebElement e = driver.findElementByXPath("//span[@aria-owns=\"UsasTimeSearchIndividual_Index_Div_1cboCourse_listbox\"]");
        e.click();
        e.sendKeys(Keys.DOWN);
        e.sendKeys(Keys.DOWN);
        e.sendKeys(Keys.DOWN);
        e.sendKeys(Keys.RETURN);

        driver.findElementById("UsasTimeSearchIndividual_Index_Div_1-saveButton").click();

        try {
            WebElement myElem = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("UsasTimeSearchIndividual_PersonSearchResults_Grid-1")));
            boolean stop = false;
            while (!stop) {
                List<WebElement> elementList = myElem.findElements(By.tagName("td"));
                for (WebElement element : elementList) {
                    if (element.getText().equals(teamName)) {
                        WebElement parent = element.findElement(By.xpath(".."));
                        parent.findElement(By.className("pointer")).click();
                        stop = true;
                        break;
                    }
                }

                if (!stop) {
                    driver.findElementById("UsasTimeSearchIndividual_PersonSearchResults_Grid-1-UsasGridPager-pgNext").click();
                    myElem = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.elementToBeClickable(By.id("UsasTimeSearchIndividual_PersonSearchResults_Grid-1")));
                }
            }
        }
        catch (TimeoutException error) {

        }

        try {
            WebElement download = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("UsasTimeSearchIndividual_TimeResults_Grid-1_exportExcel")));
            download.click();
        }
        catch (TimeoutException error) {
            error.printStackTrace();
        }

        Thread.sleep(5000);
        driver.quit();

        Map<String, List<Tuple<Long, Long>>> times = new HashMap<String, List<Tuple<Long, Long>>>();
        times.put("50 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("100 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("200 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("500 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("1000 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("1650 FR", new ArrayList<Tuple<Long, Long>>());
        times.put("50 BK", new ArrayList<Tuple<Long, Long>>());
        times.put("100 BK", new ArrayList<Tuple<Long, Long>>());
        times.put("200 BK", new ArrayList<Tuple<Long, Long>>());
        times.put("50 BR", new ArrayList<Tuple<Long, Long>>());
        times.put("100 BR", new ArrayList<Tuple<Long, Long>>());
        times.put("200 BR", new ArrayList<Tuple<Long, Long>>());
        times.put("50 FL", new ArrayList<Tuple<Long, Long>>());
        times.put("100 FL", new ArrayList<Tuple<Long, Long>>());
        times.put("200 FL", new ArrayList<Tuple<Long, Long>>());
        times.put("100 IM", new ArrayList<Tuple<Long, Long>>());
        times.put("200 IM", new ArrayList<Tuple<Long, Long>>());
        times.put("400 IM", new ArrayList<Tuple<Long, Long>>());

        String fileName;
        if (firstName.contains(".") || lastName.contains(".")) {
            fileName = "Times For " + firstName + " " + lastName;
        }
        else {
            fileName = "Times For " + firstName + " " + lastName + ".xlsx";
        }

        File file = new File(System.getProperty("user.dir") + "/" + fileName);

        XSSFWorkbook wb = new XSSFWorkbook(file);

        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator rows = sheet.rowIterator();
        rows.next();

        while (rows.hasNext()) {
            XSSFRow row = (XSSFRow) rows.next();
            int eventNameIndex = 0;
            int timeIndex = 1;
            int dateIndex = 9;
            String eventName = row.getCell(eventNameIndex).getStringCellValue();
            Long time = null;
            XSSFCell timeCell = row.getCell(timeIndex);
            if (timeCell.getCellTypeEnum() == CellType.NUMERIC) {
                time = timeToLong(numberToTime(timeCell.getNumericCellValue()));
            }
            else if (timeCell.getCellTypeEnum() == CellType.STRING) {
                time = timeToLong(numberToTime(timeCell.getStringCellValue()));
            }

            Long date = dateToLong(row.getCell(dateIndex).getStringCellValue());
            times.get(eventName).add(new Tuple<Long, Long>(time, date));
        }
        wb.close();

        String id = addUserToDatabaseIfNeeded(firstName, lastName, teamName);
        new Factory().addSwimmer(id, times);

        file.delete();

        return Response.ok().build();
    }

    private void sendCommandForDownloadChromeHeadLess(HttpCommandExecutor driverCommandExecutor, SessionId sessionId, String downloadPath) {
        Json json = new Json();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cmd", "Page.setDownloadBehavior");
        Map<String,String> cmdParamsMap = new HashMap<>();
        cmdParamsMap.put("behavior", "allow");
        cmdParamsMap.put("downloadPath", downloadPath);
        paramsMap.put("params", cmdParamsMap);
        String content = json.toJson(paramsMap);
        URL remoteServerUri = null;
        try {
            Field field = HttpCommandExecutor.class.getDeclaredField("remoteServer");
            field.setAccessible(true);
            remoteServerUri = (URL) field.get(driverCommandExecutor);
        }catch (Exception e) {
            return;
        }
        CloseableHttpClient httpclient = null;
        try {
            httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(remoteServerUri.toURI());
            builder.setPath("session/"+sessionId.toString()+"/chromium/send_command");
            HttpPost sendCommandPost = new HttpPost(builder.build());
            sendCommandPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            StringEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
            sendCommandPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(sendCommandPost);
            int statusCode = response.getStatusLine().getStatusCode();
        }catch (IOException e) {
        } catch (URISyntaxException e) {
        }finally {
            if(httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                }
            }
        }

    }

    private String numberToTime(Object o) {
        // The time was a relay split
        if (o instanceof String) {
            String result = (String) o;
            return result.replace("r", "");
        }
        // It's an actual time
        else {
            Double num = (Double) o;
            num = num - 33.5;
            num = num * 24 * 60 * 60;
            num = (double)Math.round(num * 100) / 100;
            int minute = (int)(num / 60);
            String minuteString = Integer.toString(minute);
            num = num - minute * 60;
            int second = (int)(num / 1);
            int hundredth = (int)Math.round((num - second) * 100);
            String hundredthString = Integer.toString(hundredth);
            String secondString = Integer.toString(second);
            if (secondString.length() == 1) {
                secondString = "0" + secondString;
            }
            if (hundredthString.length() == 1) {
                hundredthString = "0" + hundredthString;
            }

            if (minuteString.equals("0")) {
                return secondString + "." + hundredth;
            }
            else {
                return minuteString + ":" + secondString + "." + hundredthString;
            }
        }
    }

    private long timeToLong(String time) {
        if (time.contains(":")) {
            String[] timeArray = time.split(":");
            int minutes = Integer.parseInt(timeArray[0]);
            timeArray = timeArray[1].split("\\.");
            int seconds = Integer.parseInt(timeArray[0]);
            int hundredths = Integer.parseInt(timeArray[1]);
            return (long)(minutes * 60 * 100 + seconds * 100 + hundredths);
        }
        else {
            String[] timeArray = time.split("\\.");
            int seconds = Integer.parseInt(timeArray[0]);
            int hundredths = Integer.parseInt(timeArray[1]);
            return (long)(seconds * 100 + hundredths);
        }
    }

    private long dateToLong(String date)
        throws Exception {
        String[] dateArray = date.split("/");
        String month = dateArray[0];
        String day = dateArray[1];
        String year = dateArray[2];
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        calendar.setTime(simpleDateFormat.parse(month + "/" + day + "/" + year));
        return calendar.getTimeInMillis()+calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
    }

    private String addUserToDatabaseIfNeeded(String firstName, String lastName, String teamName)
    throws Exception {
        String userId = isUserInDatabase(firstName, lastName);
        if (!userId.equals("")) {
            return userId;
        }
        else {
            Connection connection = null;
            PreparedStatement statement = null;
            String id = UUID.randomUUID().toString();
            try {
                connection = DataSource.getDatabaseConnection();
                statement = connection.prepareStatement(
                    "insert into id_to_name" +
                    "(first_name, last_name, id, team_name) " +
                    "values(?, ?, ?, ?)");
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, id);
                statement.setString(4, teamName);
                statement.executeUpdate();
                return id;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
            }

            return id;
        }
    }

    private String isUserInDatabase(String firstName, String lastName)
    throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select id from id_to_name " +
                "where first_name = ? " +
                "and last_name = ?");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return "";
    }
}
