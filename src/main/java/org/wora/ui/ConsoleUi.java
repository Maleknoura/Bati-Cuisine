package org.wora.ui;

import org.wora.entity.Client;
import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.repository.ClientRepository;
import org.wora.repository.ComponentRepository;
import org.wora.service.ComponentService;
import org.wora.service.ProjectService;
import org.wora.service.QuoteService;
import org.wora.utilitaire.ValidationUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

import static org.wora.utilitaire.InputScanner.*;

public class ConsoleUi {
    private ProjectService projectService;
    private ClientRepository clientRepository;
    private ComponentRepository<Labor> laborRepository;
    private ComponentRepository<Material> materialRepository;
    private ClientUI clientUI;
    private LaborUI laborUI;
    private MaterialUI materialUI;
    private QuoteService quoteService;
    private ComponentService<Labor> laborService;

    private ComponentService<Material> materialService;
    private ProjectUI projectUI;

    public ConsoleUi(ProjectService projectService,
                     ClientRepository clientRepository,
                     ComponentRepository<Labor> laborRepository,
                     ComponentRepository<Material> materialRepository,
                     QuoteService quoteService,
                     ClientUI clientUI,
                     ComponentService<Labor> laborService,
                     LaborUI laborUI,
                     MaterialUI materialUI,
                     ProjectUI projectUI,
                     ComponentService<Material> materialService) {
        this.projectService = projectService;
        this.clientRepository = clientRepository;
        this.laborRepository = laborRepository;
        this.materialRepository = materialRepository;
        this.quoteService = quoteService;
        this.clientUI = clientUI;
        this.laborUI = laborUI;
        this.materialUI = materialUI;
        this.projectUI = projectUI;
        this.laborService = laborService;
        this.materialService = materialService;
    }


    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("------- Menu Principal -------");
            System.out.println("1. Créer un projet");
            System.out.println("2. Afficher un projet");
            System.out.println("3. calculer un projet");
            System.out.println("4. Supprimer un projet");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");

            Integer option = scanInt("Choisissez une option: ", ValidationUtils.POSITIVE_INT);

            switch (option) {
                case 1:
                    createProject();
                    break;
                case 2:
                    projectUI.displayAllProjects();
                    break;
                case 3:
                    projectUI.afficherProjet();
                    break;
                case 4:
                    projectUI.deleteProject(scanner, projectService);
                    break;
                case 5:
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option entre 1 et 5.");
            }
        }
    }


    public void createProject() {
        System.out.println("\033[35m==================== Création d'un Nouveau Projet ====================\033[0m");

        Optional<Client> clientOptional = clientUI.handleClientSelection();

        if (clientOptional.isEmpty()) {
            System.out.println("client not found !");
            return;
        }
        final Client client = clientOptional.get();
        Project project = new Project();
        System.out.println("--- Création du Projet ---");

        project.setName(
                scanString("ENtrez le nom: ", ValidationUtils.NOT_BLANK)
        );
        project.setClient(client);
        System.out.println("Client ID associé au projet : " + client.getId());

        projectService.createProject(project);
        System.out.println("Projet créé avec succès. ID du projet : " + project.getId());

        if (scanBoolean("Do you want to add labors: "))
            laborUI.addLabor(project);
        if (scanBoolean("Do you want to add materiels"))
            materialUI.addMaterial(project);


        Boolean applyTva = scanBoolean("Souhaitez-vous appliquer une TVA au project ? (y/n): ");
        double vatPercentage = 0;
        if (applyTva) {
            scanDouble("Entrez le pourcentage de TVA (%): ", ValidationUtils.POSITIVE_DOUBLE);
            for (Material material : materialService.findByProjectId(project.getId())) {
                materialService.updateTaxRate(material.getId(), vatPercentage);
            }
            for (Labor labor : laborService.findByProjectId(project.getId())) {
                laborService.updateTaxRate(labor.getId(), vatPercentage);
            }
        }
        Boolean applyMargin = scanBoolean("Do you want to appy profit margin: ");
        Double marginPercentage = 0.0;
        if (applyMargin) {
            marginPercentage = scanDouble("Please to enter the profit margin : ", ValidationUtils.POSITIVE_DOUBLE);
            projectService.updateProfitMargin(project.getId(), marginPercentage);
        }
        System.out.println("\033[35m==================== Calcul du Coût ====================\033[0m");

        double totalMaterialCost = projectService.calculateTotalMaterialCost(project.getId());
        double totalLaborCost = projectService.calculateTotalLaborCost(project.getId());
        double totalCostBeforeDiscount = totalMaterialCost + totalLaborCost;


        System.out.printf("%50s: %10.2f\n", "Coût total avant remise", totalCostBeforeDiscount);

        double totalCostWithDiscount = projectService.calculateTotalCostWithDiscount(project.getId());
        System.out.printf("%50s: %10.2f\n", "Coût total après remise", totalCostWithDiscount);

        System.out.printf("\n%50s\n", "-------- Devis --------");
        System.out.printf("%50s: %10.2f\n", "Coût total des matériaux", totalMaterialCost);
        System.out.printf("%50s: %10.2f\n", "Coût total de la main-d'œuvre", totalLaborCost);

        double finalCost = totalCostWithDiscount;
        System.out.printf("%50s: %10.2f\n", "Coût total avant marge & TVA", finalCost);

        if (applyMargin) {
            double marginAmount = finalCost * marginPercentage / 100;
            finalCost += marginAmount;
            System.out.printf("%50s: %10.2f\n", "Marge bénéficiaire", marginAmount);
            System.out.printf("%50s: %10.2f\n", "Coût avant TVA", finalCost);
        }

        if (applyTva) {
            double vatAmount = finalCost * vatPercentage / 100;
            finalCost += vatAmount;
            System.out.printf("%50s: %10.2f\n", "Montant de la TVA", vatAmount);
        }

        System.out.println("\033[35m==================== Coût Final du Projet ====================\033[0m");
        System.out.printf("%50s: %10.2f\n", "Coût total du projet après TVA", finalCost);

        projectService.updateTotalCost(project.getId(), finalCost);

        if (scanBoolean("Souhaitez-vous enregistrer ce devis ? (y/n): ")) {
            LocalDate issueDate = scanDate("Date d'émission (AAAA-MM-JJ) : ", ValidationUtils.FUTURE_DATE);
            LocalDate validityDate = scanDate("Date de validité (AAAA-MM-JJ) : ", ValidationUtils.combine(
                    ValidationUtils.FUTURE_DATE,
                    issueDate::isBefore
            ));
            quoteService.saveQuote(finalCost, issueDate, validityDate, false, project.getId());
            System.out.printf("%50s\n", "Devis enregistré avec succès.");
        }

        System.out.printf("%50s\n", "Le projet et toutes ses ressources ont été créés avec succès.");
    }


}
