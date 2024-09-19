package org.wora.service;

import org.wora.entity.Project;
import java.util.Optional;

public interface ProjectService {

    void createProject(Project project);

    Optional<Project> getProjectById(int projectId);

    double calculateTotalMaterialCost(int projectId);

    double calculateTotalLaborCost(int projectId);

    double calculateTotalCostProject(int projectId);

    double calculateCostWithProfitMargin(int projectId);

    double calculateCostWithVAT(int projectId, double vatRate);
    void updateProfitMargin(int projectId, double profitMargin);
}
