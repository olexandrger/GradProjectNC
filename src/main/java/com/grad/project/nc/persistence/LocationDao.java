package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Location;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by DeniG on 30.04.2017.
 */
@Repository
public class LocationDao extends AbstractDao{


    public Location getLocationByAddress(Address address){

    }

    @Override
    public Object add(Object entity) {
        return null;
    }

    @Override
    public Object update(Object entity) {
        return null;
    }

    @Override
    public Object find(long id) {
        return null;
    }

    @Override
    public Collection findAll() {
        return null;
    }

    @Override
    public void delete(Object entity) {

    }
}
