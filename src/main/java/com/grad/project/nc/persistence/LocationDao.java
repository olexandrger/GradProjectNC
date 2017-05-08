package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Location;

import java.util.List;

public interface LocationDao extends CrudDao<Location> {

    Location findAddressLocationById(Long addressId);

    List<Location> findByRegionId(Long regionId);
}
