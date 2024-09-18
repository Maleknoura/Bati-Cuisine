package org.wora.ui;

import org.wora.entity.*;
import org.wora.entity.Enum.Status;
import org.wora.repository.ClientRepository;
import org.wora.repository.ProjectRepository;
import org.wora.repository.QuoteRepository;
import org.wora.service.ProjectService;
import org.wora.serviceImpl.ProjectServiceImpl;

import java.sql.Connection;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUi {
    private Connection connection;
    private ProjectService projectService;
    private ClientRepository clientRepository;

    public ConsoleUi(Connection connection, ProjectService projectService, ClientRepository clientRepository) {
        this.connection = connection;
        this.projectService = projectService;
        this.clientRepository = clientRepository;
    }

    public void createProject() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("------- Création d'un Nouveau Projet -----");

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
            Optional<Client> clientOpt = clientRepository.findClientByName(clientName);

            if (clientOpt.isEmpty()) {
                System.out.println("Client non trouvé.");
                return;
            }

            client = clientOpt.get();
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
            System.out.print("Le client est-il un professionnel ? (oui/non) : ");
            String isProfessionnelInput = scanner.nextLine();
            boolean isProfessionnel = isProfessionnelInput.equalsIgnoreCase("oui");

            client = new Client();
            client.setName(name);
            client.setAdress(address);
            client.setNumberPhone(phoneNumber);
            client.setProfessionel(isProfessionnel);

            clientRepository.addClient(client);
        }

        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();

        System.out.println("Sélectionnez le statut du projet : ");
        System.out.println("1. En cours");
        System.out.println("2. Terminé");
        System.out.println("3. Annulé");

        int statusOption = scanner.nextInt();
        scanner.nextLine();

        Status status;
        switch (statusOption) {
            case 1:
                status = Status.IN_PROGRESS;
                break;
            case 2:
                status = Status.COMPLETED;
                break;
            case 3:
                status = Status.CANCELLED;
                break;
            default:
                System.out.println("Option non valide, le statut sera défini à 'En cours' par défaut.");
                status = Status.IN_PROGRESS;
                break;
        }

        Project project = new Project();
        project.setName(projectName);
        project.setClient(client);
        project.setStatus(status);

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
            material.setName(materialName);
            material.setQuantity(quantity);
            material.setUnitCost(unitCost);
            material.setTransportCost(transportCost);
            material.setQualityCoefficient(qualityFactor);

            project.add(material);

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
            double workHours = scanner.nextDouble();
            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivityFactor = scanner.nextDouble();
            scanner.nextLine();

            Labor labor = new Labor();
            labor.setComponentType(laborType);
            labor.setHourlyRate(hourlyRate);
            labor.setWorkHours(workHours);
            labor.setWorkerProductivity(productivityFactor);

            project.add(labor);

            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            String continueAdding = scanner.nextLine();
            if (!continueAdding.equalsIgnoreCase("y")) {
                break;
            }
        }

        projectService.createProject(project);
        System.out.println("Projet créé avec succès !");
    }
}
