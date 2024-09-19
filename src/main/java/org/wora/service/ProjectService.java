package org.wora.service;

import org.wora.entity.Project;

public interface ProjectService {

    void createProject(Project project);


        double calculateTotalMaterialCost(int projectId);

        double calculateTotalLaborCost(int projectId);

        double calculateTotalCostProject(int projectId);

        double calculateCostWithProfitMargin(int projectId);

        double calculateCostWithVAT(int projectId, double vatRate);
    }


