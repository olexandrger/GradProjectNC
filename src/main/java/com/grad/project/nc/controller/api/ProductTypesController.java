package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.controller.api.dto.FrontendProductType;
import com.grad.project.nc.service.product.type.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class ProductTypesController {

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypesController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @RequestMapping(value = "/dataTypes", method = RequestMethod.GET)
    public List<FrontendCategory> getDataTypes() {
        return productTypeService.findProductCharacteristicDataTypes()
                .stream()
                .map(FrontendCategory::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/productTypes/get/{id}", method = RequestMethod.GET)
    public FrontendProductType getById(@PathVariable("id") Long id) {
        return FrontendProductType.fromEntity(productTypeService.find(id));
    }

    @RequestMapping(value = "/productTypes/all", method = RequestMethod.GET)
    public List<FrontendProductType> getAll() {
        return productTypeService.findAll()
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());
    }
}
