package org.wora.ui;



import org.wora.entity.Labor;
import org.wora.entity.Project;
import org.wora.service.ComponentService;

import java.util.Scanner;

public class LaborUI {
    private ComponentService<Labor> laborService;


    public LaborUI(ComponentService<Labor> laborService) {
        this.laborService = laborService;
    }

    public void addLabor(Scanner scanner, Project project) {
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


                System.out.print("Taux horaire : ");
                labor.setHourlyRate(Double.parseDouble(scanner.nextLine()));

                System.out.print("Heures de travail : ");
                labor.setWorkHours(Double.parseDouble(scanner.nextLine()));

                System.out.print("Productivité des travailleurs : ");
                labor.setWorkerProductivity(Double.parseDouble(scanner.nextLine()));

                labor.setProject(project);

                laborService.add(labor, project.getId());

                System.out.println("Main-d'œuvre ajoutée au projet.");
            } else {
                break;
            }
        }
    }
}

