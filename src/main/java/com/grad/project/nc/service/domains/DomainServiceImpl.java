package com.grad.project.nc.service.domains;

import com.grad.project.nc.controller.api.dto.FrontendAddress;
import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainServiceImpl implements DomainService {
    private DomainDao domainDao;
    private LocationDao locationDao;
    private AddressDao addressDao;
    private CategoryDao categoryDao;
    private RegionDao regionDao;
    private LocationService locationService;

    @Autowired
    public DomainServiceImpl(DomainDao domainDao, LocationDao locationDao, AddressDao addressDao,
                             CategoryDao categoryDao, RegionDao regionDao, LocationService locationService) {
        this.domainDao = domainDao;
        this.locationDao = locationDao;
        this.addressDao = addressDao;
        this.categoryDao = categoryDao;
        this.regionDao = regionDao;
        this.locationService = locationService;
    }

    @Override
    public Domain find(Long id) {
        return domainDao.find(id);
    }

    @Override
    public void add(Domain domain) {
        locationDao.add(domain.getAddress().getLocation());
        addressDao.add(domain.getAddress());
        domainDao.add(domain);
        for (User user : domain.getUsers()) {
            domainDao.addDomainUser(domain.getDomainId(), user.getUserId());
        }
    }

    @Override
    public void update(Domain domain) {
        locationDao.update(domain.getAddress().getLocation());
        addressDao.update(domain.getAddress());
        domainDao.update(domain);
        //TODO update all domain users
        //Collection<User> domainUsers = domainDao.fin
        for (User user : domain.getUsers()) {
            domainDao.addDomainUser(domain.getDomainId(), user.getUserId());
        }
    }

    @Override
    public void delete(Domain domain) {
        locationDao.delete(domain.getAddress().getLocation().getLocationId());
        addressDao.delete(domain.getAddress().getAddressId());
        //TODO delete all domain users
        domainDao.delete(domain.getDomainId());
    }

    @Override
    public List<Domain> findByUserId(Long id) {
        return domainDao.findByUserId(id);
    }

    @Override
    public Domain convertFrontendDomainToDomain(FrontendDomain frontendDomain) {
        Domain domain = new Domain();
        domain.setDomainName(frontendDomain.getDomainName());
        domain.setAddress(convertFrontendAddresToAddress(frontendDomain.getAddress()));
        domain.setDomainType(convertFrontendCategoryToCategory(frontendDomain.getDomainType()));
        return domain;
    }

    private Address convertFrontendAddresToAddress(FrontendAddress frontendAddress) {
        Location location = new Location();
        locationService.doRequestForJSONByAddress(frontendAddress.getCity() + " " + frontendAddress.getStreet() + " "
                + frontendAddress.getBuilding());
        location.setGooglePlaceId(locationService.getGooglePlaceId());
        // TODO need method regionDao.findByName()
        //location.setRegion(regionDao.fin);
        Address address = new Address();
        address.setApartmentNumber(frontendAddress.getApartment());
        address.setLocation(location);
        return null;
    }

    private Category convertFrontendCategoryToCategory(FrontendCategory frontendCategory) {
        Category domainType = new Category();
        domainType.setCategoryType(new CategoryType());
        domainType.getCategoryType().setCategoryTypeId(new Long(6));
        domainType.setCategoryName(frontendCategory.getCategoryName());
        return null;
    }
}
