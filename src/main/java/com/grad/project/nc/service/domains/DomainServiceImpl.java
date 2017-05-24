package com.grad.project.nc.service.domains;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grad.project.nc.controller.api.dto.FrontendAddress;
import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.controller.api.dto.FrontendDomain;
import com.grad.project.nc.controller.api.dto.FrontendUser;
import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainServiceImpl implements DomainService {
    private DomainDao domainDao;
    private LocationDao locationDao;
    private AddressDao addressDao;
    private CategoryDao categoryDao;
    private RegionDao regionDao;
    private UserDao userDao;
    private LocationService locationService;

    @Autowired
    public DomainServiceImpl(DomainDao domainDao, LocationDao locationDao, AddressDao addressDao,
                             CategoryDao categoryDao, RegionDao regionDao, UserDao userDao, LocationService locationService) {
        this.domainDao = domainDao;
        this.locationDao = locationDao;
        this.addressDao = addressDao;
        this.categoryDao = categoryDao;
        this.regionDao = regionDao;
        this.userDao = userDao;
        this.locationService = locationService;
    }

    @Override
    public Domain find(Long id) {
        return domainDao.find(id);
    }


    //TODO add instances
    @Override
    public void add(Domain domain) {
        domain.getAddress().getLocation().setRegion(
                regionDao.findByName(domain.getAddress().getLocation().getRegion().getRegionName()));
        locationDao.add(domain.getAddress().getLocation());
        addressDao.add(domain.getAddress());
        domain.setDomainType(categoryDao.findCategoryByName(domain.getDomainType().getCategoryName()));
        domainDao.add(domain);
        for (User user : domain.getUsers()) {
            domainDao.addDomainUser(domain.getDomainId(), user.getUserId());
        }
    }

    //TODO delete instances
    @Override
    public void delete(Domain domain) {
        domain.getAddress().setLocation(locationDao.findAddressLocationById(domain.getAddress().getAddressId()));
        domainDao.deleteDomainUsers(domain.getDomainId());
        domainDao.delete(domain.getDomainId());
        addressDao.delete(domain.getAddress().getAddressId());
        locationDao.delete(domain.getAddress().getLocation().getLocationId());
    }

    //TODO get all instances
    @Override
    public List<Domain> getAllDomains(Long id) {
        List<Domain> domains = domainDao.findByUserId(id);
        for (Domain domain : domains) {
            domain.setDomainType(categoryDao.findDomainType(domain.getDomainId()));
            domain.setAddress(addressDao.findDomainAddressById(domain.getDomainId()));
            domain.getAddress().setLocation(locationDao.findAddressLocationById(domain.getAddress().getAddressId()));
            domain.getAddress().getLocation().setRegion(
                    regionDao.findLocationRegionById(domain.getAddress().getLocation().getLocationId()));
            domain.setUsers(userDao.findByDomainId(domain.getDomainId()));
        }
        return domains;
    }

    //TODO update instances
    @Override
    public void update(Domain domain) {
        domain.getAddress().getLocation().setRegion(
                regionDao.findByName(domain.getAddress().getLocation().getRegion().getRegionName()));
        domain.getDomainType().setCategoryId(
                categoryDao.findCategoryByName(domain.getDomainType().getCategoryName()).getCategoryId());
        domain.getAddress().setAddressId(addressDao.findDomainAddressById(domain.getDomainId()).getAddressId());
        domain.getAddress().getLocation().setLocationId(
                locationDao.findAddressLocationById(domain.getAddress().getAddressId()).getLocationId());
        locationDao.update(domain.getAddress().getLocation());
        addressDao.update(domain.getAddress());
        domainDao.update(domain);
        List<User> oldUsers = userDao.findByDomainId(domain.getDomainId());
        List<User> newUsers = domain.getUsers();
        List<Long> oldUserIds = new ArrayList<>();
        List<Long> newUserIds = new ArrayList<>();
        for (User user : oldUsers) {
            oldUserIds.add(user.getUserId());
        }
        for (User user : newUsers) {
            newUserIds.add(user.getUserId());
        }
        usersUpdate(domain.getDomainId(), oldUserIds, newUserIds);
    }

    private void usersUpdate(Long domainId, List<Long> oldUsersIds, List<Long> newUsersIds) {
        for (Long userId : oldUsersIds) {
            if (!newUsersIds.contains(userId)) {
                domainDao.deleteDomainUser(domainId, userId);
            }
        }
        for (Long userId : newUsersIds) {
            if (!oldUsersIds.contains(userId)) {
                domainDao.addDomainUser(domainId, userId);
            }
        }
    }
}
