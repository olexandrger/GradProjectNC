package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.model.Location;
import com.grad.project.nc.persistence.LocationDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AddressProxy extends Address {
    @Getter @Setter
    private Long locationId;

    private final LocationDao locationDao;

    private boolean locationLoaded;

    @Autowired
    public AddressProxy(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public Location getLocation() {
        if (!locationLoaded) {
            this.setLocation(locationDao.findAddressLocationById(getAddressId()));
        }
        return super.getLocation();
    }

    @Override
    public void setLocation(Location location) {
        locationLoaded = true;
        super.setLocation(location);
    }
}