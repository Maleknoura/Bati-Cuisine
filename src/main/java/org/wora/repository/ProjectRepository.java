package org.wora.repository;

import org.wora.entity.Project;


import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    void createProject(Project project);

    Optional<Project> getProjectById(int projectId);

    List<Project> displayAllProjects();


    void deleteProject(int projectId);
    void updateProfitMargin(int projectId, double profitMargin);
    void updateTotalCost(int projectId, double totalCost);

}
