package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.data.DiscountResponseHolder;
import com.grad.project.nc.controller.api.dto.FrontEndProductRegionPrice;
import com.grad.project.nc.controller.api.dto.FrontendDiscount;
import com.grad.project.nc.controller.api.dto.FrontendRegion;
import com.grad.project.nc.model.Discount;
import com.grad.project.nc.service.discount.DiscountService;
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


    private DiscountService discountService;

    @Autowired
    public AdminDiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public DiscountResponseHolder add(@RequestBody FrontendDiscount frontendDiscount) {

        Discount discount = FrontendDiscount.toEntity(frontendDiscount);
        DiscountResponseHolder responseHolder = new DiscountResponseHolder();

        log.info("Adding discount " + frontendDiscount.getDiscountTitle() );

        if (!discountService.add(discount)) {
            responseHolder.setMessage("Error during discount adding");
            responseHolder.setStatus("error");
        } else {
            responseHolder.setMessage("Discount added successfully");
            responseHolder.setStatus("success");

        }

        return responseHolder;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public DiscountResponseHolder update(@RequestBody FrontendDiscount frontendDiscount) {
        Discount discount = FrontendDiscount.toEntity(frontendDiscount);
        DiscountResponseHolder responseHolder = new DiscountResponseHolder();

        log.info("Updating discount " + frontendDiscount.getDiscountTitle() + frontendDiscount.getStartDate() );

        if (!discountService.update(discount)) {
            responseHolder.setMessage("Error during discount updating");
            responseHolder.setStatus("error");
        } else {
            responseHolder.setMessage("Discount updated successfully");
            responseHolder.setStatus("success");

        }

        return responseHolder;
    }



    @RequestMapping(value = "/allDiscounts", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendDiscount> getDiscounts() {

        return discountService.getDiscounts().stream()
                .map(FrontendDiscount::fromEntity)
                .collect(Collectors.toList());

    }

    @RequestMapping(value = "/allRegions", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontendRegion> getRegions() {
        return discountService.getRegions().stream()
                .map(FrontendRegion::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/productPricesForRegion", method = RequestMethod.GET)
    @ResponseBody
    public Collection<FrontEndProductRegionPrice> getProductsRegionPricesForRegion(@RequestParam("regionId") Long regionId) {
        log.info( "Found:" + regionId );

        return discountService.getProductsRegionPricesForRegion(regionId).stream()
                .map(FrontEndProductRegionPrice::fromEntity)
                .collect(Collectors.toList());
    }
}
