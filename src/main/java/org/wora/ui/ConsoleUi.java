package org.wora.ui;

import org.wora.entity.Client;
import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.entity.Enum.Status;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.service.ProjectService;

import java.sql.Connection;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUi {
    private final Connection connection;
    private final ProjectService projectService;
    private final ClientRepository clientRepository;
    private final ComponentRepository<Labor> laborRepository;
    private final ComponentRepository<Material> materialRepository;

    public ConsoleUi(Connection connection, ProjectService projectService, ClientRepository clientRepository,
                     ComponentRepository<Labor> laborRepository, ComponentRepository<Material> materialRepository) {
        this.connection = connection;
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;
    }

    public void createProject() {
        Scanner scanner = new Scanner(System.in);

        try {
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

            // Calcul des coûts
            calculateCosts(scanner, project);

            System.out.println("Le projet et toutes ses ressources ont été créés avec succès.");
        } finally {
            scanner.close();
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
        } else if (option == 2) {
            return createNewClient(scanner);
        }
        return null;
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
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
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
        String isProfessionnelInput = scanner.nextLine();
        boolean isProfessionnel = isProfessionnelInput.equalsIgnoreCase("oui");

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
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("oui")) {
                Labor labor = new Labor();

                System.out.print("Nom de la main-d'œuvre : ");
                labor.setName(scanner.nextLine());

                System.out.print("Coût unitaire : ");
                labor.setUnitCost(getValidDouble(scanner));

                System.out.print("Quantité : ");
                labor.setQuantity(getValidDouble(scanner));

                System.out.print("Taux de taxe : ");
                labor.setTaxRate(getValidDouble(scanner));

                System.out.print("Taux horaire : ");
                labor.setHourlyRate(getValidDouble(scanner));

                System.out.print("Heures de travail : ");
                labor.setWorkHours(getValidDouble(scanner));

                System.out.print("Productivité des travailleurs : ");
                labor.setWorkerProductivity(getValidDouble(scanner));

                labor.setProject(project);

                laborRepository.add(labor, project.getId());

                System.out.println("Main-d'œuvre ajoutée au projet.");
            } else {
                break;
            }
        }
    }

    private void addMaterial(Scanner scanner, Project project) {
        while (true) {
            System.out.println("Voulez-vous ajouter du matériel au projet ? (oui/non)");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("oui")) {
                Material material = new Material();

                System.out.print("Nom du matériel : ");
                material.setName(scanner.nextLine());

                System.out.print("Coût unitaire : ");
                material.setUnitCost(getValidDouble(scanner));

                System.out.print("Quantité : ");
                material.setQuantity(getValidDouble(scanner));

                System.out.print("Taux de taxe : ");
                material.setTaxRate(getValidDouble(scanner));

                System.out.print("Coût de transport : ");
                material.setTransportCost(getValidDouble(scanner));

                System.out.print("Coefficient de qualité : ");
                material.setQualityCoefficient(getValidDouble(scanner));

                material.setProject(project);

                materialRepository.add(material, project.getId());
                System.out.println("Matériel ajouté au projet.");
            } else {
                break;
            }
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

        System.out.println("Calcul du coût en cours...");

        double totalMaterialCost = projectService.calculateTotalMaterialCost(project.getId());
        double totalLaborCost = projectService.calculateTotalLaborCost(project.getId());

        System.out.println("Coût total des matériaux : " + totalMaterialCost);
        System.out.println("Coût total de la main-d'œuvre : " + totalLaborCost);

        double totalCostBeforeMargin = totalMaterialCost + totalLaborCost;

        if (applyMargin) {
            projectService.updateProfitMargin(project.getId(), marginPercentage / 100);
        }

        double marginAmount = applyMargin ? (totalCostBeforeMargin * marginPercentage / 100) : 0;
        double finalCost = totalCostBeforeMargin + marginAmount;

        System.out.println("Coût total du projet avant TVA : " + totalCostBeforeMargin);
        System.out.println("Montant de la marge bénéficiaire : " + marginAmount);

        if (applyVAT) {
            double vatAmount = finalCost * vatPercentage / 100;
            finalCost += vatAmount;
            System.out.println("Montant de la TVA : " + vatAmount);
        }

        System.out.println("Coût total du projet après TVA : " + finalCost);
    }


    private int getValidOption(Scanner scanner, int min, int max) {
        int option = -1;
        while (option < min || option > max) {
            try {
                System.out.print("Entrez une option (" + min + "-" + max + ") : ");
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Option invalide. Veuillez entrer un nombre.");
            }
        }
        return option;
    }

    private double getValidDouble(Scanner scanner) {
        double value = -1;
        while (value < 0) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.println("La valeur ne peut pas être négative. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valeur invalide. Veuillez entrer un nombre.");
            }
        }
        return value;
    }
}
