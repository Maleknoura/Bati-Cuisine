package org.wora.ui;

import org.wora.entity.*;
import org.wora.entity.Enum.Status;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.service.ProjectService;
import org.wora.service.QuoteService;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUi {
    private final Connection connection;
    private final ProjectService projectService;
    private final ClientRepository clientRepository;
    private final ComponentRepository<Labor> laborRepository;
    private final ComponentRepository<Material> materialRepository;
    private final QuoteService quoteService;

    public ConsoleUi(Connection connection, ProjectService projectService, QuoteService quoteService,
                     ClientRepository clientRepository, ComponentRepository<Labor> laborRepository,
                     ComponentRepository<Material> materialRepository) {
        this.connection = connection;
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;
        this.quoteService = quoteService;
    }

    public void createProject() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("------- Création d'un Nouveau Projet -----");

            Client client = handleClientSelection(scanner);

            if (client == null) {
                System.out.println("Échec de la sélection du client.");
                return;
            }

            Project project = new Project();
            System.out.println("--- Création du Projet ---");

            System.out.print("Nom du projet : ");
            project.setName(scanner.nextLine());

            System.out.print("Statut du projet (EN_COURS/TERMINE/ANNULE) : ");
            project.setStatus(Status.valueOf(scanner.nextLine().toUpperCase()));

            project.setClient(client);
            projectService.createProject(project);
            System.out.println("Projet créé avec succès. ID du projet : " + project.getId());

            addLabor(scanner, project);
            addMaterial(scanner, project);
            calculateCosts(scanner, project);

            System.out.println("Le projet et toutes ses ressources ont été créés avec succès.");
        }
    }

    private Client handleClientSelection(Scanner scanner) {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int option = getValidOption(scanner, 1, 2);

        if (option == 1) {
            return findExistingClient(scanner);
        } else {
            return createNewClient(scanner);
        }
    }

    private Client findExistingClient(Scanner scanner) {
        System.out.print("Entrez le nom du client : ");
        String clientName = scanner.nextLine();
        Optional<Client> clientOpt = clientRepository.findClientByName(clientName);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            System.out.println("Client trouvé :");
            System.out.println("Nom: " + client.getName());
            System.out.println("Adresse : " + client.getAdress());
            System.out.println("Numéro de téléphone : " + client.getNumberPhone());

            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                return client;
            }
        } else {
            System.out.println("Client non trouvé.");
        }
        return null;
    }

    private Client createNewClient(Scanner scanner) {
        System.out.println("--- Ajout d'un nouveau client ---");

        System.out.print("Nom du client : ");
        String name = scanner.nextLine();
        System.out.print("Adresse du client : ");
        String address = scanner.nextLine();
        System.out.print("Numéro de téléphone : ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Le client est-il un professionnel ? (oui/non) : ");
        boolean isProfessionnel = scanner.nextLine().equalsIgnoreCase("oui");

        Client client = new Client();
        client.setName(name);
        client.setAdress(address);
        client.setNumberPhone(phoneNumber);
        client.setProfessionel(isProfessionnel);

        clientRepository.addClient(client);
        return client;
    }

    private void addLabor(Scanner scanner, Project project) {
        while (true) {
            System.out.println("Voulez-vous ajouter de la main-d'œuvre au projet ? (oui/non)");
            if (!scanner.nextLine().equalsIgnoreCase("oui")) {
                break;
            }

            Labor labor = new Labor();
            System.out.print("Nom de la main-d'œuvre : ");
            labor.setName(scanner.nextLine());
            System.out.print("Coût unitaire : ");
            labor.setUnitCost(getValidDouble(scanner));
            System.out.print("Quantité : ");
            labor.setQuantity(getValidDouble(scanner));
            labor.setTaxRate(0);
            System.out.print("Taux horaire : ");
            labor.setHourlyRate(getValidDouble(scanner));
            System.out.print("Heures de travail : ");
            labor.setWorkHours(getValidDouble(scanner));
            System.out.print("Productivité des travailleurs : ");
            labor.setWorkerProductivity(getValidDouble(scanner));
            labor.setProject(project);

            laborRepository.add(labor, project.getId());
            System.out.println("Main-d'œuvre ajoutée au projet.");
        }
    }

    private void addMaterial(Scanner scanner, Project project) {
        while (true) {
            System.out.println("Voulez-vous ajouter du matériel au projet ? (oui/non)");
            if (!scanner.nextLine().equalsIgnoreCase("oui")) {
                break;
            }

            Material material = new Material();
            System.out.print("Nom du matériel : ");
            material.setName(scanner.nextLine());
            System.out.print("Coût unitaire : ");
            material.setUnitCost(getValidDouble(scanner));
            System.out.print("Quantité : ");
            material.setQuantity(getValidDouble(scanner));
            material.setTaxRate(0);
            System.out.print("Coût de transport : ");
            material.setTransportCost(getValidDouble(scanner));
            System.out.print("Coefficient de qualité : ");
            material.setQualityCoefficient(getValidDouble(scanner));
            material.setProject(project);

            materialRepository.add(material, project.getId());
            System.out.println("Matériel ajouté au projet.");
        }
    }

    private void calculateCosts(Scanner scanner, Project project) {
        System.out.println("--- Calcul du Coût Total ---");

        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        boolean applyVAT = scanner.nextLine().equalsIgnoreCase("y");
        double vatPercentage = 0;

        if (applyVAT) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            vatPercentage = getValidDouble(scanner);
        }

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        boolean applyMargin = scanner.nextLine().equalsIgnoreCase("y");
        double marginPercentage = 0;

        if (applyMargin) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginPercentage = getValidDouble(scanner);
        }

        double totalMaterialCost = projectService.calculateTotalMaterialCost(project.getId());
        double totalLaborCost = projectService.calculateTotalLaborCost(project.getId());
        double totalCostBeforeMargin = totalMaterialCost + totalLaborCost;

        if (applyMargin) {
            projectService.updateProfitMargin(project.getId(), marginPercentage / 100);
        }

        double marginAmount = applyMargin ? (totalCostBeforeMargin * marginPercentage / 100) : 0;
        double finalCost = totalCostBeforeMargin + marginAmount;

        if (applyVAT) {
            double vatAmount = finalCost * vatPercentage / 100;
            finalCost += vatAmount;
            System.out.println("Montant de la TVA : " + vatAmount);
        }

        System.out.println("Coût total du projet après TVA : " + finalCost);
        projectService.updateTotalCost(project.getId(), finalCost);

        System.out.print("Souhaitez-vous enregistrer ce devis ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Veuillez saisir les informations pour le devis :");
            System.out.print("Date d'émission (AAAA-MM-JJ) : ");
            LocalDate issueDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Date de validité (AAAA-MM-JJ) : ");
            LocalDate validityDate = LocalDate.parse(scanner.nextLine());


            quoteService.saveQuote(finalCost, issueDate, validityDate, false, project.getId());
            System.out.println("Devis enregistré avec succès.");
        }

    }

    private int getValidOption(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option >= min && option <= max) {
                    return option;
                } else {
                    System.out.println("Veuillez entrer une option valide entre " + min + " et " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
    }

    private double getValidDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
            }
        }
    }
}
