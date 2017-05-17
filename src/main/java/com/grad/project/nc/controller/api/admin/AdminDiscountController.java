package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontEndProductRegionPrice;
import com.grad.project.nc.controller.api.dto.FrontendDiscount;
import com.grad.project.nc.controller.api.dto.FrontendRegion;
import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.CrudDao;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.RegionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Alex on 5/16/2017.
 */
@RestController
@RequestMapping("/api/admin/discounts")
@Slf4j
public class AdminDiscountController {

    private CrudDao<Region> regionsDao;
    private CrudDao<Discount> discountDao;
    private ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public AdminDiscountController(RegionDao regionsDao, DiscountDao discountDao, ProductRegionPriceDao productRegionPriceDao) {
        this.regionsDao = regionsDao;
        this.discountDao = discountDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @RequestMapping(value = "/allDiscounts", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendDiscount> getDiscounts() {

        return discountDao.findAll().stream()
                .map(FrontendDiscount::fromEntity)
                .collect(Collectors.toList());

    }

    @RequestMapping(value = "/allRegions", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendRegion> getRegions() {
        return regionsDao.findAll().stream()
                .map(FrontendRegion::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/productPricesForRegion", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontEndProductRegionPrice> getProductsRegionPricesForRegion(@RequestParam("regionId") Long regionId) {
        log.info( "Found:" + regionId );

        return productRegionPriceDao.findByRegionId(regionId).stream()
                .map(FrontEndProductRegionPrice::fromEntity)
                .collect(Collectors.toList());
    }
}
