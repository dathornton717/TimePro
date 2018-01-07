package main.java.factory;

import main.java.event.Event;

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
    public Map<Long, Long> getTimesByName(Event event, String firstName, String lastName) throws Exception {
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
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
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
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
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
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
        Map<Long, Long> result = new HashMap<Long, Long>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
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

    public List<String> getAllNames()
    throws Exception {
        List<String> result = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DataSource.getInstance().getConnection();
            statement = connection.prepareStatement(
                "select first_name, last_name " +
                "from id_to_name"
            );
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                result.add(firstName + " " + lastName);
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
}
