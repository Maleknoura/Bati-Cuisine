package org.wora.serviceImpl;

import org.wora.entity.Material;
import org.wora.repository.ComponentRepository;
import org.wora.service.ComponentService;

import java.util.List;

public class MaterialServiceImpl implements ComponentService<Material> {
    private final ComponentRepository<Material> materialRepository;

    public MaterialServiceImpl(ComponentRepository<Material> materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Override
    public void add(Material material, int projectId) {
        materialRepository.add(material, projectId);
    }

    public List<Material> findByProjectId(int projectId) {
        return materialRepository.findByProjectId(projectId);
    }
}
