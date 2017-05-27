package com.grad.project.nc.service.discount;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.RegionDao;
import com.grad.project.nc.service.exceptions.DiscountException;
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
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String DISCOUNT_RANGE = "Error! Discount must be between 0 to 100 percent!";
    private static final String DISCOUNT_DATE = "Error! Start date must be before end date!";
    private static final String DUPLICATE_PRODUCT = "Some of added products already have a discount";
    private static final String GENERAL_ADD_ERROR = "Error during adding";
    private static final String GENERAL_UPDATE_ERROR = "Error during updating";
    private static final String ADDED_SUCCESSFULLY = "Discount added successfully!";
    private static final String UPDATED_SUCCESSFULLY = "Discount updated successfully!";

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
    public void add(Discount discount) throws DiscountException {
        addedDiscountId = discount.getDiscountId();

        try {
            validate(discount);
            addedDiscountId = discountDao.add(discount).getDiscountId();
            message = ADDED_SUCCESSFULLY;
            status = SUCCESS;
        }
        catch (DuplicateKeyException dk){
            status = ERROR;
            throw new DiscountException(DUPLICATE_PRODUCT);
        }
        catch (DataAccessException e){
            status = ERROR;
            throw new DiscountException(GENERAL_ADD_ERROR);
        }

    }

    @Override
    public void update(Discount discount) throws DiscountException {

        try {
            validate(discount);
            discountDao.update(discount);
            message = UPDATED_SUCCESSFULLY;
            status = SUCCESS;
        }
        catch (DuplicateKeyException dk){
            status = ERROR;
            throw new DiscountException(DUPLICATE_PRODUCT);
        }
        catch (DataAccessException e){
            status = ERROR;
            throw new DiscountException(GENERAL_UPDATE_ERROR);
        }

    }

    private void validate(Discount discount) throws DiscountException{

        if (!discount.getEndDate().isAfter(discount.getStartDate())){
            message = DISCOUNT_DATE;
            status = ERROR;

            throw new DiscountException(DISCOUNT_DATE);
        }
        if (!(discount.getDiscount() >= 0.0 && discount.getDiscount() <= 100.0)){
            message = DISCOUNT_RANGE;
            status = ERROR;

            throw new DiscountException(DISCOUNT_RANGE);
        }

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
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
