package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.FrontendProductType;
import com.grad.project.nc.service.product.type.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/productTypes")
public class ProductTypesController {

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypesController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @RequestMapping(params = {"active", "regionId"}, method = RequestMethod.GET)
    public List<FrontendProductType> getCatalogProductTypes(@RequestParam("regionId") Long regionId) {
        return productTypeService.findValuableAndActiveByRegionId(regionId)
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());
    }
}
