package com.grad.project.nc.controller.api.admin;

import com.grad.project.nc.controller.api.dto.FrontendProductType;
import com.grad.project.nc.controller.api.dto.TypeaheadItem;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.service.product.type.ProductTypeService;
import com.grad.project.nc.support.pagination.Page;
import com.grad.project.nc.support.validation.ProductTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/productTypes")
public class AdminProductTypesController {

    private final ProductTypeService productTypeService;
    private final ProductTypeValidator productTypeValidator;

    @Autowired
    public AdminProductTypesController(ProductTypeService productTypeService, ProductTypeValidator productTypeValidator) {
        this.productTypeService = productTypeService;
        this.productTypeValidator = productTypeValidator;
    }

    @PostMapping
    public ResponseEntity<?> addProductType(@RequestBody FrontendProductType frontendProductType) {
        productTypeValidator.validate(frontendProductType);

        ProductType result = productTypeService.add(frontendProductType.toModel());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getProductTypeId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/{productTypeId}", method = RequestMethod.PUT)
    public ResponseEntity<FrontendProductType> updateProductType(@RequestBody FrontendProductType frontendProductType) {
        productTypeValidator.validate(frontendProductType);

        ProductType productType = frontendProductType.toModel();
        productTypeService.update(productType);

        FrontendProductType updatedProductType = FrontendProductType
                .fromEntity(productTypeService.find(productType.getProductTypeId()));

        return ResponseEntity.ok(updatedProductType);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FrontendProductType> getAll() {
        return productTypeService.findAll()
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());
    }

    @RequestMapping(params = { "page", "amount" }, method = RequestMethod.GET)
    public Page<FrontendProductType> getPaginated(@RequestParam("page") int page,
                                                  @RequestParam("amount") int amount) {
        Page<ProductType> productTypePage = productTypeService.findPaginated(page, amount);

        List<FrontendProductType> content = productTypePage.getContent()
                .stream()
                .map(FrontendProductType::fromEntity)
                .collect(Collectors.toList());


        return new Page<>(content, productTypePage.getTotalPages());
    }

    @RequestMapping(value = "/{productTypeId}", method = RequestMethod.GET)
    public FrontendProductType getById(@PathVariable("productTypeId") Long productTypeId) {
        return FrontendProductType.fromEntity(productTypeService.find(productTypeId));
    }

    @RequestMapping(value = "/search", params = {"query"}, method = RequestMethod.GET)
    public List<TypeaheadItem> fetchProductTypeTypeaheadItemsByQuery(@RequestParam("query") String query) {
        return productTypeService.findByNameContaining(query)
                .stream()
                .map(pt -> new TypeaheadItem(pt.getProductTypeId(), pt.getProductTypeName()))
                .collect(Collectors.toList());
    }
}
