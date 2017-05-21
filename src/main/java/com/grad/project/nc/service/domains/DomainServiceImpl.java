package com.grad.project.nc.service.domains;

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
    public void delete(Domain domain) {
        domainDao.deleteDomainUsers(domain.getDomainId());
        domainDao.delete(domain.getDomainId());
        addressDao.delete(domain.getAddress().getAddressId());

        //TODO why result is null??
        //locationDao.delete(locationDao.findAddressLocationById(domain.getAddress().getAddressId()).getLocationId());
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
        //Commented because of incompatible type error must something like this domain.setUsers(frontendDomain.getUsers().stream().map(FrontendUser::toModel).collect(Collectors.toList()));
        //domain.setUsers(frontendDomain.getUsers());
        return domain;
    }

    @Override
    public void update(Domain domain) {
        //TODO why don't work updates?
        locationDao.update(domain.getAddress().getLocation());
        addressDao.update(domain.getAddress());
        domainDao.update(domain);
        System.out.println("location, address, domain updated");
        //TODO why userDao gives null?
        List<User> oldUsers = userDao.findByDomainId(domain.getDomainId());
        List<User> newUsers = domain.getUsers();
        List<Long> oldUserIds = new ArrayList<>();
        List<Long> newUserIds = new ArrayList<>();
        System.out.println("old users");
        System.out.println(oldUsers);
        for (User user : oldUsers) {
            oldUserIds.add(user.getUserId());
            System.out.println(user.getUserId());
        }

        System.out.println("new users");
        System.out.println(newUsers);
        for (User user : newUsers) {
            newUserIds.add(user.getUserId());
            System.out.println(user.getUserId());
        }
        deleteOldUsers(oldUserIds, newUserIds, domain);
        addNewUsers(oldUserIds, newUserIds, domain);
    }

    private void deleteOldUsers(List<Long> oldUserIds, List<Long> newUserIds, Domain domain) {
        System.out.println("user deleting");
        for (Long id : oldUserIds) {
            if (!newUserIds.contains(id)) {
                System.out.println("deleting user with id: "+id);
                domainDao.deleteDomainUser(domain.getDomainId(), id);
            }
        }
    }

    private void addNewUsers(List<Long> oldUserIds, List<Long> newUserIds, Domain domain) {
        System.out.println("user add");
        for (Long id : newUserIds) {
            if (!oldUserIds.contains(id)) {
                System.out.println("adding user with id: "+id);
                domainDao.addDomainUser(domain.getDomainId(), id);
            }
        }
    }

    private Address convertFrontendAddresToAddress(FrontendAddress frontendAddress) {
        Location location = new Location();
        locationService.doRequestForJSONByAddress(frontendAddress.getCity() + " " + frontendAddress.getStreet() + " "
                + frontendAddress.getBuilding());
        location.setGooglePlaceId(locationService.getGooglePlaceId());
        location.setRegion(regionDao.findByName(locationService.getRegionName()));
        Address address = new Address();
        address.setApartmentNumber(frontendAddress.getApartment());
        address.setLocation(location);
        return address;
    }

    private Category convertFrontendCategoryToCategory(FrontendCategory frontendCategory) {
        return categoryDao.find(frontendCategory.getCategoryId());
    }
}
