package org.wora;


import org.wora.Database.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            System.out.println("Connexion à la base de données établie ");
        } else {
            System.out.println("Échec de la connexion ");
        }
        }
    }
