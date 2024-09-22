package org.wora.service;

import org.wora.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    void createProject(Project project);

    Optional<Project> getProjectById(int projectId);

    double calculateTotalMaterialCost(int projectId);

    double calculateTotalLaborCost(int projectId);

    double calculateTotalCostProject(int projectId);

    double calculateCostWithProfitMargin(int projectId);
    List<Project> displayAllProjects();

    double calculateCostWithVAT(int projectId, double vatRate);
    void updateProfitMargin(int projectId, double profitMargin);
    void updateTaxRate(int componentId, double taxRate);
    void updateTotalCost(int projectId, double totalCost);
}
