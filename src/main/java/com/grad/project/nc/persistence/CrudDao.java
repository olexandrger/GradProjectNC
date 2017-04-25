package com.grad.project.nc.persistence;

import java.util.Collection;

public interface CrudDao<T> {
    T add(T entity);
    T update(T entity);
    //add optional here?
    T find(long id);
    Collection<T> findAll();
    void delete(T entity);
}
