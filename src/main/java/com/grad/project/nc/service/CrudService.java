package com.grad.project.nc.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CrudService<T> {

    @Transactional
    T add (T entity);

    @Transactional
    T update (T entity);

    @Transactional
    T find(Long id);

    @Transactional
    List<T> findAll();

    @Transactional
    void delete(T entity);
}
