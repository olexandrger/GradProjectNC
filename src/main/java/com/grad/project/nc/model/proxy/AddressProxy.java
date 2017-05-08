package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Location;
import com.grad.project.nc.persistence.impl.LocationDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AddressProxy extends Address {
    private Long locationId;

    private final LocationDaoImpl locationDao;

    @Autowired
    public AddressProxy(LocationDaoImpl locationDao) {
        this.locationDao = locationDao;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public Location getLocation() {
        if (super.getLocation() == null) {
            super.setLocation(locationDao.findAddressLocationById(getAddressId()));
        }
        return super.getLocation();
    }
}