package com.grad.project.nc.service.instances;

import com.grad.project.nc.controller.api.dto.catalog.FrontendCatalogDiscount;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstance;
import com.grad.project.nc.controller.api.dto.instance.FrontendInstanceAddress;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.service.discount.DiscountService;
import com.grad.project.nc.service.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class InstanceServiceImpl implements InstanceService {

    private ProductInstanceDao productInstanceDao;
    private LocationService locationService;
    private DiscountService discountService;

    @Autowired
    public InstanceServiceImpl(ProductInstanceDao productInstanceDao, LocationService locationService, DiscountService discountService) {
        this.productInstanceDao = productInstanceDao;
        this.locationService = locationService;
        this.discountService = discountService;
    }

    @Override
    public ProductInstance getById(Long id) {
        return productInstanceDao.find(id);
    }

    @Override
    public boolean isInstanceOwnedBy(long instanceId, long userId) {
        return productInstanceDao.find(instanceId).getDomain().getUsers().stream().anyMatch(user -> user.getUserId() == userId);
    }

    @Override
    public Collection<ProductInstance> getByDomainId(Long domainId) {
        return productInstanceDao.findByDomainId(domainId);
    }

    @Override
    public Collection<ProductInstance> getAll() {
        return productInstanceDao.findAll();
    }

    @Override
    public Collection<ProductInstance> getAllPage(Long size, Long offset) {
        return productInstanceDao.findAll(size,offset);
    }

    @Override
    public Collection<ProductInstance> getAllByStatusId(Long size, Long offset, Long statusId) {

        return productInstanceDao.findByStatus(size,offset, statusId);
    }

    @Override
    public Collection<ProductInstance> getByUserId(Long id, Long size, Long offset) {
         return productInstanceDao.findByUserId(id, size, offset);
    }

    public FrontendInstance setAddressAndDiscount(FrontendInstance frontendInstance){
        ProductInstance productInstance = getById(frontendInstance.getInstanceId());

        locationService.doRequestForJSONByGooglePlaceId(productInstance.getDomain().getAddress().getLocation().getGooglePlaceId());
        frontendInstance.setAddress(FrontendInstanceAddress.builder()
                .city(locationService.getCity())
                .street(locationService.getStreet())
                .building(locationService.getBuildingNumber())
                .apartment(productInstance.getDomain().getAddress().getApartmentNumber()).build());

        frontendInstance.getPrice().setDiscount(
                (discountService.findLargestDiscountByPriceId(frontendInstance.getPrice().getPriceId()) != null)
                        ? FrontendCatalogDiscount.fromEntity(discountService.findLargestDiscountByPriceId(frontendInstance.getPrice().getPriceId())) : null);


        return frontendInstance;

    }


}
