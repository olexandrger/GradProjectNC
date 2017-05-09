package com.grad.project.nc.persistence;

import java.util.List;

public interface CrudDao<T> {

    T add(T entity);

    T update(T entity);

    T find(Long id);

    List<T> findAll();

    default List<T> findAll(long size, long offset) {
        //TODO implement in all dao
        throw new UnsupportedOperationException();
    }

    void delete(Long id);
}
