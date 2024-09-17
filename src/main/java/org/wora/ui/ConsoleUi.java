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

        // Création du projet
        System.out.println("--- Création d'un Nouveau Projet ---");
        System.out.print("Entrez le nom du projet : ");
        String name = scanner.nextLine();

        System.out.print("Entrez la marge bénéficiaire du projet (%) : ");
        double profitMargin = scanner.nextDouble();

        System.out.print("Entrez le coût total du projet (€) : ");
        double totalCost = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Entrez le statut du projet : ");
        String status = scanner.nextLine();

        // Sélectionner un devis
        Quote quote = selectQuote();

        // Sélectionner un client
        Client client = selectClient();

        // Créer un objet Project
        Project project = new Project();
        project.setName(name);
        project.setProfitMargin(profitMargin);
        project.setTotalCost(totalCost);
        project.setStatus(Status.valueOf(status.toUpperCase()));
        project.setQuote(quote);
        project.setClient(client);

        // Ajouter des composants
        List<Component> components = new ArrayList<>();
        boolean addingComponents = true;

        while (addingComponents) {
            System.out.println("--- Ajout des matériaux ---");
            System.out.print("Entrez le nom du matériau : ");
            String materialName = scanner.nextLine();

            System.out.print("Entrez la quantité de ce matériau (en m² ou litres) : ");
            double quantity = scanner.nextDouble();

            System.out.print("Entrez le coût unitaire de ce matériau (€) : ");
            double unitCost = scanner.nextDouble();

            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            double transportCost = scanner.nextDouble();

            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double qualityCoefficient = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Material material = new Material();
            material.setName(materialName);
            material.setQuantity(quantity);
            material.setUnitCost(unitCost);
            material.setTransportCost(transportCost);
            material.setQualityCoefficient(qualityCoefficient);

            components.add(material);

            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String response = scanner.nextLine();
            addingComponents = response.equalsIgnoreCase("y");
        }

        project.setComponents(components);

        // Sauvegarder le projet
        projectRepository.save(project);

        System.out.println("Projet créé avec succès !");
    }

    private Quote selectQuote() {
        // Implémentez une méthode pour sélectionner un devis existant
        List<Quote> quotes = quoteRepository.findAll();
        // Afficher les devis disponibles et permettre à l'utilisateur de choisir
        return null; // Remplacez par la sélection réelle
    }

    private Client selectClient() {
        // Implémentez une méthode pour sélectionner un client existant
        List<Client> clients = clientRepository.findAll();
        // Afficher les clients disponibles et permettre à l'utilisateur de choisir
        return null; // Remplacez par la sélection réelle
    }
}
