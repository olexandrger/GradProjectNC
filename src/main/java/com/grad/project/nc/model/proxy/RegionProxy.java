package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Location;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.LocationDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class RegionProxy extends Region {

    private final LocationDao locationDao;
    private final ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public RegionProxy(LocationDao locationDao, ProductRegionPriceDao productRegionPriceDao) {
        this.locationDao = locationDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @Override
    public List<Location> getLocations() {
        if (super.getLocations() == null) {
            super.setLocations(locationDao.findByRegionId(getRegionId()));
        }

        return super.getLocations();
    }

    @Override
    public List<ProductRegionPrice> getPrices() {
        if (super.getPrices() == null) {
            super.setPrices(productRegionPriceDao.findByRegionId(getRegionId()));
        }

        return super.getPrices();
    }
}
