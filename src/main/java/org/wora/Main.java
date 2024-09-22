package org.wora;


import org.wora.Database.DatabaseConnection;
import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.repository.ProjectRepository;

import org.wora.repository.QuoteRepository;
import org.wora.repository.repositoryImpl.*;


import org.wora.service.ClientService;
import org.wora.service.ComponentService;
import org.wora.service.ProjectService;
import org.wora.service.QuoteService;
import org.wora.service.serviceImpl.*;
import org.wora.ui.*;

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





        ClientRepository clientRepository = new ClientRepositoryImpl(connection);


        ClientService clientService = new ClientServiceImpl(clientRepository);


        ProjectRepository projectRepository = new ProjectRepositoryImpl(connection, clientService);


        ComponentRepository<Labor> laborRepository = new LaborRepositoryImpl(connection);
        ComponentRepository<Material> materialRepository = new MaterialRepositoryImpl(connection);
        QuoteRepository quoteRepository = new QuoteRepositoryImpl(connection);


        ComponentService<Labor> laborService = new LaborServiceImpl(laborRepository);
        ComponentService<Material> materialService = new MaterialServiceImpl(materialRepository);
        ProjectService projectService = new ProjectServiceImpl(projectRepository, materialService, laborService);


        ClientUI clientUI = new ClientUI(clientRepository);
        LaborUI laborUI = new LaborUI(laborService);
        MaterialUI materialUI = new MaterialUI(materialService);
        ProjectUI projectUI = new ProjectUI(projectService);
        QuoteService quoteService = new QuoteServiceImpl(quoteRepository);


        ConsoleUi consoleUI = new ConsoleUi(projectService, clientRepository, laborRepository, materialRepository, quoteService, clientUI, laborService, laborUI, materialUI, projectUI, materialService);
        consoleUI.start();
    }}