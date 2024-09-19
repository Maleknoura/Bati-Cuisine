package org.wora.serviceImpl;

import org.wora.entity.Labor;
import org.wora.repositoryImpl.LaborRepositoryImpl;
import org.wora.service.ComponentService;

public class LaborServiceImpl implements ComponentService<Labor> {
    private final LaborRepositoryImpl laborRepository;

    public LaborServiceImpl(LaborRepositoryImpl laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    public void add(Labor labor, int projectId) {
        laborRepository.add(labor, projectId);
    }


}

