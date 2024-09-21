package org.wora.service;

import java.util.List;

public interface ComponentService <T> {
    void add(T component, int projectId);
    List<T> findByProjectId(int projectId);
    void updateTaxRate(int componentId, double taxRate);
    int getId(T component);

}
