package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Location;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LocationProxy extends Location {
    private Long regionId;

    private RegionDao regionDao;

    @Autowired
    public LocationProxy(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Override
    public Region getRegion() {
        if (super.getRegion() == null) {
            super.setRegion(regionDao.find(getRegionId()));
        }
        return super.getRegion();
    }
}