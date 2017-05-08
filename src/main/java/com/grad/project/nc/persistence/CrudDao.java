package com.grad.project.nc.persistence;

import java.util.List;

public interface CrudDao<T> {

    T add(T entity);

    T update(T entity);

    T find(Long id);

    List<T> findAll();

    void delete(Long id);
}
