package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUtility {

    private static ResultSet rset;

    private static ResultSetMetaData rSetMetaData;

    public static ResultSet getResultSet(String sqlQuery) {

        Connection conn = null;
        Statement statement = null;

        try {
            conn = DriverManager.getConnection(
                    ConfigReader.getPropertyValue("dbUrl"),
                    ConfigReader.getPropertyValue("dbUsername"),
                    ConfigReader.getPropertyValue("dbPassword"));

            statement = conn.createStatement();
            rset = statement.executeQuery(sqlQuery);


        } catch (SQLException e) {
            e.printStackTrace();

        }
        return rset;
    }

    public static ResultSetMetaData getRsetMetadata(String query) {

        rset = getResultSet(query);

        rSetMetaData = null;

        try {
            rSetMetaData = rset.getMetaData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rSetMetaData;
    }

    public static List<Map<String, String>> getListOfMapsFromRset(String query) {

        rSetMetaData = getRsetMetadata(query);

        List<Map<String, String>> listFromRset = new ArrayList<>();
        Map<String, String> mapData;

        try {

            while (rset.next()) {
                mapData = new LinkedHashMap<>();

                for (int i = 1; i <= rSetMetaData.getColumnCount(); i++) {
                    String key = rSetMetaData.getColumnName(i);
                    String value = rset.getString(key);
                    mapData.put(key, value);
                }
                listFromRset.add(mapData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


            return listFromRset;

        }

    }

