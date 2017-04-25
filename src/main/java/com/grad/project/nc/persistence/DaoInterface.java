package com.grad.project.nc.persistence;

/**
 * Created by Alex on 4/25/2017.
 */
interface GeneralDao<T> {
    T find(int id);
T add(T item);}
