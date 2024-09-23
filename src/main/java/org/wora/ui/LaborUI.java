package org.wora.ui;


import org.wora.entity.Labor;
import org.wora.entity.Project;
import org.wora.service.ComponentService;
import org.wora.utilitaire.ValidationUtils;

import java.util.Scanner;

import static org.wora.utilitaire.InputScanner.*;

public class LaborUI {
    private ComponentService<Labor> laborService;


    public LaborUI(ComponentService<Labor> laborService) {
        this.laborService = laborService;
    }

    public void addLabor(Project project) {
        Labor labor = new Labor();

        labor.setName(
                scanString("Nom de la main d'oeuvre: ", ValidationUtils.NOT_BLANK)
        );
        labor.setUnitCost(scanDouble("Coût unitaire : ", ValidationUtils.POSITIVE_DOUBLE));

        labor.setQuantity(
                scanInt("Quantité : ",ValidationUtils.POSITIVE_INT));

        labor.setHourlyRate(scanDouble("Taux horaire:",ValidationUtils.POSITIVE_DOUBLE));

        labor.setWorkHours(scanDouble("Heures de travail : ",ValidationUtils.POSITIVE_DOUBLE));

        labor.setWorkerProductivity(scanDouble("Productivité des travailleurs",ValidationUtils.POSITIVE_DOUBLE));

        labor.setProject(project);

        laborService.add(labor, project.getId());

        System.out.println("Main-d'œuvre ajoutée au projet.");

        Boolean addMore = scanBoolean("Voulez-vous ajouter de la main-d'œuvre au projet ? (oui/non)");
        if (addMore) {
            addLabor(project);
        }
    }
}


