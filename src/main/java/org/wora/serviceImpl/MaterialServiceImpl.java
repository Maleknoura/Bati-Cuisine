package org.wora.serviceImpl;

import org.wora.entity.Material;
import org.wora.repositoryImpl.MaterialRepositoryImpl;
import org.wora.service.ComponentService;

public class MaterialServiceImpl implements ComponentService<Material> {
    private final MaterialRepositoryImpl materialRepository;

    public MaterialServiceImpl(MaterialRepositoryImpl materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public void add(Material material, int projectId) {
        materialRepository.add(material, projectId);
    }


}

