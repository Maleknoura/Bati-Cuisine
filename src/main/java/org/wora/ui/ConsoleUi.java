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
    private Connection connection;
    private ProjectService projectService;
    private ClientRepository clientRepository;
    private ComponentRepository<Labor> laborRepository;
    private ComponentRepository<Material> materialRepository;

    public ConsoleUi(Connection connection, ProjectService projectService, ClientRepository clientRepository, ComponentRepository<Labor> laborRepository, ComponentRepository<Material> materialRepository) {
        this.connection = connection;
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;
    }

    public void createProject() {
        Scanner scanner = new Scanner(System.in);

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

        System.out.println("Le projet et toutes ses ressources ont été créés avec succès.");
    }

    private Client handleClientSelection(Scanner scanner) {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");

        int option = scanner.nextInt();
        scanner.nextLine();

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
                labor.setUnitCost(Double.parseDouble(scanner.nextLine()));

                System.out.print("Quantité : ");
                labor.setQuantity(Double.parseDouble(scanner.nextLine()));



                System.out.print("Taux de taxe : ");
                labor.setTaxRate(Double.parseDouble(scanner.nextLine()));

                System.out.print("Taux horaire : ");
                labor.setHourlyRate(Double.parseDouble(scanner.nextLine()));

                System.out.print("Heures de travail : ");
                labor.setWorkHours(Double.parseDouble(scanner.nextLine()));

                System.out.print("Productivité des travailleurs : ");
                labor.setWorkerProductivity(Double.parseDouble(scanner.nextLine()));

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
                material.setUnitCost(Double.parseDouble(scanner.nextLine()));

                System.out.print("Quantité : ");
                material.setQuantity(Double.parseDouble(scanner.nextLine()));

                System.out.print("Taux de taxe : ");
                material.setTaxRate(Double.parseDouble(scanner.nextLine()));

                System.out.print("Coût de transport : ");
                material.setTransportCost(Double.parseDouble(scanner.nextLine()));

                System.out.print("Coefficient de qualité : ");
                material.setQualityCoefficient(Double.parseDouble(scanner.nextLine()));

                material.setProject(project);

                materialRepository.add(material, project.getId());
                System.out.println("Matériel ajouté au projet.");
            } else {
                break;
            }
        }
    }
}
