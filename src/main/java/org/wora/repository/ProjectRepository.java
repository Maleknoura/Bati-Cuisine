package org.wora.repository;

import org.wora.entity.Project;

public interface ProjectRepository {
    void save(Project project);
    Project findById(int id);
}
