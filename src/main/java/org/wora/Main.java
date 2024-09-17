package org.wora;


import org.wora.Database.DatabaseConnection;
import org.wora.repository.ClientRepository;
import org.wora.repository.ProjectRepository;
import org.wora.repository.QuoteRepository;
import org.wora.repositoryImpl.ClientRepositoryImpl;
import org.wora.repositoryImpl.ProjectRepositoryImpl;
import org.wora.repositoryImpl.QuoteRepositoryImpl;

import org.wora.ui.ConsoleUi;

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
        ProjectRepository projectRepository = new ProjectRepositoryImpl(connection);
        QuoteRepository quoteRepository = new QuoteRepositoryImpl(connection);
        ClientRepository clientRepository = new ClientRepositoryImpl(connection);


        ConsoleUi projectCreationUI = new ConsoleUi(connection, projectRepository, quoteRepository, clientRepository);


        projectCreationUI.createProject();

    }
    }
