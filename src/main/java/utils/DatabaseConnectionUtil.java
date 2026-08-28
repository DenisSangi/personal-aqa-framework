package utils;

import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class DatabaseConnectionUtil {

    private final String dbName, dbUserName, dbPassword, host, port;
    private static final int CONNECT_TIMEOUT = 5, SOCKET_TIMEOUT = 5;

    private Connection getConnection() throws Exception {
        String url = String.format("jdbc:postgresql://%s:%s/%s?connectTimeout=%d&socketTimeout=%d", host, port, dbName, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
        Connection connection = DriverManager.getConnection(url, dbUserName, dbPassword);

        if (connection == null || connection.isClosed()) {
            throw new Exception("Failed to establish database connection");
        }

        return connection;
    }

    public List<Map<String, String>> executeQuery(String query) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();

            while(resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metadata.getColumnName(i);
                    String value = resultSet.getString(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query: " + (e.getCause() == null ? e.getMessage() : e.getMessage() + " " + e.getCause()), e);
        }

        return results;
    }

    public int executeUpdate(String query, Object... params) throws Exception {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i]==null) {
                    preparedStatement.setNull(i + 1, Types.NULL);
                } else if (params[i] instanceof Boolean boolValue) {
                    preparedStatement.setBoolean(i + 1, boolValue);
                } else if (params[i] instanceof String stringValue) {
                    preparedStatement.setString(i + 1, stringValue);
                } else {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute update query: " + (e.getCause() == null ? e.getMessage() : e.getMessage() + " " + e.getCause()), e);
        }
    }
}
