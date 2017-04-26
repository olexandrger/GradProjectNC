package com.grad.project.nc.persistence;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface CrudDao<T> {

    @Transactional
    T add(T entity);

    @Transactional
    T update(T entity);
    //add optional here?

    @Transactional
    T find(long id);

    @Transactional
    Collection<T> findAll();

    @Transactional
    void delete(T entity);
}
