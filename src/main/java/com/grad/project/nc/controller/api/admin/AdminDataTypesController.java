package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.service.product.type.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/dataTypes")
public class AdminDataTypesController {

    private final ProductTypeService productTypeService;

    @Autowired
    public AdminDataTypesController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FrontendCategory> getDataTypes() {
        return productTypeService.findProductCharacteristicDataTypes()
                .stream()
                .map(FrontendCategory::fromEntity)
                .collect(Collectors.toList());
    }
}
