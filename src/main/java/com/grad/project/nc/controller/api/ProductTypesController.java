package com.grad.project.nc.controller.api;

import com.grad.project.nc.controller.api.dto.FrontendCategory;
import com.grad.project.nc.controller.api.dto.FrontendProductType;
import com.grad.project.nc.controller.api.dto.TypeaheadItem;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.service.product.type.ProductTypeService;
import com.grad.project.nc.support.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
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

    @RequestMapping(value = "/productTypes/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/productTypes/active", method = RequestMethod.GET)
    public List<FrontendProductType> getActiveProductTypes() {
        return productTypeService.findActive()
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/productTypes", params = { "page", "amount" }, method = RequestMethod.GET)
    public Page<FrontendProductType> getPaginated(@RequestParam("page") int page,
                                                  @RequestParam("amount") int amount) {
        Page<ProductType> productTypePage = productTypeService.findPaginated(page, amount);

        List<FrontendProductType> content = productTypePage.getContent()
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());


        return new Page<>(content, productTypePage.getTotalPages());
    }

    @RequestMapping(value = "productTypes/search", params = {"query"}, method = RequestMethod.GET)
    public List<TypeaheadItem> fetchProductTypeTypeaheadItemsByQuery(@RequestParam("query") String query) {
        return productTypeService.findByNameContaining(query)
                .stream()
                .map(pt -> new TypeaheadItem(pt.getProductTypeId(), pt.getProductTypeName()))
                .collect(Collectors.toList());
    }
}
