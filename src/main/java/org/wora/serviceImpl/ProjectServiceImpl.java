package org.wora.serviceImpl;

import org.wora.entity.Project;
import org.wora.repository.ProjectRepository;
import org.wora.service.ProjectService;

public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void createProject(Project project) {
        projectRepository.createProject(project);
    }
}
