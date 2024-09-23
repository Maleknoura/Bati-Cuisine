package org.wora.ui;

import org.wora.entity.*;
import org.wora.service.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectUI {
    private ProjectService projectService;
    private static final Scanner scanner = new Scanner(System.in);

    public ProjectUI(ProjectService projectService) {
        this.projectService = projectService;
    }


    public void displayAllProjects() {
        List<Project> projects = projectService.displayAllProjects();


        String title = "Liste des Projets";
        int width = 80;
        int padding = (width - title.length()) / 2;
        System.out.println("\033[35m" + " ".repeat(padding) + title + "\033[0m");


        String format = "%-10s %-30s %-15s %-15s %-15s %-15s\n";
        System.out.printf(format, "ID", "Nom du Projet", "Marge", "Coût Total", "Statut", "Client");
        System.out.println("=========================================================================================");

        for (Project project : projects) {
            Client client = project.getClient();
            System.out.printf(format,
                    project.getId(),
                    project.getName(),
                    project.getProfitMargin(),
                    project.getTotalCost(),
                    project.getStatus(),
                    client.getName()
            );
        }

        System.out.println("===============================================================================");


        for (Project project : projects) {
            System.out.println("\nDétails du Projet ID: " + project.getId());
            for (Component component : project.getComponents()) {
                if (component instanceof Material) {
                    Material material = (Material) component;
                    System.out.println("\033[35mMatériau:\033[0m " + material.getName() +
                            " | Coût Unitaire: " + material.getUnitCost() +
                            " | Quantité: " + material.getQuantity());
                } else if (component instanceof Labor) {
                    Labor labor = (Labor) component;
                    System.out.println("\033[35mMain d'œuvre:\033[0m " + labor.getName() +
                            " | Taux Horaire: " + labor.getHourlyRate() +
                            " | Heures de Travail: " + labor.getWorkHours());
                }
            }
            System.out.println("==============================================");
        }
    }

    public void deleteProject(Scanner scanner, ProjectService projectService) {
        System.out.print("Veuillez saisir l'ID du projet à supprimer : ");
        int projectId = Integer.parseInt(scanner.nextLine());


        System.out.print("Êtes-vous sûr de vouloir supprimer le projet avec l'ID " + projectId + " ? (y/n) : ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            projectService.deleteById(projectId);
            System.out.println("Le projet avec l'ID " + projectId + " a été supprimé avec succès.");
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    public void afficherProjet() {

        System.out.print("\033[35m" + "Entrez l'ID du projet que vous souhaitez afficher : " + "\033[0m");
        int projectId = scanner.nextInt();


        Optional<Project> projectOptional = projectService.FindProjectById(projectId);


        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            System.out.println("\033[1;37m" + "=== Détails du projet ===" + "\033[0m");
            System.out.println("\033[35m" + "Nom du projet : " + "\033[37m" + project.getName());
            System.out.println("\033[35m" + "Marge bénéficiaire : " + "\033[37m" + project.getProfitMargin() + "%");
            System.out.println("\033[35m" + "Coût total : " + "\033[37m" + project.getTotalCost() + "€");
            System.out.println("\033[35m" + "Statut : " + "\033[37m" + project.getStatus());
            System.out.println("\033[35m" + "Client : " + "\033[37m" + project.getClient().getName());
            System.out.println("\033[35m" + "Téléphone du client : " + "\033[37m" + project.getClient().getNumberPhone());


            System.out.println("\033[35m" + "--- Composants ---" + "\033[0m");
            project.getComponents().forEach(component -> {
                if (component instanceof Material) {
                    Material material = (Material) component;
                    System.out.println("\033[35m" + "Type : " + "\033[37m" + "Matériau");
                    System.out.println("\033[35m" + "Nom : " + "\033[37m" + material.getName());
                    System.out.println("\033[35m" + "Coût unitaire : " + "\033[37m" + material.getUnitCost());
                    System.out.println("\033[35m" + "Quantité : " + "\033[37m" + material.getQuantity());
                } else if (component instanceof Labor) {
                    Labor labor = (Labor) component;
                    System.out.println("\033[35m" + "Type : " + "\033[37m" + "Travail");
                    System.out.println("\033[35m" + "Nom : " + "\033[37m" + labor.getName());
                    System.out.println("\033[35m" + "Taux horaire : " + "\033[37m" + labor.getHourlyRate());
                    System.out.println("\033[35m" + "Heures travaillées : " + "\033[37m" + labor.getWorkHours());
                }
            });
        } else {
            System.out.println("\033[31m" + "Projet introuvable pour l'ID : " + projectId + "\033[0m");
        }
    }
}