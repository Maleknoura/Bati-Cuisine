package org.wora.serviceImpl;

import org.wora.entity.Labor;
import org.wora.entity.Material;
import org.wora.entity.Project;
import org.wora.repository.ProjectRepository;
import org.wora.service.ProjectService;

import java.util.Optional;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }
    @Override
    public double calculateTotalMaterialCost(int projectId) {
        Optional<Project> projectOpt = projectRepository.getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return project.getComponents().stream()
                    .filter(c -> c instanceof Material)
                    .mapToDouble(c -> ((Material) c).getCost())
                    .sum();
        }
        throw new RuntimeException("Project not found");
    }

    @Override
    public double calculateTotalLaborCost(int projectId) {
        Optional<Project> projectOpt = projectRepository.getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return project.getComponents().stream()
                    .filter(c -> c instanceof Labor)
                    .mapToDouble(c -> ((Labor) c).getCost())
                    .sum();
        }
        throw new RuntimeException("Project not found");
    }

    @Override
    public double calculateTotalCostProject(int projectId) {
        double materialCost = calculateTotalMaterialCost(projectId);
        double laborCost = calculateTotalLaborCost(projectId);
        return materialCost + laborCost;
    }

    @Override
    public double calculateCostWithProfitMargin(int projectId) {
        Optional<Project> projectOpt = projectRepository.getProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            double totalCost = calculateTotalCostProject(projectId);
            return totalCost * (1 + project.getProfitMargin());
        }
        throw new RuntimeException("Project not found");
    }

    @Override
    public double calculateCostWithVAT(int projectId, double vatRate) {
        double totalCost = calculateTotalCostProject(projectId);
        return totalCost * (1 + vatRate);
    }
}
