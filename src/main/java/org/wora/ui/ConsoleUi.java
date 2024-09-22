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
import org.wora.service.serviceImpl.ProjectServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleUi {
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
    private ProjectUI projectUI;

    public ConsoleUi(ProjectService projectService,
                     ClientRepository clientRepository,
                     ComponentRepository<Labor> laborRepository,
                     ComponentRepository<Material> materialRepository,
                     QuoteService quoteService,
                     ClientUI clientUI,
                     LaborUI laborUI,
                     MaterialUI materialUI,
                     ProjectUI projectUI) {
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;
        this.quoteService = quoteService;
        this.clientUI = clientUI;
        this.laborUI = laborUI;
        this.materialUI = materialUI;
        this.projectUI = projectUI;
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
                    projectUI.displayAllProjects();
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

        System.out.println("\033[35m==================== Création d'un Nouveau Projet ====================\033[0m");

        Client client = clientUI.handleClientSelection(scanner);

        if (client == null) {
            System.out.println("Échec de la sélection du client.");
            return;
        }

        Project project = new Project();
        System.out.println("--- Création du Projet ---");

        System.out.print("Nom du projet : ");
        project.setName(scanner.nextLine());

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
            for (Material material : materialService.findByProjectId(project.getId())) {
                materialService.updateTaxRate(material.getId(), vatPercentage);
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
        }


        System.out.println("\033[35m==================== Calcul du Coût ====================\033[0m");

        double totalMaterialCost = projectService.calculateTotalMaterialCost(project.getId());
        double totalLaborCost = projectService.calculateTotalLaborCost(project.getId());


        System.out.printf("\n%50s\n", "-------- Devis --------");
        System.out.printf("%50s: %10.2f\n", "Coût total des matériaux", totalMaterialCost);
        System.out.printf("%50s: %10.2f\n", "Coût total de la main-d'œuvre", totalLaborCost);

        double totalCostBeforeMargin = totalMaterialCost + totalLaborCost;
        System.out.printf("%50s: %10.2f\n", "Coût total avant marge & TVA", totalCostBeforeMargin);

        double finalCost = totalCostBeforeMargin;
        if (applyMargin) {
            double marginAmount = totalCostBeforeMargin * marginPercentage / 100;
            finalCost += marginAmount;
            System.out.printf("%50s: %10.2f\n", "Marge bénéficiaire", marginAmount);
            System.out.printf("%50s: %10.2f\n", "Coût avant TVA", finalCost);
        }

        if (applyVAT) {
            double vatAmount = finalCost * vatPercentage / 100;
            finalCost += vatAmount;
            System.out.printf("%50s: %10.2f\n", "Montant de la TVA", vatAmount);
        }


        System.out.println("\033[35m==================== Coût Final du Projet ====================\033[0m");
        System.out.printf("%50s: %10.2f\n", "Coût total du projet après TVA", finalCost);

        projectService.updateTotalCost(project.getId(), finalCost);


        System.out.print("Souhaitez-vous enregistrer ce devis ? (y/n) : ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Date d'émission (AAAA-MM-JJ) : ");
            LocalDate issueDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Date de validité (AAAA-MM-JJ) : ");
            LocalDate validityDate = LocalDate.parse(scanner.nextLine());
            quoteService.saveQuote(finalCost, issueDate, validityDate, false, project.getId());
            System.out.printf("%50s\n", "Devis enregistré avec succès.");
        }

        System.out.printf("%50s\n", "Le projet et toutes ses ressources ont été créés avec succès.");
    }





}
