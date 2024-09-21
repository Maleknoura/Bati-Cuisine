package org.wora.ui;



import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.service.ComponentService;

import java.util.Scanner;

public class MaterialUI {
    private ComponentService<Material> materialService;

    public MaterialUI(ComponentService<Material> materialService) {
        this.materialService = materialService;
    }

    public void addMaterial(Scanner scanner, Project project) {
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
                System.out.print("Coût de transport : ");
                material.setTransportCost(Double.parseDouble(scanner.nextLine()));


                System.out.print("Coefficient de qualité : ");
                material.setQualityCoefficient(Double.parseDouble(scanner.nextLine()));

                material.setProject(project);

                materialService.add(material, project.getId());
                System.out.println("Matériel ajouté au projet.");
            } else {
                break;
            }
        }
    }
}

