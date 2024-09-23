package org.wora.repository;

import java.util.List;

public interface ComponentRepository<T> {
     void add(T component, int projectId);
     List<T> findByProjectId(int projectId);
     void updateTaxRate(int componentId, double taxRate);
     int getId(T component);
     void deleteById(int id);

}
