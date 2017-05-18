package com.grad.project.nc.service.discount;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Alex on 5/16/2017.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    private CrudDao<Region> regionsDao;
    private CrudDao<Discount> discountDao;
    private ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public DiscountServiceImpl(RegionDao regionsDao, DiscountDao discountDao, ProductRegionPriceDao productRegionPriceDao) {
        this.regionsDao = regionsDao;
        this.discountDao = discountDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }


    @Override
    public Collection<Discount> getDiscounts() {

        return discountDao.findAll();
    }

    @Override
    public Collection<Region> getRegions() {

        return regionsDao.findAll();
    }


    @Override
    public Collection<ProductRegionPrice> getProductsRegionPricesForRegion(Long regionId) {

        return productRegionPriceDao.findByRegionId(regionId);
    }

    @Override
    public Boolean add(Discount discount) {

        try {

            discountDao.add(discount);
        }
        catch (DataAccessException e){

            return false;
        }

        return true;
    }

    @Override
    public Boolean update(Discount discount) {

        try {

            discountDao.update(discount);
        }
        catch (DataAccessException e){

            return false;
        }

        return true;

    }


}
