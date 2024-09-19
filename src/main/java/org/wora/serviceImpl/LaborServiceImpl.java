package org.wora.serviceImpl;

import org.wora.entity.Labor;
import org.wora.repository.ComponentRepository;
import org.wora.service.ComponentService;

import java.util.List;

public class LaborServiceImpl implements ComponentService<Labor> {
    private final ComponentRepository<Labor> laborRepository;

    public LaborServiceImpl(ComponentRepository<Labor> laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    public void add(Labor labor, int projectId) {
        laborRepository.add(labor, projectId);
    }

    public List<Labor> findByProjectId(int projectId) {
        return laborRepository.findByProjectId(projectId);
    }
}
