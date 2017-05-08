package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Location;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.RegionDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LocationProxy extends Location {
    @Getter @Setter
    private Long regionId;

    private boolean regionLoaded;

    private RegionDao regionDao;

    @Autowired
    public LocationProxy(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    @Override
    public Region getRegion() {
        if (!regionLoaded) {
            this.setRegion(regionDao.find(getRegionId()));
        }
        return super.getRegion();
    }

    @Override
    public void setRegion(Region region) {
        regionLoaded = true;
        super.setRegion(region);
    }
}