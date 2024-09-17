package org.wora.ui;

import org.wora.entity.*;
import org.wora.entity.Enum.Status;
import org.wora.repository.ClientRepository;
import org.wora.repository.ProjectRepository;
import org.wora.repository.QuoteRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUi{
    private Connection connection;
    private ProjectRepository projectRepository;
    private QuoteRepository quoteRepository;
    private ClientRepository clientRepository;

    public ConsoleUi(Connection connection, ProjectRepository projectRepository, QuoteRepository quoteRepository, ClientRepository clientRepository) {
        this.connection = connection;
        this.projectRepository = projectRepository;
        this.quoteRepository = quoteRepository;
        this.clientRepository = clientRepository;
    }

    public void createProject() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Création d'un Nouveau Projet ---");


        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        int option = scanner.nextInt();
        scanner.nextLine();

        Client client = null;

        if (option == 1) {
            System.out.print("Entrez le nom du client : ");
            String clientName = scanner.nextLine();
            client = clientRepository.clientExistsByName(clientName);
            if (client == null) {
                System.out.println("Client non trouvé.");
                return;
            }
            System.out.println("Client trouvé !");
            System.out.println("Nom: " + client.getName());
            System.out.println("Adresse : " + client.getAdress());
            System.out.println("Numéro de téléphone : " + client.getNumberPhone());
            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                return;
            }
        } else if (option == 2) {

            System.out.print("Entrez le nom du client : ");
            String name = scanner.nextLine();
            System.out.print("Entrez l'adresse du client : ");
            String address = scanner.nextLine();
            System.out.print("Entrez le numéro de téléphone du client : ");
            String phoneNumber = scanner.nextLine();
            client = new Client();
            clientRepository.addClient(client);
        }


        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();


        Project project = new Project();
        project.setName(projectName);

        project.setClient(client);


        System.out.println("--- Ajout des matériaux ---");
        while (true) {
            System.out.print("Entrez le nom du matériau : ");
            String materialName = scanner.nextLine();
            System.out.print("Entrez la quantité de ce matériau (en m² ou litres) : ");
            double quantity = scanner.nextDouble();
            System.out.print("Entrez le coût unitaire de ce matériau (€/m² ou €/litre) : ");
            double unitCost = scanner.nextDouble();
            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            double transportCost = scanner.nextDouble();
            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double qualityFactor = scanner.nextDouble();
            scanner.nextLine();

            Material material = new Material();
            project.addMaterial(material);

            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String continueAdding = scanner.nextLine();
            if (!continueAdding.equalsIgnoreCase("y")) {
                break;
            }
        }


        System.out.println("--- Ajout de la main-d'œuvre ---");
        while (true) {
            System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            String laborType = scanner.nextLine();
            System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double hourlyRate = scanner.nextDouble();
            System.out.print("Entrez le nombre d'heures travaillées : ");
            int hoursWorked = scanner.nextInt();
            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivityFactor = scanner.nextDouble();
            scanner.nextLine();

            Labor labor = new Labor();
            project.addLabor(labor);

            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            String continueAdding = scanner.nextLine();
            if (!continueAdding.equalsIgnoreCase("y")) {
                break;
            }
        }


        projectRepository.save(project);
        System.out.println("Projet créé avec succès !");
    }
    private Quote selectQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        return null;
    }

    private Client selectClient() {

        List<Client> clients = clientRepository.findAll();
        return null;
    }
}
