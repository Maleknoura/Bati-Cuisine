package org.wora.ui;


import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.service.ComponentService;
import org.wora.utilitaire.ValidationUtils;

import static org.wora.utilitaire.InputScanner.*;

public class MaterialUI {
    private ComponentService<Material> materialService;

    public MaterialUI(ComponentService<Material> materialService) {
        this.materialService = materialService;
    }

    public void addMaterial(Project project) {
        Material material = new Material();
        String name = scanString("Entrez le nom du : ", ValidationUtils.NOT_BLANK);
        material.setName(name);

        material.setUnitCost(scanDouble("Cout unitaire", ValidationUtils.POSITIVE_DOUBLE));

        material.setQuantity(scanInt("Quantité", ValidationUtils.POSITIVE_INT));

        material.setTransportCost(scanDouble("Coût de transport : ", ValidationUtils.POSITIVE_DOUBLE));

        material.setQualityCoefficient(scanDouble("Coefficient de qualité : ", ValidationUtils.POSITIVE_DOUBLE));

        material.setProject(project);

        materialService.add(material, project.getId());
        System.out.println("Matériel ajouté au projet.");

        Boolean addMateriel = scanBoolean("Voulez-bous ajouter du materiel au project ? (oui/non): ");
        if (addMateriel) {
            addMaterial(project);
        }
    }
}

