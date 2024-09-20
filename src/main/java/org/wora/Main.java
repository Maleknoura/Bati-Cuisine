package org.wora;


import org.wora.Database.DatabaseConnection;
import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.repository.ProjectRepository;

import org.wora.repository.QuoteRepository;
import org.wora.repositoryImpl.*;

import org.wora.service.ComponentService;
import org.wora.service.ProjectService;
import org.wora.service.QuoteService;
import org.wora.serviceImpl.LaborServiceImpl;
import org.wora.serviceImpl.MaterialServiceImpl;
import org.wora.serviceImpl.ProjectServiceImpl;
import org.wora.serviceImpl.QuoteServiceImpl;
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
        ClientRepository clientRepository = new ClientRepositoryImpl(connection);
        ComponentRepository<Labor> laborRepository = new LaborRepositoryImpl(connection);
        ComponentRepository<Material> materialRepository = new MaterialRepositoryImpl(connection);
        QuoteRepository quoteRepository = new QuoteRepositoryImpl(connection);


        ComponentService<Labor> laborService = new LaborServiceImpl(laborRepository);
        ComponentService<Material> materialService = new MaterialServiceImpl(materialRepository);
        ProjectService projectService = new ProjectServiceImpl(projectRepository, materialService, laborService);
        QuoteService quoteService = new QuoteServiceImpl(quoteRepository);


        ConsoleUi projectCreationUI = new ConsoleUi(connection, projectService, clientRepository, laborRepository, materialRepository);


        projectCreationUI.createProject();
    }
}