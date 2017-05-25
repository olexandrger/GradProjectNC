package com.grad.project.nc.service.discount;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Alex on 5/16/2017.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    private RegionDao regionDao;
    private DiscountDao discountDao;
    private ProductRegionPriceDao productRegionPriceDao;

    private String status;
    private String message;
    private Long addedDiscountId = Long.valueOf(-1);
    private final String SUCCESS = "success";
    private final String ERROR = "error";
    private final String DISCOUNT_RANGE = "Error! Discount must be between 0 to 100 percent!";
    private final String DISCOUNT_DATE = "Error! Start date must be before end date!";

    @Autowired
    public DiscountServiceImpl(RegionDao regionDao, DiscountDao discountDao, ProductRegionPriceDao productRegionPriceDao) {
        this.regionDao = regionDao;
        this.discountDao = discountDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }


    @Override
    public Collection<Discount> getDiscounts() {

        return discountDao.findAll();
    }

    @Override
    public Collection<Discount> getDiscountsPage(Long size, Long offset) {

        return discountDao.findAll(size, offset);
    }

    @Override
    public Collection<Region> getRegions() {

        return regionDao.findAll();
    }


    @Override
    public Collection<ProductRegionPrice> getProductsRegionPricesForRegion(Long regionId) {

        return productRegionPriceDao.findByRegionId(regionId);
    }

    @Override
    public Boolean add(Discount discount) {
        addedDiscountId = discount.getDiscountId();

        try {

            if (validate(discount)){
                addedDiscountId = discountDao.add(discount).getDiscountId();
                message = "Discount added successfully!";
            }
            else {
                return false;
            }
        }
        catch (DuplicateKeyException dk){

            status = ERROR;
            message = "Some of added products already have a discount";
            dk.printStackTrace();
            return false;

        }
        catch (DataAccessException e){
            status = ERROR;
            message = "Error during adding" ;

            return false;
        }

        status = SUCCESS;
        return true;
    }

    @Override
    public Boolean update(Discount discount) {

        try {
            if (validate(discount)){
                discountDao.update(discount);
                message = "Discount updated successfully!";
            }
            else {
                return false;
            }
        }
        catch (DuplicateKeyException dk){

            status = ERROR;
            message = "Some of added products already have a discount";
            dk.printStackTrace();
            return false;

        }
        catch (DataAccessException e){
            e.printStackTrace();

            status = ERROR;
            message = "Error during updating";

            return false;
        }

        status = SUCCESS;

        return true;

    }

    private boolean validate(Discount discount){

        if (!discount.getEndDate().isAfter(discount.getStartDate())){
            message = DISCOUNT_DATE;
            status = ERROR;
            return false;
        }
        if (!(discount.getDiscount() >= 0.0 && discount.getDiscount() <= 100.0)){
            message = DISCOUNT_RANGE;
            status = ERROR;
            return false;
        }

        return true;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAddedDiscountId() {
        return addedDiscountId;
    }

    @Override
    @Transactional
    public Discount findLargestDiscountByPriceId(Long priceId) {
        return discountDao.findLargestDiscountByPriceId(priceId);
    }
}
