package org.wora.repository;

public interface ComponentRepository<T> {
     void add(T component, int projectId);

}
