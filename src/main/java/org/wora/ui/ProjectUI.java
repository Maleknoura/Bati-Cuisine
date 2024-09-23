package org.wora.ui;

import org.wora.entity.*;
import org.wora.service.ProjectService;

import java.util.List;
import java.util.Scanner;

public class ProjectUI {
    private ProjectService projectService;

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

}
