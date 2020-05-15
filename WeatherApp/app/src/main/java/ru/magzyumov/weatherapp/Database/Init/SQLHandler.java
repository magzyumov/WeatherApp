package ru.magzyumov.weatherapp.Database.Init;

import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.Database.Location.Location;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement psGetAllData;

    //Основные методы для работы с базой данных
    public static boolean connect() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("org.sqldroid.SQLDroidDriver").newInstance());
            try {
                String jdbcUrl = "jdbc:sqldroid:" + "/data/data/ru.magzyumov.weatherapp/databases/storage";
                connection = DriverManager.getConnection(jdbcUrl);
                prepareAllStatements();
                makeLog("Connection to Store DB successful.");
                return true;
            } catch (SQLException ex) {
                makeLog("SQLException: " + ex.getMessage());
                return false;
            }
        } catch (Exception ex) {
            makeLog("Connection failed...");
            makeLog("Exception: " + ex.getMessage());
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException {
        psGetAllData = connection.prepareStatement("SELECT region, city FROM cities");
        makeLog("All Prepared Statement ready");
    }

    public static void disconnect(){
        if (psGetAllData != null) {
            try {
                psGetAllData.close();
                makeLog("GetAllData Prepared Statement Store DB close.");
            } catch (SQLException sqlEx) {
                makeLog("SQLException: " + sqlEx.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
                makeLog("Connection to Store DB close.");
            } catch (SQLException sqlEx) {
                makeLog("SQLException: " + sqlEx.getMessage());
            }
        }
    }

    public static List<Location> getAllData(){
        List<Location> result = new ArrayList<>();
        String query = "SELECT region, city FROM cities;";
        ResultSet resultSet = null;

        try {
            if (psGetAllData.execute()) {
                makeLog("Query executed: " + query);
                resultSet = psGetAllData.getResultSet();
            }
            assert resultSet != null;
            while (resultSet.next()){
                Location location = new Location();
                location.city = resultSet.getString("city");
                location.region = resultSet.getString("region");
                result.add(location);
            }
        } catch (SQLException | NullPointerException ex) {
            makeLog("SQLException: " + ex.getMessage());
        }
        return result;
    }

    private static void makeLog (String message){
        Log.e("SQL", message);
    }
}
