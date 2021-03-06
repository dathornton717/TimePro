package factory;

import event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Tuple;
import utility.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Factory {
    private static final Logger LOGGER = LogManager.getLogger(Factory.class.getName());

    public Map<Long, Long> getTimesByName(Event event, String firstName, String lastName) throws Exception {
        LOGGER.info("Getting times for " + firstName + " " + lastName + " for event " + event.eventToTable());
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select b.date_swam, b.time_swam " +
                    "from " + event.eventToTable() + " b " +
                    "inner join id_to_name i " +
                    "where i.first_name = ? " +
                    "and i.last_name = ?" +
                    "and b.id = i.id");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long date = resultSet.getLong("date_swam");
                Long time = resultSet.getLong("time_swam");

                if (result.containsKey(date)) {
                    Long current = result.get(date);
                    if (time.compareTo(current) < 0) {
                        result.put(date, time);
                    }
                }
                else {
                    result.put(date, time);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return result;
    }

    public Map<Long, Long> getTimesBetweenDatesByName(
        Event event,
        String firstName,
        String lastName,
        Long start,
        Long end)
    throws Exception {
        LOGGER.info("Getting times for " + firstName + " " + lastName + " for event " + event.eventToTable() + " from " + start + " to " + end);
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select b.date_swam, b.time_swam " +
                    "from " + event.eventToTable() + " b " +
                    "inner join id_to_name i " +
                    "where i.first_name = ? " +
                    "and i.last_name = ? " +
                    "and b.date_swam > ? " +
                    "and b.date_swam < ? " +
                    "and b.id = i.id");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setLong(3, start);
            statement.setLong(4, end);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long date = resultSet.getLong("date_swam");
                Long time = resultSet.getLong("time_swam");

                if (result.containsKey(date)) {
                    Long current = result.get(date);
                    if (time.compareTo(current) < 0) {
                        result.put(date, time);
                    }
                }
                else {
                    result.put(date, time);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return result;
    }

    public Map<Long, Long> getTimesToPresentByName(Event event, String firstName, String lastName, Long start)
    throws Exception {
        LOGGER.info("Getting times for " + firstName + " " + lastName + " for " + event.eventToTable() + " from " + start + " to present");
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select b.date_swam, b.time_swam " +
                    "from " + event.eventToTable() + " b " +
                    "inner join id_to_name i " +
                    "where i.first_name = ? " +
                    "and i.last_name = ? " +
                    "and b.date_swam > ?");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setLong(3, start);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long date = resultSet.getLong("date_swam");
                Long time = resultSet.getLong("time_swam");

                if (result.containsKey(date)) {
                    Long current = result.get(date);
                    if (time.compareTo(current) < 0) {
                        result.put(date, time);
                    }
                }
                else {
                    result.put(date, time);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return result;
    }

    public Map<Long, Long> getBestTimeByName(Event event, String firstName, String lastName)
    throws Exception {
        LOGGER.info("Getting best times for " + firstName + " " + lastName + " for " + event.eventToTable());
        Map<Long, Long> times = getTimesByName(event, firstName, lastName);
        List<Long> timeList = new ArrayList<Long>();
        for (Long calendar : times.keySet()) {
            timeList.add(times.get(calendar));
        }

        Long bestTime = null;
        Long bestDate = null;
        for (Long t : timeList) {
            if (bestTime != null) {
                if (t < bestTime) {
                    bestTime = t;
                }
            }
            else {
                bestTime = t;
            }
        }

        for (Long calendar : times.keySet()) {
            if (times.get(calendar) == bestTime) {
                bestDate = calendar;
            }
        }

        Map<Long, Long> result = new HashMap<Long, Long>();
        result.put(bestDate, bestTime);
        return result;
    }

    public Map<Long, Long> getTimesById(Event event, UUID id)
    throws Exception {
        LOGGER.info("Getting times for id " + id.toString() + " for " + event.eventToTable());
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select b.date_swam, b.time_swam " +
                "from " + event.eventToTable() + " b " +
                "inner join id_to_name i " +
                "where i.id = ?"
            );
            statement.setString(1, id.toString());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long date = resultSet.getLong("date_swam");
                Long time = resultSet.getLong("time_swam");

                if (result.containsKey(date)) {
                    Long current = result.get(date);
                    if (time.compareTo(current) < 0) {
                        result.put(date, time);
                    }
                }
                else {
                    result.put(date, time);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return result;
    }

    public Map<String, List<String>> getAllNames()
    throws Exception {
        LOGGER.info("Getting all names from database");
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select team_name, first_name, last_name " +
                "from id_to_name"
            );
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String teamName = resultSet.getString("team_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                if (!result.containsKey(teamName)) {
                    result.put(teamName, new ArrayList<String>());
                }
                result.get(teamName).add(firstName + " " + lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return result;
    }

    public void addSwimmer(String id, Map<String, List<Tuple<Long, Long>>> times)
    throws Exception {
        LOGGER.info("Adding swimmer with id " + id);
        for (String event : times.keySet()) {
            System.out.println(event);
            List<Tuple<Long, Long>> tupleList = times.get(event);
            addTimesToDatabase(id, event, tupleList);
        }
    }

    private void addTimesToDatabase(String id, String event, List<Tuple<Long, Long>> tupleList)
        throws Exception {
        LOGGER.info("Adding times for event " + event + " for id " + id);
        Connection connection = null;
        PreparedStatement statement = null;
            for (Tuple<Long, Long> tuple : tupleList) {
                Long time = tuple.getFirst();
                Long date = tuple.getSecond();
                if (!isTimeInDatabase(id, event, time, date)) {
                    try {
                        connection = DataSource.getDatabaseConnection();
                        statement = connection.prepareStatement(
                            "insert into " + eventToTable(event) +
                                "(id, date_swam, time_swam) " +
                                "values(?, ?, ?)");
                        statement.setString(1, id);
                        statement.setLong(2, date);
                        statement.setLong(3, time);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                        if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
                    }
                }
            }
    }

    private boolean isTimeInDatabase(String id, String event, Long time, Long date)
        throws Exception {
        LOGGER.info("Checking if time " + time + " for id " + id + " for event " + event + " for date " + date);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getDatabaseConnection();
            statement = connection.prepareStatement(
                "select count(*) " +
                    "from " + eventToTable(event) +
                    " where id = ? " +
                    "and date_swam = ? " +
                    "and time_swam = ?");
            statement.setString(1, id);
            statement.setLong(2, date);
            statement.setLong(3, time);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1) != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        throw new Exception("An error occurred");
    }

    private String eventToTable(String event) {
        return Utilities.stringToEvent(event).eventToTable();
    }
}
