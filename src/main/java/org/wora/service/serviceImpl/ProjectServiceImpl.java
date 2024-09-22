package org.wora.service.serviceImpl;

import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.repository.ProjectRepository;
import org.wora.service.ComponentService;
import org.wora.service.ProjectService;

import java.util.List;
import java.util.Optional;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ComponentService<Material> materialService;
    private final ComponentService<Labor> laborService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ComponentService<Material> materialService, ComponentService<Labor> laborService) {
        this.projectRepository = projectRepository;
        this.materialService = materialService;
        this.laborService = laborService;
    }

    @Override
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }

    @Override
    public Optional<Project> getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    @Override
    public double calculateTotalMaterialCost(int projectId) {
        double totalMaterialCost = 0;
        List<Material> materials = ((MaterialServiceImpl) materialService).findByProjectId(projectId);
        for (Material material : materials) {
            totalMaterialCost += material.getUnitCost() * material.getQuantity();
        }
        return totalMaterialCost;
    }

    @Override
    public double calculateTotalLaborCost(int projectId) {
        double totalLaborCost = 0;
        List<Labor> labors = ((LaborServiceImpl) laborService).findByProjectId(projectId);
        for (Labor labor : labors) {
            totalLaborCost += labor.getHourlyRate() * labor.getWorkHours();
        }
        return totalLaborCost;
    }

    @Override
    public double calculateTotalCostProject(int projectId) {
        double materialCost = calculateTotalMaterialCost(projectId);
        double laborCost = calculateTotalLaborCost(projectId);
        return materialCost + laborCost;
    }

    @Override
    public double calculateCostWithProfitMargin(int projectId) {
        Optional<Project> projectOpt = getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            double totalCost = calculateTotalCostProject(projectId);
            double profitMargin = project.getProfitMargin();
            return totalCost * (1 + profitMargin);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    @Override
    public double calculateCostWithVAT(int projectId, double vatRate) {
        double totalCost = calculateTotalCostProject(projectId);
        return totalCost * (1 + vatRate / 100);
    }

    @Override
    public void updateProfitMargin(int projectId, double profitMargin) {
        Optional<Project> projectOpt = getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            project.setProfitMargin(profitMargin);
            projectRepository.updateProfitMargin(projectId, profitMargin);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    @Override
    public void updateTaxRate(int componentId, double taxRate) {

    }
    @Override
    public void updateTotalCost(int projectId, double totalCost) {
        projectRepository.updateTotalCost(projectId, totalCost);
    }
    @Override
    public List<Project> displayAllProjects() {
        return projectRepository.displayAllProjects();
    }

}
