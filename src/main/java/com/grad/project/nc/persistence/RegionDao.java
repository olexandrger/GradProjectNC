package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Region;

public interface RegionDao extends CrudDao<Region> {

    Region findLocationRegionById(Long locationId);

    Region findByProductRegionPriceId(Long productRegionPriceId);

    Region findByName(String name);
}
