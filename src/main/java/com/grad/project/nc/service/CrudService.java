package com.grad.project.nc.service;

import java.util.List;

public interface CrudService<T> {

    T add(T entity);

    T update(T entity);

    T find(Long id);

    List<T> findAll();
}
