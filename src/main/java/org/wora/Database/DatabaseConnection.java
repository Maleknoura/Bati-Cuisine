package org.wora.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        String url = "jdbc:postgresql://localhost:5432/BatiCuisine";
        String user = "postgres";
        String password = "administrateur";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
