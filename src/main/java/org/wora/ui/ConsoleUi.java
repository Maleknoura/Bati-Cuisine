package org.wora.ui;

import org.wora.entity.Client;
import org.wora.entity.Enum.Status;
import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.service.ProjectService;
import org.wora.service.serviceImpl.LaborServiceImpl;
import org.wora.service.serviceImpl.MaterialServiceImpl;
import org.wora.service.QuoteService;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleUi {
    private Connection connection;
    private ProjectService projectService;
    private ClientRepository clientRepository;
    private ComponentRepository<Labor> laborRepository;
    private ComponentRepository<Material> materialRepository;
    private ClientUI clientUI;
    private LaborUI laborUI;
    private MaterialUI materialUI;
    private QuoteService quoteService;
    private LaborServiceImpl laborService;
    private MaterialServiceImpl materialService;

    public ConsoleUi(Connection connection, ProjectService projectService,
                     ClientRepository clientRepository,
                     ComponentRepository<Labor> laborRepository,
                     ComponentRepository<Material> materialRepository,
                     QuoteService quoteService) {
        this.connection = connection;
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;

        this.clientUI = new ClientUI(clientRepository);
        this.laborService = new LaborServiceImpl(laborRepository);
        this.materialService = new MaterialServiceImpl(materialRepository);

        this.laborUI = new LaborUI(laborService);
        this.materialUI = new MaterialUI(materialService);
        this.quoteService = quoteService;
    }


    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("------- Menu Principal -------");
            System.out.println("1. Créer un projet");
            System.out.println("2. Afficher un projet");
            System.out.println("3. Modifier un projet");
            System.out.println("4. Supprimer un projet");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createProject(scanner);
                    break;
                case 2:
                    System.out.println("Afficher le projet");
                    break;
                case 3:
                    System.out.println("Modifier le projet");
                    break;
                case 4:
                    System.out.println("Supprimer le projet");
                    break;
                case 5:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option entre 1 et 5.");
            }
        }
    }

    public void createProject(Scanner scanner) {
        System.out.println("------- Création d'un Nouveau Projet -----");

        Client client = clientUI.handleClientSelection(scanner);

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

        laborUI.addLabor(scanner, project);
        materialUI.addMaterial(scanner, project);

        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        boolean applyVAT = scanner.nextLine().equalsIgnoreCase("y");
        double vatPercentage = 0;
        if (applyVAT) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            vatPercentage = Double.parseDouble(scanner.nextLine());
            System.out.println("tva from user "+vatPercentage);
            for (Material material : materialService.findByProjectId(project.getId()))
            {
                System.out.println("id du projet " + project.getId());
                materialService.updateTaxRate(material.getId(), vatPercentage);
                System.out.println(" material id "+ material.getId() + " tax rate" + vatPercentage);
            }


            for (Labor labor : laborService.findByProjectId(project.getId())) {
                laborService.updateTaxRate(labor.getId(), vatPercentage);
            }
        }



        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        boolean applyMargin = scanner.nextLine().equalsIgnoreCase("y");
        double marginPercentage = 0;
        if (applyMargin) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginPercentage = Double.parseDouble(scanner.nextLine());
            projectService.updateProfitMargin(project.getId(), marginPercentage);
            System.out.println("Profit margin après update: " + project.getProfitMargin());


        }

        System.out.println("Calcul du coût en cours...");
        double totalMaterialCost = projectService.calculateTotalMaterialCost(project.getId());
        double totalLaborCost = projectService.calculateTotalLaborCost(project.getId());
        System.out.println("Coût total des matériaux : " + totalMaterialCost);
        System.out.println("Coût total de la main-d'œuvre : " + totalLaborCost);
        double totalCostBeforeMargin = totalMaterialCost + totalLaborCost;

        double finalCost = totalCostBeforeMargin;
        if (applyMargin) {
            double marginAmount = (totalCostBeforeMargin * marginPercentage / 100);
            finalCost += marginAmount;
            System.out.println("Montant de la marge bénéficiaire : " + marginAmount);
        }

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

        System.out.println("Le projet et toutes ses ressources ont été créés avec succès.");
    }


}
